package com.example.demo.DTO;

import java.util.List;

public class InvoiceConfirmationRequest
{
	private Long AppointmentId;
	
	private List<ServiceSelection> services;
	
	private String staffName;
	
	private String discountType;
	
	private Integer manualDiscountPercent;
	
	private Integer pointsDeducted;
	
	private Integer defaultDiscountPercent;
	
	private String customerId;
	
	private Integer offerCostPoints;
	
	private Integer offerDiscountPercent;
	
	public static class ServiceSelection 
	{
	    private String name;
	    private double price;
	    private String staffName;
		
	    public ServiceSelection()
		{
			super();
		}

		public ServiceSelection(String name, double price, String staffName)
		{
			super();
			this.name = name;
			this.price = price;
			this.staffName = staffName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public String getStaffName() {
			return staffName;
		}

		public void setStaffName(String staffName) {
			this.staffName = staffName;
		}

	}

	public InvoiceConfirmationRequest(Long appointmentId, List<ServiceSelection> services, String staffName,
			String discountType, Integer manualDiscountPercent, Integer pointsDeducted, Integer defaultDiscountPercent, String customerId, Integer offerCostPoints, Integer offerDiscountPercent)
	{
		super();
		AppointmentId = appointmentId;
		this.services = services;
		this.staffName = staffName;
		this.discountType = discountType;
		this.manualDiscountPercent = manualDiscountPercent;
		this.pointsDeducted = pointsDeducted;
		this.defaultDiscountPercent = defaultDiscountPercent;
		this.customerId = customerId;
		this.offerCostPoints = offerCostPoints;
		this.offerDiscountPercent = offerDiscountPercent;
	}

	public InvoiceConfirmationRequest() 
	{
		super();
	}

	public Long getAppointmentId() {
		return AppointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		AppointmentId = appointmentId;
	}

	public List<ServiceSelection> getServices() {
		return services;
	}

	public void setServices(List<ServiceSelection> services) {
		this.services = services;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public Integer getPointsDeducted() {
		return pointsDeducted;
	}

	public void setPointsDeducted(Integer pointsDeducted) {
		this.pointsDeducted = pointsDeducted;
	}

	public Integer getDefaultDiscountPercent() {
		return defaultDiscountPercent;
	}

	public void setDefaultDiscountPercent(Integer defaultDiscountPercent) {
		this.defaultDiscountPercent = defaultDiscountPercent;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getOfferCostPoints() {
		return offerCostPoints;
	}

	public void setOfferCostPoints(Integer offerCostPoints) {
		this.offerCostPoints = offerCostPoints;
	}

	public Integer getOfferDiscountPercent() {
		return offerDiscountPercent;
	}

	public void setOfferDiscountPercent(Integer offerDiscountPercent) {
		this.offerDiscountPercent = offerDiscountPercent;
	}
}