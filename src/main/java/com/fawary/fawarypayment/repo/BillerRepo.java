package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.Biller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BillerRepo extends JpaRepository<Biller, String> {

}
