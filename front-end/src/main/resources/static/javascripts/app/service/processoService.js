app.service('ProcessoService', function(ProcessoFactory) {
	this.getLanches = function() {
		return ProcessoFactory.get(host_app + '/produto/lanches/')
	}

	this.getIngredientes = function() {
		return ProcessoFactory.get(host_app + '/produto/ingredientes/')
	}
	
	this.getValorLiquido = function(lanche){
		return ProcessoFactory.post(lanche, host_app + '/produto/lanche/valorLiquido')
	}
})