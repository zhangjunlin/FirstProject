package com.auxing.znhy.controller;


import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Message;
import com.auxing.znhy.mapper.MessageMapper;
import com.auxing.znhy.service.IMessageService;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-11-12
 */
@Api(tags = "常用消息保存接口",value = "/message")
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {
    @Autowired
    MessageMapper messageMapper;

    @ApiOperation(value = "常用消息保存接口")
    @RequestMapping(value = "/saveMesg",method = RequestMethod.POST)
    public JSONObject saveMesg(@RequestParam @ApiParam(value = "信息详情")String mesg){
        JSONObject respJson = new JSONObject();
        try{
            int counts = messageMapper.selectCount(null);
            if(counts < 10){
                Message message = new Message();
                message.setMessage(mesg);
                messageMapper.insert(message);
                //respJson.put("data", result);
                respJson.put("msg", "添加成功");
                respJson.put("code", ResultCode.SUCCESS);
            }else {
                respJson.put("code", ResultCode.ACCEPTED);
                respJson.put("msg", "常用短消息设置已满(上限10条)！");
            }
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "添加失败!");
            e.printStackTrace();
        }
        return  respJson;
    }

    @ApiOperation(value = "常用消息删除接口")
    @RequestMapping(value = "/delMesg",method = RequestMethod.POST)
    public JSONObject delMesg(@RequestParam @ApiParam(value = "删除的id号",required = true) List<Integer> ids){
        JSONObject respJson = new JSONObject();
        try{
            for (Integer id:ids){
                messageMapper.deleteById(id);
            }
            //respJson.put("data", result);
            respJson.put("msg", "删除成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "删除失败!");
            e.printStackTrace();
        }
        return  respJson;
    }

    @ApiOperation(value = "常用消息修改接口")
    @RequestMapping(value = "/changeMesg",method = RequestMethod.POST)
    public JSONObject changeMesg(@RequestParam @ApiParam(value = "删除的id号",required = true) Integer id,@RequestParam @ApiParam(value = "信息详情",required = true) String mesg){
        JSONObject respJson = new JSONObject();
        try{
            Message message = new Message();
            message.setId(id);
            message.setMessage(mesg);
            messageMapper.updateById(message);
            //respJson.put("data", result);
            respJson.put("msg", "修改成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return  respJson;
    }

    @ApiOperation(value = "常用消息查询接口")
    @RequestMapping(value = "/getMesg",method = RequestMethod.GET)
    public JSONObject getMesg(){
        JSONObject respJson = new JSONObject();
        try{
            Page page = new Page();
            page.setSize(10);
            page.setCurrent(1);
            QueryWrapper wrapper = new QueryWrapper();
            IPage<Message> pageRole = messageService.page(page, wrapper);
            respJson.put("data", pageRole);
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败!");
            e.printStackTrace();
        }
        return  respJson;
    }
}
