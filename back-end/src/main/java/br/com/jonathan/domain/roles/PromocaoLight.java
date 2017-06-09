package br.com.jonathan.domain.roles;

import java.util.Map;

import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.roles.interfaces.Promocao;

public class PromocaoLight implements Promocao {

	@Override
	public double calculaDesconto(Map<Ingrediente, Long> ingredientes, double valorBruto) {
		Long quantidadeAlface = findIngredienteByNome(ingredientes, "Alface");
		Long quantidadeBacon = findIngredienteByNome(ingredientes, "Bacon");

		double desconto = 0;
		if (quantidadeAlface > 0 && quantidadeBacon == 0) {
			desconto = valorBruto * 0.1;
		}
		return desconto;
	}

}