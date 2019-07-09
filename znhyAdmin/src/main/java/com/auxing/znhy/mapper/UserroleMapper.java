package com.auxing.znhy.mapper;

import com.auxing.znhy.entity.Userrole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
public interface UserroleMapper extends BaseMapper<Userrole> {

    void removeByRoleId(@Param("roleId") String roleId);
}
