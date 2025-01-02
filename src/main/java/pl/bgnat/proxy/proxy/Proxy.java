package pl.bgnat.proxy.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Entity(name = "Proxy")
@Table(
        name = "proxy",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "proxy_host_port_constraint",
                    columnNames = {"host", "port"})
        })
public class Proxy {
    @Id
    @SequenceGenerator(
            name = "proxy_id_generator",
            sequenceName = "proxy_id_generator",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proxy_id_generator")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private String port;

    @Column(name = "country_code", nullable = false)
    private String countryCode;
}
