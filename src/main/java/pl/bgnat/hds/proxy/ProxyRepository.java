package pl.bgnat.hds.proxy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {
    boolean existsByHostAndPort(String host, String port);
}
