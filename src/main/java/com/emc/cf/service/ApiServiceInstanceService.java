package com.emc.cf.service;

import java.util.Date;
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
import com.emc.cf.data.PersistedServiceInstance;
import com.emc.cf.data.PersistedServiceInstanceRepository;

@Service
public class ApiServiceInstanceService implements ServiceInstanceService {
	
//	private Map<String,ServiceInstance> serviceInstances = Collections.synchronizedMap(new HashMap<String,ServiceInstance>());
	
	@Autowired
	private PersistedServiceInstanceRepository serviceInstanceRepository;
	
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
		PersistedServiceInstance persistedInstance = PersistedServiceInstance.getPersisted(instance);
		serviceInstanceRepository.save(persistedInstance);
		//return
		return instance;
	}

	@Override
	public ServiceInstance getServiceInstance(String serviceInstanceId) {
		PersistedServiceInstance persistedInstance = serviceInstanceRepository.findOne(serviceInstanceId);
		if (persistedInstance != null) {
			return persistedInstance.getServiceInstance();
		} else {
			return null;
		}//end if
	}

	@Override
	public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) throws ServiceBrokerException {
		//delete by the key
		repository.delete(deleteServiceInstanceRequest.getServiceInstanceId());
		serviceInstanceRepository.delete(deleteServiceInstanceRequest.getServiceInstanceId());
		//remove the instance
		//return
		return null;//GONE
	}

	@Override
	public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest updateServiceInstanceRequest) throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
		//TODO - add update logic
		PersistedServiceInstance persistedServiceInstance = serviceInstanceRepository.findOne(updateServiceInstanceRequest.getServiceInstanceId());
		//return
		return persistedServiceInstance.getServiceInstance();
	}

}
