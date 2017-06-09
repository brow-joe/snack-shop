var host_app = 'http://192.168.99.100:8888'
var app = angular.module('home_app', [ 'ngRoute' ])

app.config(function($routeProvider) {
	$routeProvider.when('/shop', {
		templateUrl : 'inicio.html',
		controller : 'InicioController'
	}).when('/ingredients', {
		templateUrl : 'item.html',
		controller : 'ItemController'
	}).otherwise({
		redirectTo : '/shop'
	})
})