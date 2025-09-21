package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill , Long> {
}
