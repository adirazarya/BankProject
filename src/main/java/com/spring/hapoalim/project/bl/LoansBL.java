package com.spring.hapoalim.project.bl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.hapoalim.project.dao.ClientDAO;
import com.spring.hapoalim.project.dao.LoanDAO;
import com.spring.hapoalim.project.entities.Account;
import com.spring.hapoalim.project.entities.Client;
import com.spring.hapoalim.project.entities.Loan;

import lombok.Data;

@Data
@Service
public class LoansBL {

	@Autowired
	private ClientDAO clientDao;

	@Autowired
	private LoanDAO loanDao;

	@Transactional
	public Loan createAndGetLoan(int clientId, Loan loan) throws Exception {
		if (clientDao.existsById(clientId)) {
			Client client = clientDao.findById(clientId).get();
			Account account = client.getAccount();

			if (account.getStatus() != Account.PAUSED) {
				loan.setPaymentPerMonth(loan.getAmount()/loan.getMonths());
				loan.setAccount(account);
				account.getLoans().add(loan);
				account.setBalance(account.getBalance() +loan.getAmount());
				loanDao.save(loan);
				return loan;
			}
			else {
				throw new Exception("Can't create loan, account is paused");
			}
		}
		else {
			throw new Exception("Client id: " + clientId + " does not exist");
		}
	}

	public List<Loan> getLoan(int clientId) throws Exception {
		if (clientDao.existsById(clientId)) {
			Client client = clientDao.findById(clientId).get();
			
			if (client.getAccount().getLoans().isEmpty()) {
				throw new Exception("No loans for client id: " + clientId);
			}
			return client.getAccount().getLoans();
		}
		else {
			throw new Exception("Client id: " + clientId + " does not exist");
		}
	}

	@Transactional
	public Loan loanRepayment(int loanId, float amount) throws Exception {
		if (loanDao.existsById(loanId)) {
			Loan loan = loanDao.findById(loanId).get();
			Account account = loan.getAccount();

			if (account.getStatus() != Account.PAUSED) {
				float balanceAfter = account.getBalance() - amount;
				float loanAfter = loan.getAmount() - amount;

				if (loanAfter < 0) {
					throw new Exception("Cannot pay more than the loan remain amount");
				}
				if (account.getStatus() == Account.LIMITED && balanceAfter < 0) {
					balanceAfter = account.getLimitation();
					loanAfter = loan.getAmount() - (account.getBalance() - account.getLimitation());
				}

				account.setBalance(balanceAfter);
				loan.setAmount(loanAfter);
				loan.setPaymentPerMonth(loan.getAmount()/loan.getMonthsRemain());

				if (loan.getAmount() == 0) {
					account.getLoans().remove(loan);
					loanDao.delete(loan);
				}
				else {
					loanDao.save(loan);
				}
				
				return loan;
			}
			else {
				throw new Exception("Cant preform action. Account is paused");
			}
		}
		else {
			throw new Exception("Loan id: " + loanId + " does not exist");
		}
	}
}
