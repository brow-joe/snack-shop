app.service('DataService', function() {
	var lanche
	var ingredientes = []

	var setIngredientes = function(arr) {
		ingredientes = arr
	}

	var setLanche = function(obj) {
		lanche = obj
	}

	var getLanche = function() {
		return lanche
	}

	var getIngredientes = function() {
		return ingredientes
	}

	return {
		setLanche : setLanche,
		setIngredientes : setIngredientes,
		getLanche : getLanche,
		getIngredientes : getIngredientes
	}
})