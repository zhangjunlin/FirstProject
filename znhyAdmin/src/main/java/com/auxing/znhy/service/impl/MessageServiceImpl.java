package com.auxing.znhy.service.impl;

import com.auxing.znhy.entity.Message;
import com.auxing.znhy.mapper.MessageMapper;
import com.auxing.znhy.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-11-12
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
