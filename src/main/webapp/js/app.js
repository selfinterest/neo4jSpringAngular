/**
 * Created by twatson on 8/7/14.
 */
angular.module("Neo4jTestApp", ["ui.router"])
    .controller("QueryController", ["$scope", "queryService", function($scope, queryService){
        $scope.query = "";
        $scope.submitQuery = function(){
            queryService.submit($scope.query).then(function(result){
                $scope.queryResult = result;
                $scope.query = "";
            }, function(err){
                console.log(err);
                $scope.queryResult = err;
            });
        }

    }])
    .service("queryService", ["$http", "$q", function($http, $q){
        this.submit = function(query){
            var defer = $q.defer();
            $http.post("/api/raw", {query: query}).success(function(result){
                defer.resolve(result);
            }).error(function(err){
               defer.reject(err);
            });

            return defer.promise;
        }
    }]);