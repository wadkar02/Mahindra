package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.exception.LeadAlreadyExistsException;
import com.nt.model.ApiResponse;
import com.nt.model.ErrorResponse;
import com.nt.model.Lead;
import com.nt.repo.LeadRepository;

@Service
public class LeadService {

	@Autowired
	private LeadRepository leadRepository;

	public String createLead(Lead lead) {
		if (leadRepository.existsByLeadId(lead.getLeadId())) {
			throw new LeadAlreadyExistsException("Lead Already Exists in the database with the lead id");
		}

		// Perform other validations

		// Save the lead to the database
		leadRepository.save(lead);

		return "Created Leads Successfully";
	}

	public ApiResponse fetchLeadsByMobileNumber(String mobileNumber) {
		List<Lead> leads = leadRepository.findByMobileNumber(mobileNumber);

		if (leads.isEmpty()) {
			return new ApiResponse("error",
					new ErrorResponse("E10011", "No Lead found with the Mobile Number " + mobileNumber));
		}

		return new ApiResponse("success", leads);
	}

	
}
