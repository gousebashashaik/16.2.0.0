define ("tui/widget/search/AirportAutocomplete", ["dojo", 
												"tui/utils/SimpleQueryEngine",
												"dojo/text!tui/widget/Templates/Search/DestinationNoMatchTmpl.html",
												"dojo/json",
												"dojo/date/locale",
												"dojo/topic",
												"dojo/html",
												"tui/widget/search/CookieSearchSave"], function(dojo, tuiSimpleQueryEngine, destinationNoMatchTmpl, json){


	dojo.declare("tui.widget.search.AirportAutocomplete", [tui.widget.form.AutoComplete, tui.widget.search.CookieSearchSave], {
		
		charNo: 3,
					
		targetURL: "/fcsun/page/common/search/freetextsearchpanelupdate.page",
		
		queryEngine: tuiSimpleQueryEngine,
		
		titleProp: "nm",
		
		valueProp: "cd",
		
		idProperty: "cd",
		
		searchProperty: "nm",
		
		cacheonly: true,
		
		appendResults: false,
		
		datePattern: "d MMMM yyyy",
		
		date: null,
		
		parentWidget: null,
				
		postCreate: function() {
			// summary:
			//		Method is called once the autocomplete compoment has been created.
			//		We subscribe the airport autocomplete to the "tui/widget/SearchDatePicker/onSelectedDate"
			//		to listen for any changes to 
			var airportAutocomplete = this;
			airportAutocomplete.inherited(arguments);
			airportAutocomplete.parentWidget = airportAutocomplete.getParent();
					
			var datewhen = tui.getFormElementByName("datewhen", airportAutocomplete.parentWidget.domNode);
			airportAutocomplete.date = dojo.date.locale.parse(datewhen.value, {selector: "date", datePattern: airportAutocomplete.datePattern});			
			airportAutocomplete.setData(airportAutocomplete.jsonData);

			// Reset airport related details from cookie.
			var savedsearch = airportAutocomplete.getSaveFormData("sc/ss");
			if (savedsearch) {
				savedsearch = dojo.fromJson(savedsearch);
				airportAutocomplete.setSelectedValue(savedsearch.departureAirportCode);
			} else {
				airportAutocomplete.setFormFieldValues();
			} 
			
			// subscribe to channels for events.
			airportAutocomplete.subscribe("tui/widget/SearchDatePicker/onSelectedDate", function (date) {
				airportAutocomplete.onSelectedDate(date);
			})
			
			airportAutocomplete.subscribe("tui/widget/search/AirportGuideExpandable/selected", function(value){
				airportAutocomplete.setSelectedValue(value);
			});
    	},
    	
    	isSelectedAirportAvailable: function(airportData, data){
    		var airportAutocomplete = this;
    		var  answer = false;
    		for (var i = 0; i < data.length; i++){
    			if(data[i][airportAutocomplete.valueProp] === airportData.listData[airportAutocomplete.valueProp]){
    				answer = true;
    				break;
    			}
    		}
    		return answer;
    	},
    	
    	createQueryObject: function(element, keycode){
    		var airportAutocomplete = this;
    		var queryObj = airportAutocomplete.inherited(arguments);
    		queryObj["cd"] = element.value;
    		return queryObj;
    	},
    	 
    	onSelectedDate: function(/*Date*/ date){
    		// summary:
			//		OnSelectDate methods subscribe to the  channel "tui/widget/SearchDatePicker/onSelectedDate". 
			//		Any changes on this channel, and this method is excuted.
			//
			//		With the date passed from the given channel, we request all airport which we have fly from on
			//		that date.
			//      With the returned airport list, we first check if selected airport in the autcompete field (if one exists)
			//		exists in the airport list. If not we reset the autocompelete field, departureAirportCode field on form.
			//		Also we need to alert use of the change.
			// 		If airport exist, in new list. We then need to update the destination autocompelete list data, as the
			//		date and airport combination has changed.   
    		var airportAutocomplete = this;
    		airportAutocomplete.date = date;
    		date = dojo.date.locale.format(date, {selector: "date", datePattern: "d/MM/yyyy"});
    		var key = "airport-" + date.replace(/\//g,'');
    		var results =  (typeof(Storage) !== "undefined" && sessionStorage[key]) ?
    				dojo.fromJson(sessionStorage[key]) : airportAutocomplete.requestJson(["?selectedDate=", date].join(""));
    				
    		dojo.when(results, function(dataresults){
    			
    			var selectedAirport = airportAutocomplete.getSelectedData();
    			
    			try {
    				if (typeof(Storage)!== "undefined"){ 
    					if (dataresults != null){
    						sessionStorage[key] = json.stringify(dataresults);
    					}
    				}
    			} catch (e) {
	 				if (e == QUOTA_EXCEEDED_ERR) 
	 					sessionStorage.clear()
				}
				
    			airportAutocomplete.setData(dataresults);
    			
    			dojo.publish("tui/widget/search/AirportAutocomplete/airportUpdate", [dataresults]);	
    			
    			if (selectedAirport){
					var answer = airportAutocomplete._cacheStore.get(selectedAirport.value);
					if(!answer){
						var flightDate = dojo.date.locale.format(airportAutocomplete.date, {selector: "date", datePattern: "d MMMM yyyy"})
						var errorMessage = dojo.attr(airportAutocomplete.domNode, "data-error-dateNoAirport");
    					tui.showDefaultSearchErrorPopup(airportAutocomplete.domNode, dojo.string.substitute(errorMessage,[airportAutocomplete.domNode.value, flightDate]));
						airportAutocomplete.unSelect();
					} else {
						dojo.publish("tui/widget/AirportAutocomplete/onChange", [null, null, selectedAirport, airportAutocomplete.date]);	
					}
				}
			})
    	},
    	
    	unSelect: function(){
    		var airportAutocomplete = this;
    		airportAutocomplete.domNode.value = "";
            dojo.publish("tui/widget/search/AirportAutocomplete/unSelect");
    		airportAutocomplete.inherited(arguments);
    	},
    	
    	onChange: function(name, oldValue, value){
    		// summary:
			//		Method is called when a value change has occured on the autocomplete.
			//		We first set the departureAirportCode hidden field on the form, and then
			//		publish resuls to channel "tui/widget/AirportAutocomplete/onChange" for listening subscribers.
			
    		var airportAutocomplete = this;


    		if (value.listData.cd === "airportguide") {
    			// if we have selected the airport guide,
    			// so just wiew it the airport guide. 
    			airportAutocomplete.unSelect();
    			airportAutocomplete.airportGuideSelected();
    			return;
    		}
    		airportAutocomplete.inherited(arguments);
    		
    		// set airport code on form to the appropriate fields
    		var airportCode = (value) ? value.listData.cd : "";
    		airportAutocomplete.setFormFieldValues(airportCode);
    		dojo.publish("tui/widget/AirportAutocomplete/onChange", [name, oldValue, value, airportAutocomplete.date]);
    	},
    	
    	setFormFieldValues: function(airportCode){
    		var airportAutocomplete = this;
    		
    		var departureAirportCode = tui.getFormElementByName("departureAirportCode", airportAutocomplete.parentWidget.domNode);
    		var selectedDepartureAirport = tui.getFormElementByName("selectedDepartureAirport", airportAutocomplete.parentWidget.domNode);
    		
    		departureAirportCode.value = "";
    		selectedDepartureAirport.value = "";
    		
    		if (airportCode){
    			departureAirportCode.value = airportCode;
    			selectedDepartureAirport.value = airportCode;
    		}
    	},
    	
    	airportGuideSelected: function(){
    		dojo.publish("tui/widget/search/AirportAutocomplete/airportguide");
    	},
    	
    	onNoResults: function(listElementUL){
			var airportAutocomplete = this;
			var noNoMatch = dojo.html.set(listElementUL, airportAutocomplete.createNoMatchMessage());
			var airportguide = dojo.query(".airportguide-ac", noNoMatch)[0];
			airportAutocomplete.noMatchConnect = airportAutocomplete.connect(airportguide, "onclick", function(){
				airportAutocomplete.airportGuideSelected();
			})
		},
		
		onResults: function(data){
			// summary:
			//		Method is overriden from AutoCompleteable, so we can add airport guide option in list.
			var airportAutocomplete = this;
			if (airportAutocomplete.noMatchConnect){
				airportAutocomplete.disconnect(airportAutocomplete.noMatchConnect);
			}
    		if (data && data.length > 0){
    			var airportguide = {};
    			airportguide[airportAutocomplete.titleProp] = "Airport guide";
    			airportguide[airportAutocomplete.valueProp] = "airportguide";
    			data.push(airportguide);
    		}
    		airportAutocomplete.inherited(arguments);
		},
		
		createNoMatchMessage: function(){
			var airportAutocomplete = this;
			var context = {
				templateName: "airportNoMatch"
			}
			
			airportAutocomplete.noMatchMessage = airportAutocomplete.createTmpl(context);
			return  airportAutocomplete.noMatchMessage;
		},
		
		createTmpl: function(context){
			var template = new dojox.dtl.Template(destinationNoMatchTmpl);
			context = new dojox.dtl.Context(context);
			return template.render(context);
		}
	})

	return tui.widget.search.AirportAutocomplete;
})