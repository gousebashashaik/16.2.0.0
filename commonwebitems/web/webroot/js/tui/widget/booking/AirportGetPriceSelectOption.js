define ("tui/widget/booking/AirportGetPriceSelectOption", ["dojo",
                                                           "dojo/date/locale",
                                                           "dojo/cookie",
                                                           "tui/widget/form/SelectOption",
                                                           "tui/widget/search/CookieSearchSave"], function(dojo, locale, cookie){
	
	dojo.declare("tui.widget.booking.AirportGetPriceSelectOption", [tui.widget.form.SelectOption, tui.widget.search.CookieSearchSave], {

		targetUrl: "/fcsun/page/common/search/freetextaccomsearchpanelupdate.page?selectedDate=",
		
		fixedWidth: true,
										
		postCreate: function(){
			var airportGetPriceSelectOption = this;
            airportGetPriceSelectOption.parentWidget = airportGetPriceSelectOption.getParent();
            airportGetPriceSelectOption.inherited(arguments);
            airportGetPriceSelectOption.setDefaultFormValues();

            // subscribe to tui/widget/GetPriceDatePicker/onSelectedDate channel
			airportGetPriceSelectOption.subscribe("tui/widget/GetPriceDatePicker/onSelectedDate", function(date){
				var date = locale.format(date, {selector: "date", datePattern: "d/MM/yyyy"});
				var resortCodeField = tui.getFormElementByName("resortCode", airportGetPriceSelectOption.parentWidget.domNode);
				var url = [airportGetPriceSelectOption.targetUrl, date ,"&resortId=", resortCodeField.value].join("");
				airportGetPriceSelectOption.getAirportPortList(url);
			});

            airportGetPriceSelectOption.connect(airportGetPriceSelectOption, "onChange", function(name, oldValue, newvalue){
                airportGetPriceSelectOption.setFormValues(newvalue.value);
            });
		},

        setDefaultFormValues: function(){
            var airportGetPriceSelectOption = this;
            var cookiename = airportGetPriceSelectOption.parentWidget.cookieName;
            var savedsearch = airportGetPriceSelectOption.getSaveFormData(cookiename);
            if (savedsearch){
                savedsearch = dojo.fromJson(savedsearch);
            } else{
                var selectedData = airportGetPriceSelectOption.getSelectedData();
                airportGetPriceSelectOption.setFormValues(selectedData.value);
            }
        },
		
		getAirportPortList: function(url){
			var airportGetPriceSelectOption = this;
			var results = dojo.xhr("GET", {
				url: url,
				handleAs: "json",
				headers: {Accept: "application/javascript, application/json"}
			});
            airportGetPriceSelectOption._selindex = airportGetPriceSelectOption.getSelectedIndex();
			dojo.when(results, function(dataresults){
                airportGetPriceSelectOption.buildListData(dataresults);
			})
		},

        parseJsonData: function(jsonData){
            var airportGetPriceSelectOption = this;
            jsonData.forEach(function(item){
                airportGetPriceSelectOption.listData.push({
                    text: item.nm,
                    value: item.cd
                })
            })
        },

        setFormValues: function(airportValue){
            var airportGetPriceSelectOption = this;
            tui.setFormElementByName("departureAirportCode", airportGetPriceSelectOption.parentWidget.domNode, airportValue);
        }
	})
	
	return tui.widget.booking.AirportGetPriceSelectOption;
})