package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.model.Appointment;
import com.example.demo.model.Customer;
import com.example.demo.model.ManualAppointment;
import com.example.demo.model.Menu;
import com.example.demo.model.NonMemberShipCustomer;
import com.example.demo.model.Offer;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.MenuRepo;
import com.example.demo.repository.NonMembershipRepo;
import com.example.demo.repository.OfferRepo;

@Service
public class CustomerService 
{
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private OfferRepo offerRepo;
	
	@Autowired
	private MenuRepo menuRepo;
	
	@Autowired
	private NonMembershipRepo repo;
	
	/*@Async
	public Map<String, Object> getCustomerDashboards(String cardId)
	{
		Customer customer = customerRepo.findByCardId(cardId).orElseThrow( () -> new RuntimeException("Card not Registered"));
		
		List<Offer> offers = offerRepo.findByActiveTrue();
		
		return Map.of(
				"User Id", customer.getCustomerId(),
				"name", customer.getName(),
				"points", customer.getPoints(),
				"offers", offers,
				"validity", customer.getValidity()
				);		
	}*/
	
	//service to add the new item into the menu
	@Async
	public Menu addTheNewItemToMenu(Menu menu)
	{
		System.out.println("Saving menu with price: " + menu.getPrice());
		return menuRepo.save(menu);
	}
	
	//service to get the exsisting menu item, edit that and to save again
	@Async
	public Menu getTheMenuItemEditItAndSave(Long id, Menu updated) 
	{
	    Menu existingItem = menuRepo.findById(id)
	        .orElseThrow(() -> new RuntimeException("Menu item not found"));

	    if (updated.getName() != null)
	    {
	        existingItem.setName(updated.getName());
	    }

	    if (updated.getCategory() != null)
	    {
	        existingItem.setCategory(updated.getCategory());
	    }

	    if (updated.getMensCatogeries() != null)
	    {
	        existingItem.setMensCatogeries(updated.getMensCatogeries());
	    }

	    if (updated.getWomenCatogeries() != null) 
	    {
	        existingItem.setWomenCatogeries(updated.getWomenCatogeries());
	    }
	    
	    if (updated.getPrice() != null)
	    {
	    	existingItem.setPrice(updated.getPrice());
	    }
	    existingItem.setActive(updated.isActive());

	    return menuRepo.save(existingItem);
	}
	
	//service to add the new offer
	@Async
	public Offer addTheNewOffer(Offer offer)
	{
		return offerRepo.save(offer);
	}
	
	//service to get the offer edit that and saving
	@Async
	public Offer getTheOfferEditAndSave(Long id, Offer updated)
	{
		Offer exsistingOffer = offerRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Offer not found"));
		
		exsistingOffer.setTitle(updated.getTitle());
		exsistingOffer.setCost(updated.getCost());
		exsistingOffer.setDiscount(updated.getDiscount());
		exsistingOffer.setActive(updated.isActive());
		exsistingOffer.setSubscription(updated.getSubscription()); 
		exsistingOffer.setValidUpto(updated.getValidUpto());
		
		return offerRepo.save(exsistingOffer);
	}
	
	public Customer getTheCustomerEditAndSave(String customerId, Customer update)
	{
		Customer exsistingCustomer = customerRepo.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		
		exsistingCustomer.setCardId(update.getCardId());
		exsistingCustomer.setEmail(update.getEmail());
		exsistingCustomer.setName(update.getName());
		exsistingCustomer.setPhoneNumber(update.getPhoneNumber());
		exsistingCustomer.setPoints(update.getPoints());
		exsistingCustomer.setSubscription(update.getSubscription());
		exsistingCustomer.setValidity(update.getValidity());
		
		return customerRepo.save(exsistingCustomer);
	}
	
	public NonMemberShipCustomer getTheNonMembershipCustomerEditAndSave(Long id, NonMemberShipCustomer updated)
	{
		NonMemberShipCustomer exsistingCustomer = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		
		exsistingCustomer.setCustomerName(updated.getCustomerName());
		exsistingCustomer.setEmail(updated.getEmail());
		exsistingCustomer.setPhoneNumber(updated.getPhoneNumber());
		
		return repo.save(exsistingCustomer);
	}
	
	//service to get the user dashboard
	@Async
	public Map<String, Object> getCustomerDashboards(String cardId) 
	{
	    Customer customer = customerRepo.findByCardId(cardId)
	        .orElseThrow(() -> new RuntimeException("Customer not found"));

	    List<Offer> activeOffers = offerRepo.findByActiveTrue();

	    Map<String, Object> dashboard = new HashMap<>();
	    dashboard.put("name", customer.getName());
	    dashboard.put("points", customer.getPoints());
	    dashboard.put("offers", activeOffers);
	    dashboard.put("validity", customer.getValidity());
	    dashboard.put("customerId", customer.getCustomerId()); 

	    return dashboard;
	}
	
