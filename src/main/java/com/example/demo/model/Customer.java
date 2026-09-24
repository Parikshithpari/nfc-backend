package com.example.demo.model;

import java.time.LocalDate;

import com.example.demo.enums.Subscription;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Customer 
{
	@Id
	private String customerId;
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String cardId;
	
	private Integer points;
	
	private LocalDate dateOfBirth;
	
	private LocalDate validity;
	
	@Enumerated(EnumType.STRING)
	private Subscription subscription;

	public Customer(String customerId, String name, String email, String phoneNumber, String cardId, Integer points, LocalDate dateOfBirth, LocalDate validity, Subscription subscription) 
	{
		super();
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.cardId = cardId;
		this.points = points;
		this.dateOfBirth = dateOfBirth;
		this.validity = validity;
		this.subscription = subscription;
	}

	public Customer() 
	{
		super();
	}

	public String getCustomerId() 
	{
		return customerId;
	}

	public void setCustomerId(String customerId) 
	{
		this.customerId = customerId;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getPhoneNumber() 
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) 
	{
		this.phoneNumber = phoneNumber;
	}

	public String getCardId() 
	{
		return cardId;
	}

	public void setCardId(String cardId) 
	{
		this.cardId = cardId;
	}

	public Integer getPoints() 
	{
		return points;
	}

	public void setPoints(Integer points) 
	{
		this.points = points;
	}

	public Subscription getSubscription() 
	{
		return subscription;
	}

	public LocalDate getValidity()
	{
		return validity;
	}

	public void setValidity(LocalDate validity) 
	{
		this.validity = validity;
	}

	public void setSubscription(Subscription subscription) 
	{
		this.subscription = subscription;
	}

	public Object getOffers() 
	{
		return null;
	}

	public LocalDate getDateOfBirth() 
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) 
	{
		this.dateOfBirth = dateOfBirth;
	}
}