package com.jupiter.store.common.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
public class FakeDataGenerator {
    private static final Random rand = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void fake() {
        System.out.println("-- FAKE DATA GENERATOR");
        generateCategories();
        generateUnits();
        generateProductAttributes();
        generateUsers();
        generateProductsAndVariants();
        generateProductImages();
        generateProductAttributeValues();
        generateOrders();
        generatePayments();
        generateOrderHistories();
        System.out.println("-- DONE");
    }

    private void generateCategories() {
        System.out.println("-- CATEGORIES");
        for (int i = 1; i <= 20; i++) {
            System.out.printf("INSERT INTO categories (category_name) VALUES ('%s');%n", randomText("Cat"));
        }
    }

    private void generateUnits() {
        System.out.println("-- UNITS");
        for (int i = 1; i <= 20; i++) {
            System.out.printf("INSERT INTO units (unit_name) VALUES ('%s');%n", randomText("Unit"));
        }
    }

    private void generateProductAttributes() {
        System.out.println("-- PRODUCT ATTRIBUTES");
        for (int i = 1; i <= 20; i++) {
            System.out.printf("INSERT INTO product_attributes (attribute_name) VALUES ('%s');%n", randomText("Attr"));
        }
    }

    private void generateProductAttributeValues() {
        System.out.println("-- PRODUCT ATTRIBUTE VALUES");
        for (int variantId = 1; variantId <= 200; variantId++) {
            int attrCount = rand.nextInt(3) + 1;
            for (int i = 0; i < attrCount; i++) {
                int attrId = rand.nextInt(10) + 1;
                String value = randomString(4);
                System.out.printf(
                        "INSERT INTO product_attribute_values (product_variant_id, attr_id, attr_value) " +
                                "VALUES (%d, %d, '%s');%n", variantId, attrId, value
                );
            }
        }
    }

    private void generateUsers() {
        System.out.println("-- USERS");
        for (int i = 1; i <= 20; i++) {
            String fullName = "User " + randomString(5);
            String email = randomString(6) + "@example.com";
            String password = UUID.randomUUID().toString().substring(0, 10);
            String phone = "09" + (rand.nextInt(90000000) + 10000000);
            boolean gender = rand.nextBoolean();
            boolean isActive = rand.nextBoolean();
            String role = rand.nextBoolean() ? "ADMIN" : "STAFF";
            System.out.printf(
                    "INSERT INTO users (fullname, email, password, phone, is_active, role, gender) " +
                            "VALUES ('%s', '%s', '%s', '%s', %b, '%s', %b);%n",
                    fullName, email, password, phone, isActive, role, gender
            );
        }
    }

    private void generateProductsAndVariants() {
        System.out.println("-- PRODUCTS & VARIANTS");
        for (int i = 1; i <= 20; i++) {
            String name = "Product " + randomString(6);
            String desc = "Desc " + randomString(12);
            String status = rand.nextBoolean() ? "ACTIVE" : "INACTIVE";
            System.out.printf("INSERT INTO products (product_name, description, status) VALUES ('%s', '%s', '%s');%n", name, desc, status);

            // Insert 10 variants per product
            for (int j = 1; j <= 20; j++) {
                int cost = rand.nextInt(50000) + 5000;
                int price = cost + rand.nextInt(20000);
                int qty = rand.nextInt(200) + 10;
                int unitId = rand.nextInt(10) + 1;
                String sku = "SKU-" + UUID.randomUUID().toString().substring(0, 8);
                String barcode = "BAR-" + UUID.randomUUID().toString().substring(0, 8);
                int createdBy = rand.nextInt(10) + 1;
                String createdDate = randomPastDate(30);
                String modifiedDate = randomPastDate(15);

                System.out.printf(
                        "INSERT INTO product_variants (product_id, cost_price, price, quantity, unit_id, sku, barcode, expiry_date, created_by, created_date, last_modified_by, last_modified_date) " +
                                "VALUES (%d, %d, %d, %d, %d, '%s', '%s', NULL, %d, '%s', %d, '%s');%n",
                        i, cost, price, qty, unitId, sku, barcode, createdBy, createdDate, createdBy, modifiedDate
                );
            }
        }
    }

