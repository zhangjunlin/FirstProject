package com.auxing.znhy.service;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.Roleauth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
public interface IRoleauthService extends IService<Roleauth> {

    void removeByRoleId(String roleId);

    void removeByAuthId(String authId);

    List<Auth> getBindAuthList(String roleId);
}
