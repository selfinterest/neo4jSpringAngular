/**
 * Created by twatson on 8/7/14.
 */
angular.module("Neo4jTestApp", ["ui.router", "ui.bootstrap"])
    .controller("QueryController", ["$scope", "queryService", function($scope, queryService){
        $scope.sampleQueries = {
          "Count nodes": 'MATCH (n) \n RETURN "Hello Graph with \n "+count(*)+" Nodes!" AS welcome;',
          "Delete all": 'MATCH (n) \n OPTIONAL MATCH (n)-[r]-() \n DELETE n,r'
        };

        $scope.presets = {
            "Matrix": "matrix"
        };



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

        $scope.loadPresetQuery = function(presetName){
            var jsonName = "/json/" + presetName + ".json";
            $scope.status.isOpen = false;
            queryService.loadFromJson(jsonName).then(function(result){

                $scope.query = result;
            });
        }

        $scope.status = {
            isopen: false
        };

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



        this.loadFromJson = function(jsonName){
            var defer = $q.defer();
            $http.get(jsonName).success(function(result){
                result = result.query.join(",\n");
                defer.resolve(result);
            });
            return defer.promise;
        }
    }]);