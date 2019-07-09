package com.auxing.znhy.service.impl;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.Roleauth;
import com.auxing.znhy.mapper.RoleauthMapper;
import com.auxing.znhy.service.IRoleauthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
@Service
public class RoleauthServiceImpl extends ServiceImpl<RoleauthMapper, Roleauth> implements IRoleauthService {

    @Resource
    private RoleauthMapper roleauthMapper;

    @Override
    public void removeByRoleId(String roleId) {
        roleauthMapper.removeByRoleId(roleId);
    }

    @Override
    public void removeByAuthId(String authId) {
        roleauthMapper.removeByAuthId(authId);
    }

    @Override
    public List<Auth> getBindAuthList(String roleId) {
        return roleauthMapper.getBindAuthList(roleId);
    }
}
