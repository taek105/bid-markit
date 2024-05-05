package com.capstone.bidmarkit.service;

import com.capstone.bidmarkit.dto.AddMemberRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

//    @Test
//    public void addMember() throws Exception {
//        Faker faker = new Faker(new Locale("ko"));
//        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("ko"), new RandomService());
//        int startNum = 3, endNum = 50;
//        for (; startNum <= endNum; startNum++) {
//            AddMemberRequest request = new AddMemberRequest();
//            request.setId("testid" + startNum);
//            request.setPassword("test" + startNum);
//            request.setName(faker.name().lastName() + faker.name().firstName());
//            request.setNickname(fakeValuesService.bothify("??????###"));
//            memberService.save(request);
//        }
//    }
}