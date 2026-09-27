package com.example.saleappv1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.saleappv1.model.Product;
import com.example.saleappv1.service.ProductService;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    //Endpoint cho trang chi tiết sản phẩm: /products/{productId}
    @GetMapping("/products/{productId}")
    public String showProductDetail(@PathVariable("productId") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/products"; // Nếu không tìm thấy, chuyển hướng về danh sách
        }
        model.addAttribute("product", product);
        return "product-detail"; // Trả về file product-detail.html
    }

    //Lọc và tìm kiếm sản phẩm
    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double fromPrice,
            @RequestParam(required = false) Double toPrice,
            Model model) {

        // Gọi service lọc danh sách sản phẩm
        List<Product> products = productService.searchProducts(categoryId, keyword, fromPrice, toPrice);

        // Đưa dữ liệu ra view
        model.addAttribute("products", products);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("fromPrice", fromPrice);
        model.addAttribute("toPrice", toPrice);

        return "products";
    }
}
