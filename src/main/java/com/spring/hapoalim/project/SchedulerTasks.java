package com.spring.hapoalim.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.hapoalim.project.bl.AccountsBL;
import com.spring.hapoalim.project.bl.ClientsBL;
import com.spring.hapoalim.project.bl.ExchangesBL;
import com.spring.hapoalim.project.bl.LoansBL;
import com.spring.hapoalim.project.entities.Account;
import com.spring.hapoalim.project.entities.Client;
import com.spring.hapoalim.project.entities.Loan;

@Configuration
@EnableScheduling
@Async
public class SchedulerTasks {

	@Autowired
	private ExchangesBL exchangeBL;

	@Autowired
	private LoansBL loanBL;

	@Autowired
	private AccountsBL accountBL;
	
	@Autowired
	private ClientsBL clientBL;

	@Autowired
	private JavaMailSender emailSender;

//	@Scheduled(cron= "0 0 10 * * ?") // every day on 10 am
	@Scheduled(fixedRate = 10000)
	public void updateMap() {
		try {
			// Setting URL
			String url_str = "https://v6.exchangerate-api.com/v6/ENTER YOUR TOKEN/latest/ILS";

			// Making Request
			URL url = new URL(url_str);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();

			request.connect();
			// Convert to JSON
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			JsonObject jsonobj = root.getAsJsonObject();

			JsonObject rates = jsonobj.get("conversion_rates").getAsJsonObject();
			System.out.println(rates);

			for (String key : rates.keySet()) {
				synchronized (exchangeBL.getExchangeMap()) {
					exchangeBL.getExchangeMap().put(key, rates.get(key).getAsFloat());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	@Scheduled(cron= "0 15 9 24 * ?") // every month
	public void monthlyPayments() {
		List<Account> accountList = accountBL.getAccountDao().findAll();

		// work with stream
		for (Account account : accountList) {
			for (Loan loan : account.getLoans()) {
				if (loan.getMonthsRemain() > 0) {
					loan.setAmount(loan.getAmount() - loan.getPaymentPerMonth());
					loan.setMonthsRemain(loan.getMonthsRemain() - 1);
					account.setBalance(account.getBalance() - loan.getPaymentPerMonth());
					loanBL.getLoanDao().save(loan);
				}
			}
			for (int i = 0; i < account.getLoans().size(); i++) {
				Loan loan = account.getLoans().get(i);
				
				if (loan != null && loan.getMonthsRemain() == 0) {
					account.getLoans().remove(loan);
					loanBL.getLoanDao().delete(loan);

					if (account.getLoans().isEmpty()) {
						break;
					}
				}
			}
		}
	}

	@Scheduled(fixedRate = 10000)
	//@Scheduled(cron= "0 0 10 * * ?") // every day on 10 am
	public void sendEmails() {
		List<Client> clientList = clientBL.getAll();
		
		for (Client client : clientList) {
			if (client.getAccount().getBalance() < 0) {
				SimpleMailMessage message = new SimpleMailMessage(); 
				message.setFrom("adirazarya@gmail.com");
				message.setTo(client.getEmail()); 
				message.setSubject("no-reply"); 
				message.setText("Hello " + client.getFirstName() + " " +client.getLastName() + ",\n"
						+ "Warning: Your account is in exception");
				emailSender.send(message);
			}
		}
	}
}
