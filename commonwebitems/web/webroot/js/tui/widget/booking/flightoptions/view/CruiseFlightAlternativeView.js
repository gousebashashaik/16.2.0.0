define("tui/widget/booking/flightoptions/view/CruiseFlightAlternativeView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/parser",
  "dojo/keys",
  "dojo/_base/lang",
  "dojo/_base/array",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/flightoptions/view/templates/CruiseFlightAlternativeViewTmpl.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, dom,domAttr, domConstruct, domClass, Evented, topic, parser,keys,lang, array,domStyle, _TuiBaseWidget, dtlTemplate, Templatable, CruiseFlightAlternativeViewTmpl,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.flightoptions.view.CruiseFlightAlternativeView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: CruiseFlightAlternativeViewTmpl,
    templateString: "",
    widgetsInTemplate: true,
    childAgeFlag: false,
    childMealAvailable: false,
    adultorseniorFlag: "",
    Flight_MoreAirports_Link: null,
    airportChoices: [],
    airportData: null,
    flightCalData: [],
    cruiseflightviewData: [],
    alternativeFlightsFlag:false,

    postMixInProperties: function () {
    	 var widget = this;
    	 widget.flightAirport = widget.model;
    	_.each(widget.flightAirport,  function(item){
    		if(item.selected === true && !_.isEmpty(item.flightViewData)){
    			widget.alternativeFlightsFlag = true;
    		}
    	});
    	//console.log(widget.alternativeFlightsFlag);
    	// this.inherited(arguments);
    },

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
    	var widget = this;
    	this.inherited(arguments);

    }

  });
});