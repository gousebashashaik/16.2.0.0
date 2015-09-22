define("tui/widget/booking/flightoptions/FlightOptionsMediator", [
    "dojo",
    "dojo/dom",
    "dojo/_base/xhr",
    "tui/utils/SessionStorage",
    "tui/widget/booking/constants/BookflowUrl",
    "dojo/_base/connect",
    "tui/widget/booking/constants/view/ErrorView",
    "tui/widget/booking/constants/view/ErrorOverlay",
    "dojo/dom-construct",
    "dojo/topic",
    "tui/widget/_TuiBaseWidget"], function (dojo, dom, xhr, sessionStore,BookflowUrl, connect,ErrorView,ErrorOverlay,domConstruct,topic) {

    var TARGET_URL =BookflowUrl.alternativeflighturl;


    function doWhen(condition, f) {
        return condition ? f() : null;
    }

    dojo.declare("tui.widget.booking.flightoptions.FlightOptionsMediator", [tui.widget._TuiBaseWidget], {

        controllers: [],

        targetUrl: BookflowUrl.alternativeflighturl,

        doAjaxRequest: true,

        model: null,
        model1: null,

        currentRequest: null,

        currentResponse: null,

        adaptResponse : function (initialResponse, intermediateResponse, field) {

        },


        postCreate : function(){
        	this.inherited(arguments);
        	this.tagElements(dojo.query('.stay-there'),"Stay there a bit longer");
        	this.tagElements(dojo.query('.fly-on'),"Fly on different days / times?");
        	this.tagElements(dojo.query('.fly-from'),"Fly from a different airport?");
        	this.tagElements(dojo.query('.viewAlternatives'),"changeFlight");


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


        publishToControllers: function (response) {
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
            if(resultsNode){
            dojo.addClass(resultsNode, 'updating');
            }
            mediator.doAjaxRequest = false;
        },

        afterSuccess: function () {
            var mediator = this;
            var resultsNode = dojo.query(".search-results")[0];
            if(resultsNode){
            dojo.removeClass(resultsNode, 'updating');
            }
            mediator.doAjaxRequest = true;
        },

        afterFailure: function (html,statusCode) {
            var mediator = this;
            dojo.removeClass(dojo.query(".search-results")[0], 'updating');
            mediator.doAjaxRequest = true;
            dojo.removeClass(dom.byId("top"), 'updating');
            dojo.removeClass(dom.byId("main"), 'updating');
            dojo.removeClass(dom.byId("right"), 'updating');
            dojo.addClass(dom.byId("altflight-overlay"), 'error-overlay');
            topic.publish("flightOptions.flightSummaryPlane");
            dojo.publish("tui.searchResults.view.SearchResultsComponent.renderAjaxErrorPopup");
            if(statusCode === 500){
	            if (this.errorView && this.errorView != null) {
	                this.errorView.destroyRecursive();
	                this.errorView = null;
	                this.errorOverlay.destroyRecursive();
	                this.errorOverlay = null;
	              }
	              this.errorView = new ErrorView({
	                  "Data": "test",
	                  "id": "error-overlay"
	              });
	              domConstruct.place(this.errorView.domNode, this.domNode, "last");
	              this.errorOverlay = new ErrorOverlay({widgetId: this.errorView.id, modal: true});
	              this.errorOverlay.open();
            }else{
            	domConstruct.place(html, document.body, "only");
                 dojo.parser.parse(document.body);
            }
        },

        fire: function (duration, departureAirportCode,selectedFilters, selectedDate, packageId) {
            var mediator = this;
            doWhen(mediator.doAjaxRequest, function () {
                mediator.beforeRequest();

                var jsonData= {"selectedDuration":duration,
                		"selectedDepartureAirportCode":departureAirportCode,
                		"selectedFilters":selectedFilters,
                		"selectedDate": selectedDate,
                		"packageId" : packageId
                		};
                var jsonString = dojo.toJson(jsonData);

				var request ={flightFiltercriteria:jsonString};
                var results = xhr.get({
                    url: TARGET_URL,
                    content: request,
                    handleAs: "json",
                    preventCache: true,
                    error: function (jxhr,err) {
                        if (dojoConfig.devDebug) {
                            console.error(jxhr);
                        }
                        mediator.afterFailure(err.xhr.responseText, err.xhr.status);
                    }
                });

                dojo.when(results, function (response) {

                	mediator.afterSuccess();
                	mediator.publishToControllers(response);
                	dojo.removeClass(dom.byId("top"), 'updating');
                    dojo.removeClass(dom.byId("main"), 'updating');
                    dojo.removeClass(dom.byId("right"), 'updating');

                });


            });

        }



    });

    return tui.widget.booking.flightoptions.FlightOptionsMediator;
});