
package com.example.AutoskolaDemoWithSecurity.tests;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    
    Optional<Test> findByNumber(int number);
    
}
