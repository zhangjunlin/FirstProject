package com.auxing.znhy.service.impl;

import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.mapper.ChannelMapper;
import com.auxing.znhy.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-15
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements IChannelService {
	
    @Resource
    private ChannelMapper channelMapper;
	
	
	@Override
	public void updateByConfId(String confId) {
		this.channelMapper.updateByConfId(confId);
	}


	@Override
	public void updateByConfIdHdu(String confId, String hduId) {
		this.channelMapper.updateByConfIdHdu(confId, hduId);
	}

}
