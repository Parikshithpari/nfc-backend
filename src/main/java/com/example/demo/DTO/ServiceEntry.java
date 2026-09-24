package com.example.demo.DTO;

import jakarta.persistence.Embeddable;

@Embeddable
public class ServiceEntry 
{
	private String name;
	
	private double price;

	public ServiceEntry(String name, double price)
	{
		super();
		this.name = name;
		this.price = price;
	}

	public ServiceEntry() 
	{
		super();
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public double getPrice() 
	{
		return price;
	}

	public void setPrice(double price) 
	{
		this.price = price;
	}
}
