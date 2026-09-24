package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String>
{

	Optional<Card> findByNFCId(String nFCId);
	
}
