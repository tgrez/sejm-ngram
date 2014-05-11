package com.example.helloworld;

import com.example.helloworld.health.TemplateHealthCheck;
import com.example.helloworld.resources.DemoNgramResource;
import com.example.helloworld.resources.NgramFTSResource;
import com.example.helloworld.resources.NgramResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class RestApiService extends Service<RestApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new RestApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<RestApiConfiguration> bootstrap) {
        bootstrap.setName("hello-world");
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(RestApiConfiguration configuration,
                    Environment environment) {

//        environment.addHealthCheck(new TemplateHealthCheck(template)); TODO
        
        environment.addResource(new NgramResource());
        environment.addResource(new NgramFTSResource());
        environment.addResource(new DemoNgramResource());
    }

}
