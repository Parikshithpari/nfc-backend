package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.Subscription;
import com.example.demo.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> 
{
	Optional<Customer> findByCardId(String cardId);

	@Query("SELECT c FROM Customer c WHERE c.subscription = :subscription")
	List<Customer> findAllBySubscription(@Param("subscription") Subscription subscription);

	Optional<Customer> findByCustomerId(String customerId);

	Optional<Customer> findByName(String customerName);

	void deleteByCustomerId(String customerId);

	Optional<Customer> findByPhoneNumber(String phoneNumber);

	List<Customer> findByValidity(LocalDate date);

	
}
