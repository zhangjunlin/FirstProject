package com.auxing.znhy.common;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;
/** * 任务调度监听类 */
@Component
class TriggerListenerLogMonitor implements TriggerListener { 
	@Override    
	public String getName() { 
		return "TriggerListenerLogMonitor";
	}     
	//当与监听器相关联的 Trigger 被触发，Job 上的 execute() 方法将要被执行时，Scheduler 就调用这个方法。  
	@Override   
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		System.out.println("TriggerListenerLogMonitor类:" + context.getTrigger().getKey().getName() + " 被执行");
	}     
	/**     * 在 Trigger 触发后，Job 将要被执行时由 Scheduler 调用这个方法。     * TriggerListener 给了一个选择去否决 Job 的执行。     * 假如这个方法返回 true，这个 Job 将不会为此次 Trigger 触发而得到执行。     */    
	@Override    
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;   
	}     
	/**    
	 * Scheduler 调用这个方法是在 Trigger 错过触发时。    
	 * 如这个方法的 JavaDoc 所指出的，你应该关注此方法中持续时间长的逻辑：
	 * 在出现许多错过触发的 Trigger 时，
	 * 长逻辑会导致骨牌效应。
	 *你应当保持这上方法尽量的小。    
	 */    
	@Override
	public void triggerMisfired(Trigger trigger) {
		System.out.println("Job错过触发");
	}    
	/*多米诺骨牌效应(骨牌效应):该效应产生的能量是十分巨大的。    
	 * 这种效应的物理道理是：骨牌竖着时，重心较高，倒下时重心下降，倒下过程中，
	 * 将其重力势能转化为动能，它倒在第二张牌上，这个动能就转移到第二张牌上，
	 * 第二张牌将第一张牌转移来的动能和自已倒下过程中由本身具有的重力势能转化来的动能之和，
	 * 再传到第三张牌上......所以每张牌倒下的时候，具有的动能都比前一块牌大，因此它们的速度一个比一个快，
	 * 也就是说，它们依次推倒的能量一个比一个大*/
	 /**     
	  * Trigger 被触发并且完成了 Job 的执行时，Scheduler 调用这个方法。 
	  * 这不是说这个 Trigger 将不再触发了，而仅仅是当前 Trigger 的触发(并且紧接着的 Job 执行) 结束时。
	  * 这个 Trigger 也许还要在将来触发多次的。    
	  */    
	@Override 
	public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) { 
		System.out.println("Job执行完毕,Trigger完成");
		}
	}

