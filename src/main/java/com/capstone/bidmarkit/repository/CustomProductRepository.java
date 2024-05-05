//package com.capstone.bidmarkit.repository;
//
//import com.capstone.bidmarkit.domain.Product;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.PreparedStatement;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class CustomProductRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Transactional
//    public void saveAll(List<Product> productList){
//        String sql = "INSERT INTO products (post_title, post_content)"+
//                "VALUES (?, ?)";
//
//        jdbcTemplate.batchUpdate(sql,
//                productList,
//                productList.size(),
//                (PreparedStatement ps, Product product) -> {
//                    ps.setString(1, product.getPostTitle());
//                    ps.setString(2, product.getPostContent());
//                });
//    }
//}