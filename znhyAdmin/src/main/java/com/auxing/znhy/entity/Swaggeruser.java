package com.auxing.znhy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户实体类型")
public class Swaggeruser {
    @ApiModelProperty(value = "用户UID")
    private String uid;
    @ApiModelProperty(value = "用户名", hidden = true)
    private String username;
    @ApiModelProperty(value = "用户年纪")
    private Integer age;
    @ApiModelProperty(value = "用户的多个订单实体")
    private List<Swaggerorder> orders;
}
