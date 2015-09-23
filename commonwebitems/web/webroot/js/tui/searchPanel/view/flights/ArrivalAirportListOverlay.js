define("tui/searchPanel/view/flights/ArrivalAirportListOverlay", [
    "dojo",
    "dojo/on",
    "dojo/has",
    "dojo/dom-attr",
    "dojo/text!tui/searchPanel/view/flights/templates/ArrivalAirportListOverlayTmpl.html",
    "tui/searchPanel/store/flights/ArrivalAirportGuideStore",
    "tui/searchPanel/model/AirportModel",
    "tui/widget/expand/SimpleExpandable",
    "tui/searchPanel/view/flights/ArrivalAirportGuide",
    "tui/searchPanel/view/flights/SearchErrorMessaging"],
    function (dojo, on, has, domAttr, arrivalAirportListOverlayTmpl, arrivalAirportGuideStore, AirportModel, simpleExpandable, arrivalAirportGuide) {

        dojo.declare("tui.searchPanel.view.flights.ArrivalAirportListOverlay", [tui.searchPanel.view.flights.ArrivalAirportGuide, tui.widget.expand.SimpleExpandable,tui.searchPanel.view.flights.SearchErrorMessaging], {

            // ----------------------------------------------------------------------------- properties

            tmpl: arrivalAirportListOverlayTmpl,
                   // ----------------------------------------------------------------------------- methods

            postCreate: function () {
            	var arrivalAirportListOverlay = this;
            	arrivalAirportListOverlay.inherited(arguments);
                var resultSet = arrivalAirportListOverlay.searchPanelModel.to.query();
                resultSet.observe(function (arrivalAirportListOverlay, remove, add) {
                    // if expandable is not created yet, let's ignore.
                    if (!arrivalAirportListOverlay.expandableDom) {
                        return;
                    }
                    arrivalAirportListOverlay.updateGuideCount();
                });

              //Original Code - Don't Delete
                /* arrivalAirportListOverlay.connect(document.body, "onclick", function (event) {
                 	arrivalAirportListOverlay.connect(arrivalAirportListOverlay.expandableDom, "onclick", function (event) {
                 		return;
                 	});

                 	if (document.activeElement.id == arrivalAirportListOverlay.id)return;
                 	if (arrivalAirportListOverlay.expandableDom === null || !arrivalAirportListOverlay.isShowing(arrivalAirportListOverlay.expandableDom)) {
                 		arrivalAirportListOverlay.closeExpandable();
 	                 }
                 });*/


                 arrivalAirportListOverlay.connect(document.body, "onclick", function (event) {
                 	arrivalAirportListOverlay.connect(arrivalAirportListOverlay.expandableDom, "onclick", function (event) {
                 		return;
                 	});
                 	if(event.target.id === "ukairports-arrival" || event.target.id === "overseasairports-arrival"){
                 		return
                 	}

                 	if (arrivalAirportListOverlay.expandableDom === null || !arrivalAirportListOverlay.isShowing(arrivalAirportListOverlay.expandableDom)) {
                 		arrivalAirportListOverlay.closeExpandable();
 	                }
                 	try{
                 	console.log(document.activeElement.id)
                 	console.log(arrivalAirportListOverlay.id)
                 	var activeClosest=dojo.query(document.activeElement).closest("#where-to-text")[0];
                 	if (activeClosest && activeClosest.id === arrivalAirportListOverlay.id) {
                 			if(arrivalAirportListOverlay.expandableDom){
                 				if(arrivalAirportListOverlay.isShowing(arrivalAirportListOverlay.expandableDom)){
                 					arrivalAirportListOverlay.setHeight();
                 				}
                 				if(event.target.id === "ukairports-arrival" || event.target.id === "overseasairports-arrival"){
                 				setTimeout(function(){arrivalAirportListOverlay.highlightTabs()},1000);
                 				}
                 			}
                 		return;
                 	}
                 	else if (document.activeElement.id === "tui_searchPanel_view_flights_AirportAutoComplete_1") {
                 		dojo.addClass(dojo.byId("where-to"),"border-sel-active");
                 		return;
                 	}
                 	else {
                 		arrivalAirportListOverlay.closeExpandable();
                 	}
                 	} catch(err) {
                 		console.log(err);
                 	}


                 });

                 arrivalAirportListOverlay.tagElement(arrivalAirportListOverlay.domNode, "where-to");

                 //TODO: Error message calling suppress. Need to do permanent fix.
                 dojo.global.isToErrorMessageNew=true;


            },


            onAfterTmplRender: function(){
            	var arrivalAirportListOverlay = this;
            	arrivalAirportListOverlay.inherited(arguments);


            	dojo.place(dojo.byId("where-to"), dojo.byId("pillsOnFlyArrival"), "last");
            	dojo.connect(dojo.query(".airporttype-arrival")[0], "onclick", function(evt){
            		if(evt.target.id == "ukairports-arrival"){
            			dojo.style(dojo.query(".guide .wrapper .overseas-container-arrival")[0], {
            				"display":"none"
            			});
            			dojo.style(dojo.query(".guide .wrapper .ukairports-container-arrival")[0], {
            				"display":"block"
            			});

            			dojo.query(".ArrivalsUKAiportCnt").removeClass('hide').addClass('show');
            			dojo.query(".ArrivalsOverseasairportsCnt").removeClass('show').addClass('hide');

            			dojo.addClass("ukairports-arrival","ukairports-arrival-selected");
            			dojo.addClass("overseasairports-arrival","overseasairports-arrival-notselected");
            			dojo.removeClass("ukairports-arrival","ukairports-arrival-notselected");
            			dojo.removeClass("overseasairports-arrival","overseasairports-arrival-selected");


            		} else if(evt.target.id == "overseasairports-arrival"){
            			dojo.style(dojo.query(".guide .wrapper .ukairports-container-arrival")[0], {
            				"display":"none"
            			});
            			dojo.style(dojo.query(".guide .wrapper .overseas-container-arrival")[0], {
            				"display":"block"
            			});

            			dojo.query(".ArrivalsUKAiportCnt").removeClass('show').addClass('hide');
            			dojo.query(".ArrivalsOverseasairportsCnt").removeClass('hide').addClass('show');



            			dojo.addClass("ukairports-arrival","ukairports-arrival-notselected");
            			dojo.addClass("overseasairports-arrival","overseasairports-arrival-selected");

            			dojo.removeClass("ukairports-arrival","ukairports-arrival-selected");
            			dojo.removeClass("overseasairports-arrival","overseasairports-arrival-notselected");

            		}
            		var whereToWidget = dijit.byId("where-to");
            		if(whereToWidget.id == "where-to"){
            			whereToWidget.searchPanelModel.searchErrorMessages.set("to", {})
            			if(whereToWidget.autocomplete.listShowing) whereToWidget.hideWidget(whereToWidget.autocomplete.listElement);
    				}
            		arrivalAirportListOverlay.setHeight();
                });

            	setTimeout(function(){arrivalAirportListOverlay.highlightTabs()},100);
            },



            updateGuideCount: function () {
                // summary:
                //		Updates airport guide count with given value.
                var arrivalAirportListOverlay = this;
                console.log(arrivalAirportListOverlay);
                //console.dir(dojo.query(".empty-airport-model", airportGuide.expandableDom)[0]);
                // Remove inactive class if we have airports which we can remove.
                //console.dir(dojo.query(".empty-airport-model")[0]);
                var emptyAirportModel = dojo.query(".empty-airport-model", arrivalAirportListOverlay.expandableDom)[0];
                var clearSelection=dojo.query(".empty-airport-model", arrivalAirportListOverlay.expandableDom)[1];
                if (arrivalAirportListOverlay.searchPanelModel.to.data.length > 0 && arrivalAirportListOverlay.searchPanelModel.to.data[0].countryCode == 'GBR' ) {
                    dojo.removeClass(emptyAirportModel, "inactive");

                } else {
                    dojo.addClass(emptyAirportModel, "inactive");
                }

                //clear  selection overseas
                if (arrivalAirportListOverlay.searchPanelModel.to.data.length > 0 && arrivalAirportListOverlay.searchPanelModel.to.data[0].countryCode != 'GBR') {
                    dojo.removeClass(clearSelection, "inactive");

                } else {
                    dojo.addClass(clearSelection, "inactive");
                }

                // update airport guide selected count.
                console.log(arrivalAirportListOverlay.searchPanelModel.to.selectedSize + "!!");
                //dojo.query(".airport-guide-count", arrivalAirportListOverlay.expandableDom).text(arrivalAirportListOverlay.searchPanelModel.to.selectedSize);
                if(arrivalAirportListOverlay.searchPanelModel.to.selectedSize === 0 ){
                	dojo.query("#wheretoPlaceholder").style("display","block");
                	dojo.query("#wheretoValue").style("display","none");
                }else if(arrivalAirportListOverlay.searchPanelModel.to.selectedSize > 0 ){
                	arrivalAirportListOverlay.updatePillText();
                }
            },

            /*code added*/

            displayToError: function (name, oldError, newError) {
                // displays ' to' validation message if validation error occurs
            	 var arrivalAirportListOverlay = this;
            	 if(dojo.global.isToErrorMessageNew){
                arrivalAirportListOverlay.validateErrorMessage(newError.emptyTo, {
                  errorMessage: newError.emptyTo,
                  arrow: true,
                  errorPopupClass: "error-jumbo",
                  floatWhere: "position-bottom-center",
                  field: "emptyTo",
                  key: "emptyTo"
                });
                dojo.global.isToErrorMessageNew=false;
            	}
              },

              /*code added*/


            openExpandable:function(){
            	var arrivalAirportListOverlay = this;
            	arrivalAirportListOverlay.inherited(arguments);
            	dojo.addClass(dojo.byId("where-to-text"),"border-sel-active");
            },
            closeExpandable:function(){
            	var arrivalAirportListOverlay = this;
            	arrivalAirportListOverlay.inherited(arguments);
            	dojo.removeClass(dojo.byId("where-to-text"),"border-sel-active");
            	if(dijit.byId("where-to").id == "where-to"){
                	dijit.byId("where-to").searchPanelModel.searchErrorMessages.set("to", {})
                	dijit.byId("where-to").autocomplete.hideWidget(dijit.byId("where-to").autocomplete.listElement);
				}
            	}
        });
        return tui.searchPanel.view.flights.ArrivalAirportListOverlay;
    });

