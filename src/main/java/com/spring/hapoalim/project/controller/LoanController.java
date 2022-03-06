package com.spring.hapoalim.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hapoalim.project.bl.LoansBL;
import com.spring.hapoalim.project.entities.Loan;

@RestController
@RequestMapping("loans")
public class LoanController {

	@Autowired
	private LoansBL loanBL;

	@PostMapping("createAndGetLoan")
	public ResponseEntity<String> createAndGetLoan(@RequestParam int clientId, @RequestBody Loan loan) {
		try {
			Loan newLoan = this.loanBL.createAndGetLoan(clientId, loan);
			return ResponseEntity.ok(newLoan.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("getLoan")
	public ResponseEntity<String> getLoan(@RequestParam int clientId) {
		try {
			List<Loan> list = this.loanBL.getLoan(clientId);
			return ResponseEntity.ok(list.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("loanRepayment")
	public ResponseEntity<String> loanRepayment(@RequestParam int loanId, @RequestParam float amount) {
		try {
			Loan loanAfterPayment = this.loanBL.loanRepayment(loanId, amount);
			return ResponseEntity.ok(loanAfterPayment.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
