package br.com.jonathan.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;

import br.com.jonathan.domain.roles.interfaces.Promocao;

@XmlRootElement
public class Lanche extends Produto {
	private static final long serialVersionUID = 1L;

	private List<Ingrediente> ingredientes = new ArrayList<>();

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	@Override
	public double getValorBruto() {
		if (CollectionUtils.isNotEmpty(ingredientes)) {
			return ingredientes.stream()
					.mapToDouble(Produto::getValorBruto)
					.sum();
		}
		return 0;
	}

	public double getValorLiquido(Promocao promocao) {
		Map<Ingrediente, Long> ingredientes = ingredientesToCount();
		double valorBruto = getValorBruto();
		double desconto = promocao.calculaDesconto(
				ingredientes, valorBruto
		);
		return valorBruto - desconto;
	}

	private Map<Ingrediente, Long> ingredientesToCount() {
		return ingredientes.stream()
				.collect(
						Collectors.groupingBy(
								Function.identity(), 
								Collectors.counting()
						)
				);
	}

}