package com.auxing.znhy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-09
 */
public interface UserMapper extends BaseMapper<User> {
	long selectTotalCount(@Param("user")User user);
	List<User> selectUserPage(@Param("page")Page<User> page, @Param("user")User user);
}
