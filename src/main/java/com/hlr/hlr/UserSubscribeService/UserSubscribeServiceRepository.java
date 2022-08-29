package com.hlr.hlr.UserSubscribeService;

import com.hlr.hlr.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscribeServiceRepository extends JpaRepository<UserSubscribeService,Integer> {

    void deleteByUsers(Users users);

    @Query(value = "SELECT * FROM user_subscribe_service WHERE users_id=?1 and  service_id =?2",nativeQuery = true)
    List<UserSubscribeService> findUserSubscribedService(int user_id, int service_id);
}
