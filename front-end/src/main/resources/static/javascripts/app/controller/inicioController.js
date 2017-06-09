app.controller('InicioController', function($scope, $window, ProcessoService, DataService) {
	ProcessoService.getLanches().then(function(list) {
		$scope.lanches = list
	})

	ProcessoService.getIngredientes().then(function(list) {
		DataService.setIngredientes(list)
	})

	$scope.escolherLanche = function(lanche) {
		DataService.setLanche(lanche)
		$window.location.href = "/#!/ingredients"
	}
})