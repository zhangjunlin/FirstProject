package com.auxing.znhy.mapper;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.Roleauth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
public interface RoleauthMapper extends BaseMapper<Roleauth> {

    void removeByRoleId(String roleId);

    void removeByAuthId(String authId);

    List<Auth> getBindAuthList(String roleId);
}
