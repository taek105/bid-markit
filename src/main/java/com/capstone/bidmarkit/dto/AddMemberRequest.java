package com.capstone.bidmarkit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String id;
    private String password;
    private String name;
    private String nickname;
}
