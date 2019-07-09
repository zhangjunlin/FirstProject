package com.auxing.znhy.service;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.ZTreeNode;
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
public interface IAuthService extends IService<Auth> {

    List<Auth> getBindList(String modId);

    List<ZTreeNode> findTreeDetails();

    List<Auth> getBindLevelOneList(String modId);

    List<Auth> getBindLevelOtherList(String modId);
}
