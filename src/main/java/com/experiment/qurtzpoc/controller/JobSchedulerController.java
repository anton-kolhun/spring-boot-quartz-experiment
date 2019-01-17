package com.experiment.qurtzpoc.controller;

import com.experiment.qurtzpoc.job.SimpleJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("job")
public class JobSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;

    @GetMapping("cron")
    public String scheduleCronJob() throws Exception {
        JobDetail jobDetail = buildJobDetail("cronJob");
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
        Trigger trigger = buildJobTrigger(jobDetail, scheduleBuilder, new Date());
        scheduler.scheduleJob(jobDetail, trigger);
        return "cron job has been scheduled";

    }

    @GetMapping("regular")
    public String scheduleRegularJob() throws Exception {
        JobDetail jobDetail = buildJobDetail("regularJob");
        ScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 15);
        Trigger trigger = buildJobTrigger(jobDetail, scheduleBuilder, calendar.getTime());
        scheduler.scheduleJob(jobDetail, trigger);
        return "regular job has been scheduled";

    }

    private JobDetail buildJobDetail(String jobName) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", "test@test.com");

        return JobBuilder.newJob(SimpleJob.class)
                .withIdentity(jobName, "jobs")
                .withDescription("this is job")
                .usingJobData(jobDataMap)
                //.storeDurably()  // if set completed jobs still remain in qrtz_job_details table
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ScheduleBuilder scheduleBuilder, Date startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "test-triggers")
                .withDescription("Test Trigger")
                .startAt(startAt)
                .usingJobData("test", "test")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
