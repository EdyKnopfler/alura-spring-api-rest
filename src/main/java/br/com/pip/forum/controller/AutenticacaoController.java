package br.com.pip.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pip.forum.configuracao.seguranca.TokenService;
import br.com.pip.forum.dto.TokenDTO;
import br.com.pip.forum.formulario.LoginForm;

@RestController
@RequestMapping("/auth")
@Profile({"test", "prod"})  // Esta é para o ambiente de produção; precisa rodar o teste dela
public class AutenticacaoController {
	
	@Autowired  // Necessário habilitar a injeção em SecurityConfigurations.java
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm form) {
		
		try {
			Authentication dadosLogin = form.converter();
			
		    // Aqui o Spring usa as configs na SecurityConfigurations.java
			// (chama o service, etc.)
			Authentication authentication = authManager.authenticate(dadosLogin);
			
			// Geramos o token JWT
			String token = tokenService.gerarToken(authentication);
			
			return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
		}
		catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
