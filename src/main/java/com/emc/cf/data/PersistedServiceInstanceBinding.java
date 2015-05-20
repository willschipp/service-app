package com.emc.cf.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;


@Entity
public class PersistedServiceInstanceBinding {

	@Id
	private String id;
	
	@Column
	private String serviceInstanceId;
	
	@ElementCollection
	@MapKeyColumn(name="name")
	@Column(name="value")
	@CollectionTable(name="credentials",joinColumns=@JoinColumn(name="service_id"))
	private Map<String,String> credentials = new HashMap<String,String>();
	
	@Column
	private String syslogDrainUrl;
	
	@Column
	private String appGuid;	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public Map<String, String> getCredentials() {
		return credentials;
	}

	public void setCredentials(Map<String, String> credentials) {
		this.credentials = credentials;
	}

	public String getSyslogDrainUrl() {
		return syslogDrainUrl;
	}

	public void setSyslogDrainUrl(String syslogDrainUrl) {
		this.syslogDrainUrl = syslogDrainUrl;
	}

	public String getAppGuid() {
		return appGuid;
	}

	public void setAppGuid(String appGuid) {
		this.appGuid = appGuid;
	}

	public static PersistedServiceInstanceBinding getPersistedServiceInstanceBinding(ServiceInstanceBinding binding) {
		PersistedServiceInstanceBinding persisted = new PersistedServiceInstanceBinding();
		persisted.setAppGuid(binding.getAppGuid());
		//convert the credentials for persistence
		Map<String,String> credentials = new HashMap<String,String>();
		for (Entry<String,Object> entry : binding.getCredentials().entrySet()) {
			credentials.put(entry.getKey(),entry.getValue().toString());
		}
		persisted.setCredentials(credentials);
		persisted.setId(binding.getId());
		persisted.setServiceInstanceId(binding.getServiceInstanceId());
		persisted.setSyslogDrainUrl(binding.getSyslogDrainUrl());
		return persisted;
	}
	
	public ServiceInstanceBinding getServiceInstanceBinding() {
		//convert credentials
		Map<String,Object> credentials = new HashMap<String,Object>();
		for (Entry<String,String> entry : this.getCredentials().entrySet()) {
			credentials.put(entry.getKey(),entry.getValue());
		}//end for
		//return
		return new ServiceInstanceBinding(this.getId(), this.getServiceInstanceId(), credentials, this.getSyslogDrainUrl(), this.getAppGuid());
	}
	
}
