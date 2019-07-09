package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Meetroom;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
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
 * @since 2018-10-16
 */
@Api(tags = "会议室操作接口",value = "/meetroom")
@RestController
@RequestMapping("/meetroom")
public class MeetroomController extends BaseController {

    @ApiOperation(value = "新增会议室接口")
    @RequestMapping(value = "/addMeetRoom", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增成功"),
            @ApiResponse(code = 500, message = "新增失败")
    })
    public JSONObject addMeetRoom(@RequestParam @ApiParam(value = "名称",required = true) String name, 
    		@RequestParam @ApiParam(value = "容量",required = true) String capacity, 
    		@RequestParam @ApiParam(value = "面积", required = true) String acreage, 
    		@RequestParam @ApiParam(value = "区域", required = true) String area, 
    		@RequestParam @ApiParam(value = "类型", required = true) Integer type, 
    		@RequestParam(value = "des",required = false) @ApiParam(value = "备注") String des){
        JSONObject respJson = new JSONObject();
        try{
        	Meetroom m = new Meetroom();
        	m.setName(name);
        	QueryWrapper<Meetroom> wrapper = new QueryWrapper<Meetroom>(m);
        	m = this.meetroomService.getOne(wrapper);
        	if(m != null){
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议室名称已存在");
                return respJson;
        	}
        	
            Meetroom meetroom = new Meetroom();
            meetroom.setName(name);
            meetroom.setCapacity(capacity);
            meetroom.setAcreage(acreage);
            meetroom.setArea(area);
            meetroom.setType(type);
            meetroom.setStatus(0);
            meetroom.setDes(des);
            meetroomService.save(meetroom);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "新增成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "修改会议室信息接口")
    @RequestMapping(value = "/changeMeetRoom", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功"),
            @ApiResponse(code = 500, message = "修改失败")
    })
    public JSONObject changeMeetRoom(@RequestParam @ApiParam(value = "需要修改的Id",required = true) Integer id, @RequestParam @ApiParam(value = "名称",required = true) String name, @RequestParam @ApiParam(value = "容量",required = true) String capacity, @RequestParam @ApiParam(value = "面积", required = true) String acreage, @RequestParam @ApiParam(value = "区域", required = true) String area, @RequestParam @ApiParam(value = "类型", required = true) Integer type, @RequestParam @ApiParam(value = "状态", required = true) Integer status,@RequestParam(value = "des",required = false)@ApiParam(value = "备注",required = false) String des){
        JSONObject respJson = new JSONObject();
        try{
        	Meetroom m = new Meetroom();
        	m.setName(name);
        	QueryWrapper<Meetroom> wrapper = new QueryWrapper<Meetroom>(m);
        	m = this.meetroomService.getOne(wrapper);
        	if(m != null && m.getId() != id){
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议室名称已存在");
                return respJson;
        	}
        	
            Meetroom meetroom = new Meetroom();
            meetroom.setId(id);
            meetroom.setName(name);
            meetroom.setCapacity(capacity);
            meetroom.setAcreage(acreage);
            meetroom.setArea(area);
            meetroom.setType(type);
            meetroom.setStatus(status);
            meetroom.setDes(des);
            meetroomService.updateById(meetroom);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "修改成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "删除会议室信息接口")
    @RequestMapping(value = "/delMeetRoom", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 500, message = "系统异常")
    })
    public JSONObject delMeetRoom(@RequestParam @ApiParam(value = "需要删除的Id",required = true) List<Integer> id){
        JSONObject respJson = new JSONObject();
        try{
            meetroomService.removeByIds(id);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "删除成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "查询会议室信息接口")
    @RequestMapping(value = "/getMeetRoom", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getMeetRoom(@RequestParam @ApiParam(value = "当前页",required = true) Long currentPage){
        JSONObject respJson = new JSONObject();
        try{
            Page page = new Page();
            page.setSize(10);
            page.setCurrent(currentPage);
            User loginUser = (User) request.getSession().getAttribute("user");
            List<ZTreeNode> selectDepsByUser = departmentService.listDepsByUser(loginUser.getDepartment(),loginUser.getDomainId());
            QueryWrapper<Meetroom> wrapper = new QueryWrapper<Meetroom>();
            List<String> str = new ArrayList<String>();
            for (ZTreeNode zTreeNode : selectDepsByUser) {
            	str.add(zTreeNode.getId());
			}
            wrapper.eq("DEPARTMENT_ID", loginUser.getDepartment()).or().in("DEPARTMENT_ID", str);
            IPage<Meetroom> meetroomIPage = meetroomService.page(page,wrapper);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<Meetroom> resultMeetRoom = new ArrayList<>();
            for(int i=0;i<meetroomIPage.getRecords().size();i++){
                Meetroom resMeet = new Meetroom();
                resMeet.setId(meetroomIPage.getRecords().get(i).getId());
                resMeet.setName(meetroomIPage.getRecords().get(i).getName());
                resMeet.setCapacity(meetroomIPage.getRecords().get(i).getCapacity());
                resMeet.setAcreage(meetroomIPage.getRecords().get(i).getAcreage());
                resMeet.setArea(meetroomIPage.getRecords().get(i).getArea());
                resMeet.setType(meetroomIPage.getRecords().get(i).getType());
                resMeet.setStatus(meetroomIPage.getRecords().get(i).getStatus());
                resMeet.setDes(meetroomIPage.getRecords().get(i).getDes());
                resMeet.setCreator(meetroomIPage.getRecords().get(i).getCreator());
                resMeet.setCreatTime(meetroomIPage.getRecords().get(i).getCreatTime());
                resMeet.setUpdateTime(meetroomIPage.getRecords().get(i).getUpdateTime());
                resMeet.setDepartmentId(meetroomIPage.getRecords().get(i).getDepartmentId());
                if (meetroomIPage.getRecords().get(i).getUseBegin() != null && !meetroomIPage.getRecords().get(i).getUseBegin().isEmpty()){
                    resMeet.setSusTime(meetroomIPage.getRecords().get(i).getUseBegin()+"~"+meetroomIPage.getRecords().get(i).getUseEnd());//持续时间
                }else {
                    resMeet.setSusTime("-");//持续时间
                }
                resultMeetRoom.add(resMeet);
            }
            resultMap.put("records", resultMeetRoom);
            resultMap.put("total", meetroomIPage.getTotal());
            resultMap.put("size", meetroomIPage.getSize());
            resultMap.put("current", meetroomIPage.getCurrent());
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", resultMap);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "查询空闲中的会议室信息接口")
    @RequestMapping(value = "/getFreeMeetRoom", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getFreeMeetRoom(){
        JSONObject respJson = new JSONObject();
        try{
            User loginUser = (User) request.getSession().getAttribute("user");
            List<Meetroom> meetroomList = meetroomService.getFreeMeetRoom(loginUser.getDepartment());
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", meetroomList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "查询会议室使用情况接口")
    @RequestMapping(value = "/getMrUseDetails", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getMrUseDetails(@RequestParam @ApiParam(value = "年份",required = true) String year, @RequestParam @ApiParam(value = "月份" , required = true) String month){
        JSONObject respJson = new JSONObject();
        try{
            User loginUser = (User) request.getSession().getAttribute("user");
            List<ZTreeNode> selectDepsByUser = departmentService.listDepsByUser(loginUser.getDepartment(),loginUser.getDomainId());
            QueryWrapper<Meetroom> wrapper = new QueryWrapper<Meetroom>();
            List<String> str = new ArrayList<String>();
            for (ZTreeNode zTreeNode : selectDepsByUser) {
            	str.add(zTreeNode.getId());
			}
            wrapper.in("DEPARTMENT_ID", str);
            List<Meetroom> list = this.meetroomService.list(wrapper);
            
            List<Meet> meetList = meetService.getMrUseDetails(year,month,list);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", meetList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

}
