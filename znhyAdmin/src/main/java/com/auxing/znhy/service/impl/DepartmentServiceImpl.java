package com.auxing.znhy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.mapper.DepartmentMapper;
import com.auxing.znhy.mapper.UserMapper;
import com.auxing.znhy.service.IDepartmentService;
import com.auxing.znhy.service.IDomainuserService;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingOrg;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.StringUtils;
import com.auxing.znhy.util.meeting.UserDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
	
	 @Autowired
	 private Environment env;
	
	 @Resource
	 private DepartmentMapper depMapper;
	 
	 @Resource
	 private UserMapper userMapper;
	 
	 @Autowired
	 private IDomainuserService domainUserService;
	 
	 //调用域接口,捞域的集合
	@Override
	public MeetingResult getCommonDomains(){
		Domainuser domainuser = new Domainuser();
		domainuser.setName("");
        MeetInterface meetInterface = null;
		try {
			meetInterface = new MeetInterface(env.getProperty("name"), env.getProperty("pwd"), env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/amc/userdomains",params, meetInterface.getCookie());
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
		}
		return result;
	}
	
	//调用部门组织接口
	@Override
	public MeetingResult getCommonDeps(String username,String password){
		 MeetInterface meetInterface = null;
		try {
			meetInterface = new MeetInterface(username,password, env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("account_token", meetInterface.getToken());
		 MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/amc/departments",params, meetInterface.getCookie());
		 if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
		}
		return result;
	}
	
	
	
	//将域以及部门自动同步到数据库
		public void SynchronousDomain(){
			/**
			 * 自动同步部门组织
			 */

			//查询域域用户的关系记录
			Domainuser doma = new Domainuser("1");
			QueryWrapper<Domainuser> wrappers = new QueryWrapper<Domainuser>(doma);
			List<Domainuser> listDomainusers = domainUserService.list(wrappers);
			
			for (Domainuser domainuser : listDomainusers) {
				//捞部门组织的数据
				 MeetingResult results = null;
				try {
					results = this.getCommonDeps(domainuser.getUsername(),domainuser.getPassword());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 List<MeetingOrg> listdeps = results.getDepartments();
				 Department department = new Department(results.getDepartment_moid());
				 QueryWrapper<Department> wrapper = new QueryWrapper<Department>(department);
				 Department depone = this.getOne(wrapper);
				 
				 Department dep = new Department(results.getDepartment_name(),results.getDepartment_moid(),domainuser.getMoid(),"1"); 
				if(depone != null){
					this.update(dep, wrapper);
				}else{
					this.save(dep);
				}
				//循环抓取的部门数据
				for (int i = 0; i < listdeps.size(); i++) {
					Department deporg = new Department();
					deporg.setDepartmentMoid(listdeps.get(i).getDepartment_moid());
					QueryWrapper<Department> wrapperone = new QueryWrapper<Department>(deporg);
					
					Department deptwo= this.getOne(wrapperone);//通过moid查询该数据
					
					//重新new一个对象(若使用之前的对象dep会将所有参数带入查询条件)
					Department deps = new Department(listdeps.get(i).getDepartment_name(),listdeps.get(i).getDepartment_moid(),dep.getDepartmentMoid(),"1");
					//若不存在该条记录,新增
					if(deptwo ==null){
						this.save(deps);
					}else{
						//存在,更新
						this.update(deps,wrapperone);
					}
					
					List<MeetingOrg> listDepNexts = listdeps.get(i).getDepartments();
					//调用递归
					getDepChild(listdeps.get(i).getDepartment_moid(),listDepNexts);
				}
				 
			}
		}
		
		//手动同步域和部门组织
		@Override
		public void SynchronousDomainByHand(){
			//更新前删除所有部门
			this.remove(null);
			//将域插入数据库
			MeetingResult result = this.getCommonDomains();
			//得到所有的域,并插入数据库
			List<UserDomain> listUserDomains = result.getUserDomains();
			for (UserDomain userDomain : listUserDomains) {
				Department deps = new Department(userDomain.getName(),userDomain.getMoid(),userDomain.getParentId(),"0");
				this.save(deps);
			}
			/**
			 * 手动同步部门
			 */
			//查询域域用户的关系记录
			Domainuser doma = new Domainuser("1");
			QueryWrapper<Domainuser> wrappers = new QueryWrapper<Domainuser>(doma);
			List<Domainuser> listDomainusers = domainUserService.list(wrappers);
			//循环域域用户关系的集合
			for (Domainuser domainuser : listDomainusers) {
				//捞部门组织的数据
				 MeetingResult results = this.getCommonDeps(domainuser.getUsername(),domainuser.getPassword());
				 
				 List<MeetingOrg> listdeps = results.getDepartments();
				 Department dep = new Department(results.getDepartment_name(),results.getDepartment_moid(),domainuser.getMoid(),"1"); 
				 this.save(dep);
				//循环抓取的部门数据
				for (int i = 0; i < listdeps.size(); i++) {
					//重新new一个对象(若使用之前的对象dep会将所有参数带入查询条件)
					Department deps = new Department(listdeps.get(i).getDepartment_name(),listdeps.get(i).getDepartment_moid(),dep.getDepartmentMoid(),"1");
					//若不存在该条记录,新增
					this.save(deps);
					List<MeetingOrg> listDepNexts = listdeps.get(i).getDepartments();
					//调用递归
					getDepChildByHand(listdeps.get(i).getDepartment_moid(),listDepNexts);
				}
			}
		}     
	
	//查询所有部门组织
	@Override
	public List<ZTreeNode> listAlldeps() {
		
		return depMapper.listAlldeps();
		
	}
	//查询所有域
	@Override
	public List<ZTreeNode> listAllDomains() {
		return depMapper.listAllDomains();
	}
	    //查询省厅以及直属下一级的所有域
		@Override
		public List<ZTreeNode> listByStDomains(List<String> domain_ids) {
			return depMapper.listByStDomains(domain_ids);
		}
	  //域管理员获取
		@Override
		public List<ZTreeNode> listDepsByUseradmin(String domain_id) {
			return depMapper.listDepsByUseradmin(domain_id);
		}
		
	//根据登录用户查询部门组织
	@Override
	public List<ZTreeNode> listDepsByUser(String moid,String domain_id) {
			return depMapper.listDepsByUser(moid,domain_id);
	}
	//部门自动同步递归循环的通用方法
	public void getDepChild(String pid,List<MeetingOrg> listDepNexts) {
		for(MeetingOrg org :listDepNexts){
			Department depSecond = new Department();
			depSecond.setDepartmentMoid(org.getDepartment_moid());
			QueryWrapper<Department> wrappersecond = new QueryWrapper<Department>(depSecond);
			
			Department deptwo= this.getOne(wrappersecond);//通过moid查询该数据
			
			//重新new一个对象(若使用之前的对象depSecond会将所有参数带入查询条件)
			Department depSeconds = new Department(org.getDepartment_name(),org.getDepartment_moid(),pid,"1");
			
			if(deptwo ==null){
				this.save(depSeconds);
			}else{
				//存在,更新
				this.update(depSeconds,wrappersecond);
			}
			List<MeetingOrg> listDepCommonNexts = org.getDepartments();
			//自身调用
		    getDepChild(org.getDepartment_moid(),listDepCommonNexts);
		}
	}
	    //部门手动同步递归循环的通用方法
		public void getDepChildByHand(String pid,List<MeetingOrg> listDepNexts) {
			for(MeetingOrg org :listDepNexts){
				//重新new一个对象(若使用之前的对象depSecond会将所有参数带入查询条件)
				Department depSeconds = new Department();
				depSeconds.setDepartmentMoid(org.getDepartment_moid());
				depSeconds.setDepartmentName(org.getDepartment_name());
				depSeconds.setParentId(pid);
				//插入部门
				this.save(depSeconds);
				List<MeetingOrg> listDepCommonNexts = org.getDepartments();
				//自身调用
			    getDepChildByHand(org.getDepartment_moid(),listDepCommonNexts);
			}
		}
		
	
	//查询部门树递归循环的通用方法
	public List<ZTreeNode> getChild(String id, List<ZTreeNode> treeList) {
	    // 子菜单
	    List<ZTreeNode> childList = new ArrayList<>();
	    for (ZTreeNode tree : treeList) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(tree.getpId())) {
	            if (tree.getpId().equals(id)) {
	                childList.add(tree);
	            }
	        }
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (ZTreeNode tree : childList) {// 没有url子菜单还有子菜单
	         // 递归
	    	 List<ZTreeNode> list = getChild(tree.getId(), treeList);
	    	 tree.setChildren(list);
	    } // 递归退出条件
	    if (childList.size() == 0) {
	        return null;
	        
	    }
	    return childList;
	}
	@Override
	public List<ZTreeNode> getUserChild(String id, List<ZTreeNode> listAlldeps, List<String> ids) {
	    // 子菜单
	    List<ZTreeNode> childList = new ArrayList<>();
	    for (ZTreeNode tree : listAlldeps) {
	        // 遍历所有节点，将父菜单id与传过来的id比较
	        if (StringUtils.isNotBlank(tree.getpId())) {
	            if (tree.getpId().equals(id)) {
	                childList.add(tree);
	    			User user = new User();
	    			user.setDepartment(id);
	    			QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
	    	    	List<User> list = userMapper.selectList(wrapper); 
	    	    	for (User u : list) {
	    	    		ZTreeNode treeNode = new ZTreeNode(u.getMoid(), u.getDepartment(), u.getActuralName(), 1,u.getE164());
	    	    		if(ids != null){
	    	    			if(ids.contains(u.getMoid())){
	    	    				treeNode.setChecked(true);
	    	    			}
	    	    		}
	    	    		childList.add(treeNode);
	    			}
	            }
	        }
	        
	    }
	    // 把子菜单的子菜单再循环一遍
	    for (ZTreeNode tree : childList) {
	         // 递归
	    	 tree.setChildren(getUserChild(tree.getId(), listAlldeps, ids));
	    } // 递归退出条件
	    if (childList.size() == 0) {
			User user = new User();
			user.setDepartment(id);
			QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
	    	List<User> list = userMapper.selectList(wrapper);
	    	for (User u : list) {
	    		ZTreeNode treeNode = new ZTreeNode(u.getMoid(), u.getDepartment(), u.getActuralName(), 1,u.getE164());
	    		if(ids != null){
	    			if(ids.contains(u.getMoid())){
	    				treeNode.setChecked(true);
	    			}
	    		}
	    		childList.add(treeNode);
			}
	    	
	        return childList;
	    }
	    return childList;
	}

	@Override
	public Department getDepByChildren(String userDep, String department) {
		return this.depMapper.getDepByChildren(userDep,department);
	}
  
}
