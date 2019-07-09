package com.auxing.znhy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Swaggeruser;


@Api(tags = "swagger测试接口",value = "/swagger")
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {

    @RequestMapping(value = "/saveOrders", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增订单成功", response = Swaggeruser.class),
            @ApiResponse(code = 500, message = "新增订单失败")
    })
    public JSONObject saveOrders(@RequestBody @ApiParam(value = "用户和订单列表", required = true) Swaggeruser user){
        JSONObject respJson = new JSONObject();
        respJson.put("code", 200);
        respJson.put("msg", "新增订单成功");
        respJson.put("data", user);
        return respJson;
    }

}
