package com.capstone.bidmarkit.repository;

import com.capstone.bidmarkit.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer>{
    List<ProductImg> findByProductId(int productId);

//    @Query("SELECT p.imgUrl FROM ProductImg p WHERE p.product.Id = :productId")
//    String findImgUrlsByProductId(@Param("productId") int productId);

    @Query("SELECT p.imgUrl FROM ProductImg p WHERE p.product.id = :productId")
    List<String> findImgUrlsByProductId(@Param("productId") int productId);

    @Query("SELECT p.imgUrl FROM ProductImg p WHERE p.product.id = :productId AND p.isThumbnail = true")
    String findThumbnailUrlByProductId(@Param("productId") int productId);
}
