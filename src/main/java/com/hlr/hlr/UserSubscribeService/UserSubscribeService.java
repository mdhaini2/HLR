package com.hlr.hlr.UserSubscribeService;

import com.hlr.hlr.Service.Service;
import com.hlr.hlr.Users.Users;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class UserSubscribeService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;
    private String status;
    private String currentData;
    @ManyToOne
    @JoinColumn(name = "user_info")
    private Users userInfo;

    @ManyToOne
    @JoinColumn(name = "service_info")
    private Service serviceInfo;



    private Date createdDate;
    private Date updateDate;


}
