define("tui/flightWhereWeFly/WhereWeFlyController",[
      "dojo",
      "dojo/ready",
      "dojo/_base/declare",
      "dojo/has",
	  "dojo/on",
	  "dojo/query",
	  "dojo/topic",
	  "dojo/_base/lang",
	  "dojo/dom-attr",
	  "dojo/dom-class",
	  "dojo/parser",
	  "dojo/dom-construct",
	  "dojo/io-query",
	  "dojo/dom",
	  "dijit/registry",
	  "dojo/text!tui/flightWhereWeFly/templates/airportListTmpl.html",
	  "tui/widget/mixins/Templatable",
	  "tui/widget/_TuiBaseWidget",
	  "tui/search/nls/Searchi18nable",
	  "tui/flightWhereWeFly/model/WhereWeFlyPanelModel",
	  "tui/searchPanel/view/flights/AirportListGrouping",
	  "dojo/domReady!"
	  ],function(dojo, ready, declare, has, on, query, topic, lang, domAtrr, domClass, parser, domConstruct, ioQuery, dom,registry,
			  airportListTmpl, Templatable, _TuiBaseWidget, Searchi18nable, WhereWeFlyPanelModel, AirportListGrouping){

	declare("tui.flightWhereWeFly.WhereWeFlyController",[Templatable,_TuiBaseWidget,Searchi18nable,WhereWeFlyPanelModel ,AirportListGrouping ],{
		/************************* properties ******************************/
		tmpl:airportListTmpl,

		seasonsList : null,

		airportList : null,

		totalDays : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],

		stopSeasonOnChange: false,


		postCreate: function(){
			var whereWeFlyController = this;
				whereWeFlyController.seasonsList = whereWeFlyController.seasonsList;
				whereWeFlyController.airportList = airportList;
				whereWeFlyController.renderAirportPanel();
				whereWeFlyController.renderSeasonsList(false);
				whereWeFlyController.attachEvents();
				query("#content").removeClass("loading");
		},

		renderAirportPanel : function(){
			var whereWeFlyController = this;

					whereWeFlyController.ukAirports = whereWeFlyController.getDealsUKAiportList(whereWeFlyController.airportList);
					whereWeFlyController.overseasAirports = whereWeFlyController.getDealsOverseasAirportList(whereWeFlyController.airportList);

					data = {
							ukAirports : whereWeFlyController.ukAirports,
							overseasAirports : whereWeFlyController.overseasAirports
				   	}

					html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, airportListTmpl));

					target = query("#airportCnt")[0];

					dojo.html.set(target,html,{
					    	parseContent: true
						});

					dojo.removeClass(query('#airportCnt')[0],"loading");


		},

		renderSeasonsList : function(checkForDisable/*boolean to check item to disable or not*/){
			var whereWeFlyController = this,seasonsList, option={}, options=[],lastIndex=0;
				if(checkForDisable == null || checkForDisable == undefined){
					checkForDisable = false;
				} else {
					whereWeFlyController.stopSeasonOnChange = true;
				}
				seasonsList = whereWeFlyController.seasonsList;
				if(registry.byId("seasonSelect").getSelectedIndex()>0){
					lastIndex = registry.byId("seasonSelect").getSelectedIndex();
				}
				_.each(seasonsList, function(season){
					option  = {
						"text"  : season.seasonStartMonth + " " + season.seasonStartYear + " - " + season.seasonEndMonth + " " + season.seasonEndYear,
						"value" : whereWeFlyController.getSelectedMonth(season),
						"disabled" : checkForDisable ? !season.seasonFlag : false
					};
					options.push(option);
				});

				registry.byId("seasonSelect").listData = options;
	    		registry.byId("seasonSelect").appendOption("<span style='font-style:italic; color:#666; font-size:13.5px;' class='disable-list-select'>All date ranges</span>", "",0);
	    		registry.byId("seasonSelect").renderList();
	    		registry.byId("seasonSelect").disableItem(0);
	    		if(lastIndex > 0) registry.byId("seasonSelect").setSelectedIndex(lastIndex);
	    		whereWeFlyController.stopSeasonOnChange = false;
		},

		getSelectedMonth : function(season){
			var whereWeFlyController = this;
				stDate = new Date(season.seasonStartMonth +" 01, "+ season.seasonStartYear);
				enDate = new Date(season.seasonEndMonth +" 01, "+ season.seasonEndYear);

				enDate.setDate(parseInt(whereWeFlyController.totalDays[enDate.getMonth()], 10));

				return whereWeFlyController.formatDate(stDate) +"," +whereWeFlyController.formatDate(enDate);


		},

		formatDate: function (date) {
		      var whereWeFlyController = this;
		      return dojo.date.locale.format(date, {
		        selector: "date",
		        datePattern: "yyyy-MM-dd"
		      });
		},

		attachEvents : function(){
			 var whereWeFlyController = this,
			 	flyfromObj = dijit.byId("WWFFlyFrom");

				 query(".country-airport-tabs .tabs li").on("click",function(event){
					 query(".country-airport-tabs .tabs li").removeClass("active");
					 tabId = event.target.parentNode.id;

				 	if(tabId === "UK_Cnt"){
				 		query("#UK_Cnt").addClass("active");
				 		query("#ukAirports-content").addClass("show");
				 		query("#overseasAirports-content").removeClass("show");
				 	}else if(tabId === "Overseas_Cnt"){
				 		query("#Overseas_Cnt").addClass("active");
				 		query("#overseasAirports-content").addClass("show");
				 		query("#ukAirports-content").removeClass("show");
				 	}

				 });

			 on(dijit.byId("seasonSelect"),"change", function(){
				 if(whereWeFlyController.stopSeasonOnChange) return;
				 query(".clear-filter").removeClass("disabled");
				 if(this.getSelectedIndex() !== 0){
					 whereWeFlyController.getValidAirportData();
				 }
			 });
			/* on(dijit.byId("WWFFlyFrom").autocomplete,"change", function(){
				 query(".clear-filter").removeClass("disabled");
				 if(this.getSelectedData() !== null){
					 whereWeFlyController.getValidAirportData();
				 }
			 });*/
			on(dom.byId("clear-filter"),"click",function(evt){
				 if(domClass.contains(this,"disabled")) return;
				 whereWeFlyController.resetAllValues(evt);
			 })
		},

		getValidAirportData : function(){
			var whereWeFlyController = this,
				flyFrom,
				flyFromValue,
				seasonValue,
				params,
				startDate,
				endDate,
				mapComp;

				flyFrom = dijit.byId("WWFFlyFrom").autocomplete;
				season = dijit.byId("seasonSelect");

				if(season.getSelectedIndex() !== 0){
					seasonValue =  season.getSelectedData().value.split(",");
					startDate 	=  seasonValue[0];
					endDate		=  seasonValue[1];
				}
				if(flyFrom.getSelectedData() !== null){
					flyFromValue = flyFrom.getSelectedData().value;
				}

				params ={
						"from[]"  		: flyFromValue,
						"startDate" 	: startDate,
						"endDate"		: endDate
				}
				whereWeFlyController.reloadMapWithFilterValues(params);
		},

		resetAllValues : function(evt){
			var whereWeFlyController = this,
				mapComp,
				alldestinations = query(".alldestinations")[0];

				mapComp = dijit.byId("foMapComponent");
				mapComp.showAllAirports(alldestinations);
				dijit.byId("seasonSelect").setSelectedIndex(0);
				dijit.byId("WWFFlyFrom").reset();
				dijit.byId("WWFFlyFrom").autocomplete.onChange();
				dojo.addClass(evt.target,"disabled");

				whereWeFlyController.reloadMapWithFilterValues(null);

		},

		reloadMapWithFilterValues : function(params){
			var whereWeFlyController = this,
				targetUrl = dojoConfig.paths.webRoot+"/ws/filtermap?";

				if(params && params !== null){
					targetUrl = targetUrl+ ioQuery.objectToQuery(params)
				}

				query("#content").addClass("loading");

				results = dojo.xhr("GET", {
		  			url: targetUrl,
		  			handleAs: "json"
		  		});

				 dojo.when(results, function (mapResult) {
					 mapComp = dijit.byId("foMapComponent");
					 mapComp.showAvailableAiports(mapResult);
					 query("#content").removeClass("loading");
				 });

		}





	});
	return tui.flightWhereWeFly.WhereWeFlyController;

})