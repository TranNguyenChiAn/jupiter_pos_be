package com.jupiter.store.module.product.model;

import com.jupiter.store.common.model.AbstractAuditingEntity;
import com.jupiter.store.module.product.constant.ProductVariantStatus;
import com.jupiter.store.module.product.dto.ProductReadDTO;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant extends AbstractAuditingEntity implements Serializable {
    //    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "cost_price")
    private Long costPrice;

    @Column(name = "price")
    private Long price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductVariantStatus status;

    public ProductVariantReadDTO toProductVariantReadDTO() {
        ProductVariantReadDTO dto = new ProductVariantReadDTO();
        dto.setId(this.id);
        dto.setCostPrice(this.costPrice);
        dto.setPrice(this.price);
        dto.setQuantity(this.quantity);
        dto.setUnitId(this.unitId);
        dto.setSku(this.sku);
        dto.setBarcode(this.barcode);
        dto.setExpiryDate(this.expiryDate);
        dto.setStatus(this.status);
        ProductReadDTO productDto = new ProductReadDTO();
        productDto.setProductId(this.productId);
        dto.setProduct(productDto);
        return dto;
    }
}