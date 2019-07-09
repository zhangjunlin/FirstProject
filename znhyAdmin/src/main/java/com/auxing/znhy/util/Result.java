package com.auxing.znhy.util;


/**
 * 返回结果
 * 
 * @author Joe
 */
public class Result {

	/**
	 * 结果体
	 */
	protected Object data;

	/**
	 * 状态码
	 */
	protected Integer code;

	/**
	 * 信息
	 */
	protected String message;
	
	
	protected String userId;
	
	protected Integer count;
	
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	private Result() {
		super();
	}

	private Result(Integer code) {
		this.code = code;
	}

	public static Result create(Integer code) {
		return new Result(code);
	}

	public static Result createSuccessResult() {
		return create(ResultCode.SUCCESS);
	}

	public static Result createSuccessResult(Object data, String message) {
		return createSuccessResult().setData(data).setMessage(message);
	}

	public Object getData() {
		return data;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public Result setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}
}
