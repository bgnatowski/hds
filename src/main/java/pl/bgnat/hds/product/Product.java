package pl.bgnat.hds.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Entity(name = "Product")
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_id_generator",
            sequenceName = "product_id_generator",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "actual_price", precision = 10, scale = 2)
    private BigDecimal actualPrice;

    @Column(name = "last_lowest_price", precision = 10, scale = 2)
    private BigDecimal lastLowestPrice;

    @Column(name = "last_update", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdate;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;
}
