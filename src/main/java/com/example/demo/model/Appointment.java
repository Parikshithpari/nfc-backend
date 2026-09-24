package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.JoinColumn;
import com.example.demo.enums.AppointmentStatus;
import com.example.demo.enums.Branch;
import com.example.demo.enums.Catogery;
import com.example.demo.enums.Men;
import com.example.demo.enums.StaffOfBranch1;
import com.example.demo.enums.StaffOfBranch2;
import com.example.demo.enums.StaffOfBranch3;
import com.example.demo.enums.Women;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Transient;

@Entity
public class Appointment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
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
	
	@Transient
	private List<Menu> transientServices;
	
	@ElementCollection
	@CollectionTable(
	    name = "appointment_service_staff",
	    joinColumns = @JoinColumn(name = "appointment_id")
	)
	@MapKeyColumn(name = "service_name")
	@Column(name = "staff_name")
	private Map<String, String> serviceStaffMap = new HashMap<>();

	public Appointment(Long id, String name, Catogery category, Men mensCatogeries, Women womenCatogeries, double price,
			String customerId, String customerName, String customerEmail, LocalDate appointmentDate,
			LocalTime appointmentTime, Branch branch, StaffOfBranch1 staff1, StaffOfBranch2 staff2,
			StaffOfBranch3 staff3, AppointmentStatus status, List<Menu> transientServices,
			Map<String, String> serviceStaffMap) 
	{
		super();
		this.id = id;
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
		this.transientServices = transientServices;
		this.serviceStaffMap = serviceStaffMap;
	}

	public Appointment() 
	{
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Menu> getTransientServices() {
		return transientServices;
	}

	public void setTransientServices(List<Menu> transientServices) {
		this.transientServices = transientServices;
	}

	public Map<String, String> getServiceStaffMap() {
		return serviceStaffMap;
	}

	public void setServiceStaffMap(Map<String, String> serviceStaffMap) {
		this.serviceStaffMap = serviceStaffMap;
	}

}