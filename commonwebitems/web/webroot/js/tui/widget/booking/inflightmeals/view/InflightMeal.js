define("tui/widget/booking/inflightmeals/view/InflightMeal", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/on",
  "dojo/parser",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/inflightmeals/view/templates/InflightMealTmpl.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/inflightmeals/view/FlightMealsOverlay",
  "tui/widget/booking/inflightmeals/view/InflightMealView",
  "dojo/_base/json"

], function (declare, query, domClass, domStyle, dom, on,parser, lang, domConstruct, InflightMealTmpl, html, Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable, FlightMealsOverlay, InflightMealView, jsonUtil) {

  return declare("tui.widget.booking.inflightmeals.view.InflightMeal",
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: InflightMealTmpl,
      templateString: "",
      widgetsInTemplate: true,
      controller: null,


      postMixInProperties: function () {
    	  
    	  this.mealsSectionDispay();

      },
      mealsSectionDispay: function(){
    	  if (!this.jsonData.packageViewData.multicomThirdPartyFlight && !this.jsonData.packageViewData.tracsThirdPartyFlight){
    		  if (this.jsonData.extraFacilityViewDataContainer.mealOptions.available == true)
    			  {
    			  	this.mealsComp = true;
    			  }else{
    				  this.mealsComp = false;
    			  }

    	  }
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        this.intializeInflightMealView();
        this.inherited(arguments);
        this.tagElements(dojo.query('.button', this.domNode),"changeMeals");
        // Tagging particular element.
        if (this.autoTag) {
          this.tagElements(query(this.targetSelector, this.domNode), 'toggle');
        }

        this.controller = dijit.registry.byId("controllerWidget");
        this.controller.registerView(this);
      },

      refresh: function (field, response) {


    		 var widget = this;
             widget.jsonData = response;
             widget.mealsSectionDispay();
             var html = widget.renderTmpl(widget.tmpl, widget);
             domConstruct.place(html, widget.domNode, "only");
             widget.intializeInflightMealView();
             parser.parse(widget.domNode);




      },

      intializeInflightMealView: function () {
    	 this.inflightMealButton = query("button.button", this.domNode);
        if (this.inflightMealButton) {
          on(this.inflightMealButton, "click", lang.hitch(this, this.handleInflightMealButton));
        }
      },

      handleInflightMealButton: function () {

        if (this.inflightMealView != null) {
          this.inflightMealView.destroyRecursive();
          this.inflightMealView = null;
          this.flightMealsOverlay.destroyRecursive();
          this.flightMealsOverlay = null;
        }
        this.inflightMealView = new InflightMealView({
          "inflightMealData": this.jsonData,
          "id": "inflight-meals"
        });
        domConstruct.place(this.inflightMealView.domNode, this.domNode, "last");
        this.flightMealsOverlay = new FlightMealsOverlay({widgetId: this.inflightMealView.id, modal: true});
        this.flightMealsOverlay.open();
      }

    });
});