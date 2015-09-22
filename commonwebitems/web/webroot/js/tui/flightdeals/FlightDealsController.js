define("tui/flightdeals/FlightDealsController",[
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
	  "dojo/dom-construct",
	  "tui/widget/form/flights/DealsExpandable",
	  "tui/flightdeals/FlyFromDealsExpandable",
	  "tui/flightdeals/GoToDealsExpandable",
	  "dojo/text!tui/flightdeals/templates/flightDealsSearchPanelTmpl.html",
	  "dojo/text!tui/flightdeals/templates/flightDealsSearchResultsTmpl.html",
	  "tui/widget/mixins/Templatable",
	  "tui/widget/_TuiBaseWidget",
	  "tui/search/nls/Searchi18nable",
	  "tui/widget/pagination/Pagination",
	  "dijit/form/CheckBox",
	  "dijit/form/RadioButton",
	  "tui/widget/form/SelectOption",
	  "tui/widget/form/flights/DealsTitlePane",
	  "tui/mvc/Controller",
	  "dojo/NodeList-traverse"

	  ],function(dojo, declare, has, on, query, topic, lang, domAtrr, domClass, parser, domConstruct,
			  DealsExpandable,FlyFromDealsExpandable,GoToDealsExpandable, flightDealsSearchPanelTmpl, flightDealsSearchResultsTmpl, Templatable,
			  _TuiBaseWidget, Searchi18nable){

	declare("tui.flightdeals.FlightDealscontroller",[Templatable,_TuiBaseWidget,Searchi18nable],{
		/************************* properties ******************************/
		tmpl:flightDealsSearchPanelTmpl,

		/*************************** Properties End *****************************/


		/*************************** Methods ************************************/


		constructor: function(){
			var FlightDealsController = this;
			console.log("Deals controller loaded");
		},
		postCreate: function(){
			var FlightDealsController = this;
			FlightDealsController.renderDealsSearchPanel();
			FlightDealsController.renderDealsSearchResults();
		},
		//place deals search panel component
		renderDealsSearchPanel: function(){
			var FlightDealsController = this;
			var targetNode = query("#dealSearchPanelContainer",FlightDealsController.domNode)[0];
			dojo.html.set(targetNode,FlightDealsController.tmpl,{
				parseContent: true
			});
		},
		//render deals search results
		renderDealsSearchResults: function(){
			var FlightDealsController = this;
			var targetNode = query("#dealSearchResultsContainer",FlightDealsController.domNode)[0];
			dojo.html.set(targetNode,flightDealsSearchResultsTmpl,{
				parseContent: true
			});
		}
		/********* Methods End*********/
	});
	return tui.flightdeals.FlightDealscontroller;

})