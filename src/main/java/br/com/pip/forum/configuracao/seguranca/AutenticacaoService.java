package br.com.pip.forum.configuracao.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pip.forum.modelo.Usuario;
import br.com.pip.forum.repositorio.UsuarioRepository;

// @Service: classe gerenciada pelo Spring (injetável na SecurityConfigurations.java)
// UserDetailsService: usada para obter o UserDetails na autenticação

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
		
		if (usuario.isPresent()) {
			return usuario.get();
		}
		
		throw new UsernameNotFoundException("Dados de login inválidos!");
	}
	
	

}