	@Async
	public ByteArrayOutputStream generateOnlineAppointmentsExcel(
	    List<Appointment> appointments,
	    String selectedBranch,
	    LocalDate selectedWeekStart
	) throws IOException
	{
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("Appointments");

	    int rowNum = 0;

	    Row infoRow1 = sheet.createRow(rowNum++);
	    infoRow1.createCell(0).setCellValue("Branch Selected: " + (selectedBranch != null ? selectedBranch : "All"));

	    Row infoRow2 = sheet.createRow(rowNum++);
	    LocalDate weekEnd = selectedWeekStart != null ? selectedWeekStart.plusDays(6) : null;
	    infoRow2.createCell(0).setCellValue("Week Range: " +
	        (selectedWeekStart != null ? selectedWeekStart.toString() : "Not Selected") +
	        " to " +
	        (weekEnd != null ? weekEnd.toString() : "Not Selected"));

	    rowNum++; 

	    Row header = sheet.createRow(rowNum++);
	    String[] columns =
	    	{
	        "ID", "Category", "Service", "Customer ID", "Customer Name",
	        "Appointment Date", "Appointment Time", "Branch", "Staff", "Status"
	    };
	    for (int i = 0; i < columns.length; i++) {
	        header.createCell(i).setCellValue(columns[i]);
	    }

	    for (Appointment appt : appointments)
	    {
	        Row row = sheet.createRow(rowNum++);
	        row.createCell(0).setCellValue(appt.getId());
	        row.createCell(1).setCellValue(appt.getCategory().name());
	        row.createCell(2).setCellValue(appt.getName());
	        row.createCell(3).setCellValue(appt.getCustomerId());
	        row.createCell(4).setCellValue(appt.getCustomerName());
	        row.createCell(5).setCellValue(appt.getAppointmentDate().toString());
	        row.createCell(6).setCellValue(appt.getAppointmentTime().toString());
	        row.createCell(7).setCellValue(appt.getBranch().name());
	        row.createCell(8).setCellValue(
	            appt.getStaff1() != null ? appt.getStaff1().name() :
	            appt.getStaff2() != null ? appt.getStaff2().name() :
	            appt.getStaff3() != null ? appt.getStaff3().name() : "N/A"
	        );
	        row.createCell(9).setCellValue(appt.getStatus().name());
	    }

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    workbook.write(out);
	    workbook.close();
	    return out;
	}
	
	@Async
	public ByteArrayOutputStream generateOfflineAppointmentsExcel(
	    List<ManualAppointment> appointments,
	    String selectedBranch,
	    LocalDate selectedWeekStart
	) throws IOException
	{
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("Appointments");

	    int rowNum = 0;

	    Row infoRow1 = sheet.createRow(rowNum++);
	    infoRow1.createCell(0).setCellValue("Branch Selected: " + (selectedBranch != null ? selectedBranch : "All"));

	    Row infoRow2 = sheet.createRow(rowNum++);
	    LocalDate weekEnd = selectedWeekStart != null ? selectedWeekStart.plusDays(6) : null;
	    infoRow2.createCell(0).setCellValue("Week Range: " +
	        (selectedWeekStart != null ? selectedWeekStart.toString() : "Not Selected") +
	        " to " +
	        (weekEnd != null ? weekEnd.toString() : "Not Selected"));

	    rowNum++;

	    Row header = sheet.createRow(rowNum++);
	    String[] columns =
	    	{
	        "ID", "Category", "Service", "Customer Name",
	        "Appointment Date", "Appointment Time", "Branch", "Staff", "Status"
	    };
	    for (int i = 0; i < columns.length; i++) 
	    {
	        header.createCell(i).setCellValue(columns[i]);
	    }

	    for (ManualAppointment appt : appointments) 
	    {
	        Row row = sheet.createRow(rowNum++);
	        row.createCell(0).setCellValue(appt.getId());
	        row.createCell(1).setCellValue(appt.getCategory().name());
	        row.createCell(2).setCellValue(appt.getTitle());
	        row.createCell(3).setCellValue(appt.getCustomerName());
	        row.createCell(4).setCellValue(appt.getAppointmentDate().toString());
	        row.createCell(5).setCellValue(appt.getAppointmentTime().toString());
	        row.createCell(6).setCellValue(appt.getBranch().name());
	        row.createCell(7).setCellValue(
	            appt.getStaff1() != null ? appt.getStaff1().name() :
	            appt.getStaff2() != null ? appt.getStaff2().name() :
	            appt.getStaff3() != null ? appt.getStaff3().name() : "N/A"
	        );
	        row.createCell(8).setCellValue(appt.getStatus().name());
	    }

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    workbook.write(out);
	    workbook.close();
	    return out;
	}	
	
}
