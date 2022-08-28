package com.hlr.hlr.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    public Users findByPhoneNumber(String phoneNumber);
}
