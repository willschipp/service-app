package com.emc.cf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.emc.cf","org.cloudfoundry.community"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}

}
