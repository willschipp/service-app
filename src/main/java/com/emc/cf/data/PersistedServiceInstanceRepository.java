package com.emc.cf.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistedServiceInstanceRepository extends
		JpaRepository<PersistedServiceInstance, String> {

}
