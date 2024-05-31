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
import java.time.temporal.ChronoUnit;
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
//        Reader reader = new FileReader("src/main/resources/CrawlingFiles-24-05-28/중고 상품 크롤링 패션.json");
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
//            int categoryNum;
//            switch (category) {
//                case "패션의류": categoryNum = 0; break;
//                case "패션잡화": categoryNum = 0; break;
//                case "모바일/태블릿": categoryNum = 1; break;
//                case "노트북/PC": categoryNum = 2; break;
//                case "가전제품": categoryNum = 3; break;
//                case "도서/음반/문구": categoryNum = 4; break;
//                case "스포츠": categoryNum = 5; break;
//                case "가구/인테리어": categoryNum = 6; break;
//                case "반려동물/취미": categoryNum = 7; break;
//                default: continue;
//            }
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
//                    .category(categoryNum)
//                    .content(detail)
//                    .deadline(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(8 + random.nextInt(7)).plusHours(random.nextInt(20)))
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

    @Test
    public void insertTestProduct() {
        Product product = Product.builder()
                .memberId("testid1")
                .name("test")
                .category(0)
                .content("testtest")
                .deadline(LocalDateTime.now().plusMinutes(2))
                .price(20000)
                .initPrice(10000)
                .build();

        List<ProductImg> imgList = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            ProductImg newImg = ProductImg.builder().imgUrl("testImg" + j).product(product).isThumbnail(j == 0).build();
            imgList.add(newImg);
        }
        product.setImages(imgList);
        productRepository.save(product);
    }
}