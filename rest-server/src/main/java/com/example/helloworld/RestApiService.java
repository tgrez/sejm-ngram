package com.example.helloworld;

import org.skife.jdbi.v2.DBI;

import com.example.helloworld.resources.DemoNgramResource;
import com.example.helloworld.resources.NgramFTSResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.jdbi.bundles.DBIExceptionsBundle;

public class RestApiService extends Service<RestApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new RestApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<RestApiConfiguration> bootstrap) {
        bootstrap.setName("sejm-ngram");
        
        // static assets
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
        
        // unwrap any thrown SQLException or DBIException instances
        // necessary for getting full stack trace in logs
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(RestApiConfiguration config,
                    Environment environment) throws ClassNotFoundException {
    	final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, 
        		config.getDatabaseConfiguration(), "mysql");
    	
//        environment.addHealthCheck(new TemplateHealthCheck(template)); TODO
        
//        environment.addResource(new NgramResource());
        environment.addResource(new NgramFTSResource(
        		jdbi,
        		config.getPartiaIdFilename(),
        		config.getPoselIdFilename()));
        environment.addResource(new DemoNgramResource());
    }

}
