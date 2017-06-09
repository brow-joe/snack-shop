package br.com.jonathan.domain.roles.interfaces;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;

import br.com.jonathan.domain.model.Ingrediente;

@FunctionalInterface
public interface Promocao {

	@Async
	public double calculaDesconto(Map<Ingrediente, Long> ingredientes, double valorBruto);

	default Long findIngredienteByNome(Map<Ingrediente, Long> map, String finder) {
		return map.getOrDefault(new Ingrediente(finder), 0L);
	}

}