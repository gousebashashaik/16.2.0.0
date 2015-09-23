define("tui/flights/status/FlightStatusController",[
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
	"dojo/text!tui/flights/status/templates/flightStatusPage.html",
	//"tui/widget/form/flights/FlightStatusQueryServiceStore",
	"dojo/data/ItemFileReadStore",
	"dojo/io-query",
	"tui/widget/form/SelectOption",
	"tui/flights/status/FlightNumber",
	"tui/flights/status/StatusFilteringSelect",
	"tui/flights/status/FlyToAutoSuggest",
	"tui/flights/status/AutoSuggest",
	"tui/flights/status/FlightNumAutoSuggest"
      ],function(dojo, declare, has, on, query, topic, lang, domAtrr, domClass, parser,registry,domConstruct,Templatable,
    		  _TuiBaseWidget, Searchi18nable,flightTimeTableSearchPanelTmpl,ItemFileReadStore,ioQuery,SelectOption,FlightNumber){

	 declare("tui.flights.status.FlightStatusController",[Templatable,_TuiBaseWidget,Searchi18nable,FlightNumber],{



		 /************** Methods **********************/


		postCreate: function(){
			var FlightStatusController = this;
			FlightStatusController.renderTimeTableSearchPanel();
		//	FlightStatusController.createAndAttachStores();
			FlightStatusController.attachEvents();
		},

		renderTimeTableSearchPanel: function(){
			var FlightStatusController = this,
			targetNode = query("#flightStatusSearchPanel",FlightStatusController.domNode)[0];
			dojo.html.set(targetNode,flightTimeTableSearchPanelTmpl,{
				parseContent: true
			});
		},
		createAndAttachStores: function(){
			var FlightStatusController = this;
			FlightStatusController.setStoreToFlightNo();
			//FlightStatusController.setStoreToDepAirport();
		//	FlightStatusController.setStoreToArrAirport();
		},
		getSelectedFlightNo: function(){
			var widget = dijit.byId("flightNumber");
			var value = "";
			if(widget.item != null){
				value = widget.item !== undefined ? widget.item.flightnr[0].replace(" ","") : "";
			} else {
				value = "";
			}
			return value;
		},
		getSelectedDepAirport: function(){
			var widget = dijit.byId("flyFromStat");
			var value = "";
			if(widget.autocomplete.getSelectedData() != null){
				value = widget.autocomplete.getSelectedData().value;
			} else {
				value = "";
			}
			return value;
		},
		getSelectedArrAirport: function(){
			var widget = dijit.byId("flyToStat");
			var value = "";
			if(widget.autocomplete.getSelectedData() != null){
				value = widget.autocomplete.getSelectedData().value
			} else {
				value = "";
			}
			return value;
		},

		setStoreToFlightNo: function(){
			var FlightStatusController = this;

			var flightNoStore = new ItemFileReadStore({
				url:"./flightNumberAutoSuggestData?arrAirPortCode="+ FlightStatusController.getSelectedArrAirport() +"&depAirPortCode="+FlightStatusController.getSelectedDepAirport()

			});
			registry.byId("flightNumber").set('store',flightNoStore);
		},/*
		setStoreToDepAirport: function(){
			var FlightStatusController = this;

			var flyFromStore = new ItemFileReadStore({
				url:"./departureAutoSuggestData?arrAirPortCode="+ FlightStatusController.getSelectedArrAirport() + "&flightNumber="+ FlightStatusController.getSelectedFlightNo()

			});
			registry.byId("flyFromStat").set('store',flyFromStore);
		},
		setStoreToArrAirport: function(){
			var FlightStatusController = this;
			var flyToStore = new ItemFileReadStore({
				url:"./arrivalAutoSuggestData?depAirPortCode="+ FlightStatusController.getSelectedDepAirport() + "&flightNumber="+ FlightStatusController.getSelectedFlightNo()
			});

			registry.byId("flyToStat").set('store',flyToStore);
		},*/
		attachEvents: function(){

			var FlightStatusController = this,
				FlightNumNode = dijit.byId("flightNumber"),
				FlyFromNode = dijit.byId("flyFromStat"),
				FlyToNode = dijit.byId("flyToStat"),
				AllDep = dojo.byId("allDeps"),
				AllArriv = dojo.byId("allArvs");
			on(FlightNumNode.autocomplete,"change",function(){
				if(this.getSelectedData() == null) return;
				FlightStatusController.resetShowAll();
				FlightStatusController.searchTypes(this.domNode.parentNode.parentNode.id);
			});


			on(FlyFromNode.autocomplete,"change",function(){
				if(this.getSelectedData() == null) return;
				FlightStatusController.resetShowAll();
				FlightStatusController.searchTypes(this.domNode.parentNode.parentNode.id);
			});

			on(FlyToNode.autocomplete,"change",function(){
				if(this.getSelectedData() == null) return;
				FlightStatusController.resetShowAll();
				FlightStatusController.searchTypes(this.domNode.parentNode.parentNode.id);
			});

			/*//onFocus update the store with service call;
			on(FlyFromNode,"focus",function(){
				FlightStatusController.setStoreToDepAirport();
			});

			on(FlyToNode,"focus",function(){
				FlightStatusController.setStoreToArrAirport();
			});*/

			on(FlightNumNode,"focus",function(){
				FlightStatusController.setStoreToFlightNo();
			});


			on(AllDep,"click",function(){
				FlightStatusController.enableShowAllDep();
				FlightStatusController.callJson("allDepartures", this.id);
			});

			on(AllArriv,"click",function(){
				FlightStatusController.enableShowAllArriv();
				FlightStatusController.callJson("allArrivals", this.id);
			});

		},

		enableShowAllDep:function(){
			dijit.byId("flyFromStat").reset();//reset Flyfrom placeholder
			dijit.byId("flyToStat").reset();//reset To placeholder
			dijit.byId("flightNumber").reset();

			dojo.setStyle(dojo.query("#allDeps")[0], "color", "black");
			dojo.setStyle(dojo.query("#allArvs")[0], "color", "#73afdc");
			if(dojo.isIE){
				dojo.query(".flightImg.flightShowDeptIcon")[0].style.backgroundPosition = "-10px -135px";
				dojo.query(".flightImg.flightShowArvlIcon")[0].style.backgroundPosition = "-10px -156px";

			}else{
				dojo.setStyle(dojo.query(".flightImg.flightShowDeptIcon")[0], "background-position", "-10px -135px");
				dojo.setStyle(dojo.query(".flightImg.flightShowArvlIcon")[0], "background-position", "-10px -156px");

			}
		},

		enableShowAllArriv:function(){
			dijit.byId("flyToStat").reset();//reset To placeholder
			dijit.byId("flyFromStat").reset();;//reset Flyfrom placeholder
			dijit.byId("flightNumber").reset();

			dojo.setStyle(dojo.query("#allArvs")[0], "color", "black");
			dojo.setStyle(dojo.query("#allDeps")[0], "color", "#73afdc");
			if(dojo.isIE){
				dojo.query(".flightImg.flightShowArvlIcon")[0].style.backgroundPosition = "-10px -178px";
				dojo.query(".flightImg.flightShowDeptIcon")[0].style.backgroundPosition = "-10px -114px";
			}else{
				dojo.setStyle(dojo.query(".flightImg.flightShowArvlIcon")[0], "background-position", "-10px -178px");
				dojo.setStyle(dojo.query(".flightImg.flightShowDeptIcon")[0], "background-position", "-10px -114px");

			}

		},


		resetShowAll:function(){
			dojo.setStyle(dojo.query(".flightImg.flightShowArvlIcon")[0], "background-position", "-10px -156px");
			dojo.setStyle(dojo.query("#allArvs")[0], "color", "#73afdc");
			dojo.setStyle(dojo.query("#allDeps")[0], "color", "#73afdc");
			dojo.setStyle(dojo.query(".flightImg.flightShowDeptIcon")[0], "background-position", "-10px -114px");
		},


		searchTypes: function(targetId){

			var FlightStatusController = this, flightNumVal, flyFromVal, flyToVal;

			flightNumVal = dijit.byId("flightNumber").getLabel().replace(" ","");

			flyFromVal = dijit.byId("flyFromStat").getValue();

			flyToVal = dijit.byId("flyToStat").getValue();

			/*if(flightNumVal.toLowerCase() != "tom") {
				  if (flightNumVal.indexOf( "tom" ) > -1 ) {
					  flightNumVal = flightNumVal.substring(3);
				  }
			}*/

			//flightNumVal = "TOM"+flightNumVal;




			if(flyToVal.length!=0) flyToVal = flyToVal;
			else flyToVal = "nodata";

			if(flyFromVal.length!=0) flyFromVal = flyFromVal;
			else flyFromVal = "nodata";

			if(flightNumVal.length <= 3 ) flightNumVal = "nodata";


			if(flightNumVal=="nodata" && flyToVal=="nodata" && flyFromVal != "nodata"){//alert("searchByDeparture")
				url="searchByDeparture?depAirPortCode="+flyFromVal;
			}
			else if(flightNumVal=="nodata" && flyFromVal=="nodata" && flyToVal!="nodata"){//alert("searchByArrival")
				url = "searchByArrival?arrAirPortCode="+flyToVal;
			}
			else if(flyToVal=="nodata" && flyFromVal=="nodata" && flightNumVal!="nodata"){//alert("searchByFlightNumber")
				url="searchByFlightNumber?flightCode="+flightNumVal;
			}
			else if(flightNumVal=="nodata" && flyToVal=="nodata" && flyFromVal == "nodata"){//alert("none")
				return;
			}
			else{
				url="searchByAll?flightCode="+flightNumVal+"&departFrom="+flyFromVal+"&arriveTo="+flyToVal+"&event="+targetId;
			}

			FlightStatusController.callJson(url,targetId);

		}


		/************** Methods **********************/
	});

	 return tui.flights.status.FlightStatusController;
});