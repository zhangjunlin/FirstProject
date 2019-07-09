package com.auxing.znhy.service;

import com.auxing.znhy.entity.Meetroom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-16
 */
public interface IMeetroomService extends IService<Meetroom> {

    List<Meetroom> getFreeMeetRoom(String department);
}
