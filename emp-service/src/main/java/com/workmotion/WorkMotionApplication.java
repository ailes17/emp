package com.workmotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main class to start Workmotion springBoot app
 * @author Adel
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "com.workmotion.mongo.repository")
@ComponentScan
public class WorkMotionApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WorkMotionApplication.class);

    public static void main(String[] args) {
    	LOG.info("Starting EMP WorkMotion Service");
        SpringApplication.run(WorkMotionApplication.class, args);
    }


}
