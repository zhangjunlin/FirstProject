package com.auxing.znhy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
public interface RoleMapper extends BaseMapper<Role> {

	List<Role> findRolesByUserId(@Param("id") String id);

	void updateByRoleId(@Param("role") Role role);
}
