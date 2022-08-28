package com.hlr.hlr.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Service {
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



}
