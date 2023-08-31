package com.example.springboot.webflux.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.example.springboot.webflux.app.models.documents.Cliente;
import com.example.springboot.webflux.app.models.services.ClienteService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApirest23Application implements CommandLineRunner{
	
	@Autowired
	private ClienteService serviceClien;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApirest23Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApirest23Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		mongoTemplate.dropCollection("cliente").subscribe();
				
		Flux.just(new Cliente("MANUEL ALI", "TORRES CALDERON", "CORREO_TORRES@GMAIL.COM","44094233", formato.parse("1986-01-01")),
				new Cliente("VICTOR CARLO", "MAREATEGUI PEREZ", "CORREO_MAREATEGUI@GMAIL.COM","44094255",formato.parse("1986-02-02")),
				new Cliente("MARIA LUZ", "CARRION RONDON", "CORREO_CARRION@GMAIL.COM","44094222",formato.parse("1987-03-03")),
				new Cliente("LUCIA ANDREA", "ULLOA SANCHEZ", "CORREO_ULLOA@GMAIL.COM","44094200",formato.parse("1988-04-04")))
		//.flatMap(serviceClien::saveCliente)
		.flatMap(cliente -> {
			cliente.setFechaCreacion(new Date());
			return serviceClien.saveCliente(cliente);
			})
		.subscribe(cliente -> log.info("Cliente creado : " + cliente.getNombre() + " " + cliente.getApellido() + " " + cliente.getEmail() + " " + cliente.getDni()));		
	}

}

