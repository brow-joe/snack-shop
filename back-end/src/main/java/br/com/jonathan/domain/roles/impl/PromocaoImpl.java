package br.com.jonathan.domain.roles.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import org.apache.commons.collections.CollectionUtils;

import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.roles.interfaces.Promocao;

public class PromocaoImpl implements Promocao {
	private final List<Promocao> promocoes;

	public PromocaoImpl(List<Promocao> promocoes) {
		if (CollectionUtils.isEmpty(promocoes)) {
			promocoes = new ArrayList<>();
		}
		this.promocoes = promocoes;
	}

	@Override
	public double calculaDesconto(Map<Ingrediente, Long> ingredientes, double valorBruto) {
		ToDoubleFunction<Promocao> function = getFunction(ingredientes, valorBruto);
		return promocoes.stream().mapToDouble(function).sum();
	}

	private ToDoubleFunction<Promocao> getFunction(Map<Ingrediente, Long> ingredientes, double valorBruto) {
		return promocao -> promocao.calculaDesconto(
				ingredientes, valorBruto
		);
	}
}