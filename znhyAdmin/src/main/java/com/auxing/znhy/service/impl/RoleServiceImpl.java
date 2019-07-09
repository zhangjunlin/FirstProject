package com.auxing.znhy.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auxing.znhy.entity.Role;
import com.auxing.znhy.mapper.RoleMapper;
import com.auxing.znhy.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
	@Resource
	private RoleMapper roleMapper;
	 
	@Override
	public List<Role> findRolesByUserId(String id) {
		return roleMapper.findRolesByUserId(id);
	}

	@Override
	public void updateByRoleId(Role role) {
		roleMapper.updateByRoleId(role);
	}

}
