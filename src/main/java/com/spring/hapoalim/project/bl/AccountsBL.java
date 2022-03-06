package com.spring.hapoalim.project.bl;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.hapoalim.project.dao.AccountDAO;
import com.spring.hapoalim.project.entities.Account;

import lombok.Data;

@Data
@Service
public class AccountsBL {

	@Autowired
	private AccountDAO accountDao;

	@Transactional
	public Account addAccount(Account account) throws Exception {
		if (account != null) {
			accountDao.save(account);
			return account;
		}
		else {
			throw new Exception("Cannot create account");
		}
	}

	@Transactional
	public Account limitAccount(int accountId, int limit) throws Exception {
		if (accountDao.existsById(accountId)) {
			Account toUpdate = accountDao.findById(accountId).get();
			toUpdate.setStatus(Account.LIMITED);
			toUpdate.setLimitation(limit);
			accountDao.save(toUpdate);
			return toUpdate;
		}
		else {
			throw new Exception("Account id: " + accountId + " does not exist");
		}
	}

	@Transactional
	public Account pauseAccount(int accountId, boolean toPause) throws Exception {
		if (accountDao.existsById(accountId)) {
			Account toPauseAccount = accountDao.findById(accountId).get();
			toPauseAccount.setStatus(toPause ? Account.PAUSED : Account.REGULAR);
			accountDao.save(toPauseAccount);
			return toPauseAccount;
		}
		else {
			throw new Exception("Account id: " + accountId + " does not exist");
		}
	}

	@Transactional
	public Account deposit(int accountId, float amount) throws Exception {
		if (accountDao.existsById(accountId)) {
			Account depositAccount = accountDao.findById(accountId).get();
			
			if (depositAccount.getStatus() == Account.PAUSED) {
				throw new Exception("Cannot deposit. Account id: " + accountId + " is paused");
			}
			else {
				depositAccount.setBalance(depositAccount.getBalance() + amount);
				accountDao.save(depositAccount);
				return depositAccount;
			}
		}
		else {
			throw new Exception("Account id: " + accountId + " does not exist");
		}
	}

	@Transactional
	public Account withdraw(int accountId, float amount) throws Exception {
		if (accountDao.existsById(accountId)) {
			Account withdrawAccount = accountDao.findById(accountId).get();

			if (withdrawAccount.getStatus() == Account.PAUSED) {
				throw new Exception("Account id: " + accountId + " is paused");
			}
			else if (withdrawAccount.getBalance() == withdrawAccount.getLimitation()) {
				throw new Exception("Cannot withraw. Account balance has reached its limitation");
			}
			else {
				float afterWithdraw = withdrawAccount.getBalance() - amount;

				if (withdrawAccount.getStatus() == Account.LIMITED && afterWithdraw < withdrawAccount.getLimitation()) {
					afterWithdraw = withdrawAccount.getLimitation();
				}

				withdrawAccount.setBalance(afterWithdraw);
				accountDao.save(withdrawAccount);
				return withdrawAccount;	
			}	
		}
		else {
			throw new Exception("Account id: " + accountId + " does not exist");
		}
	}

	public float getBalance(int accountId) throws Exception {
		if (accountDao.existsById(accountId)) {
			Account account = accountDao.findById(accountId).get();
			return account.getBalance();
		}
		else {
			throw new Exception("Account id: " + accountId + " does not exist");
		}
	}

	@Transactional
	public List<Account> transfer(int accountFromId, int accountToId, float amount) throws Exception {
		if (accountDao.existsById(accountFromId) && accountDao.existsById(accountToId)) {
			Account accountFrom = accountDao.findById(accountFromId).get();
			Account accountTo = accountDao.findById(accountToId).get();

			if (accountFrom.getStatus() != Account.PAUSED && accountTo.getStatus() != Account.PAUSED) {
				float balanceAfterSub = accountFrom.getBalance() - amount;
				float balanceAfterAdd = accountTo.getBalance() + amount;

				if (accountFrom.getStatus() == Account.LIMITED && balanceAfterSub < accountFrom.getLimitation()) {
					if (accountFrom.getBalance() == accountFrom.getLimitation()) {
						throw new Exception("Account id: " + accountFromId + " has reached its limitation");
					}
					else {
						balanceAfterSub = accountFrom.getLimitation();
						balanceAfterAdd = accountTo.getBalance() + accountFrom.getBalance() - accountFrom.getLimitation();
					}	
				}

				accountFrom.setBalance(balanceAfterSub);
				accountTo.setBalance(balanceAfterAdd);

				ArrayList<Account> accountAffected = new ArrayList<Account>();
				accountAffected.add(accountFrom);
				accountAffected.add(accountTo);
				accountDao.save(accountFrom);
				accountDao.save(accountTo);
				return accountAffected;
			}
			else {
				throw new Exception("One or both accounts are paused");
			}
		}
		else {
			throw new Exception("Account id: " + accountFromId + " or account id: " + accountToId + " do not exist");
		}
	}
}
