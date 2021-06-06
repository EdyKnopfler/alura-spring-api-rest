package br.com.pip.forum.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import br.com.pip.forum.modelo.Topico;

public class TopicoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	public TopicoDTO(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public static Page<TopicoDTO> converter(Page<Topico> page) {
		return page.map(TopicoDTO::new);
	}
	
	
	
}
