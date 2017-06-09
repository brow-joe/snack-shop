package br.com.jonathan.application.utils;

public final class MathUtil {

	public static double round(double numero, double decimal) {
		long factor = (long) Math.pow(10, decimal);
		numero = numero * factor;
		long base = Math.round(numero);
		return (double) base / factor;
	}

}