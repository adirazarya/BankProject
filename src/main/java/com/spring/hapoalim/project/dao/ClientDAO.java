package com.spring.hapoalim.project.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.hapoalim.project.entities.Client;

@Repository
public interface ClientDAO extends JpaRepository<Client, Integer>{
	
	@Modifying
	@Query(value= "update clients set clients.first_name = ?, clients.last_name = ?, clients.account_id = ? "
			+ "where clients.id = ?", nativeQuery = true)
	public void updateClient(String firstName, String lastName, int accountId, int clientId);
}
