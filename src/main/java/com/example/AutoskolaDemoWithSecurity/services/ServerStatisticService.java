
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ServerStatistic;
import com.example.AutoskolaDemoWithSecurity.repositories.ServerStatisticRepository;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ServerStatisticService {
    
    @Autowired
    private ServerStatisticRepository ssr;
    
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");    
    
    private int successfulRequests;
    
    private int badRequests;
    
    private int unauthorizedRequests;
    
    private int wrongUrlRequests;
    
    private int serverErrors;

    private ArrayList<Double> meanResponseTime = new ArrayList<>();
    
    //cas pride v sekundach
    //200,400,401,404,>=500

    public void addResponse(HttpServletResponse response, double responseTime) {
            
        if(response.getStatus() == 200) {
            this.successfulRequests +=1;
            this.meanResponseTime.add(responseTime);
        } else if(response.getStatus() == 401) {
            this.unauthorizedRequests +=1;
        } else if(response.getStatus() == 404) {
            this.wrongUrlRequests +=1;
        } else if(response.getStatus() >= 500) {
            this.serverErrors +=1;
        } else {
            this.badRequests +=1;
        }
    }
   
    //ak pre dnsni den neexistuje zaznam - vytvori novy, ak existuje - iba updatuje
    //ulozi do databazy, a vynulluje
    @Scheduled(fixedDelay = 10000)
    public void statistics(){
        String now = formatter.format(new Date(System.currentTimeMillis()));
        ServerStatistic statistics;
        if(ssr.existsByDate(now)) {
            statistics = ssr.findByDate(now);
            statistics.setBadRequests(statistics.getBadRequests()+this.badRequests);
            statistics.setServerErrors(statistics.getServerErrors()+this.serverErrors);
            statistics.setSuccessfulRequests(statistics.getSuccessfulRequests()+this.successfulRequests);
            statistics.setUnauthorizedRequests(statistics.getUnauthorizedRequests()+this.unauthorizedRequests);
            statistics.setWrongUrlRequests(statistics.getWrongUrlRequests()+this.wrongUrlRequests);
            int all = badRequests+serverErrors+successfulRequests+unauthorizedRequests+wrongUrlRequests;
            statistics.setTotalRequests(statistics.getTotalRequests()+all);
            if(!meanResponseTime.isEmpty()) {
                statistics.setMeanResponseTime((statistics.getMeanResponseTime()+getMedian(meanResponseTime))/2);
            }
        } else {
            statistics = new ServerStatistic(now, successfulRequests, badRequests
                    , unauthorizedRequests, wrongUrlRequests, serverErrors, getMedian(meanResponseTime));
        }
        ssr.save(statistics);
        this.badRequests = 0;
        this.meanResponseTime = new ArrayList<Double>();
        this.serverErrors = 0;
        this.unauthorizedRequests = 0;
        this.successfulRequests = 0;
        this.wrongUrlRequests = 0;
    }

    
    public double getMedian(ArrayList<Double> list) {
        if(!list.isEmpty()) {
            double median;
            double[] arr = list.stream().mapToDouble(d -> d).toArray();
            Arrays.sort(arr);
            if (arr.length % 2 == 0)
                median = ((double)arr[arr.length/2] + (double)arr[arr.length/2 - 1])/2;
            else
                median = (double) arr[arr.length/2];
            System.out.println("Medain: "+median);
            return median;
        }
        return 0;
    }
    
}
