package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.entity.Tvwall;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.mapper.ChannelMapper;
import com.auxing.znhy.mapper.TvwallMapper;
import com.auxing.znhy.util.Hdus;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-10-13
 */
@Slf4j
@Api(tags = "电视墙配置接口",value = "/tvwall")
@RestController
@RequestMapping("/tvwall")
public class TvwallController extends BaseController {

    @Autowired
    TvwallMapper tvwallMapper;

    @Autowired
    ChannelMapper channelMapper;

    @ApiOperation(value = "新增电视墙接口")
    @RequestMapping(value = "/saveRoles", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增电视墙成功"),
            @ApiResponse(code = 500, message = "新增失败")
    })
    public JSONObject addTvWall(@RequestParam @ApiParam(value = "电视墙名称",required = true) String name, @RequestParam @ApiParam(value = "电视墙风格：行",required = true) Integer row, @RequestParam @ApiParam(value = "电视墙风格：列",required = true) Integer column, @Param("jsonStr") @ApiParam(value = "channelList",required = true)String channelList, @RequestParam @ApiParam(value = "用户modid",required = true)String modid){
        JSONObject respJson = new JSONObject();
        try {
        	Tvwall tvwall = new Tvwall();
        	tvwall.setName(name);
        	QueryWrapper<Tvwall> wrapper = new QueryWrapper<Tvwall>(tvwall);
        	tvwall = this.tvwallService.getOne(wrapper);
        	if(tvwall != null){
        		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "电视墙名称已存在");
                return respJson;
        	}else{
                List<Channel> list =	JSONObject.parseArray(channelList,Channel.class);
                tvwallService.addTvWall(name,row,column,list,modid);
                respJson.put("code", ResultCode.SUCCESS);
                respJson.put("msg", "新增电视墙成功");
        	}
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "新增失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "获取设备通道信息接口")
    @RequestMapping(value = "/getDevChannel", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "列表获取成功",response = Hdus.class),
            @ApiResponse(code = 500, message = "列表获取失败")
    })
    public JSONObject getDevChannel(){
        JSONObject respJson = new JSONObject();
        try {
		    User loginUser = (User) request.getSession().getAttribute("user");
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    Map<String, Object> params = new HashMap<String, Object>();
            params.put("account_token", meetInterface.getToken());
            MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/hdus",params, meetInterface.getCookie());
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取电视墙通道:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
    		Map<String, Object> map = new HashMap<String, Object>();
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		for (Hdus hdus : result.getHdus()) {
				if(map.get(hdus.getHdu_name()) == null){
					map.put(hdus.getHdu_name(), hdus.getHdu_id());
					Map<String, Object> hduMap = new HashMap<String, Object>();
					hduMap.put("hduName", hdus.getHdu_name());
					list.add(hduMap);
				}
			}
    		
    		for (Map<String, Object> m : list) {
    			List<String> idList = new ArrayList<String>();
				for (Hdus hdus : result.getHdus()) {
					if(m.get("hduName").toString().equals(hdus.getHdu_name())){
						idList.add(hdus.getHdu_id());
					}
				}
				m.put("hduIdList", idList);
			}
    		
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", list);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "修改完保存电视墙信息接口")
    @RequestMapping(value = "/changeTvWall", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "列表获取成功"),
            @ApiResponse(code = 500, message = "列表获取失败")
    })
    public JSONObject changeTvWall(@RequestParam @ApiParam(value = "要修改的电视墙的ID",required = true)Integer id,@RequestParam @ApiParam(value = "电视墙名称") String name, @RequestParam @ApiParam(value = "电视墙风格：行") Integer row, @RequestParam @ApiParam(value = "电视墙风格：列") Integer column, @Param("jsonStr") @ApiParam(value = "绑定设备、通道号(只要获取设备和通道号两个参数，其他无用)")String channelList){
        JSONObject respJson = new JSONObject();
        try {
        	Tvwall tvwall = new Tvwall();
        	tvwall.setName(name);
        	QueryWrapper<Tvwall> wrapper = new QueryWrapper<Tvwall>(tvwall);
        	tvwall = this.tvwallService.getOne(wrapper);
        	if(tvwall != null && id != tvwall.getId()){
        		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "电视墙名称已存在");
                return respJson;
        	}else{
                List<Channel> list =	JSONObject.parseArray(channelList,Channel.class);
                int rest = tvwallService.changeTvWall(id,name,row,column,list);
                if (rest == 0){
                    respJson.put("code", ResultCode.SUCCESS);
                    respJson.put("msg", "修改成功");
                }else{
                    respJson.put("code", ResultCode.ACCEPTED);
                    respJson.put("msg", "该电视墙风格正在使用中，修改失败！");
                }  
        	}
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "修改失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "删除电视墙接口")
    @RequestMapping(value = "/delTvWalls", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 500, message = "删除失败")
    })
    public JSONObject delTvWalls(@ApiParam(value = "需要删除的电视墙的ID",required = true) @RequestParam List<Integer> list){
        JSONObject respJson = new JSONObject();
        try {
            int rest = tvwallService.deleteByIds(list);
            if(rest == 0){
                respJson.put("code", ResultCode.SUCCESS);
                respJson.put("msg", "删除成功");
            }else{
                respJson.put("code", ResultCode.ACCEPTED);
                respJson.put("msg", "所选电视墙风格正在使用中，删除失败！");
            }
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "删除失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "获取电视墙列表信息接口")
    @RequestMapping(value = "/getTvWalls", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getTvWalls(@RequestParam @ApiParam(value = "当前页",required = true) Long currentPage){
        JSONObject respJson = new JSONObject();
        try {
            Page page = new Page();
            page.setCurrent(currentPage);
            page.setSize(10);
            User loginUser = (User) request.getSession().getAttribute("user");
            Tvwall tvwall = new Tvwall();
            tvwall.setDepartmentId(loginUser.getDepartment());
            QueryWrapper<Tvwall> wrapper = new QueryWrapper<Tvwall>(tvwall);
            wrapper.orderByDesc("CREAT_TIME");
            IPage<Tvwall> tvwallIPage = tvwallService.page(page, wrapper);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", tvwallIPage);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "点击修改展示页面时获取初始数据的接口")
    @RequestMapping(value = "/getChangeData", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getChangeData(@RequestParam @ApiParam(value = "该条数据的id",required = true)String id){
        JSONObject respJson = new JSONObject();
        try {
            Tvwall tvwall = tvwallMapper.selectById(id);
            List<Channel> channel = channelMapper.getByTvId(tvwall.getTelevisionId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tvwall",tvwall);
            map.put("channel",channel);
            respJson.put("data", map);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

}


