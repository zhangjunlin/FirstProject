package com.auxing.znhy.service.impl;

import com.auxing.znhy.entity.Meetroom;
import com.auxing.znhy.mapper.MeetroomMapper;
import com.auxing.znhy.service.IMeetroomService;
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
 * @since 2018-10-16
 */
@Service
public class MeetroomServiceImpl extends ServiceImpl<MeetroomMapper, Meetroom> implements IMeetroomService {
    @Autowired
    MeetroomMapper meetroomMapper;

    @Override
    public List<Meetroom> getFreeMeetRoom(String department) {
        return meetroomMapper.getFreeMeetRoom(department);
    }
}
