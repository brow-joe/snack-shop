package br.com.jonathan.domain.model;

import java.io.Serializable;

public abstract class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nome = "";

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public abstract double getValorBruto();

}