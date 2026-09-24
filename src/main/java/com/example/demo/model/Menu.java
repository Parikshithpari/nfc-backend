package com.example.demo.model;

import com.example.demo.enums.Catogery;
import com.example.demo.enums.Men;
import com.example.demo.enums.Women;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Menu 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private Catogery category;
	
	@Enumerated(EnumType.STRING)
	private Men mensCatogeries;
	
	@Enumerated(EnumType.STRING)
	private Women womenCatogeries;

	private String name;
	
	private Integer price;
	
	private boolean active;

	public Menu(Long id, Catogery category, Men mensCatogeries, Women womenCatogeries, String name, Integer price,
			boolean active) 
	{
		super();
		this.id = id;
		this.category = category;
		this.mensCatogeries = mensCatogeries;
		this.womenCatogeries = womenCatogeries;
		this.name = name;
		this.price = price;
		this.active = active;
	}

	public Menu() 
	{
		super();
	}

	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Catogery getCategory() 
	{
		return category;
	}

	public void setCategory(Catogery category) 
	{
		this.category = category;
	}

	public Men getMensCatogeries() 
	{
		return mensCatogeries;
	}

	public void setMensCatogeries(Men mensCatogeries)
	{
		this.mensCatogeries = mensCatogeries;
	}

	public Women getWomenCatogeries() 
	{
		return womenCatogeries;
	}

	public void setWomenCatogeries(Women womenCatogeries) 
	{
		this.womenCatogeries = womenCatogeries;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public Integer getPrice() 
	{
		return price;
	}

	public void setPrice(Integer price)
	{
		this.price = price;
	}

	public boolean isActive() 
	{
		return active;
	}

	public void setActive(boolean active) 
	{
		this.active = active;
	}
}