    private void generateProductImages() {
        System.out.println("-- PRODUCT IMAGES");
        for (int variantId = 1; variantId <= 20; variantId++) {
            int imgCount = rand.nextInt(2) + 1;
            for (int i = 1; i <= imgCount; i++) {
                String path = "/images/" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
                System.out.printf(
                        "INSERT INTO product_images (product_variant_id, path) VALUES (%d, '%s');%n",
                        variantId, path
                );
            }
        }
    }

    private void generateOrders() {
        System.out.println("-- ORDERS & ORDER_DETAILS");
        for (int orderId = 1; orderId <= 30; orderId++) {
            int userId = rand.nextInt(10) + 1;
            int customerId = rand.nextInt(10) + 1;
            String status = rand.nextBoolean() ? "PENDING" : "COMPLETED";
            long amount = 0;

            String now = randomPastDate(20);
            System.out.printf("INSERT INTO orders (user_id, order_date, order_status, total_amount, receiver_name, receiver_phone, receiver_address, note, tax_rate, customer_id, created_by, created_date, last_modified_by, last_modified_date) " +
                            "VALUES (%d, '%s', '%s', 0, 'Receiver %d', '09%d', 'Address %d', 'Note %d', 10.00, %d, %d, '%s', %d, '%s');%n",
                    userId, now, status, orderId, rand.nextInt(99999999), orderId, orderId, customerId, userId, now, userId, now
            );

            int itemCount = rand.nextInt(3) + 1;
            for (int i = 0; i < itemCount; i++) {
                int variantId = rand.nextInt(100) + 1;
                int price = rand.nextInt(20000) + 10000;
                int qty = rand.nextInt(5) + 1;
                int soldPrice = price * qty;
                amount += soldPrice;

                System.out.printf("INSERT INTO order_details (order_id, product_variant_id, price, sold_quantity, sold_price) " +
                        "VALUES (%d, %d, %d, %d, %d);%n", orderId, variantId, price, qty, soldPrice);
            }

            // Update total_amount
            System.out.printf("UPDATE orders SET total_amount = %d WHERE id = %d;%n", amount, orderId);
        }
    }

    private void generatePayments() {
        System.out.println("-- PAYMENTS");
        for (int i = 1; i <= 30; i++) {
            long total = rand.nextInt(500000) + 100000;
            long paid = total;
            long remaining = 0;
            String now = randomPastDate(10);
            System.out.printf("INSERT INTO payments (order_id, payment_method, status, date, paid, remaining, created_by, created_date, last_modified_by, last_modified_date) " +
                            "VALUES (%d, 'CASH', 'PAID', '%s', %d, %d, %d, '%s', %d, '%s');%n",
                    i, now, paid, remaining, 1, now, 1, now
            );
        }
    }

    private void generateOrderHistories() {
        System.out.println("-- ORDER HISTORIES");
        for (int i = 1; i <= 30; i++) {
            String created = randomPastDate(5);
            System.out.printf("INSERT INTO order_histories (order_id, old_status, new_status, created_by, created_date, last_modified_by, last_modified_date) " +
                            "VALUES (%d, 'PENDING', 'COMPLETED', %d, '%s', %d, '%s');%n",
                    i, 1, created, 1, created
            );
        }
    }

    private String randomText(String prefix) {
        return prefix + "-" + randomString(5);
    }

    private String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String randomPastDate(int maxDaysAgo) {
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long past = now - rand.nextInt(maxDaysAgo * 86400); // up to maxDaysAgo days ago
        return LocalDateTime.ofEpochSecond(past, 0, ZoneOffset.UTC).format(formatter);
    }
}

