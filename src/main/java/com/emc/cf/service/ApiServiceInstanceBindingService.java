package com.emc.cf.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emc.cf.app.data.ApiKey;
import com.emc.cf.app.data.ApiKeyRepository;

@Service
public class ApiServiceInstanceBindingService implements ServiceInstanceBindingService {

	private Map<String,ServiceInstanceBinding> bindings = Collections.synchronizedMap(new HashMap<String,ServiceInstanceBinding>());
	
	@Autowired
	private ApiKeyRepository repository;
	
	@Override
	public ServiceInstanceBinding createServiceInstanceBinding(CreateServiceInstanceBindingRequest createServiceInstanceBindingRequest) throws ServiceInstanceBindingExistsException, ServiceBrokerException {
		//get the instance
		ApiKey apiKey = repository.findByInstanceId(createServiceInstanceBindingRequest.getServiceInstanceId());
		//add the binding
		apiKey.getApplicationId().add(createServiceInstanceBindingRequest.getAppGuid());
		repository.save(apiKey);
		//build
		String key = UUID.randomUUID().toString();
		ServiceInstanceBinding binding = new ServiceInstanceBinding(key, createServiceInstanceBindingRequest.getServiceInstanceId(), null, null, createServiceInstanceBindingRequest.getAppGuid());
		bindings.put(key, binding);
		//return
		return binding;
	}

	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest deleteServiceInstanceBindingRequest) throws ServiceBrokerException {
		//get the binding
		ServiceInstanceBinding binding = bindings.get(deleteServiceInstanceBindingRequest.getBindingId());
		ApiKey apiKey = repository.findByApplicationId(binding.getAppGuid());
		//add the binding
		apiKey.getApplicationId().remove(binding.getAppGuid());
		repository.save(apiKey);
		//return 
		return binding;
	}

}
