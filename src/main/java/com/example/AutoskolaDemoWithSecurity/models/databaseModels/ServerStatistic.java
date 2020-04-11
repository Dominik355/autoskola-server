
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServerStatistic implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true)
    private int id;
    
    private String date;
    
    private int totalRequests;
    
    private int successfulRequests;
    
    private int badRequests;

    private int unauthorizedRequests;
    
    private int wrongUrlRequests;
    
    private int serverErrors;
    
    private double meanResponseTime;
    
    public ServerStatistic() {
    
    }

    public ServerStatistic(String date,int successfulRequests, int badRequests, int unauthorizedRequests, int wrongUrlRequests, int serverErrors, double meanResponseTime) {
        this.date = date;
        this.successfulRequests = successfulRequests;
        this.badRequests = badRequests;
        this.unauthorizedRequests = unauthorizedRequests;
        this.wrongUrlRequests = wrongUrlRequests;
        this.serverErrors = serverErrors;
        this.totalRequests = (serverErrors+wrongUrlRequests+unauthorizedRequests+badRequests+successfulRequests);
        if(successfulRequests == 0) {
            this.meanResponseTime = 0;
        } else {
            this.meanResponseTime = (meanResponseTime/successfulRequests);
        }
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setSuccessfulRequests(int successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public int getBadRequests() {
        return badRequests;
    }

    public void setBadRequests(int badRequests) {
        this.badRequests = badRequests;
    }

    public int getUnauthorizedRequests() {
        return unauthorizedRequests;
    }

    public void setUnauthorizedRequests(int unauthorizedRequests) {
        this.unauthorizedRequests = unauthorizedRequests;
    }

    public int getWrongUrlRequests() {
        return wrongUrlRequests;
    }

    public void setWrongUrlRequests(int wrongUrlRequests) {
        this.wrongUrlRequests = wrongUrlRequests;
    }

    public int getServerErrors() {
        return serverErrors;
    }

    public void setServerErrors(int serverErrors) {
        this.serverErrors = serverErrors;
    }

    public double getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(double meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

}
