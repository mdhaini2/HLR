package com.hlr.hlr.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String lineType;
    private Date birthday;
    private double balance;

    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "users",cascade = {CascadeType.ALL})
    private Set<UserSubscribeService> userSubscribeServiceSet  = new HashSet<UserSubscribeService>();

    private Date createdDate;
    private Date updatedDate;

    public Users() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new Date(ts.getTime());
        this.createdDate = date;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long timeStamp) {
        Timestamp ts =new Timestamp(timeStamp);
        Date date=new Date(ts.getTime());
        this.updatedDate = date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<UserSubscribeService> getUserSubscribeServiceSet() {
        return userSubscribeServiceSet;
    }

    public void setUserSubscribeServiceSet(Set<UserSubscribeService> userSubscribeServiceSet) {
        this.userSubscribeServiceSet = userSubscribeServiceSet;
    }

}
