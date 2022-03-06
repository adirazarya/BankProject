package com.spring.hapoalim.project.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.hapoalim.project.entities.Account;

@Repository
public interface AccountDAO extends JpaRepository<Account, Integer>{
	
}
