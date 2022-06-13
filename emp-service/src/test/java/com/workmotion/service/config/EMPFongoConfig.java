package com.workmotion.service.config;


import java.net.InetSocketAddress;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.workmotion.mongo.config.MongoConfig;
import com.workmotion.mongo.repository.EmployeeRepository;
import com.workmotion.mongo.service.MongoServiceImpl;
import com.workmotion.service.EmployeeServiceImpl;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@Configuration
@ComponentScan(basePackageClasses= {MongoServiceImpl.class, EmployeeRepository.class, EmployeeServiceImpl.class}, excludeFilters={@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=MongoConfig.class)})
@EnableMongoRepositories("com.workmotion.mongo.repository")
@Profile("fakemongo")
public class EMPFongoConfig extends AbstractMongoClientConfiguration {

	@Override
    protected String getDatabaseName() {
        return "fongo-test-db";
    }
	
	@Override
    @Bean
    public com.mongodb.client.MongoClient mongoClient() {
        final MongoServer server = new MongoServer(new MemoryBackend());

        // bind on a random local port
        final InetSocketAddress serverAddress = server.bind();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(serverAddress)))
                	.build()).build();
        
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
