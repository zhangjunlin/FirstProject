package com.auxing.znhy.service;

import java.util.List;
import java.util.Map;

import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.TemplateUser;
import com.auxing.znhy.entity.ZTreeNode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
public interface ITemplateService extends IService<Template> {

	List<Template> page(Page<Template> page, Template template, List<ZTreeNode> selectDepsByUser);

	Integer saveTemplate(Template template);

	void saveTemplateUser(Map<String, Object> map);

	Template getTemplateById(Integer id);


	void updateTemplate(Template template);

	void deleteTemplateUser(Integer tId);

	void deleteTemplate(Integer id);

	long selectTotalCount(Map<String, Object> map);

	List<String> findUserIds(Integer id);

	List<Template> findListByDep(List<ZTreeNode> selectDepsByUser);

	List<TemplateUser> getUsersById(Integer id);

	//根据登录用户查询会议模板
	List<Template> templateLists(String departmentId);

	List<Template> page(Map<String, Object> map);
	
}
