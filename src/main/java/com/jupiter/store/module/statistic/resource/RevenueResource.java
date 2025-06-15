package com.jupiter.store.module.statistic.resource;

import com.jupiter.store.module.statistic.dto.DashboardResponseDTO;
import com.jupiter.store.module.statistic.dto.ProductSalesDTO;
import com.jupiter.store.module.statistic.dto.RequestTimeDTO;
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
    public DashboardResponseDTO getDashboardData() {
        return revenueService.getDashboardData();
    }

    @PostMapping("/products")
    public List<ProductSalesDTO> getProductData(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getProductData(
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }

    @GetMapping("/customers")
    public DashboardResponseDTO getCustomerData() {
        return revenueService.getDashboardData();
    }
}
