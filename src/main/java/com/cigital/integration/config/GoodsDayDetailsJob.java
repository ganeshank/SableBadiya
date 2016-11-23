package com.cigital.integration.config;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.cigital.integration.tool.GoodsDayDataStore;
import com.cigital.integration.util.LoadPropertiesFile;

public class GoodsDayDetailsJob {
	
	final static Logger logger = Logger.getLogger(GoodsDayDetailsJob.class);
	
	public void startCronJob() throws SchedulerException{
		//Quartz 1.6.3
    	//JobDetail job = new JobDetail();
    	//job.setName("dummyJobName");
    	//job.setJobClass(HelloJob.class);
    	JobDetail job = JobBuilder.newJob(GoodsDayDataStore.class)
		.withIdentity("dummyJobName", "group1").build();

    	//Quartz 1.6.3
    	//CronTrigger trigger = new CronTrigger();
    	//trigger.setName("dummyTriggerName");
    	//trigger.setCronExpression("0/5 * * * * ?");
    	
		LoadPropertiesFile propertiesFile = new LoadPropertiesFile();
	    Properties prop = null;
 		try {
 			prop = propertiesFile.loadProperties("/application.properties");
 		} catch (Exception e2) {
 			logger.error("Error::", e2);
 			e2.printStackTrace();
 		}

    	Trigger trigger = TriggerBuilder
		.newTrigger()
		.withIdentity("dummyTriggerName", "group1")
		.withSchedule(
			CronScheduleBuilder.cronSchedule(prop.getProperty("cron.job.time.expression")))
		.build();

    	//schedule it
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);

	}
}
