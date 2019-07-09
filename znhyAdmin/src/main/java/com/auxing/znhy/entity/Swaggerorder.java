package com.auxing.znhy.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Swaggerorder {
    @ApiModelProperty(value = "订单Id")
    private String orderId;
    //required属性看不出任何效果、因此使用hidden
    @ApiModelProperty(value = "订单名字", required = false)
    private String ordername;
    @ApiModelProperty(value = "订单价格")
    private BigDecimal price;
}
