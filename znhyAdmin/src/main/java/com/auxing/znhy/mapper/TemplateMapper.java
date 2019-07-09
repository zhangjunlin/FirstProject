package com.auxing.znhy.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.TemplateUser;
import com.auxing.znhy.entity.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
public interface TemplateMapper extends BaseMapper<Template> {

	long selectTotalCount(Map<String, Object> map);

	List<Template> page(@Param("page")Page<Template> page, @Param("template")Template template,@Param("list") List<ZTreeNode> list);

	Integer saveTemplate(Template template);

	void saveTemplateUser(Map<String, Object> map);

	Template getTemplateById(Integer id);

	void updateTemplate(Template template);

	void deleteTemplateUser(Integer id);

	void deleteTemplate(Integer id);

	List<String> findUserIds(Integer id);

	List<Template> findListByDep(List<ZTreeNode> list);

	List<TemplateUser> getUsersById(Integer id);
	
	List<Template> templateLists(@Param("departmentId")String departmentId);

	List<Template> page(Map<String, Object> map);

}
