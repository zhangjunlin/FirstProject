package com.auxing.znhy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.entity.Tvwall;
import com.auxing.znhy.mapper.ChannelMapper;
import com.auxing.znhy.mapper.TvwallMapper;
import com.auxing.znhy.service.ITvwallService;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-13
 */
@Service
public class TvwallServiceImpl extends ServiceImpl<TvwallMapper, Tvwall> implements ITvwallService {

    @Autowired
    TvwallMapper tvwallMapper;

    @Autowired
    ChannelMapper channelMapper;

    /**
     *
     * 新增电视墙
     */
    @Override
    public void addTvWall(String name, Integer row, Integer column, List<Channel> channelList,String modid) {
        Tvwall tvwall = new Tvwall();
        tvwall.setName(name);
        tvwall.setLine(row);
        tvwall.setCol(column);
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        tvwall.setTelevisionId(uuid);
        tvwall.setCreateId(modid);
        tvwallMapper.insert(tvwall);
        if(channelList != null && channelList.size() != 0){
            for (int i = 0;i<channelList.size();i++){
                Channel channel = new Channel();
                channel.setTelevisionId(uuid);
                channel.setDeviceName(channelList.get(i).getDeviceName());
                channel.setChannelId(channelList.get(i).getChannelId());
                channel.setLine(channelList.get(i).getLine());
                channel.setCol(channelList.get(i).getCol());
                channelMapper.insert(channel);
            }
        }
    }

    /**
     *
     * 修改电视墙
     */
    @Override
    public int changeTvWall(Integer id, String name, Integer row, Integer column, List<Channel> channelList) {
        int flag = 0; //0可以修改 1不可以修改
        //获取该电视墙信息
        Tvwall tvwall_old = tvwallMapper.selectById(id);
        //根据电视墙ID获得绑定的通道号信息
        List<Channel> chlist = channelMapper.getByTvId(tvwall_old.getTelevisionId());
        if(chlist != null && chlist.size() != 0){
            for (int i=0;i<chlist.size();i++) {
                if (channelList.get(i).getAlias() != null) {
                    flag = 1;
                } else {
                    flag = 0;
                }
            }
        }else{
            flag = 0;
        }
        if(flag == 1){
            //不可以修改
        }else{
            //可以修改
            //更新电视墙
            Tvwall tvwall = new Tvwall();
            tvwall.setId(id);
            tvwall.setName(name);
            tvwall.setLine(row);
            tvwall.setCol(column);
            tvwallMapper.updateById(tvwall);
            if(channelList != null && channelList.size() != 0){
                //删除原有所有旧通道
                Channel channel = new Channel();
                channel.setTelevisionId(tvwall_old.getTelevisionId());
                QueryWrapper<Channel> wrapper = new QueryWrapper<Channel>(channel);
                channelMapper.delete(wrapper);
                for (int i=0;i<channelList.size();i++){
                    channel.setTelevisionId(tvwall_old.getTelevisionId());
                    channel.setDeviceName(channelList.get(i).getDeviceName());
                    channel.setChannelId(channelList.get(i).getChannelId());
                    channel.setLine(channelList.get(i).getLine());
                    channel.setCol(channelList.get(i).getCol());
                    channelMapper.insert(channel);
                }
            }

        }
        return flag;
    }

    /**
     *
     * 删除电视墙
     */
    @Override
    public int deleteByIds(List<Integer> list) {
        int flag = 0;
        for(int id:list){
            //根据Id循环判断电视墙是否有正在使用中的
            Tvwall tvwall = tvwallMapper.selectById(id);
            List<Channel> channelList= channelMapper.getByTvId(tvwall.getTelevisionId());
            if (channelList != null && channelList.size() != 0){
                for (int i=0;i<channelList.size();i++){
                    if(channelList.get(i).getAlias() != null){
                        flag = 1;
                    }
                }
            }
        }
        if (flag == 1){

        }else {
            for (int id:list){
                Tvwall tvwall = tvwallMapper.selectById(id);
                //删除电视墙
                tvwallMapper.deleteById(id);
                //删除通道
                Channel channel = new Channel();
                channel.setTelevisionId(tvwall.getTelevisionId());
                QueryWrapper<Channel> wrapper = new QueryWrapper<Channel>(channel);
                channelMapper.delete(wrapper);
            }
        }
        return flag;
    }





}
