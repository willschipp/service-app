package com.emc.cf.service;

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
import com.emc.cf.data.PersistedServiceInstanceBinding;
import com.emc.cf.data.PersistedServiceInstanceBindingRepository;

@Service
public class ApiServiceInstanceBindingService implements ServiceInstanceBindingService {

	@Autowired
	private PersistedServiceInstanceBindingRepository bindingRepository;
	
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
		Map<String,Object> credentials = new HashMap<String,Object>();
		credentials.put("api-key",apiKey.getApiKey());
		String key = UUID.randomUUID().toString();
		ServiceInstanceBinding binding = new ServiceInstanceBinding(key, createServiceInstanceBindingRequest.getServiceInstanceId(), credentials, null, createServiceInstanceBindingRequest.getAppGuid());
		//convert
		PersistedServiceInstanceBinding persistedBinding = PersistedServiceInstanceBinding.getPersistedServiceInstanceBinding(binding);
		//save
		bindingRepository.save(persistedBinding);
		//return
		return binding;
	}

	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest deleteServiceInstanceBindingRequest) throws ServiceBrokerException {
		//get the binding
		PersistedServiceInstanceBinding persistedBinding = bindingRepository.findOne(deleteServiceInstanceBindingRequest.getBindingId());
		ApiKey apiKey = repository.findByApplicationId(persistedBinding.getAppGuid());
		//add the binding
		apiKey.getApplicationId().remove(persistedBinding.getAppGuid());
		repository.save(apiKey);
		//return 
		return persistedBinding.getServiceInstanceBinding();//gone
	}

}
