package com.auxing.znhy.service.impl;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.mapper.AuthMapper;
import com.auxing.znhy.service.IAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements IAuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getBindList(String modId) {
        List<Auth> ls = authMapper.getAuthBindListById(modId);
        return ls;
    }

    @Override
    public List<ZTreeNode> findTreeDetails() {
        return authMapper.findTreeDetails();
    }

    @Override
    public List<Auth> getBindLevelOneList(String modId) {
        List<Auth> ls = authMapper.getBindLevelOneList(modId);
        return ls;
    }

    @Override
    public List<Auth> getBindLevelOtherList(String modId) {
        List<Auth> ls = authMapper.getBindLevelOtherList(modId);
        return ls;
    }
}
