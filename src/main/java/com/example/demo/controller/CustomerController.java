package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.DTO.AppointmentRequest;
import com.example.demo.DTO.InvoiceConfirmationRequest;
import com.example.demo.DTO.ManualInvoiceConfirmationRequest;
import com.example.demo.components.FlyingSaucerComponent;
import com.example.demo.enums.AppointmentStatus;
import com.example.demo.enums.Catogery;
import com.example.demo.enums.Men;
import com.example.demo.enums.StaffOfBranch1;
import com.example.demo.enums.StaffOfBranch2;
import com.example.demo.enums.StaffOfBranch3;
import com.example.demo.enums.Subscription;
import com.example.demo.enums.Women;
import com.example.demo.model.Appointment;
import com.example.demo.model.Card;
import com.example.demo.model.Customer;
import com.example.demo.model.Invoice;
import com.example.demo.model.ManualAppointment;
import com.example.demo.model.ManualInvoice;
import com.example.demo.model.Menu;
import com.example.demo.model.NonMemberShipCustomer;
import com.example.demo.model.Offer;
import com.example.demo.repository.AppointmentRepo;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.ManualAppointmentRepo;
import com.example.demo.repository.MenuRepo;
import com.example.demo.repository.NonMembershipRepo;
import com.example.demo.repository.OfferRepo;
import com.example.demo.service.CustomerService;
import com.example.demo.service.UserEmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class CustomerController 
{
	@Autowired
    private CustomerService customerService;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private CardRepository cardRepo;
	
	@Autowired
	private MenuRepo menuRepo;
	
	@Autowired
	private OfferRepo offerRepo;
	
	@Autowired
	private AppointmentRepo appointmentRepo;
	
	@Autowired
	private UserEmailService mailService;
	
	@Autowired
	private NonMembershipRepo nonMembershipRepo;
	
	@Autowired
	private ManualAppointmentRepo manualAppointmentRepo;
	
	@Autowired
	private FlyingSaucerComponent flyingSaucerComponent;
	
	//url to register the card
	@PostMapping("/admin-dashboard/registerCard")
	public ResponseEntity<?> registerCard(@RequestBody Map<String, String> payload) 
	{
	    String NFCId = payload.get("NFCId");
	    if (NFCId == null || NFCId.trim().isEmpty()) 
	    {
	        return ResponseEntity.badRequest().body("❌ NFC ID is missing.");
	    }

	    String prefix = "SS_";
	    String cardId = prefix + (1000 + new Random().nextInt(9000));

	    Card card = new Card();
	    card.setCardId(cardId);
	    card.setNFCId(NFCId);

	    try 
	    {
	        cardRepo.save(card);
	        return ResponseEntity.ok("Card registered with ID: " + cardId);
	    } 
	    catch (DataIntegrityViolationException e) 
	    {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body("❌ This NFC ID is already registered.");
	    }
	}
	
	//url to add the new user
	@PostMapping("/admin-dashboard/register")
	public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) 
	{
	    if (customerRepo.findByCardId(customer.getCardId()).isPresent())
	    {
	        return ResponseEntity.badRequest().body("Card already assigned");
	    }

	    String prefix;
	    if (Subscription.BASIC.equals(customer.getSubscription())) 
	    {
	        prefix = "Basic_";
	        customer.setPoints(100);
	    } 
	    else if (Subscription.ELITE.equals(customer.getSubscription())) 
	    {
	        prefix = "Elite_";
	        customer.setPoints(700);
	    } 
	    else if (Subscription.PRO.equals(customer.getSubscription()))
	    {
	        prefix = "Pro_";
	        customer.setPoints(300);
	    } 
	    else
	    {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Subscription type");
	    }

	    String id = prefix + (1000 + new Random().nextInt(9000));
	    customer.setCustomerId(id);
	    
	    LocalDate startDate = customer.getValidity() != null ? customer.getValidity(): LocalDate.now();
	    customer.setValidity(startDate.plusYears(1));
	    		
	    Customer saved = customerRepo.save(customer);
	    
	    mailService.sendRegistrationSuccessfulEmailAsync(customer);
	    return ResponseEntity.ok(saved);
	}
	
	//url to get the link and to paste in the nfc card
	@GetMapping("/admin-dashboard/getLink")
	public ResponseEntity<?> getLink(@RequestParam String NFCId)
	{
	    Optional<Card> card = cardRepo.findByNFCId(NFCId);
	    if (card.isEmpty()) return ResponseEntity.notFound().build();

	    Optional<Customer> customer = customerRepo.findByCardId(card.get().getCardId());
	    if (customer.isEmpty()) return ResponseEntity.ok("WAITING");

	    String link = "http://127.0.0.1:5501/nfc/schon salon/user-dashboard.html?uid=" + card.get().getCardId();
	    return ResponseEntity.ok(link);
	}
    
	//url to get the user by the card id
	@GetMapping("/user-dashboard/by-card/{cardId}")
	public ResponseEntity<?> getCustomerDashboard(@PathVariable String cardId) 
	{
	    try 
	    {
	        Map<String, Object> dashboard = customerService.getCustomerDashboards(cardId);
	        
	        return ResponseEntity.ok(dashboard);
	    } 
	    catch (RuntimeException e) 
	    {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(Map.of("error", e.getMessage()));
	    }
	}
    
    //url for show all the menu items
    @GetMapping("/user-dashboard/menu")
    public List<Menu> getAllTheMenuItems()
    {
    	return menuRepo.findByActiveTrue();
    }
    
    //url to get the list of offers
    @GetMapping("/user-dashboard/offer")
    public List<Offer> getAllTheOffers()
    {
    	return offerRepo.findByActiveTrue();
    }
    
    //url to add the new item to the menu
    @PostMapping("/admin-panel/menu/addItem")
    public ResponseEntity<?> addTheNewItemToMenu(@RequestBody Menu menu)
    {
    	customerService.addTheNewItemToMenu(menu);
    	
    	return ResponseEntity.status(HttpStatus.OK).body("Successfully added the new item to the menu");
    }
    
    //url to edit the menu item
    @PutMapping("/admin-panel/menu/updateMenu/{id}")
    public ResponseEntity<?> editTheExsistingItem(@PathVariable Long id, @RequestBody Menu updated)
    {
    	customerService.getTheMenuItemEditItAndSave(id, updated);
    	
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated the menu item");
    }
    
    //url to add the new offer 
    @PostMapping("/admin-panel/offer/addNewOffer")
    public ResponseEntity<?> addTheNewOffer(@RequestBody Offer offer) 
    {
        customerService.addTheNewOffer(offer);

        if (offer.isActive())
        {
            switch (offer.getTargetGroup().toUpperCase()) 
            {
                case "NON_MEMBERSHIP":
                    mailService.notifyNonMembershipUsers(offer);
                    break;
                case "MEMBERSHIP":
                    if (offer.getSubscription() != null)
                    {
                        mailService.notifyUserBySubscription(offer.getSubscription(), offer);
                    }
                    break;
                case "ALL":
                    mailService.notifyNonMembershipUsers(offer);
                    for (Subscription sub : Subscription.values())
                    {
                        mailService.notifyUserBySubscription(sub, offer);
                    }
                    break;
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Offer added successfully");
    }
    
    //url to edit the offer
    @PutMapping("/admin-dashboard/offer/updateOffer/{id}")
    public ResponseEntity<?> editTheOffer(@PathVariable Long id, @RequestBody Offer updated)
    {
    	customerService.getTheOfferEditAndSave(id, updated);
    	
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body("Offer updated successfully");
    }
    
    //url to edit the membership customer
    @PutMapping("/admin-dashboard/customer/editCustomer/{customerId}")
    public ResponseEntity<?> editTheCustomer(@PathVariable String customerId, @RequestBody Customer customer)
    {
    	customerService.getTheCustomerEditAndSave(customerId, customer);
    	
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body("Customer updated successfully");
    }
    
    //url to edit the non membership user
    @PutMapping("/admin-dashboard/nonMembershipCustomer/editCustomer/{id}")
    public ResponseEntity<?> editTheNonMembershipCustomer(@PathVariable Long id, @RequestBody NonMemberShipCustomer customer)
    {
    	customerService.getTheNonMembershipCustomerEditAndSave(id, customer);
    	
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body("Customer updated successfully");
    }
    
    //url to book an appointment
    @PostMapping("/user-dashboard/book-appointment")
    public ResponseEntity<?> bookAnAppointment(@RequestBody AppointmentRequest request)
    {
        Optional<Customer> customerOpt = customerRepo.findByCardId(request.getCustomerId());
        if (customerOpt.isEmpty()) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UID");
        }
        Customer customer = customerOpt.get();

        List<AppointmentRequest.ServiceSelection> serviceSelections =
                Optional.ofNullable(request.getSelectedServices()).orElse(Collections.emptyList());

        List<Menu> selectedServices = new ArrayList<>();
        double totalPrice = 0;

        for (AppointmentRequest.ServiceSelection selection : serviceSelections) 
        {
            Optional<Menu> menuOpt = menuRepo.findByNameAndCategory(selection.getName(), selection.getCategory());
            if (menuOpt.isEmpty()) 
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Service not found: " + selection.getName() + " in category " + selection.getCategory());
            }
            Menu service = menuOpt.get();
            selectedServices.add(service);
            totalPrice += service.getPrice();
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setCategory(request.getCategory());
        appointment.setMensCatogeries(request.getMensCatogeries());
        appointment.setWomenCatogeries(request.getWomenCatogeries());
        appointment.setBranch(request.getBranch());

        appointment.setStaff1(request.getStaff1() != null ? request.getStaff1() : null);
        appointment.setStaff2(request.getStaff2() != null ? request.getStaff2() : null);
        appointment.setStaff3(request.getStaff3() != null ? request.getStaff3() : null);

        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setPrice(totalPrice);
        appointment.setTransientServices(selectedServices);

        appointment.setCustomerEmail(customer.getEmail());
        appointment.setCustomerName(customer.getName());
        appointment.setCustomerId(customer.getCustomerId());

        appointmentRepo.save(appointment);

        mailService.sendAppointmentConfirmationEmailToSubscriptionCustomer(appointment);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Appointment request sent successfully");
    }

    
    //url for showing the appointments in the admin panel
    @GetMapping("/admin-panel/appointments")
    public List<Appointment> getAllTheAppoitmentBookings()
    {
    	return appointmentRepo.findAll();
    }
    
    //url to get the list of offers
    @GetMapping("/admin-dashboard/getAllOffers")
    public List<Offer> getAllOffers()
    {
    	return offerRepo.findByActiveTrue();
    }
    
    //url to get all the active and inactive offers
    @GetMapping("/admin-dashboard/getInactiveAndActiveOffers")
    public List<Offer> getAllTheActiveAndInactiveOffers()
    {
        LocalDate today = LocalDate.now();
        List<Offer> allOffers = offerRepo.findAll();

        for (Offer offer : allOffers) 
        {
            if (offer.getValidUpto() != null && offer.getValidUpto().isBefore(today))
            {
                offer.setActive(false);
                offerRepo.save(offer);
            }
        }

        return allOffers;
    }
    
    //url to get the active and inactive menu items
    @GetMapping("/admin-dashboard/getInactiveAndActiveMenuItems")
    public List<Menu> getAllTheActiveAndInactiveMenuItems()
    {
    	return menuRepo.findAll();
    }
    
    //url to get all the customers
    @GetMapping("/admin-dashboard/getAllTheCustomers")
    public List<Customer> getAllTheCustomers()
    {
    	return customerRepo.findAll();
    }
    
    //url to get all silver users
   @GetMapping("/admin-dashboard/getAllBasicCustomers")
    public List<Customer> getAllTheBasicCustomers() 
    {
        return customerRepo.findAllBySubscription(Subscription.BASIC);
    }

    //url to get all gold users
    @GetMapping("/admin-dashboard/getAllProCustomers")
    public List<Customer> getAllTheProCustomers() 
    {
        return customerRepo.findAllBySubscription(Subscription.PRO);
    }

    //url to get all platinum users
    @GetMapping("/admin-dashboard/getAllEliteCustomers")
    public List<Customer> getAllTheEliteCustomers() 
    {
        return customerRepo.findAllBySubscription(Subscription.ELITE);
    }
    
    //url to register the non membership user
    @PostMapping("/admin-dashboard/regsiterNonMembershipUser")
    public ResponseEntity<?> registerNonMemberShipCustomer(@RequestBody NonMemberShipCustomer customer) 
    {
        try {
            if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty() ||
                customer.getCustomerName() == null || customer.getCustomerName().trim().isEmpty()) 
            {
                return ResponseEntity.badRequest().body("❌ Missing required fields: name or phone number.");
            }

            // Optional: check if user already exists
            boolean exists = nonMembershipRepo.existsByPhoneNumber(customer.getPhoneNumber());
            if (exists) 
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ℹ️ User already registered.");
            }

            nonMembershipRepo.save(customer);
            mailService.sendRegistrationSuccessfulEmailForNonMembershipUser(customer);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("✅ User Registered successfully");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("❌ Registration failed due to server error.");
        }
    }
    
    //url to get all the non membership users
    @GetMapping("/admin-dashboard/getAllNonMembershipUsers")
    public List<NonMemberShipCustomer> getAllTheNonMembershipCustomers()
    {
    	return nonMembershipRepo.findAll();
    }
    
    @GetMapping("/admin-dashboard/getAllCustomers")
    public List<Map<String, Object>> getAllCustomers() 
    {
        List<Map<String, Object>> all = new ArrayList<>();

        List<Customer> members = customerRepo.findAll();
        for (Customer c : members) 
        {
            Map<String, Object> map = new HashMap<>();
            map.put("customerId", c.getCustomerId());
            map.put("name", c.getName());
            map.put("email", c.getEmail());
            map.put("cardId", c.getCardId());
            map.put("points", c.getPoints());
            map.put("subscription", c.getSubscription().name());
            map.put("validity", c.getValidity());
            map.put("type", c.getSubscription().name());
            all.add(map);
        }

        List<NonMemberShipCustomer> nonMembers = nonMembershipRepo.findAll();
        for (NonMemberShipCustomer c : nonMembers) 
        {
            Map<String, Object> map = new HashMap<>();
            map.put("customerId", c.getId());
            map.put("name", c.getCustomerName());
            map.put("email", c.getEmail());
            map.put("cardId", "-");
            map.put("points", "-");
            map.put("subscription", "NONMEMBER");
            map.put("validity", "-");
            map.put("type", "NONMEMBER");
            all.add(map);
        }

        return all;
    }
    
    //url for booking the manual appoitment by the staff
    @PostMapping("/admin-dashboard/bookManualAppointment")
    public ResponseEntity<?> bookTheManualAppointment(@RequestBody ManualAppointment appointment)
    {	
    	appointment.setStatus(appointment.getStatus().PENDING);
    	
    	mailService.sendAppointmentConfirmationEmailToManualAppointments(appointment);
    	manualAppointmentRepo.save(appointment);
    	return ResponseEntity.status(HttpStatus.OK).body("Appointment Booked");
    }
    
    //url to get all the manual appointments
    @GetMapping("/admin-dashboard/getAllTheManualAppointments")
    public List<ManualAppointment> getAllTheManualAppointments()
    {
    	return manualAppointmentRepo.findAll();
    }
    
    //url to update the status of the appointment and to send the invoice to the us
    @PostMapping("/admin-dashboard/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) 
    {
        Optional<Appointment> opt = appointmentRepo.findById(id);
        if (opt.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        }

        Appointment appointment = opt.get();
        appointment.setStatus(status);
        appointmentRepo.save(appointment);

        return ResponseEntity.ok("Status updated to " + status);
    }
    
    @GetMapping("/admin-panel/customer/{customerId}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable String customerId) 
    {
        Optional<Customer> customerOpt = customerRepo.findByCustomerId(customerId);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer customer = customerOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("customerId", customer.getCustomerId());
        response.put("customerName", customer.getName());
        response.put("subscription", customer.getSubscription().name());
        response.put("Validity", customer.getValidity());
        response.put("points", customer.getPoints());

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin-panel/customer-by-phone/{phoneNumber}")
    public ResponseEntity<?> getCustomerByPhone(@PathVariable String phoneNumber) 
    {
        Optional<Customer> customerOpt = customerRepo.findByPhoneNumber(phoneNumber);
        if (customerOpt.isEmpty()) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer customer = customerOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("customerId", customer.getCustomerId());
        response.put("customerName", customer.getName());
        response.put("subscription", customer.getSubscription().name());
        response.put("points", customer.getPoints());

        return ResponseEntity.ok(response);
    }
    
    //url to update the manual appointment status
    @PostMapping("/admin-dashboard/updateManualAppointment-status/{id}")
    public ResponseEntity<?> updateTheManualAppointmentStatus(@PathVariable Long id, @RequestParam AppointmentStatus status)
    {
    	Optional<ManualAppointment> opt = manualAppointmentRepo.findById(id);
    	if(opt.isEmpty())
    	{
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
    	}
    	
    	ManualAppointment appointment = opt.get();
    	appointment.setStatus(status);
    	manualAppointmentRepo.save(appointment);
    	
    	return ResponseEntity.ok("Status Updated to "+ status);
    }
    
    @DeleteMapping("/admin-dashboard/deleteMenuItemById")
    public ResponseEntity<?> deleteTheMenuItem(@RequestParam Long id)
    {
        // Step 1: Delete from join table
        appointmentRepo.deleteFromJoinTableByMenuId(id);

        // Step 2: Delete the menu item
        menuRepo.deleteById(id);

        return ResponseEntity.ok("Menu Item Deleted successfully");
    }
    
    @DeleteMapping("/admin-dashboard/deleteOfferById")
    public ResponseEntity<?> deleteTheOfferById(@RequestParam Long id)
    {
    	offerRepo.deleteById(id);
    	return ResponseEntity.ok("Offer Deleted Successfully");
    }
       
    //url to get the menu by the category
    @GetMapping("/user-dashboard/menu-by-category/{category}")
    public ResponseEntity<List<Menu>> fetchMenuByCategory(@PathVariable String category) 
    {
        if (category == null || category.trim().isEmpty()) 
        {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Menu> items = menuRepo.findByCategory(category.toUpperCase());
        return ResponseEntity.ok(items);
    }
    
    @PostMapping("/admin-dashboard/confirm-invoice")
    public ResponseEntity<?> confirmAndSendInvoice(@RequestBody InvoiceConfirmationRequest request) throws Exception {
        Optional<Appointment> opt = appointmentRepo.findById(request.getAppointmentId());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        }

        Appointment appointment = opt.get();
        String branch = appointment.getBranch().name();

        StringBuilder serviceDetails = new StringBuilder();
        double total = 0;
        List<Menu> persistedServices = new ArrayList<>();

        for (InvoiceConfirmationRequest.ServiceSelection entry : request.getServices()) {
            String rawName = entry.getName();
            String staffName = entry.getStaffName();

            if (rawName == null || rawName.equalsIgnoreCase("Not selected")) continue;
            if (staffName == null || staffName.isBlank()) {
                return ResponseEntity.badRequest().body("Missing staff for service: " + rawName);
            }

            try {
                switch (branch) {
                    case "SALON_B1" -> StaffOfBranch1.valueOf(staffName);
                    case "SALON_B2" -> StaffOfBranch2.valueOf(staffName);
                    case "SALON_B3" -> StaffOfBranch3.valueOf(staffName);
                    default -> throw new IllegalArgumentException("Unknown branch: " + branch);
                }
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid staff name for branch: " + staffName);
            }

            appointment.getServiceStaffMap().put(rawName, staffName);

            List<Menu> matches = menuRepo.findAllByName(rawName);
            if (matches.isEmpty()) {
                throw new RuntimeException("Menu not found: " + rawName);
            }

            Menu menu = matches.get(0);
            double price = menu.getName().equalsIgnoreCase(appointment.getName()) ? appointment.getPrice() : menu.getPrice();

            serviceDetails.append(menu.getName()).append(" - ₹").append(price).append("\n");
            total += price;
            persistedServices.add(menu);
        }

        appointment.setTransientServices(persistedServices);
        appointment.setStatus(AppointmentStatus.COMPLETED);

        if (!persistedServices.isEmpty()) {
            String combinedNames = persistedServices.stream()
                .map(Menu::getName)
                .distinct()
                .collect(Collectors.joining(", "));
            appointment.setName(combinedNames);
            appointment.setPrice((int) total);
        }

        String firstStaff = request.getServices().stream()
            .map(InvoiceConfirmationRequest.ServiceSelection::getStaffName)
            .filter(s -> s != null && !s.isBlank())
            .findFirst()
            .orElse(null);

        if (firstStaff != null) {
            switch (branch) {
                case "SALON_B1" -> appointment.setStaff1(StaffOfBranch1.valueOf(firstStaff));
                case "SALON_B2" -> appointment.setStaff2(StaffOfBranch2.valueOf(firstStaff));
                case "SALON_B3" -> appointment.setStaff3(StaffOfBranch3.valueOf(firstStaff));
            }
        }

        appointmentRepo.save(appointment);

        // ✅ Discount and GST logic
        double originalAmount = total;
        double discountAmount = 0;

        // ✅ Use request.getCustomerId() for manual flow
        String customerId = request.getCustomerId() != null ? request.getCustomerId() : appointment.getCustomerId();
        Optional<Customer> customerOpt = customerId != null
            ? customerRepo.findByCustomerId(customerId)
            : Optional.empty();

        if (customerOpt.isEmpty()) {
            return ResponseEntity.ok("Invoice not sent (customer missing)");
        }

        Customer customer = customerOpt.get();
        int currentPoints = customer.getPoints() != null ? customer.getPoints() : 0;
        Subscription subscription = customer.getSubscription();

        String discountType = request.getDiscountType() != null ? request.getDiscountType().toUpperCase() : "NONE";

        int maxAllowed = switch (subscription) {
            case BASIC, PRO -> 20;
            case ELITE -> 25;
            default -> 0;
        };

        int pointsToDeduct = 0;

        if ("DEFAULT".equalsIgnoreCase(discountType)) {
            int selectedPercent = request.getDefaultDiscountPercent() != null ? request.getDefaultDiscountPercent() : 0;

            if (subscription == Subscription.BASIC && currentPoints < 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Basic users with less than 100 points cannot use default discount.");
            }

            if (selectedPercent <= 0 || selectedPercent > maxAllowed) {
                return ResponseEntity.badRequest()
                    .body("❌ Invalid discount percent for " + subscription + " (max " + maxAllowed + "%)");
            }

            discountAmount = (selectedPercent / 100.0) * originalAmount;
            pointsToDeduct = request.getPointsDeducted() != null ? request.getPointsDeducted() : 0;
        } 
        else if ("MANUAL".equalsIgnoreCase(discountType) && request.getManualDiscountPercent() != null)
        {
            discountAmount = (request.getManualDiscountPercent() / 100.0) * originalAmount;
        }
        else if ("OFFER".equalsIgnoreCase(discountType)) 
        {
            int offerCostPoints = request.getOfferCostPoints() != null ? request.getOfferCostPoints() : 0;
            int offerDiscountPercent = request.getOfferDiscountPercent() != null ? request.getOfferDiscountPercent() : 0;

            if (offerCostPoints <= 0 || offerDiscountPercent <= 0) {
                return ResponseEntity.badRequest()
                    .body("❌ Invalid offer configuration: missing cost or discount percent.");
            }

            if (subscription == Subscription.BASIC && currentPoints < offerCostPoints) {
                return ResponseEntity.badRequest()
                    .body("❌ Basic users need at least " + offerCostPoints + " points to redeem this offer.");
            }

            discountAmount = (offerDiscountPercent / 100.0) * originalAmount;
            pointsToDeduct = offerCostPoints;
        }

        double discountedTotal = originalAmount - discountAmount;
        double gstAmount = 0.05 * discountedTotal;
        double finalAmount = discountedTotal + gstAmount;

        // ✅ Loyalty logic
        int multiplier = switch (subscription) {
            case BASIC -> 1;
            case PRO -> 2;
            case ELITE -> 3;
            default -> 0;
        };
        int earnedPoints = (int) (discountedTotal / 50) * multiplier;
        int finalPoints = Math.max(0, currentPoints - pointsToDeduct) + earnedPoints;
        customer.setPoints(finalPoints);
        customerRepo.save(customer);

        // ✅ Build and send invoice
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + appointment.getId());
        invoice.setDate(LocalDate.now());
        invoice.setCustomerName(appointment.getCustomerName());
        invoice.setCustomerId(customerId); // ✅ Use correct customer ID
        invoice.setServiceName(serviceDetails.toString());
        invoice.setPrice(originalAmount);
        invoice.setDiscountType(discountType);
        invoice.setManualDiscountPercent(request.getManualDiscountPercent());
        invoice.setDefaultDiscountPercent(request.getDefaultDiscountPercent());
        invoice.setDiscountAmount(discountAmount);
        invoice.setGstAmount(gstAmount);
        invoice.setFinalAmount(finalAmount);

        ByteArrayOutputStream pdf = flyingSaucerComponent.renderInvoice(invoice, firstStaff);
        Files.write(Paths.get("online-invoices", invoice.getInvoiceNumber() + ".pdf"), pdf.toByteArray());

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            return ResponseEntity.ok("Invoice not sent (email missing)");
        }

        try {
            mailService.sendInvoice(customer, pdf);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Invoice generated, but email failed to send.");
        }

        return ResponseEntity.ok("✅ Invoice sent to " + customer.getEmail());
    }
    
    @GetMapping("/admin-dashboard/invoice-download/{invoiceNumber}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable String invoiceNumber) throws IOException 
    {
        Path pdfPath = Paths.get("online-invoices", invoiceNumber + ".pdf");

        if (!Files.exists(pdfPath))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + invoiceNumber + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
    
    @GetMapping("/admin-dashboard/debug/manual-invoices")
    public ResponseEntity<List<String>> listManualInvoices() 
    {
        File folder = new File("manual-invoices");
        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(List.of("❌ Folder 'manual-invoices' not found"));
        }

        String[] files = folder.list((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        return ResponseEntity.ok(files != null ? Arrays.asList(files) : List.of("⚠️ No PDF files found"));
    }
    
    @GetMapping("/admin-dashboard/debug/online-invoices")
    public ResponseEntity<List<String>> listOnlineInvoices() 
    {
        File folder = new File("online-invoices");
        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(List.of("❌ Folder 'online-invoices' not found"));
        }

        String[] files = folder.list((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        return ResponseEntity.ok(files != null ? Arrays.asList(files) : List.of("⚠️ No PDF files found"));
    }

    @PostMapping("/admin-dashboard/confirm-Manualinvoice")
    public ResponseEntity<?> confirmManualInvoice(@RequestBody ManualInvoiceConfirmationRequest request) throws Exception {

        if (request.getAppointmentId() == null) {
            return ResponseEntity.badRequest().body("Missing appointmentId in request");
        }

        Optional<ManualAppointment> opt = manualAppointmentRepo.findById(request.getAppointmentId());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manual appointment not found");
        }

        ManualAppointment appointment = opt.get();
        String branch = appointment.getBranch().name();

        double total = 0;
        StringBuilder serviceDetails = new StringBuilder();
        List<Menu> persistedServices = new ArrayList<>();

        for (ManualInvoiceConfirmationRequest.ServiceSelection entry : request.getServices()) {
            String rawName = entry.getName();
            String staffName = entry.getStaffName();

            if (rawName == null || rawName.equalsIgnoreCase("Not selected")) continue;
            if (staffName == null || staffName.isBlank()) {
                return ResponseEntity.badRequest().body("Missing staff for service: " + rawName);
            }

            try {
                switch (branch) {
                    case "SALON_B1" -> StaffOfBranch1.valueOf(staffName);
                    case "SALON_B2" -> StaffOfBranch2.valueOf(staffName);
                    case "SALON_B3" -> StaffOfBranch3.valueOf(staffName);
                    default -> throw new IllegalArgumentException("Unknown branch: " + branch);
                }
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid staff name for branch: " + staffName);
            }

            appointment.getServiceStaffMap().put(rawName, staffName);

            List<Menu> matches = menuRepo.findAllByName(rawName);
            if (matches.isEmpty()) {
                throw new RuntimeException("Menu not found: " + rawName);
            }

            Menu menu = matches.get(0);
            double price = menu.getName().equalsIgnoreCase(appointment.getTitle()) ? appointment.getPrice() : menu.getPrice();

            serviceDetails.append(menu.getName()).append(" - ₹").append(price).append("\n");
            total += price;
            persistedServices.add(menu);
        }

        appointment.setServices(persistedServices);
        appointment.setStatus(AppointmentStatus.COMPLETED);

        if (!persistedServices.isEmpty()) {
            String combinedNames = persistedServices.stream()
                .map(Menu::getName)
                .distinct()
                .collect(Collectors.joining(", "));
            appointment.setTitle(combinedNames);
            appointment.setPrice((int) total);
        }

        String firstStaff = request.getServices().stream()
            .map(ManualInvoiceConfirmationRequest.ServiceSelection::getStaffName)
            .filter(s -> s != null && !s.isBlank())
            .findFirst()
            .orElse(null);

        if (firstStaff != null) {
            switch (branch) {
                case "SALON_B1" -> appointment.setStaff1(StaffOfBranch1.valueOf(firstStaff));
                case "SALON_B2" -> appointment.setStaff2(StaffOfBranch2.valueOf(firstStaff));
                case "SALON_B3" -> appointment.setStaff3(StaffOfBranch3.valueOf(firstStaff));
            }
        }

        manualAppointmentRepo.save(appointment);

        // ✅ Discount and GST
        double discountAmount = 0;
        int pointsToDeduct = request.getPointsDeducted() != null ? request.getPointsDeducted() : 0;

        if ("MANUAL".equalsIgnoreCase(request.getDiscountType()) && request.getManualDiscountPercent() != null) {
            discountAmount = (request.getManualDiscountPercent() / 100.0) * total;
        }

        if ("DEFAULT".equalsIgnoreCase(request.getDiscountType()) && request.getDefaultDiscountPercent() != null) {
            discountAmount = (request.getDefaultDiscountPercent() / 100.0) * total;
        }

        double discountedTotal = total - discountAmount;
        double gstAmount = 0.05 * discountedTotal;
        double finalAmount = discountedTotal + gstAmount;

        // ✅ Loyalty logic
        String customerId = request.getCustomerId();
        Optional<Customer> customerOpt = customerId != null
            ? customerRepo.findByCustomerId(customerId)
            : Optional.empty();

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            int currentPoints = customer.getPoints() != null ? customer.getPoints() : 0;
            Subscription subscription = customer.getSubscription();

            int multiplier = switch (subscription) {
                case BASIC -> 1;
                case PRO -> 2;
                case ELITE -> 3;
                default -> 0;
            };

            int earnedPoints = (int) (discountedTotal / 50) * multiplier;
            int finalPoints = Math.max(0, currentPoints - pointsToDeduct) + earnedPoints;

            customer.setPoints(finalPoints);
            customerRepo.save(customer);
            System.out.println("Current points: " + currentPoints);
            System.out.println("Points deducted: " + pointsToDeduct);
            System.out.println("Discounted total: " + discountedTotal);
            System.out.println("Earned points: " + earnedPoints);
            System.out.println("Final points: " + finalPoints);

        }

        // ✅ Build and send invoice
        ManualInvoice invoice = new ManualInvoice();
        invoice.setInvoiceNumber("INV_MANUAL-" + appointment.getId());
        invoice.setDate(LocalDate.now());
        invoice.setCustomerName(appointment.getCustomerName());
        invoice.setCustomerId(customerId); // ✅ Use correct customer ID
        invoice.setServiceName(serviceDetails.toString());
        invoice.setPrice(finalAmount);
        invoice.setDiscountAmount(discountAmount);
        invoice.setGstAmount(gstAmount);
        invoice.setDiscountType(request.getDiscountType());
        invoice.setManualDiscountPercent(request.getManualDiscountPercent());
        invoice.setManualDiscountPercent(request.getManualDiscountPercent());

        ByteArrayOutputStream pdf = flyingSaucerComponent.renderManualInvoice(invoice);
        Files.createDirectories(Paths.get("manual-invoices"));
        Files.write(Paths.get("manual-invoices", invoice.getInvoiceNumber() + ".pdf"), pdf.toByteArray());

        if (appointment.getEmail() == null || appointment.getEmail().isBlank()) {
            return ResponseEntity.ok("Invoice not sent (email missing)");
        }

        try {
            mailService.sendManualInvoice(appointment, pdf);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Invoice generation succeeded, but email failed to send.");
        }

        return ResponseEntity.ok("✅ Manual invoice sent to " + appointment.getEmail());
        
    }
    
    @GetMapping("/admin-dashboard/manual-invoice-download/{invoiceNumber}")
    public ResponseEntity<byte[]> downloadManualInvoice(@PathVariable String invoiceNumber) throws IOException {
        Path pdfPath = Paths.get("manual-invoices", invoiceNumber + ".pdf");

        if (!Files.exists(pdfPath))
        {
            System.out.println("❌ File not found: " + pdfPath.toAbsolutePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + invoiceNumber + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
    
    
    //url to get the services based in the catogeries
    @GetMapping("/user-dashboard/menu-by-category/{category}/{subcategory}")
    public List<Menu> getByCategoryAndSubcategory(@PathVariable String category,
                                                  @PathVariable String subcategory) 
    {
        Catogery cat = Catogery.valueOf(category.toUpperCase());

        if (cat == Catogery.MEN) 
        {
            Men menSub = Men.valueOf(subcategory.toUpperCase());
            return menuRepo.findByCategoryAndMensCatogeries(cat, menSub);
        }
        else if (cat == Catogery.WOMEN) 
        {
            Women womenSub = Women.valueOf(subcategory.toUpperCase());
            return menuRepo.findByCategoryAndWomenCatogeries(cat, womenSub);
        } 
        else
        {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
    
    //url to membership appointments
    @GetMapping("/admin-dashboard/export-filtered-membership-appointments")
    public ResponseEntity<byte[]> exportFilteredMembershipAppointmentsToExcel(
        @RequestParam(required = false) String branch,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) throws IOException {
        List<Appointment> allAppointments = appointmentRepo.findByStatusIn(
            List.of(AppointmentStatus.RESCHEDULED, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED)
        );

        // ✅ Apply branch and week filters
        List<Appointment> filteredAppointments = allAppointments.stream()
            .filter(appt -> {
                boolean branchMatch = branch == null || branch.equals("ALL") || appt.getBranch().name().equals(branch);
                boolean dateMatch = true;
                if (weekStart != null && appt.getAppointmentDate() != null) {
                    LocalDate date = appt.getAppointmentDate();
                    LocalDate weekEnd = weekStart.plusDays(6);
                    dateMatch = !date.isBefore(weekStart) && !date.isAfter(weekEnd);
                }
                return branchMatch && dateMatch;
            })
            .toList();

        ByteArrayOutputStream excelStream = customerService.generateOnlineAppointmentsExcel(
            filteredAppointments,
            branch,
            weekStart
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
            .filename("Online appointments.xlsx")
            .build());

        return new ResponseEntity<>(excelStream.toByteArray(), headers, HttpStatus.OK);
    }
    
    //url to download the non membership appoitments
    @GetMapping("/admin-dashboard/export-filtered-nonmembership-appointments")
    public ResponseEntity<byte[]> exportFilteredOfflineAppointmentsToExcel(
        @RequestParam(required = false) String branch,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) throws IOException 
    {
        List<ManualAppointment> allAppointments = manualAppointmentRepo.findByStatusIn(
            List.of(AppointmentStatus.RESCHEDULED, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED)
        );

        List<ManualAppointment> filteredAppointments = allAppointments.stream()
            .filter(appt -> {
                boolean branchMatch = branch == null || branch.equals("ALL") || appt.getBranch().name().equals(branch);
                boolean dateMatch = true;
                if (weekStart != null && appt.getAppointmentDate() != null) 
                {
                    LocalDate date = appt.getAppointmentDate();
                    LocalDate weekEnd = weekStart.plusDays(6);
                    dateMatch = !date.isBefore(weekStart) && !date.isAfter(weekEnd);
                }
                return branchMatch && dateMatch;
            })
            .toList();

        ByteArrayOutputStream excelStream = customerService.generateOfflineAppointmentsExcel(
            filteredAppointments,
            branch,
            weekStart
        );

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDisposition(ContentDisposition.builder("attachment")
            .filename("Offline appointments.xlsx")
            .build());

        return new ResponseEntity<>(excelStream.toByteArray(), header, HttpStatus.OK);
    }
    

    //url to delete subscription members
    @DeleteMapping("/admin-dashboard/deleteCustomer/{customerId}")
    @Transactional
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) 
    {
        try 
        {
            String decodedId = URLDecoder.decode(customerId, StandardCharsets.UTF_8);
            customerRepo.deleteByCustomerId(decodedId);
            return ResponseEntity.ok(Map.of("message", "Customer deleted successfully"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to delete customer"));
        }
    }

    //url to delete non-membership users
    @DeleteMapping("/admin-dashboard/deleteNonMember/{id}")
    public ResponseEntity<?> deleteNonMember(@PathVariable Long id) 
    {
        try 
        {
            nonMembershipRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Non-member deleted successfully"));
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to delete non-member"));
        }
    }
}