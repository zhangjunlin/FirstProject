package com.auxing.znhy.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Userrole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
public interface IUserroleService extends IService<Userrole> {
	JSONObject AaveAndDelete(String id,List<String>listIds);

    void removeByRoleId(String roleId);
}
