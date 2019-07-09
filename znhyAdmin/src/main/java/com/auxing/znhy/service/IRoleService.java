package com.auxing.znhy.service;

import java.util.List;

import com.auxing.znhy.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
public interface IRoleService extends IService<Role> {

	List<Role> findRolesByUserId(String id);

    void updateByRoleId(Role role);
}
