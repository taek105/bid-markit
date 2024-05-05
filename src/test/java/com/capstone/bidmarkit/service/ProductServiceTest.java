package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.ProductImg;
import com.capstone.bidmarkit.repository.ProductRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;

//    @Test
//    public void insertProduct() throws Exception {
//        JSONParser parser = new JSONParser();
//        Reader reader = new FileReader("src/main/resources/CrawlingFiles/중고 상품 크롤링 패션의류.json");
//        JSONArray dataArray = (JSONArray) parser.parse(reader);
//        Random random = new Random(System.currentTimeMillis());
//        List<Product> insertList = new ArrayList<>();
//
//        for (int i = 0; i < dataArray.size(); i++) {
//            String memberId = "testid" + (random.nextInt(50) + 1);
//            JSONObject element = (JSONObject) dataArray.get(i);
//            List<String> imgs = List.of(element.get("Imgs").toString().split("\r\n"));
//            String category = element.get("Category").toString(),
//                    title = element.get("Title").toString(), detail = element.get("Detail").toString();
//            int price;
//            try {
//                price = Integer.parseInt(element.get("Price").toString());
//            } catch (NumberFormatException e) {
//                continue;
//            }
//            if(imgs.isEmpty() || category.isBlank() || title.isBlank() || detail.isBlank()) continue;
//            List<ProductImg> imgList = new ArrayList<>();
//
//            Product product = Product.builder()
//                    .memberId(memberId)
//                    .name(title)
//                    .category(category)
//                    .content(detail)
//                    .deadline(LocalDateTime.of(2024, 5, 8, 0, 0, 0).plusDays(random.nextInt(7)))
//                    .price(price + price / 10 / 1000 * 1000 * (5 + random.nextInt(6)))
//                    .initPrice(price)
//                    .build();
//
//            for (int j = 0; j < imgs.size(); j++) {
//
//                ProductImg newImg = ProductImg.builder().imgUrl(imgs.get(j)).product(product).isThumbnail(j == 0).build();
//                imgList.add(newImg);
//            }
//            product.setImages(imgList);
//            insertList.add(product);
//        }
//        productRepository.saveAll(insertList);
//    }
}