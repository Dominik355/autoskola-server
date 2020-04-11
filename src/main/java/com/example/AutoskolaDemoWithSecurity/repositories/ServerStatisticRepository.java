
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ServerStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServerStatisticRepository extends JpaRepository<ServerStatistic, Integer> {
 
    ServerStatistic findByDate(String date);
    
    boolean existsByDate(String date);
    
}
