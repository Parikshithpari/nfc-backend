package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.enums.Subscription;
import com.example.demo.model.Appointment;
import com.example.demo.model.Customer;
import com.example.demo.model.ManualAppointment;
import com.example.demo.model.NonMemberShipCustomer;
import com.example.demo.model.Offer;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.NonMembershipRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserEmailService 
{
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private NonMembershipRepo nonMembershipRepo;
	
	@Async
	public void sendRegistrationSuccessfulEmailAsync(Customer customer) 
	{
	    String subject = "Welcome to Salon – Registration Successful!";

	    String body = "Hi " + customer.getName() + ",\n\n"
	                + "We're excited to have you on board! Your registration was successful.\n"
	                + "Here are your details:\n"
	                + "Customer ID: " + customer.getCustomerId() + "\n\n"
	                + "Points: " + customer.getPoints() + "\n\n"
	                + "You can now enjoy all the benefits of our platform.\n"
	                + "If you have any questions, feel free to reach out to our support team.\n\n"
	                + "Warm regards,\n"
	                + "salon Team";
	    
	    SimpleMailMessage message = new SimpleMailMessage();
	    
	    message.setFrom("mailforalltestings@gmail.com");
	    message.setTo(customer.getEmail());
	    message.setSubject(subject);
	    message.setText(body);
	    
	    mailSender.send(message);
	}
	
	@Async
	public void sendRegistrationSuccessfulEmailForNonMembershipUser(NonMemberShipCustomer customer)
	{
		String subject = "Welcome to Our Salon – Registration Successful!";
		
		 String body = "Hi " + customer.getCustomerName() + ",\n\n"
	                + "We're excited to have you on board! Your registration was successful.\n"
	                + "You can now enjoy all the benefits of our platform.\n"
	                + "If you have any questions, feel free to reach out to our support team.\n\n"
	                + "Warm regards,\n"
	                + "salon Team";
		 
		 SimpleMailMessage message = new SimpleMailMessage();
		    
		 message.setFrom("mailforalltestings@gmail.com");
		 message.setTo(customer.getEmail());
		 message.setSubject(subject);
		 message.setText(body);
		    
		 mailSender.send(message);
	}
	
	@Async
	public void sendInvoice(Customer customer, ByteArrayOutputStream pdf) throws MessagingException
	{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(customer.getEmail());
        helper.setSubject("Your Salon Invoice");
        helper.setText(
            "Hi " + customer.getName() + ",\n\n" +
            "Thank you for visiting Saloon!\n" +
            "Please find your invoice attached.\n\n" +
            "Warm regards,\n" +
            "Salon Team"
        );

        helper.addAttachment("invoice.pdf", new ByteArrayResource(pdf.toByteArray()));
        mailSender.send(message);
    }
	
	@Async
	 public void sendManualInvoice(ManualAppointment appointment, ByteArrayOutputStream pdf) throws MessagingException
	 {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(appointment.getEmail());
	        helper.setSubject("Your Salon Invoice");
	        helper.setText("Hi " + appointment.getCustomerName() + ",\n\nPlease find your invoice attached.\n\nSalon Team");
	        helper.addAttachment("invoice.pdf", new ByteArrayResource(pdf.toByteArray()));

	        mailSender.send(message);
	 }
	 
	 @Async
	 public void sendAppointmentConfirmationEmailToSubscriptionCustomer(Appointment appointment)
	 {
		    String subject = "🎉 Your Appointment is Confirmed!";

		    String body =
		        "Hi " + appointment.getCustomerName() + ",\n\n" +
		        "We're excited to let you know that your appointment has been successfully booked!\n\n" +
		        "📅 Date & Time: " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime() + "\n" +
		        "Please arrive a few minutes early to ensure a smooth experience.\n\n" +
		        "If you have any questions or need to reschedule, feel free to contact us.\n\n" +
		        "Warm regards,\n" +
		        "Salon ✨";

		    SimpleMailMessage message = new SimpleMailMessage();
		    message.setFrom("mailforalltestings@gmail.com");
		    message.setTo(appointment.getCustomerEmail());
		    message.setSubject(subject);
		    message.setText(body);

		    mailSender.send(message);
		}
	 
	 @Async
	 public void sendAppointmentConfirmationEmailToManualAppointments(ManualAppointment appointment) 
	 {
		 	String subject = "🎉 Your Appointment is Confirmed!";

		    String body =
		        "Hi " + appointment.getCustomerName() + ",\n\n" +
		        "We're excited to let you know that your appointment has been successfully booked!\n\n" +
		        "📅 Date & Time: " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentTime() + "\n" +
		        "Please arrive a few minutes early to ensure a smooth experience.\n\n" +
		        "If you have any questions or need to reschedule, feel free to contact us.\n\n" +
		        "Warm regards,\n" +
		        "Salon ✨";

		    SimpleMailMessage message = new SimpleMailMessage();
		    message.setFrom("mailforalltestings@gmail.com");
		    message.setTo(appointment.getEmail());
		    message.setSubject(subject);
		    message.setText(body);

		    mailSender.send(message);
		}
	 
	 @Async
	 public void notifyUserBySubscription(Subscription subscription, Offer offer) {
		    List<Customer> targetCustomers = customerRepo.findAllBySubscription(subscription);

		    for (Customer user : targetCustomers) {
		        String subject = "🎉 New Offer Just For You!";
		        String body = String.format(
		            "Hi %s,\n\nA new offer \"%s\" has been added for your subscription tier (%s).\n\nDetails:\n- Points Required: ₹%d\n- Discount: %s \n- Note: %s\n- Valid Till: %s\n\nVisit your dashboard to claim it!",
		            user.getName(),
		            offer.getTitle(),
		            subscription.name(),
		            offer.getCost(),
		            offer.getDiscount(),
		            offer.getShortNote(),
		            offer.getValidUpto()
		        );

		        String to = user.getEmail();
		        String from = "mailforalltestings@gmail.com"; 

		        sendEmail(from, to, subject, body);
		    }
		}
	 
	 @Async
	 public void sendEmail(String from, String to, String subject, String body)
	 {
		 SimpleMailMessage message = new SimpleMailMessage();
		    message.setFrom(from);
		    message.setTo(to);
		    message.setSubject(subject);
		    message.setText(body);

		    mailSender.send(message);
	 }
	 
	 public void notifyNonMembershipUsers(Offer offer)
	 {
		    List<NonMemberShipCustomer> users = nonMembershipRepo.findAll();

		    for (NonMemberShipCustomer user : users)
		    {
		        String subject = "🎉 New Offer Available!";
		        String body = String.format(
		            "Hi %s,\n\nA new offer \"%s\" is now available for all users.\n\nDetails:\n- Discount: %d%%\n- Note: %s\n- Valid Till: %s\n\nVisit our site to claim it!",
		            user.getCustomerName(),
		            offer.getTitle(),
		            offer.getCost(),
		            offer.getShortNote(),
		            offer.getValidUpto()
		        );

		        sendEmail("mailforalltestings@gmail.com", user.getEmail(), subject, body);
		    }
		}
}
