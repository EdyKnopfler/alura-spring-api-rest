package br.com.pip.forum.configuracao.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.pip.forum.repositorio.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean  // habilita injeção de AuthenticationManager
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	// Controle de acesso: serviço que recebe as solicitações de login.
	// A autenticação devolve o token que deverá ser enviado no header das próximas requisições.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(autenticacaoService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	// Configurações de autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/topicos").permitAll()
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.anyRequest().authenticated()
				
			// Usanmos JWT (ver pom.xml) e não Session para não ferir o modelo REST.
			// Sem isto, perdemos não só o formulário mas o controller de login,
			// por isso criamos o AutenticacaoController.
			//.and().formLogin();
			
			// Desabilitando para API (com o JWT já estamos livres)
			.and().csrf().disable()
			
			// Não criar Session!
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
			// filtro de validação do token antes 
			.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), 
					UsernamePasswordAuthenticationFilter.class);
	}
	
	// Recursos estáticos (hmmmmmm, interessante...)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
}
