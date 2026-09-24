package com.example.demo.components;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.demo.model.Invoice;
import com.example.demo.model.ManualInvoice;


@Component
public class FlyingSaucerComponent 
{

    @Autowired
    private TemplateEngine templateEngine;

    public ByteArrayOutputStream renderInvoice(Invoice invoice) throws Exception {
        Context context = new Context();
        context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
        context.setVariable("date", invoice.getDate());
        context.setVariable("customerName", invoice.getCustomerName());
        context.setVariable("serviceName", invoice.getServiceName());
        context.setVariable("price", invoice.getPrice());

        String html = templateEngine.process("invoice", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        return outputStream;
    }

    public ByteArrayOutputStream renderInvoice(Invoice invoice, String staffName) throws Exception {
        Context context = new Context();
        context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
        context.setVariable("date", invoice.getDate());
        context.setVariable("customerName", invoice.getCustomerName());
        context.setVariable("serviceName", invoice.getServiceName());
        context.setVariable("price", invoice.getPrice());
        context.setVariable("staffName", staffName);
        context.setVariable("originalPrice", invoice.getPrice());
        context.setVariable("discountType", invoice.getDiscountType());
        context.setVariable("manualDiscountPercent", invoice.getManualDiscountPercent());
        context.setVariable("discountAmount", invoice.getDiscountAmount());
        context.setVariable("gstAmount", invoice.getGstAmount());
        context.setVariable("finalAmount", invoice.getFinalAmount());

        String html = templateEngine.process("invoice", context);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        return outputStream;
    }

    
    public ByteArrayOutputStream renderManualInvoice(ManualInvoice invoice) throws Exception
    {
        // Sanitize key fields
        invoice.setCustomerName(escapeHtml(invoice.getCustomerName()));
        invoice.setServiceName(escapeHtml(invoice.getServiceName()));
        invoice.setInvoiceNumber(escapeHtml(invoice.getInvoiceNumber()));

        Context context = new Context();
        context.setVariable("customerName", invoice.getCustomerName());
        context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
        context.setVariable("date", invoice.getDate());
        context.setVariable("serviceName", invoice.getServiceName());

        // ✅ Add billing breakdown
        context.setVariable("originalPrice", invoice.getPrice()); // optional: original total
        context.setVariable("discountType", invoice.getDiscountType());
        context.setVariable("manualDiscountPercent", invoice.getManualDiscountPercent());
        context.setVariable("discountAmount", invoice.getDiscountAmount());
        context.setVariable("gstAmount", invoice.getGstAmount());
        context.setVariable("finalAmount", invoice.getPrice()); // assuming invoice.getPrice() is already finalAmount

        String html = templateEngine.process("manualInvoice", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        return outputStream;
    }

	private String escapeHtml(String input)
    {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
	
}