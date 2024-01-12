package com.nt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nt.controller.LeadController;
import com.nt.exception.LeadAlreadyExistsException;
import com.nt.model.ApiResponse;
import com.nt.model.ErrorResponse;
import com.nt.model.Lead;
import com.nt.service.LeadService;

public class LeadControllerTest {

	@InjectMocks
	private LeadController leadController;

	@Mock
	private LeadService leadService;

	@Test
	public void testCreateLead_Success() {
		Lead lead = new Lead();
		Mockito.when(leadService.createLead(Mockito.any(Lead.class))).thenReturn("Created Leads Successfully");

		ResponseEntity<?> response = leadController.createLead(lead);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Created Leads Successfully", response.getBody());
	}

	@Test
	public void testCreateLead_Conflict() {
		Lead lead = new Lead();
		Mockito.when(leadService.createLead(Mockito.any(Lead.class)))
				.thenThrow(new LeadAlreadyExistsException("Lead Already Exists in the database with the lead id"));

		ResponseEntity<?> response = leadController.createLead(lead);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("E10010", ((ErrorResponse) response.getBody()).getCode());
		assertEquals("Lead Already Exists in the database with the lead id",
				((ErrorResponse) response.getBody()).getMessages());
	}

	@Test
	public void testFetchLeadsByMobileNumber_Success() {
		String mobileNumber = "8877887788";
		Lead lead1 = new Lead();
		Lead lead2 = new Lead();
		Mockito.when(leadService.fetchLeadsByMobileNumber(mobileNumber))
				.thenReturn(new ApiResponse("success", Arrays.asList(lead1, lead2)));

		ResponseEntity<?> response = leadController.fetchLeadsByMobileNumber(mobileNumber);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", ((ApiResponse) response.getBody()).getStatus());
		assertEquals(2, ((List<?>) ((ApiResponse) response.getBody()).getData()).size());
	}

	@Test
	public void testFetchLeadsByMobileNumber_NoLeadFound() {
		String mobileNumber = "8877887788";
		Mockito.when(leadService.fetchLeadsByMobileNumber(mobileNumber)).thenReturn(new ApiResponse("error",
				new ErrorResponse("E10011", "No Lead found with the Mobile Number " + mobileNumber)));

		ResponseEntity<?> response = leadController.fetchLeadsByMobileNumber(mobileNumber);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("error", ((ApiResponse) response.getBody()).getStatus());
		assertEquals("E10011", ((ErrorResponse) ((ApiResponse) response.getBody()).getData()).getCode());
		assertEquals("No Lead found with the Mobile Number " + mobileNumber,
				((ErrorResponse) ((ApiResponse) response.getBody()).getData()).getMessages());
	}

	

}
