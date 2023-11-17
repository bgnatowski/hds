package pl.bgnat.hds.spiders;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProxySpider {
    @Value("${url.start.proxy}")
    private String start_url;

    public List<String> getProxiesListByCountryCode(String countryCode) throws IOException {
        String[] proxies = getProxiesByCountryCode(countryCode);
        return mapToJsonArrayList(countryCode, proxies);
    }

    private String[] getProxiesByCountryCode(String countryCode) throws IOException {
        Document document = Jsoup.connect(start_url + countryCode).get();
        String proxyText = document.body().text();
        String[] proxies = proxyText.split("\\s+");
        return proxies;
    }

    private List<String> mapToJsonArrayList(String countryCode, String[] proxies) {
        return Arrays.stream(proxies)
                .map(
                        proxy -> {
                            String[] parts = proxy.split(":");
                            if (parts.length == 2) {
                                String host = parts[0];
                                String port = parts[1];
                                return "{"
                                        + "\"host\":\""
                                        + host
                                        + "\","
                                        + "\"port\":\""
                                        + port
                                        + "\","
                                        + "\"countryCode\":\""
                                        + countryCode
                                        + "\""
                                        + "}";
                            } else {
                                return null;
                            }
                        })
                .filter(proxy -> proxy != null)
                .collect(Collectors.toList());
    }
}
