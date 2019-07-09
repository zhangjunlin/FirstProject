package com.auxing.znhy.service;

import com.auxing.znhy.entity.Channel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-15
 */
public interface IChannelService extends IService<Channel> {

	void updateByConfId(String confId);

	void updateByConfIdHdu(String confId, String hduId);

}
