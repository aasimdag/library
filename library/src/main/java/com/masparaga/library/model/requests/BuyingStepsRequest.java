package com.masparaga.library.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuyingStepsRequest {
    private String address;
    private String cardInfos;
}
