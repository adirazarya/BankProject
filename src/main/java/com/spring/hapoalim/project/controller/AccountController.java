package com.spring.hapoalim.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hapoalim.project.bl.AccountsBL;
import com.spring.hapoalim.project.entities.Account;

@RestController
@RequestMapping("accounts")
public class AccountController {
	
	@Autowired
	private AccountsBL accountsBL;
	
	@PostMapping("addAccount")
	public ResponseEntity<String> addAccount(@RequestBody Account account) {
		try {
			Account newAccount = this.accountsBL.addAccount(account);
			return ResponseEntity.ok(newAccount.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("limitAccount")
	public ResponseEntity<String> limitAccount(@RequestParam int accountId, @RequestParam int limit) {
		try {
			Account limitAccount = this.accountsBL.limitAccount(accountId, limit);
			return ResponseEntity.ok(limitAccount.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("pauseAccount")
	public ResponseEntity<String> pauseAccount(@RequestParam int accountId, @RequestParam boolean toPause) {
		try {
			Account pauseAccount = this.accountsBL.pauseAccount(accountId, toPause);
			return ResponseEntity.ok(pauseAccount.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("deposit")
	public ResponseEntity<String> deposit(@RequestParam int accountId, @RequestParam float amount) {
		try {
			Account depositAccount = this.accountsBL.deposit(accountId, amount);
			return ResponseEntity.ok(depositAccount.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("withdraw")
	public ResponseEntity<String> withdraw(@RequestParam int accountId, @RequestParam float amount) {
		try {
			Account withdrawAccount = this.accountsBL.withdraw(accountId, amount);
			return ResponseEntity.ok(withdrawAccount.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("checkBalance/{accountId}")
	public ResponseEntity<String> checkBalance(@PathVariable int accountId) {
		try {
			float accountBalance = this.accountsBL.getBalance(accountId);
			return ResponseEntity.ok(accountBalance +"");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("transfer")
	public ResponseEntity<String> transfer(@RequestParam int accountFrom, @RequestParam int accountTo, @RequestParam float amount) {
		try {
			List<Account> accountsAffected= this.accountsBL.transfer(accountFrom, accountTo, amount);
			return ResponseEntity.ok(accountsAffected.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
