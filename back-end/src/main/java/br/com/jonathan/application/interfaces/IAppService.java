package br.com.jonathan.application.interfaces;

import br.com.jonathan.domain.model.Lanche;

@FunctionalInterface
public interface IAppService {

	public double getValorLiquido(Lanche lanche);

}