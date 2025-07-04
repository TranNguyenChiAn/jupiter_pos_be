package com.jupiter.store.module.customer.service;

import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.dto.CustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private HelperUtils helperUtils;

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer create(CustomerDTO customerDTO) {
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isBlank()) {
            throw new RuntimeException("Vui lòng nhập tên khách hàng");
        }
        if (customerDTO.getPhone() == null || customerDTO.getPhone().isBlank()) {
            throw new RuntimeException("Vui lòng nhập số điện thoại");
        }
        if (customerRepository.findByPhone(customerDTO.getPhone()) != null) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setGender(customerDTO.isGender());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setTotalSpent(0L);
        customer.setTotalOrders(0);
        customer.setCreatedBy(SecurityUtils.getCurrentUserId());
        customer.setLastModifiedBy(SecurityUtils.getCurrentUserId());
        return customerRepository.save(customer);
    }

    public Customer findCustomer(String keyword) {
        return customerRepository.findByKeyword(keyword).orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại!"));
    }

    public Customer findById(Integer customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public Customer findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }


    public Page<Customer> search(Pageable pageable, String search, String sortBy, String sortDirection) {
        search = helperUtils.normalizeSearch(search);
        if (sortBy != null && !sortBy.isBlank()) {
            sortBy = helperUtils.convertCamelCaseToSnakeCase(sortBy);
        } else {
            sortBy = "last_modified_date";
        }
        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "DESC";
        } else {
            sortDirection = sortDirection.toUpperCase();
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return customerRepository.search(search, pageable);
    }

    public void updateAfterOrder(Integer customerId, Long totalSpent, Integer totalOrders) {
        Customer customer = findById(customerId);
        if (customer != null) {
            Long oldTotalSpent = customer.getTotalSpent() != null ? customer.getTotalSpent() : 0L;
            Integer oldTotalOrders = customer.getTotalOrders() != null ? customer.getTotalOrders() : 0;
            customer.setTotalSpent(oldTotalSpent + totalSpent);
            customer.setTotalOrders(oldTotalOrders + totalOrders);
            customerRepository.save(customer);
        }
    }

    public Customer update(Integer id, CustomerDTO customerDTO) {
        Customer existingCustomer = findById(id);
        if (existingCustomer == null) {
            throw new RuntimeException("Khách hàng không tồn tại!");
        }
        existingCustomer.setCustomerName(customerDTO.getCustomerName() == null ? "" : customerDTO.getCustomerName());
        existingCustomer.setGender(customerDTO.isGender());
        existingCustomer.setPhone(customerDTO.getPhone() == null ? existingCustomer.getPhone() : customerDTO.getPhone());
        existingCustomer.setAddress(customerDTO.getAddress() == null ? "" : customerDTO.getAddress());
        existingCustomer.setLastModifiedBy(SecurityUtils.getCurrentUserId());
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = findById(customerId);
        if (customer == null) {
            throw new RuntimeException("Khách hàng không tồn tại!");
        }
        customerRepository.delete(customer);
    }
}

