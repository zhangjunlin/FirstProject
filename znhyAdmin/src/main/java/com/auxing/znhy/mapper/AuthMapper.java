package com.auxing.znhy.mapper;

import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
public interface AuthMapper extends BaseMapper<Auth> {

    List<Auth> getAuthBindListById(@Param("modId") String modId);

    List<ZTreeNode> findTreeDetails();

    List<Auth> getBindLevelOneList(@Param("modId") String modId);

    List<Auth> getBindLevelOtherList(@Param("modId") String modId);
}
