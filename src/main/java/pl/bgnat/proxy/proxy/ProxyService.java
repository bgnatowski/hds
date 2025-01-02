package pl.bgnat.proxy.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.bgnat.proxy.spiders.ProxySpider;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProxyService {
    private final ProxyRepository proxyRepository;
    private final ProxySpider proxySpider;
    private static final List<String> COUNTRY_CODES = List.of("GB", "CA", "US", "DE", "PL");

    @PostConstruct
    public void saveStartingProxies() {
        String countryCodeUSA = "US";
        try {
            saveProxiesListByCountryCodeToDb(countryCodeUSA);
        } catch (Exception e) {
            log.error("Error getting proxies for country code {}", countryCodeUSA, e);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    void scheduledProxyUpdate() {
        doUpdateProxies();
    }

    void doUpdateProxies() {
        for (String country : COUNTRY_CODES) {
            try {
                log.info("Pobieram i zapisuję nowe proxy dla kraju: {}", country);
                saveProxiesByCountryCode(country);
                Thread.sleep(5000);
                log.info("Zakończono cykliczną aktualizację proxy dla kraju: {}", country);
            } catch (Exception e) {
                log.error("Błąd przy pobieraniu proxy dla kraju: {}", country, e);
            }
        }
        log.info("Zakończono cykliczną aktualizację proxy dla krajów: {}", COUNTRY_CODES);
    }

    Proxy getRandomProxy() {
        List<Proxy> all = proxyRepository.findAll();
        if (all.isEmpty()) {
            throw new IllegalStateException("Brak proxy w bazie!");
        }
        int randomIndex = new Random().nextInt(all.size());
        return all.get(randomIndex);
    }

    void saveProxiesByCountryCode(String countryCode) {
        try {
            saveProxiesListByCountryCodeToDb(countryCode);
        } catch (Exception e) {
            log.error("Error getting proxies for country code {}", countryCode, e);
        }
    }

    List<Proxy> getActualProxyList() {
        return proxyRepository.findAll();
    }

    private void saveProxiesListByCountryCodeToDb(String countryCode) throws IOException, InterruptedException {
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
}
