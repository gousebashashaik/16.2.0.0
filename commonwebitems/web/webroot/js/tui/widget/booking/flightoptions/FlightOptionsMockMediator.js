define("tui/widget/booking/flightoptions/FlightOptionsMockMediator", [
    "dojo",
    "tui/utils/SessionStorage",
    "dojo/_base/connect",
    "tui/widget/_TuiBaseWidget"], function (dojo, sessionStore, connect) {

    var TARGET_URL = "data/alternativeFlightOptionsData.json";


    function doWhen(condition, f) {
        return condition ? f() : null;
    }

    dojo.declare("tui.widget.booking.flightoptions.FlightOptionsMockMediator", [tui.widget._TuiBaseWidget], {

        controllers: [],


        doAjaxRequest: true,

        model: null,
        model1: null,

        currentRequest: null,

        currentResponse: null,

        adaptResponse : function (initialResponse, intermediateResponse, field) {

        },

        constructor: function () {




        },

        registerController: function (controller, dataPath) {

            var mediator = this;
            mediator.controllers.push(controller);

            //Take from session store if its history back button
            var response = mediator.currentResponse || mediator.model;
            if (_.isArray(dataPath)) {
                return _.map(dataPath, function (path) {
                    return dojo.getObject(path || '', false, response);
                })
            } else {
                return dojo.getObject(dataPath || controller.dataPath || '', false, response);
            }
        },


        publishToControllers: function (field, oldValue, newValue, response) {
            var mediator = this;
           _.each(mediator.controllers, function (controller) {

                controller.refresh(response);
            });

        },

        handleNoResults: function (field, oldValue, newValue) {
            var mediator = this;
            _.each(mediator.controllers, function (controller) {
                controller.handleNoResults ? controller.handleNoResults(field, oldValue, newValue) : null;
            });
        },

        generateRequest: function (field) {
            var mediator = this;
            var request = {};
            _.each(mediator.controllers, function (controller) {
                dojo.mixin(request, controller.generateRequest ? controller.generateRequest(field) : {});
            });
            return request;
        },

        lastSuccessfulRequest: function () {
            return this.currentRequest || this.model.searchRequest;
        },

        beforeRequest: function () {
            var mediator = this;
            var resultsNode = dojo.query(".search-results")[0];

            dojo.addClass(resultsNode, 'updating');
            mediator.doAjaxRequest = false;
        },

        afterSuccess: function () {
            var mediator = this;

            dojo.removeClass(dojo.query(".search-results")[0], 'updating');
            mediator.doAjaxRequest = true;
        },

        afterFailure: function () {
            var mediator = this;
            dojo.removeClass(dojo.query(".search-results")[0], 'updating');
            mediator.doAjaxRequest = true;
            dojo.publish("tui.searchResults.view.SearchResultsComponent.renderAjaxErrorPopup")
        },

        fire: function (field, duration, departureAirportCode,varTeaser, selectedFilters) {
            var mediator = this;
            doWhen(mediator.doAjaxRequest, function () {
                mediator.beforeRequest();

               /* var request = mediator.generateRequest(field);
                console.log(dojo.toJson(request),"request");*/

                var jsonData= {"selectedDuration":duration,
                		"selectedDepartureAirportCode":departureAirportCode,
                		"selectedDateTeaserViewDataDisplayDate":varTeaser,
                		"selectedFilters":selectedFilters};
                var jsonString = dojo.toJson(jsonData);

				var request ={flightFiltercriteria:jsonString};

				dojo.xhrGet({
					url:TARGET_URL,
 					handleAs: "json",
	 			    load:function(response){
	 				mediator.afterSuccess();
                	mediator.publishToControllers(field, duration, departureAirportCode, response);
	 			    },
	 			    error: function(error){
	    				if (dojoConfig.devDebug) {
	                        console.error(err);
	                    }
	    				mediator.afterFailure();
	 			    }
    			});

            });
        }



    });

    return tui.widget.booking.flightoptions.MockMediator;
});