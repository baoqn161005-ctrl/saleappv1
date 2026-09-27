package com.example.saleappv1.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.saleappv1.model.Product;

import tools.jackson.databind.ObjectMapper;

@Service
public class ProductService {

    public List<Product> getAllProducts() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = new ClassPathResource("data/products.json").getInputStream();
            Product[] products = mapper.readValue(inputStream, Product[].class);
            return new ArrayList<>(Arrays.asList(products));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //Tìm sản phẩm theo ID
    public Product getProductById(Long id) {
        List<Product> products = getAllProducts();
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    //Phương thức lọc và tìm kiếm sản phẩm
    public List<Product> searchProducts(Integer categoryId, String keyword, Double fromPrice, Double toPrice) {
        List<Product> products = getAllProducts();

        return products.stream().filter(product -> {
            boolean matches = true;

            // 1. Lọc theo categoryId
            if (categoryId != null) {
                if (product.getCategoryId() == null || !product.getCategoryId().equals(categoryId.longValue())) {
                    matches = false;
                }
            }

            // 2. Tìm theo từ khóa tên sản phẩm (không phân biệt hoa/thường)
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (product.getName() == null || !product.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                    matches = false;
                }
            }

            // 3. Lọc theo từ giá (fromPrice)
            if (fromPrice != null) {
                if (product.getPrice() == null || product.getPrice() < fromPrice) {
                    matches = false;
                }
            }

            // 4. Lọc theo đến giá (toPrice)
            if (toPrice != null) {
                if (product.getPrice() == null || product.getPrice() > toPrice) {
                    matches = false;
                }
            }

            return matches;
        }).collect(Collectors.toList());
    }
}
