package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Card 
{
	@Id
	private String cardId;
	
	@Column(unique = true)
	private String NFCId;

	public Card(String cardId, String nFCId)
	{
		super();
		this.cardId = cardId;
		NFCId = nFCId;
	}

	public Card() 
	{
		super();
	}

	public String getCardId() 
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
	}

	public String getNFCId() 
	{
		return NFCId;
	}

	public void setNFCId(String nFCId)
	{
		NFCId = nFCId;
	}
}
