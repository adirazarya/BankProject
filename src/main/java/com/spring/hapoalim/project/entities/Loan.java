package com.spring.hapoalim.project.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Scope("prototype")
@Table(name = "loans")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loanId;
	private float amount;
	private int months;
	private float paymentPerMonth;
	private int monthsRemain;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="account_id")
	private Account account;

	@Override
	public String toString() {
		return "Loan [loanId=" + loanId + ", amount=" + amount + ", months=" + months + ", paymentPerMonth="
				+ paymentPerMonth + ", monthsRemain=" + monthsRemain + "]";
	}
	
	
	
}
