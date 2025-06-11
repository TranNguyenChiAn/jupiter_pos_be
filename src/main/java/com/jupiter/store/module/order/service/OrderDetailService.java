package com.jupiter.store.module.order.service;

import com.jupiter.store.module.order.dto.OrderDetailReadDTO;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.service.ProductVariantSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductVariantSearchService productVariantSearchService;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, ProductVariantSearchService productVariantSearchService) {
        this.orderDetailRepository = orderDetailRepository;
        this.productVariantSearchService = productVariantSearchService;
    }

    public List<OrderDetail> findByOrderId(Integer id) {
        return orderDetailRepository.findByOrderId(id);
    }

    public List<OrderDetailReadDTO> setDetails(
            List<OrderDetail> orderDetails
    ) {
        return orderDetails.stream()
                .map(od -> {
                    OrderDetailReadDTO orderDetailReadDTO = new OrderDetailReadDTO(od);
                    ProductVariant productVariant = od.getProductVariant();
                    ProductVariantReadDTO productVariantDTO;
                    if (productVariant == null) {
                        productVariantDTO = new ProductVariantReadDTO();
                    } else {
                        productVariantDTO = productVariantSearchService.setDetailsForVariant(productVariant);
                    }
                    orderDetailReadDTO.setProductVariant(productVariantDTO);
                    return orderDetailReadDTO;
                })
                .collect(Collectors.toList());
    }
}
