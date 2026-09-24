package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.NonMemberShipCustomer;

@Repository
public interface NonMembershipRepo extends JpaRepository<NonMemberShipCustomer, Long>
{

	boolean existsByPhoneNumber(String phoneNumber);
	
}
