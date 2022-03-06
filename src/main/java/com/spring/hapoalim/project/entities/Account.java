package com.spring.hapoalim.project.entities;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Scope("prototype")
@Data
@Table(name = "accounts")
public class Account {
	
	public static final int REGULAR = 0;
	public static final int LIMITED = 1;
	public static final int PAUSED = 2;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int accountId;
	private String password;
	private float balance;
	private int limitation;
	private int status;
	
	@JsonIgnore
	@OneToMany(mappedBy= "account")
	private List<Client> clients;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy= "account")
	private List<Loan> loans;

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", password=" + password + ", balance=" + balance + ", limitation="
				+ limitation + ", status=" + status + "]";
	}

}
