package br.com.jonathan.test.junit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

public final class IntegrationUtil {
	public static WebClient getWebClient() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setAjaxController(new NicelyResynchronizingAjaxController());
		client.waitForBackgroundJavaScript(10000);
		return client;
	}

	public static void loadPage(HtmlPage page) {
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		long start = System.currentTimeMillis();

		while (true) {
			if (manager.getJobCount() <= 0) {
				break;
			}
			if ((System.currentTimeMillis() - start) > 60000) {
				return;
			}
		}
	}

	public static HtmlPage injectJSQuantidadeController(HtmlPage page, int quantidade) {
		ScriptResult script = page.executeJavaScript(
				"var scope = angular.element($('[ng-controller=ItemController]')).scope();scope.ingredientes.pedidos[0].quantidade = "
						+ quantidade);
		page = (HtmlPage) script.getNewPage();
		loadPage(page);
		return page;
	}
}