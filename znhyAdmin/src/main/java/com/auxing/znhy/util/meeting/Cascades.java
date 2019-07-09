package com.auxing.znhy.util.meeting;

import java.util.List;

/**
 * @auther liuyy
 * @date 2018/10/26 0026 下午 4:02
 */

public class Cascades {
    private String conf_id;					//会议号
    private String name;                    //会议名称 最大字符长度：64个字节
    private String cascade_id;              //级联号 最大字符长度：48个字节
    private String mt_id;                   //终端号 最大字符长度：48个字节
    private List<Cascades> cascades;        //级联数组
    private String pid;
    private String id;
    List <CascadeMts> childrens;//集合

    public String getConf_id() {
        return conf_id;
    }

    public void setConf_id(String conf_id) {
        this.conf_id = conf_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCascade_id() {
        return cascade_id;
    }

    public void setCascade_id(String cascade_id) {
        this.cascade_id = cascade_id;
    }

    public String getMt_id() {
        return mt_id;
    }

    public void setMt_id(String mt_id) {
        this.mt_id = mt_id;
    }

    

	public List<Cascades> getCascades() {
		return cascades;
	}

	public void setCascades(List<Cascades> cascades) {
		this.cascades = cascades;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cascades() {
		super();
	}


	public Cascades(String conf_id, String name, String cascade_id,
			String mt_id, String id) {
		super();
		this.conf_id = conf_id;
		this.name = name;
		this.cascade_id = cascade_id;
		this.mt_id = mt_id;
		this.id = id;
	}

	public Cascades(String conf_id, String name, String cascade_id,
			String mt_id, String pid, String id) {
		super();
		this.conf_id = conf_id;
		this.name = name;
		this.cascade_id = cascade_id;
		this.mt_id = mt_id;
		this.pid = pid;
		this.id = id;
	}

	public Cascades(String conf_id, String name, String cascade_id, String mt_id) {
		super();
		this.conf_id = conf_id;
		this.name = name;
		this.cascade_id = cascade_id;
		this.mt_id = mt_id;
	}

	public List<CascadeMts> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<CascadeMts> childrens) {
		this.childrens = childrens;
	}

    
}
