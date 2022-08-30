package com.hlr.hlr.Status;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Status {
    @Id
    private int id;
    private String status;
}
