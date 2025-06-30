package com.jupiter.store.module.statistic.resource;

import com.jupiter.store.module.statistic.dto.*;
import com.jupiter.store.module.statistic.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/products/low-stock")
    public List<ProductInventoryDTO> getProductInventoryData() {
        return revenueService.getProductInventoryData();
    }

    @GetMapping("/products/dead-stock")
    public List<DeadStockProductDTO>  getDeadStockData() {
        return revenueService.getDeadStockData();
    }

    @PostMapping("/customers")
    public List<CustomerResponseDTO> getCustomerData(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getCustomersData(
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }

    @GetMapping("/inactive-customers")
    public List<InactiveCustomerDTO> getInactiveCustomers(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        return revenueService.getInactiveCustomers(sortBy, sortDirection);
    }

    @GetMapping("/new-customers")
    public List<NewCustomerDTO> getNewCustomers(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return revenueService.getNewCustomers(startDate, endDate);
    }

    @PostMapping("/orders/status")
    public List<OrderStatusResponseDTO> getOrderStatusData(@RequestBody RequestTimeDTO requestTimeDTO) {
        return revenueService.getOrderStatusStatistics(
                requestTimeDTO.getStartTime(),
                requestTimeDTO.getEndTime()
        );
    }
    @GetMapping("/payment-methods")
    public List<PaymentMethodStatisticDTO> getPaymentMethodsData(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return revenueService.getPaymentMethodsData(startDate, endDate);
    }
}
