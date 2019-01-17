package com.experiment.qurtzpoc.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String recipientEmail = jobDataMap.getString("email");

        System.out.println("executing job ..." + "job data: email=" + recipientEmail);
    }

}
