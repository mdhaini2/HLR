package com.hlr.hlr.UserSubscribeService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlr.hlr.Service.Services;
import com.hlr.hlr.Users.Users;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Entity
public class UserSubscribeService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;
    private String status;
    private String currentData;

    private Date createdDate;
    private Date updateDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;


    public UserSubscribeService() {
    }

    public UserSubscribeService(String status, String currentData, long timeStamp, Services services, Users users) {
        this.status = status;
        this.currentData = currentData;
        setCreatedDate(timeStamp);
        this.services = services;
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Services getService() {
        return services;
    }

    public void setService(Services services) {
        this.services = services;
    }


    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentData() {
        return currentData;
    }

    public void setCurrentData(String currentData) {
        this.currentData = currentData;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new java.util.Date(ts.getTime());
        this.createdDate = date;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long timeStamp) {

        Timestamp ts =new Timestamp(timeStamp);
        Date date=new java.util.Date(ts.getTime());
        this.updateDate = date;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }
}
