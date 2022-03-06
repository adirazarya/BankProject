package com.spring.hapoalim.project.bl;


import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.hapoalim.project.dao.AccountDAO;
import com.spring.hapoalim.project.entities.Account;

@Service
public class ExchangesBL {
	
	@Autowired
	private AccountDAO accountDao;
	
	private Map<String, Float> exchangeMap; 
	
	public ExchangesBL() {
		exchangeMap = new HashMap<String, Float>(); 
	}

	public Map<String, Float> getExchangeMap() {
		return exchangeMap;
	}

	@Transactional
	public Account depositExchange(int accountId, float amount, String currencyType) throws Exception {
		float amountInShekels = convertCurrency(amount, currencyType);
		
		if (amountInShekels != 0) {
			Account depositAccount = deposit(accountId, amountInShekels);
			return depositAccount;
		}
		else {
			throw new Exception("No currency was found");
		}
	}
	
	@Transactional
	public Account withdrawExchange(int accountId, float amount, String currencyType) throws Exception {
		float amountInShekels = convertCurrency(amount, currencyType);
		
		if (amountInShekels != 0) {
			Account withdrawAccount = withdraw(accountId, amountInShekels);
			return withdrawAccount;
		}
		else {
			throw new Exception("No currency was found");
		}
	}
	
	private synchronized float convertCurrency(float amount, String currencyType) {	
		if (exchangeMap.containsKey(currencyType)) {
			Float value = exchangeMap.get(currencyType);
			return amount / value ;
		}
		else { 
			return 0;
		}
	}
	
	private Account deposit(int accountId, float amount) throws Exception {
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
	
	private Account withdraw(int accountId, float amount) throws Exception {
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
}
