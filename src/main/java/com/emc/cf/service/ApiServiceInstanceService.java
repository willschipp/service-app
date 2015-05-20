package com.emc.cf.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emc.cf.app.data.ApiKey;
import com.emc.cf.app.data.ApiKeyRepository;

@Service
public class ApiServiceInstanceService implements ServiceInstanceService {
	
	private Map<String,ServiceInstance> serviceInstances = Collections.synchronizedMap(new HashMap<String,ServiceInstance>());
	
	@Autowired
	private ApiKeyRepository repository;
	
	
	@Override
	public ServiceInstance createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest) throws ServiceInstanceExistsException, ServiceBrokerException {
		//create a new set of api keys
		ApiKey key = new ApiKey();
		key.setApiKey(UUID.randomUUID().toString());
		key.setConfigurationDate(new Date());
		key.setInstanceId(createServiceInstanceRequest.getServiceInstanceId());
		//save
		repository.save(key);
		//build the object
		ServiceInstance instance = new ServiceInstance(createServiceInstanceRequest);
		serviceInstances.put(createServiceInstanceRequest.getServiceInstanceId(), instance);
		//return
		return instance;
	}

	@Override
	public ServiceInstance getServiceInstance(String serviceInstanceId) {
		return serviceInstances.get(serviceInstanceId);
	}

	@Override
	public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) throws ServiceBrokerException {
		//delete by the key
		repository.delete(deleteServiceInstanceRequest.getServiceInstanceId());
		//remove the instance
		ServiceInstance instance = serviceInstances.remove(deleteServiceInstanceRequest.getServiceInstanceId());
		//return
		return instance;
	}

	@Override
	public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest updateServiceInstanceRequest) throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
		if (!serviceInstances.containsKey(updateServiceInstanceRequest.getServiceInstanceId())) {
			throw new ServiceInstanceDoesNotExistException(updateServiceInstanceRequest.getServiceInstanceId());
		}//end if
		//TODO - add update logic
		//return
		return serviceInstances.get(updateServiceInstanceRequest.getServiceInstanceId());
	}

}
