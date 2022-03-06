package com.spring.hapoalim.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hapoalim.project.bl.ExchangesBL;
import com.spring.hapoalim.project.entities.Account;

@RestController
@RequestMapping("exchange")
public class ExchangeController {
	
	@Autowired
	private ExchangesBL exchangeBL;
	
	@PutMapping("depositExchange")
	public ResponseEntity<String> depositExchange(@RequestParam int accountId, @RequestParam long amount, @RequestParam String currencyType) {
		try {
			Account account = this.exchangeBL.depositExchange(accountId, amount, currencyType);
			return ResponseEntity.ok(account.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("withdrawExchange")
	public ResponseEntity<String> withdrawExchange(@RequestParam int accountId, @RequestParam long amount, @RequestParam String currencyType) {
		try {
			Account account = this.exchangeBL.withdrawExchange(accountId, amount, currencyType);
			return ResponseEntity.ok(account.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
