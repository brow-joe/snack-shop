package br.com.jonathan.port.adapter.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jonathan.application.interfaces.IAppService;
import br.com.jonathan.application.utils.MathUtil;
import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.model.Lanche;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	protected final Logger logger = LogManager.getLogger(ProdutoController.class);

	@Autowired
	private List<Lanche> lanches;
	@Autowired
	private List<Ingrediente> ingredientes;
	@Autowired
	private IAppService service;

	@GetMapping("/lanches")
	public ResponseEntity<List<Lanche>> getLanches(HttpServletRequest request) {
		logger.info("host: " + request.getRemoteAddr());
		return ResponseEntity.ok(lanches);
	}

	@GetMapping("/ingredientes")
	public ResponseEntity<List<Ingrediente>> getIngredientes(HttpServletRequest request) {
		logger.info("host: " + request.getRemoteAddr());
		return ResponseEntity.ok(ingredientes);
	}

	@PostMapping(value = "/lanche/valorLiquido")
	public ResponseEntity<Double> getValorLiquido(HttpServletRequest request, @RequestBody Lanche lanche) {
		if (Objects.nonNull(lanche)) {
			double valorLiquido = service.getValorLiquido(lanche);
			return ResponseEntity.ok(MathUtil.round(valorLiquido, 2));
		} else {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

}