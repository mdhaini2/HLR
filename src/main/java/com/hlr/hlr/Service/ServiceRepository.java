package com.hlr.hlr.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services,Integer> {
    public Services findByServiceName(String serviceName);
}
