package com.example.demo.DTO;

import java.util.List;

public class ManualInvoiceConfirmationRequest 
{
private Long AppointmentId;
	
	private List<ServiceSelection> services;
	
	private String staffName;
	
	private String discountType;
	
	private Integer manualDiscountPercent;
	
	private Integer defaultDiscountPercent;
	
	private Integer pointsDeducted;
	
	private String customerId;
	
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

	public ManualInvoiceConfirmationRequest(Long appointmentId, List<ServiceSelection> services, String staffName,
			String discountType, Integer manualDiscountPercent, Integer pointsDeducted, String customerId, Integer defaultDiscountPercent)
	{
		super();
		AppointmentId = appointmentId;
		this.services = services;
		this.staffName = staffName;
		this.discountType = discountType;
		this.manualDiscountPercent = manualDiscountPercent;
		this.pointsDeducted = pointsDeducted;
		this.customerId = customerId;
		this.defaultDiscountPercent = defaultDiscountPercent;
	}

	public ManualInvoiceConfirmationRequest() 
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getDefaultDiscountPercent() {
		return defaultDiscountPercent;
	}

	public void setDefaultDiscountPercent(Integer defaultDiscountPercent) {
		this.defaultDiscountPercent = defaultDiscountPercent;
	}
}