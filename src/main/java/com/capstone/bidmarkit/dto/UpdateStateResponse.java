package com.capstone.bidmarkit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateStateResponse {
    private int productId;
    private int state;

}
