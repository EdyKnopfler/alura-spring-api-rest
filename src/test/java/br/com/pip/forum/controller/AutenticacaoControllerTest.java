package br.com.pip.forum.controller;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest

// AutenticacaoController e SecurityConfigurations estão definidos apenas para "test" e agora "prod".
@ActiveProfiles("test")

@AutoConfigureMockMvc
public class AutenticacaoControllerTest {
	
	// Dispara requisições
	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveriaDevolver400ComDadosDeAutenticacaoIncorretos() throws Exception {
		URI uri = new URI("/auth");
		String json = "{\"email\" : \"invalido@email.com\", \"senha\" : \"123456\"}";
		mockMvc
			.perform(
				MockMvcRequestBuilders
					.post(uri)
					.content(json)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(
				MockMvcResultMatchers
					.status().is(400));
	}

}
