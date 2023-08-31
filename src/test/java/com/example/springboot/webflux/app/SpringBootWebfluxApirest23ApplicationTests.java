package com.example.springboot.webflux.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.springboot.webflux.app.models.documents.Cliente;
import com.example.springboot.webflux.app.models.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootWebfluxApirest23ApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ClienteService service;
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Test
	public void listarTest() {
		
		client.get()
		.uri(url) // "/api/v2/clientes"  url
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBodyList(Cliente.class)
		.consumeWith(response -> {
			List<Cliente> cliente = response.getResponseBody();
			cliente.forEach(p -> {
				System.out.println(p.getNombre());
			});
			
			Assertions.assertThat(cliente.size()>0).isTrue();
		});
		//.hasSize(9);
	}
	
	@Test
	public void verTest() {
		
		Cliente cliente = service.findByDniAndEmail("44094222","CORREO_CARRION@GMAIL.COM").block();
		client.get()
		//.uri(url + "/{dni}", Collections.singletonMap("dni", cliente.getDni()) + "/{email}", Collections.singletonMap("email", cliente.getEmail()))
		.uri(url + "/" + cliente.getDni().toString() + "/" + cliente.getEmail().toString())
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody(Cliente.class)
		.consumeWith(response -> {
			Cliente a = response.getResponseBody();
			Assertions.assertThat(a.getDni()).isNotEmpty();
			Assertions.assertThat(a.getDni().length()>0).isTrue();
			Assertions.assertThat(a.getDni()).isEqualTo("44094222");
		});
		//.expectBody()
		//.jsonPath("$.id").isNotEmpty()
		//.jsonPath("$.id").isEqualTo("C002");
	}
	
	@Test
	public void crearTest() throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		Cliente cliente = new Cliente("ROMINA ANDREA", "VILLANUEVA NALVARTE", "CORREO_VILLANUEVA@GMAIL.COM","44090101",formato.parse("1987-06-08"));
		
		client.post().uri(url)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(cliente), Cliente.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.cliente.id").isNotEmpty()
		.jsonPath("$.cliente.nombre").isEqualTo("ROMINA ANDREA")
		.jsonPath("$.cliente.apellido").isEqualTo("VILLANUEVA NALVARTE");
	}
	
	@Test
	public void crear2Test() throws ParseException {		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		Cliente cliente = new Cliente("LUCIANA ANGELICA", "BETANCOURT FLORES", "CORREO_BETANCOURT@GMAIL.COM","44090102",formato.parse("1987-06-08"));
		
		client.post().uri(url)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(cliente), Cliente.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {})
		.consumeWith(response -> {
			Object o = response.getResponseBody().get("cliente");
			Cliente a = new ObjectMapper().convertValue(o, Cliente.class);
			Assertions.assertThat(a.getId()).isNotEmpty();
			Assertions.assertThat(a.getNombre()).isEqualTo("LUCIANA ANGELICA");
			Assertions.assertThat(a.getApellido()).isEqualTo("BETANCOURT FLORES");
		});
	}
   
}
