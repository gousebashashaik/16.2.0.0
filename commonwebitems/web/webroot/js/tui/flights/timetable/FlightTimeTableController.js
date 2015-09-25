define("tui/flights/timetable/FlightTimeTableController",[
	"dojo",
	"dojo/_base/declare",
	"dojo/has",
	"dojo/on",
	'dojo/query',
	"dojo/topic",
	"dojo/_base/lang",
	"dojo/dom-attr",
	"dojo/dom-class",
	"dojo/parser",
	"dijit/registry",
	"dojo/dom-construct",
	"tui/widget/mixins/Templatable",
	"tui/widget/_TuiBaseWidget",
	"tui/search/nls/Searchi18nable",
	"dojo/text!tui/flights/timetable/templates/flightTimeTableSearchPanel.html",
	"tui/widget/form/flights/QueryServiceStore",
	//"dojox/data/QueryReadStore",
	"dojo/io-query",
	"tui/widget/form/SelectOption",
	"tui/flights/widget/MonthPullDown",
	"tui/flights/timetable/TimeTableFlyToAutoSuggest",
	"tui/flights/timetable/TimeTableFlyFromAutoSuggest"
      ],function(dojo, declare, has, on, query, topic, lang, domAtrr, domClass, parser,registry,domConstruct,Templatable,
    		  _TuiBaseWidget, Searchi18nable,flightTimeTableSearchPanelTmpl,QueryServiceStore,ioQuery,SelectOption,MonthPullDown){

	 declare("tui.flights.timetable.FlightTimeTableController",[Templatable,_TuiBaseWidget,Searchi18nable,MonthPullDown],{

		 /************** Methods **********************/
		 seasonLength: dojo.global.seasonLength,

		postCreate: function(){
			var FlightTimeTableController = this;
			//FlightTimeTableController.addDisableMode(dojo.byId('searchPanelSubtle'));
			FlightTimeTableController.renderTimeTableSearchPanel();
			FlightTimeTableController.attachEvents();
		},

		renderTimeTableSearchPanel: function(){
			var FlightTimeTableController = this, flyingWhen = null;
			var targetNode = query("#timeTableSearchPanel",FlightTimeTableController.domNode)[0];
				dojo.html.set(targetNode,flightTimeTableSearchPanelTmpl,{
					parseContent: true
				});
				//Getting the option from search panel monthsAndYears and created the options
				flyingWhen = registry.byId("whenTimeTable");
				flyingWhen.listData = dojo.global.monthsAndYears;
				flyingWhen.appendOption("<span class='disable-list-select'>Select a month</span>", "",0);
				flyingWhen.renderList();
				flyingWhen.disableItem(0);
		},

		attachEvents: function(){
			var FlightTimeTableController = this,
				flyFromAirport = registry.byId("timeTableFlyFrom"),
				flyToAirport = registry.byId("timeTableFlyTo"),
				flyingMonth = registry.byId("whenTimeTable"),
				searchPanelSubtle = dojo.byId("searchPanelSubtle"),
				resultPane= dojo.byId('timetable-results-panel'),
				timeTableSearchPanel = dojo.byId("timeTableSearchPanel");
				on(flyFromAirport.autocomplete,"ElementListSelection",function(){
					FlightTimeTableController.enableSearch();
					FlightTimeTableController.renderMonthPullDown();
				});
				on(flyToAirport.autocomplete,"ElementListSelection",function(){
					FlightTimeTableController.enableSearch();
					FlightTimeTableController.renderMonthPullDown();
				});
				on(flyingMonth, "change", function(evt){
					FlightTimeTableController.enableSearch();
					var selectedMonth = this.getSelectedData().value;
					if(dojo.byId("ftselectedMonth"))
			    		{
			    			domConstruct.destroy("ftselectedMonth");
			    		}
			    		var hid = domConstruct.create("input", {
			    			type:"hidden",
			    			value:selectedMonth.split("/").join(":"),
			    			id:"ftselectedMonth"
			    		});
			    		domConstruct.place(hid, document.body, "last");
				});
				on(timeTableSearchPanel,"click",function(evt){
					FlightTimeTableController.enableTimeTablePanel();
				});
				on(resultPane,"click",function(evt){
					if(dojo.style(query(".flight-results")[0],"display") == "block"){
						FlightTimeTableController.enableTimeTablePanel();
					}
			    });
				/*on(searchPanelSubtle,"click",function(){
					if(query(".section-overlay",timeTableSearchPanel).length==0){
						domClass.add(searchPanelSubtle,"search controller open");
				 		query(".section-overlay",searchPanelSubtle).remove();
				 		domClass.remove(searchPanelSubtle,"disableAll")
				 		FlightTimeTableController.addDisableMode(timeTableSearchPanel);
				 		if(dojo.style(query(".flight-results")[0],"display") == "block"){
				 			FlightTimeTableController.addDisableMode(resultPane);
				 		}
					}
				});*/
		},
		enableSearch: function(){
			var FlightTimeTableController = this,
			flyFromAirport = registry.byId("timeTableFlyFrom"),
			flyToAirport = registry.byId("timeTableFlyTo"),
			flyingMonth = registry.byId("whenTimeTable");
				var searchButton = query("input[type='submit']",FlightTimeTableController.domNode)[0];
				if(flyFromAirport.getValue() == "" || flyToAirport.getValue() == "" || flyingMonth.getSelectedData().value === ""){
					dojo.removeClass(searchButton,"cta")
					dojo.addClass(searchButton,"disabled");
				}else{
					dojo.addClass(searchButton,"cta");
					dojo.removeClass(searchButton,"disabled");
				}
		},
		 enableTimeTablePanelForIE8: function(){
	    	  if(has("ie") < 9) {
	    		  if(query(".timetable-results-panel .section-overlay").length > 0 ){
		    		  domClass.remove(query(".timetable-results-panel")[0],"disableAll");
						query(".section-overlay",dojo.byId("timetable-results-panel")).remove();
						var element = domConstruct.create("div",{"class":"section-overlay"},dojo.byId("searchPanelSubtle"),"first");
						 domClass.add(dojo.byId("searchPanelSubtle"),"disableAll")
		    		  return true;
		    	  }
	    	  }
	    	  return false;
	      },
/*	      addDisableMode: function(node){
	    	  var FlightTimeTableController = this,
	    	  	  element = domConstruct.create("div",{"class":"section-overlay"},node,"first");
				 domClass.add(node,"disableAll");

		 },*/
		 enableTimeTablePanel: function(){
			 var FlightTimeTableController = this,
			     searchPanelSubtle = dojo.byId("searchPanelSubtle"),
				 resultPane= dojo.byId('timetable-results-panel'),
				 timeTableSearchPanel = dojo.byId("timeTableSearchPanel");
				if(query(".section-overlay",searchPanelSubtle).length==0){
					domClass.remove(timeTableSearchPanel,"disableAll");
					query(".section-overlay",timeTableSearchPanel).remove();
					//FlightTimeTableController.addDisableMode(searchPanelSubtle);
					//FlightTimeTableController.resetErrorPopUp();
					if(dojo.style(query(".flight-results")[0],"display") == "block"){
			 			query(".section-overlay", resultPane).remove();
			 			domClass.remove(resultPane,"disableAll");
			 		}
				}
			}

		/************** Methods **********************/
	});

	 return tui.flights.timetable.FlightTimeTableController;
});