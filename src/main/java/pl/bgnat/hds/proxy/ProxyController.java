package pl.bgnat.hds.proxy;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/proxy")
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping(value = "/list")
    public ResponseEntity<List<Proxy>> getProxiesList() {
        List<Proxy> actualProxyList = proxyService.getActualProxyList();
        return ResponseEntity.ok(actualProxyList);
    }

    @PostMapping(value = "/pull/{countryCode}")
    public ResponseEntity<List<Proxy>> pullProxyListByCountryCode(
            @PathVariable(name = "countryCode") String countryCode) {
        proxyService.saveProxiesByCountryCode(countryCode);
        List<Proxy> actualProxyList = proxyService.getActualProxyList();
        return ResponseEntity.ok(actualProxyList);
    }
}
