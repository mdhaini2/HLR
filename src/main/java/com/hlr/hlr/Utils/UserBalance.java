package com.hlr.hlr.Utils;

public class UserBalance {
    private double balance;
    private Object services;

    public UserBalance(double balance, Object services) {
        this.balance = balance;
        this.services = services;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Object getServices() {
        return services;
    }

    public void setServices(Object services) {
        this.services = services;
    }
}
