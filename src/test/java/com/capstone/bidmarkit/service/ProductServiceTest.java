package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.domain.Product;
import com.capstone.bidmarkit.domain.ProductImg;
import com.capstone.bidmarkit.repository.ProductImgRepository;
import com.capstone.bidmarkit.repository.ProductRepository;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImgRepository productImgRepository;

    @Test
    public void insertProduct() throws Exception {
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("src/main/resources/CrawlingFiles/중고 상품 크롤링 가전제품.json");
        JSONArray dataArray = (JSONArray) parser.parse(reader);
        Random random = new Random(System.currentTimeMillis());
        List<Product> insertList = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); i++) {
            String memberId = "testid" + (random.nextInt(50) + 1);
            JSONObject element = (JSONObject) dataArray.get(i);
            List<String> imgs = List.of(element.get("Imgs").toString().split("\r\n"));
            String category = element.get("Category").toString(),
                    title = element.get("Title").toString(), detail = element.get("Detail").toString();
            int price;
            try {
                price = Integer.parseInt(element.get("Price").toString());
            } catch (NumberFormatException e) {
                continue;
            }
            if(imgs.isEmpty() || category.isBlank() || title.isBlank() || detail.isBlank()) continue;
            List<ProductImg> imgList = new ArrayList<>();

            Product product = Product.builder()
                    .memberId(memberId)
                    .name(title)
                    .category(category)
                    .content(detail)
                    .deadline(LocalDateTime.of(2024, 5, 8, 0, 0, 0).plusDays(random.nextInt(7)))
                    .price(price + 1000 * random.nextInt(11))
                    .initPrice(price)
                    .build();

            for (String img: imgs) {
                ProductImg newImg = ProductImg.builder().imgUrl(img).product(product).build();
                imgList.add(newImg);
            }
            product.setImages(imgList);
            insertList.add(product);
        }
        productRepository.saveAll(insertList);
    }
}