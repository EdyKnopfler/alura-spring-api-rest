package br.com.pip.forum.configuracao.seguranca;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// Usando @Profile para desabilitar as restrições de segurança em ambiente de desenvolvimento, tornando os
// testes mais produtivos

@EnableWebSecurity
@Configuration
@Profile("dev")
public class DevSecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	// Configurações de autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/**").permitAll()
			.and().csrf().disable();
	}
	
}
