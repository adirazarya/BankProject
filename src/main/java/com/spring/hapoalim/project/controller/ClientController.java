package com.spring.hapoalim.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hapoalim.project.bl.ClientsBL;
import com.spring.hapoalim.project.entities.Client;

@RestController
@RequestMapping("clients")
public class ClientController {

	@Autowired
	private ClientsBL clientsBL;

	@PostMapping("addClient")
	public ResponseEntity<String> addClient(@RequestBody Client client, @RequestParam int accountId) {
		try {
			Client newClient = this.clientsBL.addClient(client, accountId);
			return ResponseEntity.ok(newClient.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("removeClient")
	public ResponseEntity<String> removeClient(@RequestParam int clientId) {
		try {
			Client removeClient = this.clientsBL.removeClient(clientId);
			return ResponseEntity.ok(removeClient.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("updateClient")
	public ResponseEntity<String> updateClient(@RequestParam int clientIdToUpdate, @RequestBody Client update, @RequestParam int accountIdUpdated) {
		try {
			Client updateClient = this.clientsBL.updateClient(clientIdToUpdate, update, accountIdUpdated);
			return ResponseEntity.ok(updateClient.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


}
