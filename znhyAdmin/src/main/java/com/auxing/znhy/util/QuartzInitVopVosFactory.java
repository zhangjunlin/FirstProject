package com.auxing.znhy.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.auxing.znhy.service.IDepartmentService;
import com.auxing.znhy.service.IUserService;
//定时器
/**
 * 初始化运营信息接口调用
 * @author wxy
 * @date 2018年6月4日 下午5:27:12
 */
/**
 * @author Administrator
 *
 */
/**Job可以理解为就是一个工作任务，
 * 代码中就是一个实现了org.quartz.Job或org.quartz.StatefulJob接口的java类。
 * 当Scheduler决定运行Job时，execute()方法就会被执行。
 */
@DisallowConcurrentExecution
public class QuartzInitVopVosFactory implements Job{
	@Autowired
	IUserService userService;
	@Autowired
	IDepartmentService depService;
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                //这里写job代码，就是这个任务，具体要实现什么，就在这里写             
                //所有的参数以及其他信息都在JobExecutionContext
                //顺带提一句，如果你没有JobFactory 这个类，在这里是没办法注入任何类的。
    					//同步用户
    					userService.SynchronousUser();
    					//同步部门
    				    depService.SynchronousDomain();
    	       // System.out.println(resjson.get("msg"));

        }
    }
