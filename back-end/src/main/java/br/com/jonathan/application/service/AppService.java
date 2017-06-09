package br.com.jonathan.application.service;

import br.com.jonathan.application.interfaces.IAppService;
import br.com.jonathan.domain.model.Lanche;
import br.com.jonathan.domain.roles.interfaces.Promocao;

public class AppService implements IAppService {
	private final Promocao promocao;

	public AppService(Promocao promocao) {
		this.promocao = promocao;
	}

	@Override
	public double getValorLiquido(Lanche lanche) {
		return lanche.getValorLiquido(promocao);
	}

}