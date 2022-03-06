package com.spring.hapoalim.project.bl;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.hapoalim.project.dao.AccountDAO;
import com.spring.hapoalim.project.dao.ClientDAO;
import com.spring.hapoalim.project.entities.Account;
import com.spring.hapoalim.project.entities.Client;

import lombok.Data;

@Data
@Service
public class ClientsBL {

	@Autowired
	private ClientDAO clientDao;

	@Autowired
	private AccountDAO accountDao;

	@Transactional
	public Client addClient(Client client, int accountId) throws Exception {
		if (!clientDao.existsById(client.getId())) {
			Account existingAccount = accountDao.findById(accountId).get();

			if (existingAccount != null) {
				existingAccount.getClients().add(client);
				client.setAccount(existingAccount);
				clientDao.save(client);
				return client;
			}
			else {
				throw new Exception("Account id: " + accountId + " do not exist");
			}
		}
		else {
			throw new Exception("Client id: " + client.getId() + " already exists");
		}
	}

	@Transactional
	public Client removeClient(int clientId) throws Exception {	
		if (clientDao.existsById(clientId)) {
			Client client = clientDao.findById(clientId).get();
			client.getAccount().getClients().remove(client);
			clientDao.deleteById(clientId);
			return client;
		}
		else {
			throw new Exception("Client id: " + clientId + " does not exist");
		}
	}

	@Transactional
	public Client updateClient(int clientId, Client update, int accountIdUpdated) throws Exception {
		if (clientDao.existsById(clientId)) {
			Client clientToUpdate = clientDao.findById(clientId).get();
			clientToUpdate.setFirstName(update.getFirstName());
			clientToUpdate.setLastName(update.getLastName());
			clientToUpdate.setEmail(update.getEmail());
			
			if (accountDao.existsById(accountIdUpdated)) {
				Account account = accountDao.findById(accountIdUpdated).get();

				if (accountIdUpdated != clientToUpdate.getAccount().getAccountId()) {
					clientToUpdate.getAccount().getClients().remove(clientToUpdate);
					account.getClients().add(clientToUpdate);
					clientToUpdate.setAccount(account);
				}	
				else {
					throw new Exception("Trying to update the same account");
				}

				clientDao.save(clientToUpdate);
				return clientToUpdate;
			}
			else {
				throw new Exception("Account id: " + accountIdUpdated + " does not exist");
			}
		}
		else {
			throw new Exception("Client id: " + clientId + " does not exist");
		}
	}
	
	public List<Client> getAll() {
		return clientDao.findAll();
	}
}
