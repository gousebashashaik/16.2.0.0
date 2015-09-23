define("tui/searchPanel/view/flights/AirportListOverlay", [
    "dojo",
    "dojo/on",
    "dojo/has",
    "dojo/dom-attr",
    "dojo/text!tui/searchPanel/view/flights/templates/AirportListOverlayTmpl.html",
    "tui/searchPanel/store/AirportGuideStore",
    "tui/searchPanel/model/AirportModel",
    "tui/widget/expand/SimpleExpandable",
    "tui/searchPanel/view/flights/AirportGuide",
    "tui/searchPanel/view/flights/SearchErrorMessaging"],
    function (dojo, on, has, domAttr, airportListOverlayTmpl, airportGuideStore, AirportModel, simpleExpandable, airportGuide) {

        dojo.declare("tui.searchPanel.view.flights.AirportListOverlay", [tui.searchPanel.view.flights.AirportGuide, tui.widget.expand.SimpleExpandable, tui.searchPanel.view.flights.SearchErrorMessaging], {

            // ----------------------------------------------------------------------------- properties

            tmpl: airportListOverlayTmpl,

                   // ----------------------------------------------------------------------------- methods

            postCreate: function () {

            	var airportListOverlay = this;

            	airportListOverlay.inherited(arguments);
                var resultSet = airportListOverlay.searchPanelModel.from.query();

                resultSet.observe(function (airportListOverlay, remove, add) {
                    // if expandable is not created yet, let's ignore.
                    if (!airportListOverlay.expandableDom) {
                        return;
                    }
                    airportListOverlay.updateGuideCount();

                });

                 //Original Code - Don't Delete
                /* airportListOverlay.connect(document.body, "onclick", function (event) {
                 	airportListOverlay.connect(airportListOverlay.expandableDom, "onclick", function (event) {
                 		return;
                 	});

                 	if (document.activeElement.id == airportListOverlay.id)return;
                 	if (airportListOverlay.expandableDom === null || !airportListOverlay.isShowing(airportListOverlay.expandableDom)) {
                 		alert("close expandable");
                 		airportListOverlay.closeExpandable();
 	                 }
                 });*/

                 airportListOverlay.connect(document.body, "onclick", function (event) {
                 	airportListOverlay.connect(airportListOverlay.expandableDom, "onclick", function (event) {
                 		return;
                 	});
                 	if(event.target.id === "ukairports" || event.target.id === "overseasairports"){
                 		return
                 	}
                 	if (airportListOverlay.expandableDom === null || !airportListOverlay.isShowing(airportListOverlay.expandableDom)) {
                 		airportListOverlay.closeExpandable();
 	                }
                 	try{
                 		var activeClosest=dojo.query(document.activeElement).closest("#where-from-text")[0];
                 	if (activeClosest && activeClosest.id === airportListOverlay.id) {

                 		if(airportListOverlay.expandableDom){
                 			if(airportListOverlay.isShowing(airportListOverlay.expandableDom)){
                 				airportListOverlay.setHeight();
                 			}
                 			if(event.target.id === "ukairports" || event.target.id === "overseasairports"){
                 					setTimeout(function(){airportListOverlay.highlightTabs(),1000});
                 			}
                 		}
                 		return;
                 	}
                 	else if (document.activeElement.id === "tui_searchPanel_view_flights_AirportAutoComplete_0") {
                 		dojo.addClass(dojo.byId("where-from"),"border-sel-active");
                 		return;
                 	}
                 	else {
                 		airportListOverlay.closeExpandable();
                 	}
                 	} catch(err){
                 		console.log(err);
                 	}



                 });

                 airportListOverlay.tagElement(airportListOverlay.domNode, "where-from");

                //TODO: Error message calling suppress. Need to do permanent fix.
                dojo.global.isFromErrorMessageNew=true;
            },


            onAfterTmplRender: function(){
            	var airportListOverlay = this;
            	airportListOverlay.inherited(arguments);


            	dojo.place(dojo.byId("where-from"), dojo.byId("pillsOnFly"), "last");
            	dojo.connect(dojo.query(".airporttype")[0], "onclick", function(evt){
            		if(evt.target.id == "ukairports"){
            			dojo.style(dojo.query(".guide .wrapper .overseas-container")[0], {
            				"display":"none"
            			});
            			dojo.style(dojo.query(".guide .wrapper .ukairports-container")[0], {
            				"display":"block"
            			});

            			if(dojo.query(".search-error")[0]){
	            			dojo.style(dojo.query(".search-error")[0], {
	            				"display":"none"
	            			});
            			}

            			dojo.query(".UKAiportCnt").removeClass('hide').addClass('show');
            			dojo.query(".OverseasairportsCnt").removeClass('show').addClass('hide');


            			dojo.addClass("ukairports","ukairports-selected");
            			dojo.addClass("overseasairports","overseasairports-notselected");
            			dojo.removeClass("ukairports","ukairports-notselected");
            			dojo.removeClass("overseasairports","overseasairports-selected");
            		} else if(evt.target.id == "overseasairports"){

            			dojo.style(dojo.query(".guide .wrapper .ukairports-container")[0], {
            				"display":"none"
            			});
            			dojo.style(dojo.query(".guide .wrapper .overseas-container")[0], {
            				"display":"block"
            			});


            			dojo.query(".UKAiportCnt").removeClass('show').addClass('hide');
            			dojo.query(".OverseasairportsCnt").removeClass('hide').addClass('show');

            			airportListOverlay.setHeight();
            			dojo.addClass("ukairports","ukairports-notselected");
            			dojo.addClass("overseasairports","overseasairports-selected");
            			dojo.removeClass("ukairports","ukairports-selected");
            			dojo.removeClass("overseasairports","overseasairports-notselected");
            		}
            		var whereFromWidget = dijit.byId("where-from");
            		if(whereFromWidget.id == "where-from"){
            			whereFromWidget.searchPanelModel.searchErrorMessages.set("from", {})
            			if(whereFromWidget.autocomplete.listShowing) whereFromWidget.hideWidget(whereFromWidget.autocomplete.listElement);
    				}
                });
            	setTimeout(function(){airportListOverlay.highlightTabs(),100});
            /*	if(dojo.query(".guide .wrapper")[1]){
	            	setTimeout(function(){
	    				var height = _.pixels(dojo.position(dojo.query(".guide .wrapper")[1]).h)
	    				dojo.query(".guide")[1].style.maxHeight = height;
	    			},100);
            	}
            	*/
            },

            updateGuideCount: function () {
                // summary:
                //		Updates airport guide count with given value.
                var airportListOverlay = this;

                //console.dir(dojo.query(".empty-airport-model", airportGuide.expandableDom)[0]);
                // Remove inactive class if we have airports which we can remove.
                var emptyAirportModel = dojo.query(".empty-airport-model", airportListOverlay.expandableDom)[0];
                var clearSelection=dojo.query(".empty-airport-model", airportListOverlay.expandableDom)[1];

                if (airportListOverlay.searchPanelModel.from.data.length > 0 && airportListOverlay.searchPanelModel.from.data[0].countryCode == 'GBR' ) {
                    dojo.removeClass(emptyAirportModel, "inactive");

                } else {
                    dojo.addClass(emptyAirportModel, "inactive");
                }

                //clear  selection overseas
                if (airportListOverlay.searchPanelModel.from.data.length > 0 && airportListOverlay.searchPanelModel.from.data[0].countryCode != 'GBR') {
                    dojo.removeClass(clearSelection, "inactive");

                } else {
                    dojo.addClass(clearSelection, "inactive");
                }

                // update airport guide selected count.
                //dojo.query(".airport-guide-count", airportListOverlay.expandableDom).text(airportListOverlay.searchPanelModel.from.selectedSize);
                if(airportListOverlay.searchPanelModel.from.selectedSize === 0 ){
					dojo.query("#wherefromPlaceholder").style("display","block");
                	dojo.query("#wherefromValue").style("display","none");
                }else if(airportListOverlay.searchPanelModel.from.selectedSize > 0){
                	airportListOverlay.updatePillText();
                }
            },

            /*code added*/

            displayFromToError: function (name, oldError, newError) {
                // displays 'from, to' validation message if validation error occurs
                var airportListOverlay = this;
                if( dojo.global.isFromErrorMessageNew){

                airportListOverlay.validateErrorMessage(newError.emptyFromTo, {
                  errorMessage: newError.emptyFromTo,
                  arrow: true,
                  errorPopupClass: "error-jumbo",
                  floatWhere: "position-bottom-center",
                  field: "fromTo",
                  key: "emptyFromTo"
                });
                	 dojo.global.isFromErrorMessageNew=false;
                }
              },

              /*code added*/

            openExpandable:function(){
            	var airportListOverlay = this;
            	airportListOverlay.inherited(arguments);
            	dojo.addClass(dojo.byId("where-from-text"),"border-sel-active");
            },
            closeExpandable:function(){
            	var airportListOverlay = this;
            	airportListOverlay.inherited(arguments);
            	dojo.removeClass(dojo.byId("where-from-text"),"border-sel-active");
            	if(dijit.byId("where-from").id == "where-from"){
                	dijit.byId("where-from").searchPanelModel.searchErrorMessages.set("from", {})
                	dijit.byId("where-from").autocomplete.hideWidget(dijit.byId("where-from").autocomplete.listElement);
				}
            }
        });
        return tui.searchPanel.view.flights.AirportListOverlay;
    });

