package br.com.jonathan.domain.model;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

@XmlRootElement
public class Ingrediente extends Produto {
	private static final long serialVersionUID = 1L;

	private double valorBruto = 0;

	public Ingrediente() {
		super();
	}

	public Ingrediente(String nome) {
		super();
		setNome(nome);
	}

	public void setValorBruto(double valorBruto) {
		this.valorBruto = valorBruto;
	}

	@Override
	public double getValorBruto() {
		return valorBruto;
	}

	@Override
	public boolean equals(Object obj) {
		if (Objects.nonNull(obj)) {
			if (obj instanceof Ingrediente) {
				Ingrediente ingrediente = (Ingrediente) obj;
				return StringUtils.equals(ingrediente.getNome(), getNome());
			} else if (obj instanceof String) {
				String nome = (String) obj;
				return StringUtils.equals(nome, getNome());
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		String nome = getNome();
		if (StringUtils.isNotEmpty(nome)) {
			return nome.hashCode();
		}
		return super.hashCode();
	}
}