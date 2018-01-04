angular.module('masterOrderForm', ['ngAnimate', 'ngSanitize','ui.bootstrap'])
    .controller('MasterOrderController', ['$http','$scope', function($http, $scope) {
        $scope.buttonEnabled = false;
        $scope.getUsers = function(user) {
            return $http.get("/users/get/" + user).then(function (response) {
                return response.data;
            }, function (exception) {
                console.log(exception);
            })
        };
        $scope.typeHeadSelect=function (event) {
            $scope.buttonEnabled = event;
        }
    }])
    .controller("NotificationController", ['$http', '$scope', function ($http, $scope) {
        $scope.notifications = [];
        setInterval(function () {
            $http.get("/notifications/get").then(function (response) {
                var array = response.data;
                if (array.length !== 0) {
                    $scope.notifications = response.data;
                }
            }, function (exception) {
                console.log(exception);
            })
        }, 1000)
    }]);
