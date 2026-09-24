package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyColumn;

@Entity
public class ManualAppointment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String customerName;
	
	private String title;
	
	private String email;
	
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Catogery category;
	
	@Enumerated(EnumType.STRING)
	private Men mensCatogeries;
	
	@Enumerated(EnumType.STRING)
	private Women womenCatogeries;

	private double price;
	
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
	
	@ManyToMany
	@JoinTable(name = "appointment_services", joinColumns = @JoinColumn(name = "appointment_id"),inverseJoinColumns = @JoinColumn(name = "menu_id"))
	private List<Menu> services;
	
	@ElementCollection
	@CollectionTable(name = "manual_appointment_service_staff", joinColumns = @JoinColumn(name = "appointment_id"))
	@MapKeyColumn(name = "service_name")
	@Column(name = "staff_name")
	private Map<String, String> serviceStaffMap = new HashMap<>();

	public ManualAppointment(Long id, String customerName, String title, String email, String phoneNumber, Catogery category,
			Men mensCatogeries, Women womenCatogeries, double price, LocalDate appointmentDate,
			LocalTime appointmentTime, Branch branch, StaffOfBranch1 staff1, StaffOfBranch2 staff2,
			StaffOfBranch3 staff3, AppointmentStatus status, List<Menu> services, Map<String, String> serviceStaffMap) 
	{
		super();
		this.id = id;
		this.customerName = customerName;
		this.title = title;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.category = category;
		this.mensCatogeries = mensCatogeries;
		this.womenCatogeries = womenCatogeries;
		this.price = price;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.branch = branch;
		this.staff1 = staff1;
		this.staff2 = staff2;
		this.staff3 = staff3;
		this.status = status;
		this.services = services;
		this.serviceStaffMap = serviceStaffMap;
	}

	public ManualAppointment() 
	{
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<Menu> getServices() {
		return services;
	}

	public void setServices(List<Menu> services) {
		this.services = services;
	}

	public Map<String, String> getServiceStaffMap() {
		return serviceStaffMap;
	}

	public void setServiceStaffMap(Map<String, String> serviceStaffMap) {
		this.serviceStaffMap = serviceStaffMap;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}