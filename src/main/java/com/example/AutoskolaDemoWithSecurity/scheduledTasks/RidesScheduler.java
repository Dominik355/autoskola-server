
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


public class RidesScheduler {

    @Autowired
    private RideRepository rideRepository;
    
    Logger log = LoggerFactory.getLogger(RidesScheduler.class);
    
    @Scheduled(fixedRate = 30000)
    private void checkRideStatus() {
        List<Ride> rides = rideRepository.findAll();
        ExecutorService executor = Executors.newCachedThreadPool();
        
        for(Ride ride : rides) {
            // 2x IF -ak je reserved - da sa na pending, ak je free a uz mala zacat - vymaze sa a da sa sprava, ze nikto neprisiel
            // IF - je pending a zaroven ubehla hodina a pol od začatia - pripomenie
        }
        executor.shutdown();
    }
    
}
