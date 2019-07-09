package com.auxing.znhy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Userrole;
import com.auxing.znhy.mapper.UserroleMapper;
import com.auxing.znhy.service.IUserroleService;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
@Service
public class UserroleServiceImpl extends ServiceImpl<UserroleMapper, Userrole> implements IUserroleService {

	@Autowired
	UserroleMapper userroleMapper;

	@Override
	public synchronized JSONObject AaveAndDelete(String id, List<String> listIds) {
		JSONObject respJson = new JSONObject();
		 Userrole userrole = new Userrole();
 	     userrole.setUserId(id);
		 QueryWrapper<Userrole> wrapper = new QueryWrapper<Userrole>(userrole);
 		 this.remove(wrapper);
 		 for (String ids : listIds) {
 			 Userrole userRole = new Userrole(); 
 			 userRole.setUserId(id);
 			 userRole.setRoleId(ids);
 			 this.save(userRole);
		}
 		respJson.put("code", ResultCode.SUCCESS);
 		respJson.put("msg", "角色分配成功!");
		return respJson;
		
	}

	@Override
	public void removeByRoleId(String roleId) {
		this.userroleMapper.removeByRoleId(roleId);
	}

}
