package com.example.demo.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.example.demo.enums.AppointmentStatus;
import com.example.demo.enums.Branch;
import com.example.demo.enums.Catogery;
import com.example.demo.enums.Men;
import com.example.demo.enums.StaffOfBranch1;
import com.example.demo.enums.StaffOfBranch2;
import com.example.demo.enums.StaffOfBranch3;
import com.example.demo.enums.Women;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AppointmentRequest 
{
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Catogery category;
	
	@Enumerated(EnumType.STRING)
	private Men mensCatogeries;
	
	@Enumerated(EnumType.STRING)
	private Women womenCatogeries;

	private double price;
	
	private String customerId;
	
	private String customerName;
	
	private String customerEmail;
	
	private LocalDate appointmentDate;
	
	private LocalTime appointmentTime;
	
	@Enumerated(EnumType.STRING)
	private Branch branch;
	
	@Enumerated(EnumType.STRING)
	private StaffOfBranch1 staff1;
	
	@Enumerated(EnumType.STRING)
	private StaffOfBranch2 staff2;
	
	@Enumerated(EnumType.STRING)
	private StaffOfBranch3 staff3;
	
	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;
	
	private List<ServiceSelection> selectedServices;

	public static class ServiceSelection 
	{
	    private String name;
	    private Catogery category;
		public ServiceSelection(String name, Catogery category) 
		{
			super();
			this.name = name;
			this.category = category;
		}
		public ServiceSelection() 
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
		public Catogery getCategory() 
		{
			return category;
		}
		public void setCategory(Catogery category)
		{
			this.category = category;
		}
	}

	public AppointmentRequest(String name, Catogery category, Men mensCatogeries, Women womenCatogeries, double price,
			String customerId, String customerName, String customerEmail, LocalDate appointmentDate,
			LocalTime appointmentTime, Branch branch, StaffOfBranch1 staff1, StaffOfBranch2 staff2,
			StaffOfBranch3 staff3, AppointmentStatus status, List<ServiceSelection> selectedServices)
	{
		super();
		this.name = name;
		this.category = category;
		this.mensCatogeries = mensCatogeries;
		this.womenCatogeries = womenCatogeries;
		this.price = price;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.branch = branch;
		this.staff1 = staff1;
		this.staff2 = staff2;
		this.staff3 = staff3;
		this.status = status;
		this.selectedServices = selectedServices;
	}

	public AppointmentRequest() 
	{
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Catogery getCategory() {
		return category;
	}

	public void setCategory(Catogery category) {
		this.category = category;
	}

	public Men getMensCatogeries() {
		return mensCatogeries;
	}

	public void setMensCatogeries(Men mensCatogeries) {
		this.mensCatogeries = mensCatogeries;
	}

	public Women getWomenCatogeries() {
		return womenCatogeries;
	}

	public void setWomenCatogeries(Women womenCatogeries) {
		this.womenCatogeries = womenCatogeries;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public LocalTime getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(LocalTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public StaffOfBranch1 getStaff1() {
		return staff1;
	}

	public void setStaff1(StaffOfBranch1 staff1) {
		this.staff1 = staff1;
	}

	public StaffOfBranch2 getStaff2() {
		return staff2;
	}

	public void setStaff2(StaffOfBranch2 staff2) {
		this.staff2 = staff2;
	}

	public StaffOfBranch3 getStaff3() {
		return staff3;
	}

	public void setStaff3(StaffOfBranch3 staff3) {
		this.staff3 = staff3;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public List<ServiceSelection> getSelectedServices() {
		return selectedServices;
	}

	public void setSelectedServices(List<ServiceSelection> selectedServices) {
		this.selectedServices = selectedServices;
	}
}