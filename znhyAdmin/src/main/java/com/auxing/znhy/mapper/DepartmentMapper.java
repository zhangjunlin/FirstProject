package com.auxing.znhy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
public interface DepartmentMapper extends BaseMapper<Department> {
	List<ZTreeNode> listAlldeps();
	List<ZTreeNode> listAllDomains();
	List<ZTreeNode> listDepsByUseradmin(@Param("domain_id")String domain_id);
	List<ZTreeNode> listDepsByUser(@Param("moid")String moid,@Param("domain_id")String domain_id);
	List<ZTreeNode> listByStDomains(@Param("domain_ids")List<String> domain_ids);//查询省厅以及直属下一级的域
	Department getDepByChildren(@Param("userDep")String userDep, @Param("department")String department);
}
