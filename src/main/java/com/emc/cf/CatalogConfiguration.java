package com.emc.cf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.model.Plan;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfiguration {

	@Bean
	public Catalog catalog() {
		return new Catalog(Arrays.asList(new ServiceDefinition("rest-service","rest-service","An example REST service",true,false,
				Arrays.asList(new Plan("basic-rest-plan","the-basic-plan","This is a basic plan",getPlanMetadata())),
				Arrays.asList("rest-service","document"),
				getServiceDefinitionMetadata(),
				null,
				null)));
	}
	
	
	private Map<String,Object> getPlanMetadata() {
		Map<String,Object> metadata = new HashMap<String,Object>();
//		metadata.put("costs",getCosts());
		metadata.put("bullets",getBullets());
		return metadata;
	}
	
	private List<String> getBullets() {
		List<String> list = new ArrayList<String>();
		list.add("Simple REST Service Example");
		list.add("Demonstrates API Key retrieval");
		list.add("Has a corresponding consuming example");
		return list;
	}	
	
	private List<Map<String,Object>> getCosts() {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> amount = new HashMap<String, Object>();
		amount.put("usd",new Double(0.0));
		
		Map<String,Object> costsMap = new HashMap<String, Object>();
		costsMap.put("amount",amount);
		costsMap.put("unit","MONTHLY");
		
		list.add(costsMap);
		
		return list;
	}
	
	private Map<String,Object> getServiceDefinitionMetadata() {
		Map<String,Object> metadata = new HashMap<String,Object>();
		metadata.put("displayName", "REST-Service");
		metadata.put("imageUrl","http://images.clipartpanda.com/example-clipart-canstock6911432.jpg");
		metadata.put("longDescription","sample RESTful service API key as a service broker");
		metadata.put("providerDisplayName","EMC");
		metadata.put("documentationUrl","http://www.emc.com");
		metadata.put("supportUrl","http://www.emc.com");
		return metadata;
	}
	
	
	
}
