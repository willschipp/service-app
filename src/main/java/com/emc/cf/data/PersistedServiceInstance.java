package com.emc.cf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;

@Entity
public class PersistedServiceInstance {

	@Id
	private String serviceInstanceId;
	
	@Column
	private String serviceDefinitionId;
	
	@Column
	private String planId;
	
	@Column
	private String organizationGuid;
	
	@Column
	private String spaceGuid;
	
	@Column
	private String dashboardUrl;	
	
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getOrganizationGuid() {
		return organizationGuid;
	}

	public void setOrganizationGuid(String organizationGuid) {
		this.organizationGuid = organizationGuid;
	}

	public String getSpaceGuid() {
		return spaceGuid;
	}

	public void setSpaceGuid(String spaceGuid) {
		this.spaceGuid = spaceGuid;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	public void setDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
	}

	public ServiceInstance getServiceInstance() {
		CreateServiceInstanceRequest request = new CreateServiceInstanceRequest(this.getServiceDefinitionId(),this.getPlanId(),this.getOrganizationGuid(),this.getSpaceGuid());
		request = request.withServiceInstanceId(this.getServiceInstanceId());
		return new ServiceInstance(request);
	}
	
	public static PersistedServiceInstance getPersisted(ServiceInstance serviceInstance) {
		PersistedServiceInstance persisted = new PersistedServiceInstance();
		persisted.setDashboardUrl(serviceInstance.getDashboardUrl());
		persisted.setOrganizationGuid(serviceInstance.getOrganizationGuid());
		persisted.setPlanId(serviceInstance.getPlanId());
		persisted.setServiceDefinitionId(serviceInstance.getServiceDefinitionId());
		persisted.setServiceInstanceId(serviceInstance.getServiceInstanceId());
		persisted.setSpaceGuid(serviceInstance.getSpaceGuid());
		return persisted;
	}
	
}
