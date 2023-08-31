package com.example.springboot.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.springboot.webflux.app.models.documents.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteDao  extends ReactiveMongoRepository<Cliente, String> {

	public Flux<Cliente> findByDniAndEmail(String dni, String email);
	
	//@Query("{ 'dni': ?0 , email: ?1 }")
	//public Flux<Cliente> obtenerPorDniAndEmail(String dni, String email);
	
	
    //public Mono<Cliente> findByDniAndEmail(String dni, String email);
	
	@Query("{ 'dni': ?0 , email: ?1 }")
	public Mono<Cliente> obtenerPorDniAndEmail(String dni, String email);
	
}

