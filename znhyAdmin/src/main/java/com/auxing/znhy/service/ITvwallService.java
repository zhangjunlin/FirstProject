package com.auxing.znhy.service;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.entity.Tvwall;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-13
 */
public interface ITvwallService extends IService<Tvwall> {

    void addTvWall(String name, Integer row, Integer column, List<Channel> channelList,String modid);

    int changeTvWall(Integer id, String name, Integer row, Integer column, List<Channel> channelList);

    int deleteByIds(List<Integer> list);
}
