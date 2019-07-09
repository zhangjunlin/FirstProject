package com.auxing.znhy.mapper;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.Domainuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-19
 */
public interface DomainuserMapper extends BaseMapper<Domainuser> {

	void updateSubsription(@Param("domainId")String domainId, @Param("subsription")Integer subsription);

	void updateAllSubsription();
}
