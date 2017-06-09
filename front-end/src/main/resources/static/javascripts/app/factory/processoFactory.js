app.factory('ProcessoFactory', function($http) {
	function successCallback(response) {
		var data = {}
		if (response.status == 200) {
			data = response.data
		}
		return data
	}

	function errorCallback(response) {
		alert(response.statusText)
	}

	var factory = {}
	factory.get = function(host) {
		return $http.get(host).then(successCallback, errorCallback)
	}

	factory.post = function(lanche, host) {
		return $http.post(host, lanche).then(successCallback, errorCallback)
	}

	return factory
})