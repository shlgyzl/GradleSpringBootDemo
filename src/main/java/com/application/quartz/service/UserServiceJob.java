package com.application.quartz.service;

import com.application.jpa.common.util.SpringContextUtil;
import com.application.jpa.domain.User;
import com.application.jpa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Map;

@Slf4j
public class UserServiceJob extends QuartzJobBean {
    private final Scheduler scheduler;

    public UserServiceJob(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 执行此业务的真正的方法
     *
     * @param context 传递进来的参数
     */
    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("执行用户定时任务...参数:[{}]", context);
        UserService userService = (UserService) SpringContextUtil.getBean("userService");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        userService.job((User) jobDataMap.get("user"));
    }

    /**
     * 根据时间触发,当时间到达某个时刻则触发
     *
     * @param triggerKeyName key名称
     * @param startTime      触发事件
     * @param params         参数
     * @param jobClass       任务实体
     */
    public void scheduleAtTime(String triggerKeyName, Date startTime, Map<String, Object> params, Class<? extends Job> jobClass) {
        // 定时任务唯一key
        TriggerKey triggerKey = new TriggerKey(triggerKeyName);

        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                log.info("startTime of trigger[{}] changed to {}", triggerKeyName, startTime);
                if (trigger.getStartTime() != startTime) {
                    scheduler.rescheduleJob(triggerKey, trigger);
                }
            } else {
                log.info("new trigger[{}] will start at {}", triggerKeyName, startTime);
                JobDetail jobDetail = JobBuilder.newJob(jobClass).build();
                jobDetail.getJobDataMap().putAll(params);
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .startAt(startTime)
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }
}
