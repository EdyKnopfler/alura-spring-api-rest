package br.com.pip.forum.configuracao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Interceptando erros de Bean Validation
// O Spring emite um 400 Bad Request, mas com um JSON enorme e confuso no corpo.
// Esta classe tem o objetivo de personalizar isso.

@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	
	@Autowired
	private MessageSource messageSource;
	
	
	// Por padrão, com tratamento ele devolveria 200 OK. Queremos manter o 400.
	// Questionamento: em um controller, ao invés de devolver um ResponseEntity, posso devolver o objeto
	// e usar esta anotação?
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	
	// ClasseDaExcecaoDoBeanValidationESeuNomeCurtinho.class
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroValidacaoDTO> handle(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		List<ErroValidacaoDTO> erros = fieldErrors
				.stream()
				.map(fe -> toDTO(fe))
				.collect(Collectors.toList());
		
		return erros;
	}
	
	private ErroValidacaoDTO toDTO(FieldError fe) {
		// LocaleContextHolder: idioma padrão do sistema ou parâmetro Accept-Language da requisição
		// (trata automaticamente)
		String mensagem = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
		return new ErroValidacaoDTO(fe.getField(), mensagem);
	}

}
