package br.com.jonathan.test.junit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;

import br.com.jonathan.StartUp;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartUp.class)
public class IntegrationTest extends TestCase {
	
	protected static final Logger logger = LogManager.getLogger(IntegrationTest.class);
	private static String host_test_app_front = "http://localhost:8080";
	private static String host_test_app_back = "http://192.168.99.100:8888";

	public static ConfigurableApplicationContext appContext;
	public static WebClient client;
	private HtmlPage homePage;
	
	public static JSONArray lanches;
	public static JSONArray ingredientes;

	@BeforeClass
	public static void startBootApp() throws JSONException {
		appContext = SpringApplication.run(StartUp.class);
		client = IntegrationUtil.getWebClient();
		lanches = IntegrationData.getLanches();
		ingredientes = IntegrationData.getIngredientes();
	}

	@AfterClass
	public static void shutdownBootApp() {
		client.close();
		appContext.close();
	}

	@Test
	public void test() throws IOException, JSONException {
		Stopwatch timer = Stopwatch.createStarted();
		logger.info("### \t----> Inicio teste de integracao ###");

		for (int i = 0; i < lanches.length(); i++) {
			toHomePage();
			List<HtmlTableRow> trs = homePage.getByXPath("//tr[contains(@class, 'lanches')]");
			assertEquals(lanches.length(), trs.size());
			HtmlTableRow tr = trs.get(i);
			JSONObject lanche = lanches.getJSONObject(i);

			List<HtmlElement> tds = tr.getElementsByTagName("td");
			assertEquals(lanche.getString("nome"), tds.get(0).asText());
			calcularLanchesSimples(lanche, (HtmlButton) tds.get(1).getFirstElementChild());
		}

		calcularPromocoes("Alface");
		calcularPromocoes("Queijo");
		calcularPromocoes("Hamburguer de carne");
		logger.info("### \t----> done - " + timer.stop());
	}

	private void calcularPromocoes(String ingrediente)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException, JSONException {
		toHomePage();
		List<HtmlTableRow> trs = homePage.getByXPath("//tr[contains(@class, 'lanches')]");
		HtmlTableRow tr = trs.get(1);
		JSONObject lanche = lanches.getJSONObject(1);

		JSONObject produto = getIngrediente(ingrediente);
		JSONArray pedidoIngredientes = new JSONArray();
		pedidoIngredientes.put(produto);
		pedidoIngredientes.put(produto);
		pedidoIngredientes.put(produto);

		lanche.put("ingredientes", pedidoIngredientes);

		double valorEsperado = round(getValorLiquido(lanche), 2);

		logger.info("#" + ingrediente + " --> Valor Liquido: " + valorEsperado);

		List<HtmlElement> tds = tr.getElementsByTagName("td");
		HtmlButton button = (HtmlButton) tds.get(1).getFirstElementChild();
		HtmlPage paginaIngredientes = button.click();
		IntegrationUtil.loadPage(paginaIngredientes);

		List<HtmlButton> removes = paginaIngredientes.getByXPath("//button[contains(@class, 'btn btn-danger btn-xs')]");
		for (HtmlButton remove : removes) {
			paginaIngredientes = remove.click();
		}
		IntegrationUtil.loadPage(paginaIngredientes);

		HtmlButton adicionar = (HtmlButton) paginaIngredientes.getElementById("adicionar");
		paginaIngredientes = adicionar.click();
		IntegrationUtil.loadPage(paginaIngredientes);

		HtmlSelect select = (HtmlSelect) paginaIngredientes
				.getFirstByXPath("//select[contains(@class, 'form-control')]");
		int index = getIngredienteIndex(produto);
		select.setSelectedIndex(index);
		
		paginaIngredientes = IntegrationUtil.injectJSQuantidadeController(paginaIngredientes, 3);

		HtmlButton finalizar = (HtmlButton) paginaIngredientes.getElementById("finalizar");
		compararValores(lanche, finalizar);
	}

	private void calcularLanchesSimples(JSONObject lanche, HtmlButton button) throws IOException, JSONException {
		HtmlPage paginaIngredientes = button.click();
		IntegrationUtil.loadPage(paginaIngredientes);

		List<HtmlTableRow> trs = paginaIngredientes.getByXPath("//tr[contains(@class, 'pedido')]");
		assertEquals(lanche.getJSONArray("ingredientes").length(), trs.size());

		HtmlButton finalizar = (HtmlButton) paginaIngredientes.getElementById("finalizar");
		compararValores(lanche, finalizar);
	}

	private void compararValores(JSONObject lanche, HtmlButton finalizar) throws IOException {
		HtmlPage paginaResultado = finalizar.click();
		IntegrationUtil.loadPage(paginaResultado);

		HtmlSpan span = (HtmlSpan) paginaResultado.getElementById("resultado");
		String valorResultado = StringUtils.remove(span.asText(), "R$:").trim();
		double valorEsperado = round(getValorLiquido(lanche), 2);
		assertEquals(valorEsperado, Double.valueOf(valorResultado));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private double getValorLiquido(Object lanche) {
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(lanche.toString(), headers);
		ResponseEntity<Double> response = template.postForEntity(host_test_app_back + "/produto/lanche/valorLiquido", entity, Double.class);
		return response.getBody();
	}
	
	public static double round(double numero, double decimal) {
		long factor = (long) Math.pow(10, decimal);
		numero = numero * factor;
		long base = Math.round(numero);
		return (double) base / factor;
	}

	private void toHomePage() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		homePage = client.getPage(host_test_app_front);
		IntegrationUtil.loadPage(homePage);
	}
	
	private JSONObject getIngrediente(String nome) throws JSONException {
		JSONObject ingrediente = null;
		for (int i = 0; i < ingredientes.length(); i++) {
			ingrediente = ingredientes.getJSONObject(i);
			if (StringUtils.equals(ingrediente.getString("nome"), nome)) {
				break;
			}
		}
		return ingrediente;
	}

	private int getIngredienteIndex(JSONObject produto) throws JSONException {
		for (int i = 0; i < ingredientes.length(); i++) {
			if (Objects.equal(ingredientes.getJSONObject(i), produto)) {
				return i;
			}
		}
		return 0;
	}

}
