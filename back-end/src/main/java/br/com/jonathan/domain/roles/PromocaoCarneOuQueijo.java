package br.com.jonathan.domain.roles;

import java.util.Map;
import java.util.function.Predicate;

import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.roles.interfaces.Promocao;

public class PromocaoCarneOuQueijo implements Promocao {

	@Override
	public double calculaDesconto(Map<Ingrediente, Long> ingredientes, double valorBruto) {
		double descontoCarne = getDescontoByNome(ingredientes, "Hamburguer de carne");
		double descontoQueijo = getDescontoByNome(ingredientes, "Queijo");
		return descontoCarne + descontoQueijo;
	}

	private double getDescontoByNome(Map<Ingrediente, Long> ingredientes, String nome) {
		Long total = findIngredienteByNome(ingredientes, nome);
		int quantidade = total.intValue() / 3;
		double desconto = 0;

		if (quantidade > 0) {
			Predicate<Ingrediente> predicate = ingrediente -> nome.equals(ingrediente.getNome());
			
			Ingrediente ingrediente = ingredientes.keySet().stream()
					.filter(predicate)
					.findFirst().get();
			
			desconto = ((double) quantidade) * ingrediente.getValorBruto();
		}
		return desconto;
	}

}