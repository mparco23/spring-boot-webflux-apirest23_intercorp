package com.example.springboot.webflux.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.example.springboot.webflux.app.models.dao.ClienteDao;
import com.example.springboot.webflux.app.models.documents.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl  implements ClienteService{

	@Autowired
	private ClienteDao dao;
	
	@Override
	public Flux<Cliente> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Cliente> findByDniAndEmail(String dni, String email) {
		/*return dao.findAll().map(alumno -> {
			alumno.setNombre(alumno.getNombre().toUpperCase());
			return alumno;
		});*/
		return dao.obtenerPorDniAndEmail(dni,email);
	}

	//@Override
	//public Mono<Cliente> findByDniAndEmail(String dni, String email) {
	//	return dao.obtenerPorDniAndEmail1(dni,email);
	//}

	@Override
	public Mono<Cliente> saveCliente(Cliente cliente) {
		//if(dao.findById(alumno.getId().toUpperCase()) != null) {
		//return null;
		//}
		return dao.save(cliente);
	}

}
