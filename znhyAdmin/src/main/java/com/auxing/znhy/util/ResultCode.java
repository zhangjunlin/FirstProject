package com.auxing.znhy.util;

/**
 * 基础返回码，具体业务返回码可继承ResultCode
 * 
 * @author Joe
 */
public class ResultCode {
	
	public final static int NO_LOGIN_ERROR = -1;// session过期
	public final static int SUCCESS = 200;// 成功
	public final static int CREATED = 201;//新资源被创建
	public final static int ACCEPTED = 202;//已接受处理请求但尚未完成（异步处理）
	public final static int MOVED_PERMANENTLY= 301;//资源的URI被更新
	public final static int SEE_OTHER = 303;//其他（如，负载均衡）
	public final static int BAD_REQUEST = 400;//指代坏请求
	public final static int NOT_FOUND= 404 ;//资源不存在
	public final static int NOT_ACCEPTABLE = 406;//服务端不支持所需表示
	public final static int CONFLICT = 409; //通用冲突
	public final static int PRECONDITION_FAILED = 412;//前置条件失败（如执行条件更新时的冲突）
	public final static int  UNSUPPORTED_MEDIA_TYPE = 415;// 接受到的表示不受支持
	public final static int INTERNAL_SERVER_ERROR = 500;// 通用错误响应
	public final static int SERVICE_UNAVAILABLE = 503;// 服务当前无法处理请求
	// 通用错误以9开头
	public final static int ERROR = 9999;// 未知错误
	public final static int APPLICATION_ERROR = 9000;// 应用级错误
	public final static int VALIDATE_ERROR = 9001;// 参数验证错误
	public final static int SERVICE_ERROR = 9002;// 业务逻辑验证错误
	public final static int DAO_ERROR = 9003;// 数据访问错误
	
	
}
