package com.example.springboot.webflux.app.models.services;

import com.example.springboot.webflux.app.models.documents.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteService {
	
	public Flux<Cliente> findAll();
	
	//public Flux<Cliente> findByDniAndEmail(String dni, String email); 
	
	public Mono<Cliente> findByDniAndEmail(String dni, String email); 
	
	public Mono<Cliente> saveCliente(Cliente cliente);	

}
