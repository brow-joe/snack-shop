package br.com.jonathan.test.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Stopwatch;

import br.com.jonathan.StartUp;
import br.com.jonathan.application.utils.MathUtil;
import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.model.Lanche;
import br.com.jonathan.domain.roles.interfaces.Promocao;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartUp.class)
public class FuncionalidadesTest extends TestCase {
	protected final Logger logger = LogManager.getLogger(FuncionalidadesTest.class);

	@Autowired
	private List<Ingrediente> ingredientes;
	@Autowired
	private List<Lanche> lanches;
	@Autowired
	private Promocao promocao;

	private List<Lanche> pedidos;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		Ingrediente alface = getIngrediente("Alface");
		Ingrediente bacon = getIngrediente("Bacon");
		Ingrediente carne = getIngrediente("Hamburguer de carne");
		Ingrediente ovo = getIngrediente("Ovo");
		Ingrediente queijo = getIngrediente("Queijo");

		pedidos = Mockito.mock(List.class);
		Lanche lanche1 = new Lanche();
		lanche1.setNome("Light 1");
		lanche1.setIngredientes(Arrays.asList(alface, alface, alface));

		Lanche lanche2 = new Lanche();
		lanche2.setNome("Muita carne");
		lanche2.setIngredientes(Arrays.asList(carne, carne, carne, carne, carne, carne));

		Lanche lanche3 = new Lanche();
		lanche3.setNome("Muito queijo");
		lanche3.setIngredientes(Arrays.asList(queijo, queijo, queijo, queijo, queijo, queijo));

		Lanche lanche4 = new Lanche();
		lanche4.setNome("Light 2");
		lanche4.setIngredientes(Arrays.asList(alface, alface, alface, ovo, ovo, ovo));

		Lanche lanche5 = new Lanche();
		lanche5.setNome("Exagero");
		lanche5.setIngredientes(Arrays.asList(carne, carne, carne, carne, carne, carne, queijo, queijo, queijo, queijo,
				queijo, queijo, bacon, bacon, bacon));

		Lanche lanche6 = new Lanche();
		lanche6.setNome("Tudo 1");
		lanche6.setIngredientes(ingredientes);

		Lanche lanche7 = new Lanche();
		lanche7.setNome("Tudo 2");
		List<Ingrediente> ingredientes = new ArrayList<>();
		ingredientes.addAll(this.ingredientes);
		ingredientes.addAll(this.ingredientes);
		ingredientes.addAll(this.ingredientes);
		ingredientes.addAll(this.ingredientes);
		ingredientes.addAll(this.ingredientes);
		lanche7.setIngredientes(ingredientes);

		Mockito.when(pedidos.stream())
				.thenReturn(Stream.of(lanche1, lanche2, lanche3, lanche4, lanche5, lanche6, lanche7));
		Mockito.when(pedidos.size()).thenReturn(7);
	}

	@Test
	public void test() {
		Stopwatch timer = Stopwatch.createStarted();
		logger.info("### \t----> Inicio teste de funcionalidades ###");
		assertEquals(lanches.size(), 4);
		assertEquals(ingredientes.size(), 5);

		lanches.forEach(lanche -> {
			valorBrutoOrigianl(lanche);
		});

		assertEquals(pedidos.size(), 7);
		pedidos.stream().forEach(lanche -> {
			valorLiquidoEBrutoPedido(lanche);
		});
		logger.info("### \t----> done - " + timer.stop());
	}

	public void valorLiquidoEBrutoPedido(Lanche lanche) {
		double valorBruto = MathUtil.round(lanche.getValorBruto(), 2);
		double valorLiquido = MathUtil.round(lanche.getValorLiquido(promocao), 2);
		double desconto = MathUtil.round(promocao.calculaDesconto(getIngredientesMap(lanche.getIngredientes()), valorBruto), 2);

		switch (lanche.getNome()) {
		case "Light 1":
			assertEquals(1.2, valorBruto);
			assertEquals(1.08, valorLiquido);
			break;
		case "Muita carne":
			assertEquals(18.0, valorBruto);
			assertEquals(12.0, valorLiquido);
			break;
		case "Muito queijo":
			assertEquals(9.0, valorBruto);
			assertEquals(6.0, valorLiquido);
			break;
		case "Light 2":
			assertEquals(3.6, valorBruto);
			assertEquals(3.24, valorLiquido);
			break;
		case "Exagero":
			assertEquals(33.0, valorBruto);
			assertEquals(24.0, valorLiquido);
			break;
		case "Tudo 1":
			assertEquals(7.7, valorBruto);
			assertEquals(7.7, valorLiquido);
			break;
		case "Tudo 2":
			assertEquals(38.5, valorBruto);
			assertEquals(34.0, valorLiquido);
			break;
		default:
			break;
		}

		double diferenca = MathUtil.round(valorBruto - valorLiquido, 2);
		assertEquals(diferenca, desconto);
	}

	public void valorBrutoOrigianl(Lanche lanche) {
		double valorBruto = MathUtil.round(lanche.getValorBruto(), 2);
		switch (lanche.getNome()) {
		case "X-Bacon":
			assertEquals(6.5, valorBruto);
			break;
		case "X-Burguer":
			assertEquals(4.5, valorBruto);
			break;
		case "X-Egg":
			assertEquals(5.3, valorBruto);
			break;
		case "X-Egg Bacon":
			assertEquals(7.3, valorBruto);
			break;
		default:
			break;
		}
	}

	private Ingrediente getIngrediente(String nome) {
		return ingredientes.get(ingredientes.indexOf(new Ingrediente(nome)));
	}

	private Map<Ingrediente, Long> getIngredientesMap(List<Ingrediente> ingredientes) {
		return ingredientes.stream()
				.collect(
						Collectors.groupingBy(
								Function.identity(), 
								Collectors.counting()
						)
				);
	}

}
