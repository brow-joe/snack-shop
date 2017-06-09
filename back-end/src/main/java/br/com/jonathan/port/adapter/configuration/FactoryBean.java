package br.com.jonathan.port.adapter.configuration;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.jonathan.application.interfaces.IAppService;
import br.com.jonathan.application.service.AppService;
import br.com.jonathan.domain.model.Ingrediente;
import br.com.jonathan.domain.model.Lanche;
import br.com.jonathan.domain.model.Produto;
import br.com.jonathan.domain.roles.PromocaoCarneOuQueijo;
import br.com.jonathan.domain.roles.PromocaoLight;
import br.com.jonathan.domain.roles.impl.PromocaoImpl;
import br.com.jonathan.domain.roles.interfaces.Promocao;

@Configuration
public class FactoryBean {
	protected static final Logger logger = LogManager.getLogger(FactoryBean.class);

	@Value("${paths.ingrediente}")
	private String pathJsonIngrediente;
	@Value("${paths.lanche}")
	private String pathJsonLanche;

	@Bean
	public List<Ingrediente> getIngredientes() {
		String jsonIngrediente = getJson(pathJsonIngrediente);
		TypeToken<List<Ingrediente>> type = new TypeToken<List<Ingrediente>>() {};
		return new Gson().fromJson(jsonIngrediente, getTypeList(type));
	}

	@Bean
	public List<Lanche> getLanches() {
		String jsonLanche = getJson(pathJsonLanche);
		TypeToken<List<Lanche>> type = new TypeToken<List<Lanche>>() {};
		return new Gson().fromJson(jsonLanche, getTypeList(type));
	}

	@Bean
	public Promocao getPromocao() {
		return new PromocaoImpl(
				Arrays.asList(
						new PromocaoLight(), 
						new PromocaoCarneOuQueijo()
				)
		);
	}
	
	@Bean
	public IAppService getService(Promocao promocao){
		return new AppService(promocao);
	}

	private String getJson(String path) {
		String json = "";
		try {
			URL resource = new ClassPathResource(path).getURL();
			json = Resources.toString(resource, StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error(e);
		}
		return json;
	}

	private <T extends Produto> Type getTypeList(TypeToken<?> type) {
		return type.getType();
	}

}