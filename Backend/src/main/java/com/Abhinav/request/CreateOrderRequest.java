package com.Abhinav.request;

import com.Abhinav.domain.OrderType;

import lombok.Data;


@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
