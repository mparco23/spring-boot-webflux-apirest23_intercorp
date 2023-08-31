package com.example.springboot.webflux.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.example.springboot.webflux.app.models.documents.Cliente;
import com.example.springboot.webflux.app.models.services.ClienteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService service;
	
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Cliente>>> lista(){
		return Mono.just(
				ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findAll())
				);
	}
	
	
	@GetMapping("/{dni}/{email}")
	public Mono<ResponseEntity<Cliente>> ver(@PathVariable String dni, @PathVariable String email){
		return service.findByDniAndEmail(dni,email).map(p -> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
		
		//return Mono.just(
		//		ResponseEntity.ok()
		//		.contentType(MediaType.APPLICATION_JSON_UTF8)
		//		.body(service.findByDniAndEmail(dni,email))
		//		);
	}	
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Cliente> monoCliente){
		
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return monoCliente.flatMap(cliente -> {
			if(cliente.getFechaCreacion()==null) {
				cliente.setFechaCreacion(new Date());
			}
			cliente.setNombre(cliente.getNombre().toUpperCase());
			cliente.setApellido(cliente.getApellido().toUpperCase());	
			cliente.setEmail(cliente.getEmail().toUpperCase());	
			
			var resultado = service.saveCliente(cliente).map(a-> {			
				
				respuesta.put("cliente", a);
				respuesta.put("mensaje", "Cliente creado con éxito");
				respuesta.put("timestamp", new Date());
				return ResponseEntity
					.created(URI.create("/api/Clientes/".concat(a.getId())))
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(respuesta);
				});	
						
			return resultado;	
			
		})
		
		.onErrorResume(t -> {
		     return Mono.just(t).cast(WebExchangeBindException.class)		
					.flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(fieldError -> "El campo "+fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList()
					.flatMap(list -> {
						respuesta.put("errors", list);
						respuesta.put("timestamp", new Date());
						respuesta.put("status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(respuesta));
					});				
		});
					

	}

}
