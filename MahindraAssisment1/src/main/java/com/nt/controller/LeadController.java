package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.exception.LeadAlreadyExistsException;
import com.nt.model.ApiResponse;
import com.nt.model.ErrorResponse;
import com.nt.model.Lead;
import com.nt.service.LeadService;

@RestController
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping("/leads")
    public ResponseEntity<?> createLead(@RequestBody Lead lead) {
        try {
            leadService.createLead(lead);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created Leads Successfully");
        } catch (LeadAlreadyExistsException e) {
            ErrorResponse errorResponse = new ErrorResponse("E10010", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

  
      
        @GetMapping("/leads")
        public ResponseEntity<?> fetchLeadsByMobileNumber(@RequestParam String mobileNumber) {
            try {
                ApiResponse response = leadService.fetchLeadsByMobileNumber(mobileNumber);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (Exception e) {
                // Handle other exceptions
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

       
    


    
    
}
