package com.emc.cf.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistedServiceInstanceBindingRepository extends
		JpaRepository<PersistedServiceInstanceBinding, String> {

}
