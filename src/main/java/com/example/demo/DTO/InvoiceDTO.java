package com.example.demo.DTO;

import java.time.LocalDate;

public class InvoiceDTO 
{
	private LocalDate date;
	
	private String customerName;
	
	private String customerId;
	
	private String serviceName;
	
	private double price;
	
	private String discountType;

    private Integer manualDiscountPercent; 
    
    private Integer defaultDiscountPercent;

    private double discountAmount; 

    private double gstAmount;

    private double finalAmount;

	public InvoiceDTO(LocalDate date, String customerName, String customerId, String serviceName, double price,
			String discountType, Integer manualDiscountPercent, Integer defaultDiscountPercent, double discountAmount, double gstAmount,
			double finalAmount) 
	{
		super();
		this.date = date;
		this.customerName = customerName;
		this.customerId = customerId;
		this.serviceName = serviceName;
		this.price = price;
		this.discountType = discountType;
		this.manualDiscountPercent = manualDiscountPercent;
		this.defaultDiscountPercent = defaultDiscountPercent;
		this.discountAmount = discountAmount;
		this.gstAmount = gstAmount;
		this.finalAmount = finalAmount;
	}

	public InvoiceDTO() 
	{
		super();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Integer getManualDiscountPercent() {
		return manualDiscountPercent;
	}

	public void setManualDiscountPercent(Integer manualDiscountPercent) {
		this.manualDiscountPercent = manualDiscountPercent;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public Integer getDefaultDiscountPercent() {
		return defaultDiscountPercent;
	}

	public void setDefaultDiscountPercent(Integer defaultDiscountPercent) {
		this.defaultDiscountPercent = defaultDiscountPercent;
	}
}