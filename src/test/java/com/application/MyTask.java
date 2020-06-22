package com.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author yanghaiyong
 * 2020/6/22-23:45
 */
public class MyTask implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(MyTask.class);

    private String jobId;

    public MyTask(String jobId){
        this.jobId = jobId;
    }
    public void run() {
        try{
            /* 上面logback.xml中discriminator根据taskId这个key的value来决定，taskId的value通过这种方式设置，
             这里设置的key-value对是保存在一个ThreadLocal<Map>中的，所以不会对其他线程中的taskId这个key产生影响
            */
            MDC.put("taskId", jobId);
            for(;;){
                // 写日志，使用SiftingAppender，由于当前调用线程taskId的value是对应this.taskId(假设是task-0), 所以会输出到File-task-0这个文件中
                LOG.info("taskId={}, threadNo={}", jobId, Thread.currentThread());
                Thread.sleep(2000);
            }
        }catch (Exception e){
        } finally{
            MDC.remove(jobId);
        }
    }



}
