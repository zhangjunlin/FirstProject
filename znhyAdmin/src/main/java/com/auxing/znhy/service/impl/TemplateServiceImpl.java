package com.auxing.znhy.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.TemplateUser;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.mapper.TemplateMapper;
import com.auxing.znhy.service.ITemplateService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements ITemplateService {
	
    @Resource
    private TemplateMapper templateMapper;
	
	@Override
	public long selectTotalCount(Map<String, Object> map) {
		return templateMapper.selectTotalCount(map);
	}

	@Override
	public List<Template> page(Page<Template> page, Template template ,List<ZTreeNode> list) {
		return templateMapper.page(page ,template ,list);
	}

	@Override
	public Integer saveTemplate(Template template) {
		return templateMapper.saveTemplate(template);
	}

	@Override
	public void saveTemplateUser(Map<String, Object> map) {
		templateMapper.saveTemplateUser(map);
	}

	@Override
	public Template getTemplateById(Integer id) {
		return templateMapper.getTemplateById(id);
	}


	@Override
	public void updateTemplate(Template template) {
		templateMapper.updateTemplate(template);
	}

	@Override
	public void deleteTemplateUser(Integer id) {
		templateMapper.deleteTemplateUser(id);
	}

	@Override
	public void deleteTemplate(Integer id) {
		templateMapper.deleteTemplate(id);
	}

	@Override
	public List<String> findUserIds(Integer id) {
		return templateMapper.findUserIds(id);
	}

	@Override
	public List<Template> findListByDep(List<ZTreeNode> list) {
		return templateMapper.findListByDep(list);
	}

	@Override
	public List<TemplateUser> getUsersById(Integer id) {
		return templateMapper.getUsersById(id);
	}

	@Override
	public List<Template> templateLists(String departmentId) {
		return templateMapper.templateLists(departmentId);
	}

	@Override
	public List<Template> page(Map<String, Object> map) {
		return templateMapper.page(map);
	}


}
