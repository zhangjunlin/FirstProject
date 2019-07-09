package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
@Api(tags = "部门操作接口")
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {
	
	@ApiOperation(value = "查询所有部门树形接口")
	@RequestMapping(value = "selectDepartments", method = RequestMethod.GET)
	public JSONObject selectDepartments() {
		JSONObject respJson = new JSONObject();
		try{
			//原始数据
			List<ZTreeNode> listAlldeps = departmentService.listAlldeps();
			 // 最后的结果
		    List<ZTreeNode> departmentList = new ArrayList<ZTreeNode>();
			// 先找到所有的一级菜单
		    for (int i = 0; i < listAlldeps.size(); i++) {
		        // 一级菜单没有parentId
		        if (listAlldeps.get(i).getpId()==null) {
		        	departmentList.add(listAlldeps.get(i));
		        }
		    }
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (ZTreeNode tree : departmentList) {
		    	List<ZTreeNode> list = departmentService.getChild(tree.getId(), listAlldeps);
		    	tree.setChildren(list);
		    }
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取部门成功");
            respJson.put("data", departmentList);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常!");
	         e.printStackTrace();
		}
		return respJson;
	}
	
	@ApiOperation(value = "查询所有用户域树形接口")
	@RequestMapping(value = "selectDomains", method = RequestMethod.GET)
	public JSONObject selectDomains() {
		JSONObject respJson = new JSONObject();
		try{
			//原始数据
			List<ZTreeNode> listAllDomains = departmentService.listAllDomains();
			 // 最后的结果
		    List<ZTreeNode> domainList = new ArrayList<ZTreeNode>();
			// 先找到所有的一级菜单
		    for (int i = 0; i < listAllDomains.size(); i++) {
		        // 一级菜单没有parentId
		        if (listAllDomains.get(i).getpId()==null) {
		        	domainList.add(listAllDomains.get(i));
		        }
		    }
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (ZTreeNode tree : domainList) {
		    	List<ZTreeNode> list = departmentService.getChild(tree.getId(), listAllDomains);
		    	tree.setChildren(list);
		    }
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取域成功");
            respJson.put("data", domainList);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常!");
	         e.printStackTrace();
		}
		return respJson;
	}
	
	@ApiOperation(value = "查询省厅域树形接口")
	@RequestMapping(value = "listByStDomains", method = RequestMethod.GET)
	public JSONObject listByStDomains() {
		JSONObject respJson = new JSONObject();
		try{
			//查询level为1的域
			Department dep = new Department();
			dep.setLevel("1");
			QueryWrapper<Department> wrapper = new QueryWrapper<Department>(dep);
			//域或部门的集合
			List<Department> listdepartmentIds = departmentService.list(wrapper);
			//部门或者域id的集合
			List<String> listDepId = new ArrayList<String>();
			if(listdepartmentIds.size() > 0){
				for (Department deps : listdepartmentIds) {
					listDepId.add(deps.getDepartmentMoid());
				}
			}
			//根据域id的集合查询域的树型结构
			List<ZTreeNode> listAllDomains = departmentService.listByStDomains(listDepId);
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取域成功");
            respJson.put("data", listAllDomains);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常!");
	         e.printStackTrace();
		}
		return respJson;
	}
	
	@SuppressWarnings("unused")
	@ApiOperation(value = "通过登录用户查询该用户所在部门以及下级部门树形接口")
	@RequestMapping(value = "selectDepsByUser", method = RequestMethod.GET)
	public JSONObject selectDepsByUser() {
	
		JSONObject respJson = new JSONObject();
		try{
			User us = (User) request.getSession().getAttribute("user");
			Domainuser domain = new Domainuser();
			domain.setUsername(us.getAccount());
			QueryWrapper<Domainuser> wrapper = new QueryWrapper<Domainuser>(domain);
			//查询域与用户的关系
			Domainuser domains = domainService.getOne(wrapper);
			
			List<ZTreeNode> selectDepsByUser  = new ArrayList<ZTreeNode>();
			if(us != null){
				 // 最后的结果
			    List<ZTreeNode> departmentList = new ArrayList<ZTreeNode>();
				//原始数据
				if(domains != null){
					selectDepsByUser = departmentService.listDepsByUseradmin(us.getDomainId());
					// 先找到所有的一级菜单
				    for (int i = 0; i < selectDepsByUser.size(); i++) {
				        // 一级菜单为域
				    	if (domains.getMoid().equals(selectDepsByUser.get(i).getId())) {
				        	departmentList.add(selectDepsByUser.get(i));
				        }
				    }
				}else{
					selectDepsByUser = departmentService.listDepsByUser(us.getDepartment(), us.getDomainId());
					// 先找到所有的一级菜单
				    for (int i = 0; i < selectDepsByUser.size(); i++) {
				        // 一级菜单为域
				        if ("0".equals(selectDepsByUser.get(i).getIsdomain())) {
				        	departmentList.add(selectDepsByUser.get(i));
				        }
				    }
				}		 
			    // 为一级菜单设置子菜单，getChild是递归调用的
			    for (ZTreeNode tree : departmentList) {
			    	tree.setChildren(departmentService.getChild(tree.getId(), selectDepsByUser));
			    }
				respJson.put("code", ResultCode.SUCCESS);
	            respJson.put("msg", "获取部门成功");
	            respJson.put("data", departmentList);
			}else{
				respJson.put("code", ResultCode.ERROR);
	            respJson.put("msg", "当前登录人不能为空!");
			}
			
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常!");
	         e.printStackTrace();
		}
		return respJson;
	}
	
	
}
