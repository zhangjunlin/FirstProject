package com.auxing.znhy.controller;

import com.auxing.znhy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BaseController {
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;
	
	@Autowired
	IUserService userService;

	@Autowired
	IAuthService authService;

	@Autowired
	IRoleService roleService;

	@Autowired
	IRoleauthService roleauthService;
	
	@Autowired
	IDepartmentService departmentService;
	
	@Autowired
	IUserroleService userroleService;

    @Autowired
    IResourceService resourceService;

    @Autowired
	ITvwallService tvwallService;
    
	@Autowired
	IMeetService meetService;
	
	@Autowired
	ITemplateService templateService;

	@Autowired
	IMeetroomService meetroomService;

	@Autowired
	IDomainuserService domainService;

	@Autowired
    IConferenceService conferenceService;
	
	@Autowired
	IChannelService channelService;

	@Autowired
	IMessageService messageService;
	
	@Autowired
	Environment env;
	
}
