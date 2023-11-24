package pl.bgnat.hds.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import pl.bgnat.hds.spiders.ProxySpider;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProxyService {
    private final ProxyRepository proxyRepository;
    private final ProxySpider proxySpider;

    @PostConstruct
    public void saveStartingProxies() {
        String countryCodeUSA = "US";
        try {
            saveProxiesListByCountryCodeToDb(countryCodeUSA);
        } catch (IOException e) {
            log.error("Error getting proxies for country code {}", countryCodeUSA, e);
        }
    }

    public void saveProxiesByCountryCode(String countryCode) {
        try {
            saveProxiesListByCountryCodeToDb(countryCode);
        } catch (IOException e) {
            log.error("Error getting proxies for country code {}", countryCode, e);
        }
    }

    private void saveProxiesListByCountryCodeToDb(String countryCode) throws IOException {
        List<String> proxyJsonList = proxySpider.getProxiesListByCountryCode(countryCode);
        ObjectMapper objectMapper = new ObjectMapper();

        proxyJsonList.forEach(
                proxyJson -> {
                    try {
                        Proxy proxy = objectMapper.readValue(proxyJson, Proxy.class);
                        if (!proxyRepository.existsByHostAndPort(proxy.getHost(), proxy.getPort()))
                            proxyRepository.save(proxy);
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing JSON to Proxy object", e);
                    }
                });
    }

    public List<Proxy> getActualProxyList() {
        return proxyRepository.findAll();
    }
}
