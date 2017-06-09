package br.com.jonathan.test.junit;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;

import com.google.common.io.Resources;

public class IntegrationData {

	public static JSONArray getIngredientes() throws JSONException {
		return getJSONArray(getJson("json/ingredientes.json"));
	}

	public static JSONArray getLanches() throws JSONException {
		return getJSONArray(getJson("json/lanches.json"));
	}

	private static JSONArray getJSONArray(String json) throws JSONException {
		return new JSONArray(json);
	}

	private static String getJson(String path) {
		String json = "";
		try {
			URL resource = new ClassPathResource(path).getURL();
			json = Resources.toString(resource, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}