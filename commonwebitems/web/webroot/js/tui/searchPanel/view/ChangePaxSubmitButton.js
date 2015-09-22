define("tui/searchPanel/view/ChangePaxSubmitButton", [
  "dojo",
  "dojo/on",
  "dojo/has",
  "tui/utils/SessionStorage",
  "dojo/dom-construct",
  "dojo/dom-style",
  "tui/search/nls/Searchi18nable",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, has, sessionStore, domConstruct, domStyle) {

  dojo.declare("tui.searchPanel.view.ChangePaxSubmitButton", [tui.widget._TuiBaseWidget, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    form: null,

    formSelector: null,

    targetUrl: dojoConfig.paths.webRoot + "/bookitineraries",

    tracs: false,

    changePaxRequest: false,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var submitButton = this;
      submitButton.inherited(arguments);


	  if(!submitButton.isHotelAvailable && location.href.indexOf("\&searchType=PaxOverlay") !== -1){
			var noResultNode = dojo.query(".no-results-found")[0];
			domConstruct.place(noResultNode, dojo.body(), "last");
			domStyle.set(noResultNode, {display: "block", position: "fixed", "zIndex": "5000"} );
			if( !dojo.query(".modal")[0] ){
				domConstruct.place('<div class="modal"></div>', dojo.body(), "last");
			}
			domStyle.set(dojo.query(".modal")[0], {display: "block", opacity: 0.4,position:"fixed"} );
	  }

	  dojo.subscribe("tui:channel=validSearchModel", function(){
		  if( !submitButton.changePaxRequest ){
			//DE38707, All search panel requests will be returned from here
			  return;
		  }

	  	var adults = submitButton.searchPanelModel.adults,
		 children = submitButton.searchPanelModel.children,
		 childrenAges = submitButton.searchPanelModel.childAges;
		 var childrenAgesStr = childrenAges && childrenAges.join();

		 var url = submitButton.url;
		 if( submitButton.stayAdded && !submitButton.tracs && url.indexOf("isAtcomMc") === -1 ){
			 //URL is FLY_CRUISE_ATCOM, and converting it to CRUISE_STAY_ATCOM URL
			 url = url + "&isAtcomMc=true";
		 }
		 var paramAray = url.split("&");
		 _.each(paramAray, function(itm, ind){
			if( itm.indexOf("noOfAdults") !== -1 ){
				paramAray[ind] = "noOfAdults=" + adults;
			}
			if( itm.indexOf("noOfChildren") !== -1 ){
				paramAray[ind] = "noOfChildren=" + children;
			}
			if( itm.indexOf("childrenAge") !== -1 ){
				paramAray[ind] = "childrenAge=" + childrenAgesStr;
			}
			if( itm.indexOf("dp=") !== -1 ){
				submitButton.altFlightAirportCode && ( paramAray[ind] = "dp=" + submitButton.altFlightAirportCode );
			}
			if( typeof submitButton.stayAdded !== "undefined" ){
				if( submitButton.stayAdded ){
					if( itm.indexOf("mc=") !== -1 ){
						paramAray[ind] = "mc=" + (submitButton.tracs ? "true" : "false");
					}
					if( itm.indexOf("addAStay") !== -1 ){
						paramAray[ind] = ( "addAStay=7:CS" );
					}
					if( itm.indexOf("stayDuration") !== -1 ){
						paramAray[ind] = ( "stayDuration=7" );
					}
					if( itm.indexOf("searchVariant") !== -1 ){
						paramAray[ind] = "searchVariant=" + (submitButton.tracs ? "CRUISE_STAY_TRACS" : "CRUISE_STAY_ATCOM");
					}

					if(submitButton.tracs ){
						if(itm.indexOf("isMCTracs") !== -1 ){
							paramAray[ind] = "isMCTracs=true";
						}
					}else{
						if(itm.indexOf("isAtcomMc") !== -1 ){
							paramAray[ind] = "isAtcomMc=true";
						}
					}
				}else{
					if( itm.indexOf("mc=") !== -1 ){
						paramAray[ind] = ( "mc=false" );
					}
					if( itm.indexOf("addAStay") !== -1 ){
						paramAray[ind] = ( "addAStay=0" );
					}
					if( itm.indexOf("stayDuration") !== -1 ){
						paramAray[ind] = ( "stayDuration=0" );
					}
					if( itm.indexOf("searchVariant") !== -1 ){
						paramAray[ind] = "searchVariant=" + (submitButton.tracs ? "FLY_CRUISE_TRACS" : "FLY_CRUISE_ATCOM");
					}

					if(submitButton.tracs &&  itm.indexOf("isMCTracs") !== -1 ){
						paramAray[ind] = "isMCTracs=false";
					}else{
						if(itm.indexOf("isAtcomMc") !== -1 ){
							paramAray[ind] = "isAtcomMc=false";
						}
					}
				}
			}
		 });
		 var paramStr = paramAray.join("&");
		 var domainStr = location.href.split("?")[0];
		 location.href = domainStr + "?" + paramStr  + ( paramStr.indexOf("searchType=PaxOverlay") !== -1 ?  "" : "&searchType=PaxOverlay");

	 });

	  dojo.subscribe("tui/summaryPanel/controller/alternateFlightAdded", function (data) {
		  submitButton.altFlightAirportCode = data.selectedResult.sailings[0] ? data.selectedResult.sailings[0].outbound.departureAirportCode : false;
      });

	  dojo.subscribe("tui/summaryPanel/controller/addStay", function(data){
		  submitButton.stayAdded = true;
		  submitButton.tracs = (data.variant && data.variant.indexOf('TRACS') > -1);
	  });

	  dojo.subscribe("tui/summaryPanel/controller/removeStay", function (data){
		  submitButton.stayAdded = false;
		  submitButton.tracs = (data.variant && data.variant.indexOf('TRACS') > -1);
	  });

     on(submitButton.domNode, "click", function (event) {
    	 submitButton.changePaxRequest = true;
    	 //wait for 500 ms, and if validation fails, set changePaxRequest to false
    	 setTimeout(function(){
    		 submitButton.changePaxRequest = false;
    	 }, 1000);
    	 submitButton.searchPanelModel.submit();
     });

    },

	submit: function(){
		 var submitButton = this;

	}




  });

  return tui.searchPanel.view.ChangePaxSubmitButton;
});
