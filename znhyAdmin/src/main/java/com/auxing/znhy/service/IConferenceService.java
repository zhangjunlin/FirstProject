package com.auxing.znhy.service;

import java.util.List;
import java.util.Map;

import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.util.HduResult;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.meeting.CascadeMts;
import com.auxing.znhy.util.meeting.Cascades;
import com.auxing.znhy.util.meeting.OpenConfParam;
import com.auxing.znhy.util.meeting.TerminalWatch;
import com.auxing.znhy.util.meeting.Vrs;

/**
 * @auther liuyy
 * @date 2018/10/26 0026 下午 4:16
 */

public interface IConferenceService {

	Cascades getAttendee(MeetInterface meetInterface,String m);
    //MeetingResult OpenConfRecord(String conf_id,Recorders recorders) throws Exception;//开启录像
    MeetingResult StopConfRecord(String conf_id,String rec_id,Integer recorder_mode,MeetInterface meetInterface);//停止录像
    MeetingResult ConfSynthesis(String conf_id,MeetInterface meetInterface);//获取画面合成信息
    MeetingResult OpenConfSynthesis(OpenConfParam openConfParam, MeetInterface meetInterface);//开启画面合成
    MeetingResult UpdatePictureSynthesis(OpenConfParam openConfParam,MeetInterface meetInterface);//修改画面合成
    MeetingResult StopConfSynthesis(String conf_id,MeetInterface meetInterface);//停止画面合成
    MeetingResult GetConfRecord(String conf_id,String rec_id, MeetInterface meetInterface);//获取录像状态
    MeetingResult UpdateConfRecord(String conf_id,String rec_id,Integer value,Integer recorder_mode, MeetInterface meetInterface);//修改录像状态
    MeetingResult GetConfRecordList(String conf_id, MeetInterface meetInterface);//获取录像列表
    MeetingResult DelayTime(String conf_id,String delay_time, MeetInterface meetInterface );//延长会议时间
    MeetingResult stopConfs(String conf_id, MeetInterface meetInterface);//结束会OpenConfRecord议
    MeetingResult SendMessage(String conf_id, Integer type, Integer roll_num, Integer roll_speed, String message, List<String> mtsList, MeetInterface meetInterface);//发送短消息
    MeetingResult addThisConfTerminal(String confId, List<String> accountList, MeetInterface meetInterface);//添加本级会议终端
    MeetingResult addOtherConfTerminal(String confId, String cascadeId, List<String> accountList, MeetInterface meetInterface);//添加级联会议终端
    MeetingResult getConfCascades(String confId, MeetInterface meetInterface);//获取该会议的级联会议信息
    MeetingResult openSoundMixing(Integer number, String confId, List<String> accountList, MeetInterface meetInterface);//混音设置
    MeetingResult addSmChannel(String confId, List<String> accountList, MeetInterface meetInterface);//添加混音成员
    MeetingResult delSmChannel(String confId, List<String> accountList, MeetInterface meetInterface);//删除混音成员
    MeetingResult confPolling(String confId, Integer mode, Integer num, Integer keepTime, List<String> accountList, MeetInterface meetInterface);//会议轮询&主席轮询
    MeetingResult getWatchTerminal(String confId, MeetInterface meetInterface);//获取终端选看列表
    MeetingResult openHdupoll(String confId, List<Channel> list, List<String> members, Integer keepTime,Integer num,Integer showMtName, MeetInterface meetInterface);//开启电视墙批量轮询
    MeetingResult changeHdupollState(String confId,Integer value,MeetInterface meetInterface);//修改电视墙批量轮询状态
	MeetingResult getMeet(MeetInterface meetInterface ,String e164);
    MeetingResult delWatchTerminal(String confId, String mt_id, MeetInterface meetInterface);//取消终端选看
    MeetingResult getIsJl(MeetInterface meetInterface,String meetid);//判断是否有级联
	MeetingResult openHdu(String confId, String hduId, String mt_id, MeetInterface meetInterface);
	MeetingResult closeHdu(String confId, String hduId, MeetInterface meetInterface);
	MeetingResult getHdus(String confId, MeetInterface meetInterface);
	MeetingResult getHdupoll(String confId,  MeetInterface meetInterface);
	MeetingResult neediframe(Integer v_port, Integer a_port, String ip, String confId, MeetInterface meetInterface);
	MeetingResult heartbeat(Integer v_port, Integer a_port, String ip, MeetInterface meetInterface, String confId);
	MeetingResult closeHdupoll(String confId ,MeetInterface meetInterface);
	MeetingResult videoMonitors(MeetInterface meetInterface, String confId, String ip, Integer v_port, String members);
	MeetingResult audioMonitors(MeetInterface meetInterface, String confId, String ip, Integer a_port, String members);
	Map<String,Object> getMonitors(MeetInterface meetInterface, String confId, String ip, Integer v_port);
    MeetingResult Mute(String conf_id,int value, MeetInterface meetInterface);//全场哑音
    MeetingResult KeepQuiet(String conf_id,int value, MeetInterface meetInterface);//全场静音List<String> mtsList
    MeetingResult CallOffLine(String conf_id, MeetInterface meetInterface);//全场静音
    MeetingResult getMixMesg(String confId ,MeetInterface meetInterface);
    MeetingResult getChairman(String confId, MeetInterface meetInterface);
    MeetingResult setPollState(String confId,Integer value, MeetInterface meetInterface );
    MeetingResult OpenConfRecord(String conf_id, String mode, String name, String publishMode, String anonymous,MeetInterface meetInterface, List<Vrs> vrsList);//开启录像
	MeetingResult callMtId(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult hangUpMtId(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult openSilence(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult closeSilence(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult appointSpeaker(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult cancelSpeaker(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult appointChairman(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult cancelChairman(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult appointDualstream(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult cancelDualstream(String confId, String mt_id, MeetInterface meetInterface);
	MeetingResult getMeetChairman(MeetInterface meetInterface, String conf_id, List<CascadeMts> children);
	MeetingResult getMeetSpeaker(MeetInterface meetInterface, String conf_id);
	MeetingResult deleteMts(String conf_id,List<String> mtsList, MeetInterface meetInterface);//批量删除终端接口
	MeetingResult setWatchTerminal(String confId, MeetInterface meetInterface, String src, String dst,Integer mode);
	MeetingResult delWatchTerminalAll(String confId, MeetInterface meetInterface, List<TerminalWatch> list);
	MeetingResult closeSoundMixing(String confId, MeetInterface meetInterface);
	MeetingResult getVrsParams(MeetInterface meetInterface);


    MeetingResult getPollDetails(String confId, MeetInterface meetInterface);
	MeetingResult getChairmanPollDetails(String confId, MeetInterface meetInterface);
	MeetingResult confChairmanPolling(String confId, Integer mode, Integer num, Integer keepTime,
			List<String> accountList, MeetInterface meetInterface);
	MeetingResult setChairmanPollState(String confId, Integer value, MeetInterface meetInterface,Integer mode);
	MeetingResult openHduVmps(MeetInterface meetInterface, OpenConfParam openConfParam);
	MeetingResult getHduVmps(MeetInterface meetInterface, String conf_id, String hdu_id);
	MeetingResult cancelHduVmps(MeetInterface meetInterface, String conf_id, String hdu_id);
	HduResult getHdu(MeetInterface meetInterface, String confId, String channelId);
	MeetingResult openHduChairman(String confId, String hdus, MeetInterface meetInterface);
	MeetingResult openHduSpeaker(String confId, String hduId, MeetInterface meetInterface);
	MeetingResult openHduDualstream(String confId, String hduId, MeetInterface meetInterface);
	MeetingResult openHduMeetPoll(String confId, String hduId, MeetInterface meetInterface);
	MeetingResult openHduChannelPoll(String confId, String hduId, MeetInterface meetInterface, Integer num, Integer keepTime, List<String> accountList);
	MeetingResult openHduSplitScreen(String confId, String hduId, MeetInterface meetInterface, List<String> accountList,
			List<Integer> chnIdxList);
	MeetingResult getHduVmpsMts(MeetInterface meetInterface, String conf_id);
	MeetingResult getHduVmpsHdus(MeetInterface meetInterface, String conf_id);
	MeetingResult updateHduVmps(MeetInterface meetInterface, OpenConfParam openConfParam);
}
