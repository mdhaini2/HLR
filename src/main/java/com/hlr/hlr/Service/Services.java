package com.hlr.hlr.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String serviceName;
    private String serviceDescription;
    private String data;
    private int duration;
    private double price;
    private boolean isPostPaid;
    private Date createdDate;
    private Date updatedDate;

    @JsonIgnore
    @OneToMany(mappedBy = "services",cascade = {CascadeType.ALL})
    private Set<UserSubscribeService> userReservedBooks  = new HashSet<UserSubscribeService>();



}
