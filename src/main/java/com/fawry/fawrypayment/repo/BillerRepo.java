package com.fawry.fawrypayment.repo;

import com.fawry.fawrypayment.entity.Biller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillerRepo extends JpaRepository<Biller, String> {

}
