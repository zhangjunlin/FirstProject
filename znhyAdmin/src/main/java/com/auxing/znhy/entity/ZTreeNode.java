package com.auxing.znhy.entity;

import java.util.List;


/**
 * 封装ztree数据格式的工具类
 * 前端需要使用simpleData模式
 * @author auxing
 *
 */

public class ZTreeNode{
	private String id;
    private String pId;  
    private String name;  
    private Boolean checked; 
    private Boolean open;
    private Integer type = 0;	//数据类型默认0 0-部门 1-用户
    private Boolean isParent;  
    private String code;
    private String icon ;
    private String isdomain;//0:是域,1:部门
    private String e164;
    
    private List<ZTreeNode> children;
    
	public ZTreeNode(String id, String pId, String name, Boolean checked, Boolean open,Integer type, Boolean isParent,String code) {  
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.checked = checked;
        this.open = open;
        this.type = type;
        this.isParent = isParent;
        this.code = code;
    }
    
    
    public ZTreeNode() {
        super();
    }
    
    
    public ZTreeNode(String id, String pId, String name, Integer type ,String e164) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.type = type;
		this.e164 = e164;
	}


	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	public List<ZTreeNode> getChildren() {
		return children;
	}


	public void setChildren(List<ZTreeNode> children) {
		this.children = children;
	}


	public String getIsdomain() {
		return isdomain;
	}


	public void setIsdomain(String isdomain) {
		this.isdomain = isdomain;
	}


	public String getE164() {
		return e164;
	}


	public void setE164(String e164) {
		this.e164 = e164;
	}

	
}
