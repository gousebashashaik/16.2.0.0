define ("tui/widget/search/CookieSearchSave", ["dojo",
                                               "dojo/cookie",
                                               "tui/widget/search/SearchLoadingPopup"],
                                               function(dojo, cookie, searchLoadingPopup){
	
	dojo.declare("tui.widget.search.CookieSearchSave", null , {

        proptosavecookie: ["search-children", "ages", "childrenAges", "datewhen",
                           "numberOfRooms","searchto", "searchfrom", "totalAdultsInRoom",
                           "totalSeniorsInRoom", "totalChildrenInRoom", "totalInfantsInRoom",
                           "durationType", "selectedDepartureAirport", "departureAirportCode", "destinationCode"],

		getSaveFormData: function(name){
			var cookieSearchSave =  this;
			// delete legacy cookie
			dojo.cookie("searchComponent/savedsearch", null, {expires: -1});
			dojo.cookie("getPriceComponent/savedsearch", null, {expires: -1});
			 
			return (typeof(Storage) !== "undefined") ? localStorage[name] : cookie(name);
		},
		
        saveFormData: function(formJson){
            var cookieSearchSave =  this;
            var cookieValues = {}
            _.forEach(cookieSearchSave.proptosavecookie, function(props){
            	var formJsonVal = formJson[props];
                cookieValues[props] = formJsonVal;
            })
            cookieValues = dojo.toJson(cookieValues);
            
            try {
    			if (typeof(Storage)!== "undefined"){
					localStorage[cookieSearchSave.cookieName] = cookieValues;
    			} else {
    				cookie(cookieSearchSave.cookieName, cookieValues, { path: dojoConfig.paths.webRoot, expires: 30 });
    			}
    		} catch (e) {
	 			if (e == QUOTA_EXCEEDED_ERR) {
	 				localStorage.clear();
	 				localStorage[cookieSearchSave.cookieName] = cookieValues;
	 			}
			}

            var dummyDiv = dojo.create("div", {
                'style':{
                    'display': 'none'
                },
                'className': 'hide'
            }, dojo.body(), "last");

           var  searchPopup = new searchLoadingPopup({}, dummyDiv);

            searchPopup.open();
            dojo.publish("tui/widget/search/CookieSearchSave/saveFormData", [cookieSearchSave])
         }
	})

	return tui.widget.search.CookieSearchSave;
})