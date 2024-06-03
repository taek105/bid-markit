package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushAlarmRequest  {
    private String memberId;
    private String productName;
    private String imgURL;
    private String content;
    private int type;

    public PushAlarmRequest(String memberId, String productName, String imgURL, String content, int type) {
        this.memberId = memberId;
        this.productName = productName;
        this.imgURL = imgURL;
        this.content = content;
        this.type = type;
    }

}