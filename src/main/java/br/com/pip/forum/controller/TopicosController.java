package br.com.pip.forum.controller;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
	@Cacheable("listaDeTopicos")  // Requer o @EnableCaching na classe da aplicação
	
	// Existe o provedor de cache que usa o Redis :)
	// O cache foi usado aqui de forma didática, mas o ideal é usar em tabelas que não sofrem atualizações
	// frequentes. Se tiver que ficar invalidando a todo momento, pode até piorar o desempenho.
	
	// Page devolve dados de paginação no JSON de resposta
	// Por default o Spring recebe os parâmetros do GET
	// O Pageable é devido ao @EnableSpringDataWebSupport na classe da aplicação
	// (page=XXX&size=YYY&sort=ZZZ,asc&sort=KKK,desc)
	public Page<TopicoDTO> lista(
			String nomeCurso, 
			@PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC) Pageable paginacao) {
		Page<Topico> page;
		
		if (nomeCurso == null) {
			page = topicoRepository.findAll(paginacao);
		}
		else {
			page = topicoRepository.findByCursoNome(nomeCurso, paginacao);
		}
		return TopicoDTO.converter(page);
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)  // invalida o cache
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
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
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
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optTopico = topicoRepository.findById(id);
		
		if (optTopico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	

}
