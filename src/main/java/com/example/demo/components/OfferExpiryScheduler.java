package com.example.demo.components;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.Customer;
import com.example.demo.model.Offer;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.OfferRepo;

import jakarta.transaction.Transactional;

@Component
public class OfferExpiryScheduler 
{
	@Autowired
	private OfferRepo offerRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void deactivateScheduledOffer()
	{
		List<Offer> activeOffers = offerRepo.findByActiveTrue();
		
		LocalDate today = LocalDate.now();
		for(Offer offer : activeOffers)
		{
			if(offer.getValidUpto() != null && offer.getValidUpto().isBefore(today))
			{
				offer.setActive(false);
				offerRepo.save(offer);
			}
		}
	}
	
	@Scheduled(cron = "0 0 0 * * ?")
    @Transactional
	public void deleteExpiredCustomers()
	{
		LocalDate today = LocalDate.now();
		List<Customer> expired = customerRepo.findByValidity(today);
		expired.forEach(customerRepo::delete);
	}
}
