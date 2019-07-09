package com.auxing.znhy.service;

import java.util.List;

import javax.management.ServiceNotFoundException;

import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.util.MeetingResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
public interface IDepartmentService extends IService<Department> {
   MeetingResult getCommonDomains();
   MeetingResult getCommonDeps(String username,String password);
   void SynchronousDomain();//自动同步域和部门到本地数据库
   void SynchronousDomainByHand();//手动同步域和部门到本地数据库
   List<ZTreeNode> listAlldeps();//查询部门
   List<ZTreeNode> listAllDomains();//查询域
   List<ZTreeNode> listDepsByUseradmin(String domain_id);
   List<ZTreeNode> listDepsByUser(String moid,String domain_id);
   List<ZTreeNode> getChild(String id, List<ZTreeNode> selectDepsByUser);
   List<ZTreeNode> getUserChild(String id, List<ZTreeNode> listAlldeps, List<String> ids);
   List<ZTreeNode> listByStDomains(List<String> domain_ids);//域或部门以及直属下一级域或部门的树查询
   Department getDepByChildren(String userDep, String department);
}
