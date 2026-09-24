package com.example.demo.model;

import java.time.LocalDate;

import com.example.demo.enums.Subscription;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Offer 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private int cost;
	
	private String shortNote;
	
	private Integer discount;
	
	@Enumerated(EnumType.STRING)
	private Subscription subscription;
	
	private LocalDate validUpto;
	
	private boolean active;
	
	private String targetGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getShortNote() {
		return shortNote;
	}

	public void setShortNote(String shortNote) {
		this.shortNote = shortNote;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public LocalDate getValidUpto() {
		return validUpto;
	}

	public void setValidUpto(LocalDate validUpto) {
		this.validUpto = validUpto;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}

	public Offer(Long id, String title, int cost, String shortNote, Integer discount, Subscription subscription,
			LocalDate validUpto, boolean active, String targetGroup) 
	{
		super();
		this.id = id;
		this.title = title;
		this.cost = cost;
		this.shortNote = shortNote;
		this.discount = discount;
		this.subscription = subscription;
		this.validUpto = validUpto;
		this.active = active;
		this.targetGroup = targetGroup;
	}

	public Offer() 
	{
		super();
	}
}
