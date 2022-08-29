package com.hlr.hlr.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
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
    private Date createdDate;
    private Date updatedDate;

    @JsonIgnore
    @OneToMany(mappedBy = "services",cascade = {CascadeType.ALL})
    private Set<UserSubscribeService> userSubscribedService = new HashSet<UserSubscribeService>();

    public Services() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new java.util.Date(ts.getTime());
        this.createdDate = date;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new java.util.Date(ts.getTime());
        this.updatedDate = date;
    }

    public Set<UserSubscribeService> getUserSubscribedService() {
        return userSubscribedService;
    }

    public void setUserSubscribedService(Set<UserSubscribeService> userSubscribedService) {
        this.userSubscribedService = userSubscribedService;
    }
}
