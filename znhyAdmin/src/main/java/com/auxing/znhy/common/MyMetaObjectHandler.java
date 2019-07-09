package com.auxing.znhy.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.User;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;


/**
 * @auther liuyy
 * @date 2018/10/15 0015 下午 2:09
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    protected HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
    	if(metaObject.hasGetter("creator") || metaObject.hasGetter("creatTime") || metaObject.hasGetter("updateTime") || metaObject.hasGetter("departmentId")){
            User loginUser = (User) request.getSession().getAttribute("user");
            setFieldValByName("creator",loginUser.getAccount(),metaObject);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            setFieldValByName("creatTime",df.format(new Date()),metaObject);
            setFieldValByName("updateTime",df.format(new Date()),metaObject);
            setFieldValByName("departmentId",loginUser.getDepartment(),metaObject);
    	}
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    	if(metaObject.hasGetter("creator") || metaObject.hasGetter("creatTime") || metaObject.hasGetter("updateTime") || metaObject.hasGetter("departmentId")){
            insertFill(metaObject);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            setFieldValByName("updateTime",df.format(new Date()), metaObject);
    	}
    }
}
