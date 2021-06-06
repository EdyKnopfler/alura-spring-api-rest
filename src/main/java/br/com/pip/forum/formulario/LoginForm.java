package br.com.pip.forum.formulario;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class LoginForm {

	private String email;
	private String senha;
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEmail() {
		return email;
	}
	public String getSenha() {
		return senha;
	}
	public Authentication converter() {
		return new UsernamePasswordAuthenticationToken(email, senha);
	}
	
	
	
}
