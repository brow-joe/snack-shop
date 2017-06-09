app.controller('ItemController', function($scope, $window, DataService, ProcessoService) {
	function removeItensNaoSelecionados () {
		var i = 0
		for (i in $scope.ingredientes.pedidos) {
			data = $scope.ingredientes.pedidos[i]
			if (data.quantidade < 1) {
				$scope.removeIngrediente(i)
			}
		}
	}
	
	function ingredientesToArray() {
		arr = []
		$scope.ingredientes.pedidos.forEach(function(pedido) {
			for (i = 0; i < pedido.quantidade; i++) {
				arr.push(pedido.ingrediente)
			}
		})
		return arr
	}
	
	$scope.selectedItem = []
	$scope.ingredientesDisponiveis = DataService.getIngredientes()
	$scope.modal = {
		header : "",
		body : ""
	}

	$scope.ingredientes = {
		pedidos : [ {
			quantidade : 0,
			ingredientes : $scope.ingredientesDisponiveis,
			ingrediente : {
				nome : '',
				valorBruto : 0
			}
		} ]
	}

	$scope.selecionaIngrediente = function(selectedItem, index) {
		$scope.selectedItem[index] = selectedItem
		data = JSON.parse(selectedItem)
		item = $scope.ingredientes.pedidos[index]
		item.ingrediente = data
		item.quantidade = 1
	}

	$scope.adicionaIngrediente = function() {
		removeItensNaoSelecionados()
		$scope.ingredientes.pedidos.push({
			quantidade : 0,
			ingredientes : $scope.ingredientesDisponiveis,
			ingrediente : {
				nome : '',
				valorBruto : 0
			}
		})
	}

	$scope.removeIngrediente = function(index) {
		$scope.ingredientes.pedidos.splice(index, 1)
		$scope.selectedItem.splice(index, 1)
	}

	$scope.finalizar = function() {
		removeItensNaoSelecionados()
		if ($scope.ingredientes.pedidos.length < 1) {
			alert('Selecione um ingrediente')
		} else {
			ingredientes = ingredientesToArray()

			lanche = DataService.getLanche()
			lanche.ingredientes = ingredientes

			ProcessoService.getValorLiquido(lanche).then(function(result) {
				$scope.modal.header = 'Preco total'
				$scope.modal.body = 'R$: ' + result
				$('#modalDialog').modal('show')
			})
		}
	}

	DataService.getLanche().ingredientes.forEach(function(item, index) {
		$scope.adicionaIngrediente()
		$scope.selecionaIngrediente(JSON.stringify(item), index)
	})
})