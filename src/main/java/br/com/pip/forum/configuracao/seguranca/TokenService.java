package br.com.pip.forum.configuracao.seguranca;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.pip.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	// Obtendo do application.properties!
	@Value("${forum.jwt.secret}")
	private String secret; 
	
	@Value("${forum.jwt.expiration}")
	private String expiracao;

	public String gerarToken(Authentication authentication) {
		Usuario usuarioLogado = (Usuario)authentication.getPrincipal();
		Date agora = new Date();
		Date dataExpiracao = new Date(agora.getTime() + Long.parseLong(expiracao));
		return Jwts
				.builder()
					.setIssuer("API do Fórum do Píp")
					.setSubject(usuarioLogado.getId().toString())
					.setIssuedAt(agora)
					.setExpiration(dataExpiracao)
					.signWith(SignatureAlgorithm.HS256, secret)
					.compact();
	}

	public boolean isTokenValido(String token) {
		try {
			Jwts
				.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token);  // Se isto não gerou exception, o token é válido
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		try {
			Claims claims = Jwts
				.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
			return Long.parseLong(claims.getSubject());  // Foi setado em gerarToken
		}
		catch (Exception e) {
			return null;
		}
	}

}
