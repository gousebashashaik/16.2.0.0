define("tui/summaryPanel/controller/Summary", [
    "dojo",
    "dojo/dom-class",
    "dojo/_base/xhr",
    "dojo/_base/Deferred",
    "tui/widget/mixins/Loadable"], function (dojo, domClass, xhr, Deferred) {

    var ADD_A_STAY_URL = dojoConfig.paths.webRoot + "/ws/stay/add"

    var REMOVE_A_STAY_URL = dojoConfig.paths.webRoot + "/ws/stay/remove"

    return  dojo.declare("tui.summaryPanel.controller.Summary", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.mixins.Loadable], {

       urlMap: {
                  "itinerary": "itinerary",
                  "overview":"overview",
                  "cabins":"cabins",
                  "deck plans":"deck-plans",
                  "dining":"dining",
                  "entertainment":"entertainment",
                  "facilities":"facilities"
       },

       postCreate: function () {
          var summaryController = this;
          summaryController.addEventListener();
       },
       addEventListener: function(){
          var summaryController = this;
          dojo.subscribe("tui/summaryPanel/controller/addStay", function (data) {
              var node = summaryController.domNode;
              summaryController.loading();
              var xhrReq = xhr.post({
                url: ADD_A_STAY_URL,
                content: {searchCriteria: JSON.stringify(data)},
                handleAs: "json",
                load: function (response, options) {
                    Deferred.when(response, function (response) {
                        dojo.publish("tui/summaryPanel/controller/updateSummaryPanel", response);
                        _.isUndefined(dojo.query(".stay-teaser")[0]) ? null : dojo.addClass(dojo.query(".stay-teaser")[0], "hide");
                        summaryController.toggleBreadcrumb();
                        summaryController.loaded();
                        summaryController.afterLoaded(response);
                    });
                },
                error: function (err) {
                   console.log(err);
                }
             });
          });

          dojo.subscribe("tui/summaryPanel/controller/removeStay", function (data) {
               var node = summaryController.domNode;
               summaryController.loading();
               var xhrReq = xhr.post({
                   url: REMOVE_A_STAY_URL,
                   content: {searchCriteria: JSON.stringify(data)},
                   handleAs: "json",
                   load: function (response, options) {
                       Deferred.when(response, function (response) {
                           dojo.publish("tui/summaryPanel/controller/updateSummaryPanel", response);
                           _.isUndefined(dojo.query(".stay-teaser")[0]) ? null : dojo.removeClass(dojo.query(".stay-teaser")[0], "hide");
                           summaryController.toggleBreadcrumb();
                           summaryController.loaded();
                           summaryController.afterLoaded(response);
                       });
                   },
                   error: function (err) {
                       console.log(err);
                   }
               });
           });

          dojo.subscribe("tui/summaryPanel/controller/alternateFlightAdded", function (data) {
              dojo.publish("tui/summaryPanel/controller/updateSummaryPanel", data);
              summaryController.afterLoaded(data);
          });
        },

        toggleBreadcrumb: function(){
            var stepIndicator = dojo.query("#step-indicators")[0];
            var altStepIndicator = dojo.query("#alt-step-indicators")[0];
            if(dojo.hasClass(stepIndicator, "hide")){
                dojo.removeClass(stepIndicator, "hide");
                dojo.addClass(altStepIndicator, "hide");
            }else if(dojo.hasClass(altStepIndicator, "hide")){
                dojo.addClass(stepIndicator, "hide");
                dojo.removeClass(altStepIndicator, "hide");
            }
        },

        //to be removed..
		toggleBreadcrumbAdd: function(){
			var stepIndicator = dojo.query("#step-indicators")[0];
            var altStepIndicator = dojo.query("#alt-step-indicators")[0];
                dojo.addClass(stepIndicator, "hide");
                dojo.removeClass(altStepIndicator, "hide");
            },

        toggleBreadcrumbRemove: function(){
            var stepIndicator = dojo.query("#step-indicators")[0];
			var altStepIndicator = dojo.query("#alt-step-indicators")[0];
                dojo.removeClass(stepIndicator, "hide");
                dojo.addClass(altStepIndicator, "hide");
        },

        afterLoaded: function(response){
           var summaryController = this;
           if(!_.isUndefined(response)){
              var urls = response.ajaxChangedUrlsMap;
              var pageTabs = dojo.query('.page-tabs')[0];
              if(!_.isUndefined(pageTabs)) {
                 _.each(dojo.query('a', pageTabs), function (num) {
                    num.href = urls[summaryController.urlMap[num.innerHTML.toLowerCase()]];
                 });
              }
              dojo.query('.itinerary-tab-link')[0].href = urls["itinerary"];
			   _.isUndefined(dojo.query('.itinerary-page-url')[0]) ? null : dojo.query('.itinerary-page-url')[0].href = urls["itinerary"];
				dojo.query('.ship-tab-link')[0].href = urls["overview"];
			   _.isUndefined(dojo.query('.ship-page-url')[0]) ? null : dojo.query('.ship-page-url')[0].href = urls["overview"];			
           }
        }
    });
});