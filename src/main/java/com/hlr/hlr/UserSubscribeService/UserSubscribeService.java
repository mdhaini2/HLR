package com.hlr.hlr.UserSubscribeService;

import com.hlr.hlr.Service.Services;
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

    private Date createdDate;
    private Date updateDate;


    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public UserSubscribeService(String status, String currentData, Date createdDate, Services services, Users users) {
        this.status = status;
        this.currentData = currentData;
        this.createdDate = createdDate;
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }
}
