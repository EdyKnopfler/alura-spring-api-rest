package br.com.pip.forum.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.pip.forum.dto.AtualizacaoTopicoForm;
import br.com.pip.forum.dto.DetalhesTopicoDTO;
import br.com.pip.forum.dto.TopicoDTO;
import br.com.pip.forum.formulario.TopicoForm;
import br.com.pip.forum.modelo.Topico;
import br.com.pip.forum.repositorio.CursoRepository;
import br.com.pip.forum.repositorio.TopicoRepository;

@RestController // não precisa mais ficar pondo @ResponseBody nos métodos
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso) {  // nomeCurso já recebe o parâmetro do GET
		List<Topico> topicos;
		if (nomeCurso == null) {
			topicos = topicoRepository.findAll();
		}
		else {
			topicos = topicoRepository.findByCursoNome(nomeCurso);
		}
		return TopicoDTO.converter(topicos);
	}
	
	@PostMapping
	@Transactional
	// ResponseEntity: devolve o objeto, porém controlando a resposta HTTP
	// @RequestBody: avisa que o parâmetro vem no corpo
	// @Valid: chama o Bean Validation
	// UriComponentsBuilder: para montar a URL do recurso criado
	public ResponseEntity<TopicoDTO> cadastrar(
			@RequestBody @Valid TopicoForm form,
			UriComponentsBuilder uriBuilder) {
		
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		UriComponents uri = uriBuilder.path("/topicos/" + topico.getId()).buildAndExpand();
		return ResponseEntity.created(uri.toUri()).body(new TopicoDTO(topico));  // 201 CREATED
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDTO> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDTO(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	// PUT = sobrescrever todo o recurso
	// PATCH = atualizar apenas alguns campos
	// No geral se usa o PUT, não fazendo a distinção
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> atualizar(
			@PathVariable Long id, 
			@RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optTopico = topicoRepository.findById(id);
		
		if (optTopico.isPresent()) {
			Topico topico = optTopico.get();
			topico.setTitulo(form.getTitulo());
			topico.setMensagem(form.getMensagem());
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		
		return ResponseEntity.notFound().build();
		
		// No final do método o UPDATE será disparado e a transação será commitada
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optTopico = topicoRepository.findById(id);
		
		if (optTopico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	

}
