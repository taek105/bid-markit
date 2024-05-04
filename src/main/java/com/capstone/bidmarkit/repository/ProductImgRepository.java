package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer>{
    List<ProductImg> findByProductId(int productId);
}
