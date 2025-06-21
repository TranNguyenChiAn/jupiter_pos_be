package com.jupiter.store.module.statistic.resource;

import com.jupiter.store.module.statistic.dto.*;
import com.jupiter.store.module.statistic.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class RevenueResource {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenues")
    public TodayResponseDTO getTodayData() {
        return revenueService.getTodayData();
    }

    @PostMapping("/net-revenues")
    public List<NetRevenueDTO> getNetRevenue(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getNetRevenue(
                requestTimeDTO.getLabel(),
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }

    @PostMapping("/products")
    public List<ProductSalesDTO> getProductData(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getProductData(
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }

    @PostMapping("/customers")
    public List<CustomerResponseDTO> getCustomerData(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getCustomersData(
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }
}
