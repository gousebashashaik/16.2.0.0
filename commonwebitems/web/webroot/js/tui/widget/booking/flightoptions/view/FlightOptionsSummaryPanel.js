define("tui/widget/booking/flightoptions/view/FlightOptionsSummaryPanel", [
    "dojo/topic",
    "dojo/_base/declare",
    "dojo/query",
    "dojo/_base/lang",
    "dojo/dom-attr",
    "dojo/date",
    "dojo/dom-class",
    "dojo/_base/array",
    "dojo/on",
    "dojo/dom-construct",
    "dojo/text!tui/widget/booking/flightoptions/view/templates/FlightOptionsSummaryPanel.html",
    "tui/widget/_TuiBaseWidget",
    "dojox/dtl/_Templated",
    "tui/widget/mixins/Templatable",
    "dojo/Evented"
], function (topic, declare, query, lang, domAttr, dateUtils, domClass, arrayUtils, on, domConstruct, flightOptionsSummaryPanelTmpl, _TuiBaseWidget, dtlTemplate, Templatable, Evented) {

    return declare("tui.widget.booking.flightoptions.view.FlightOptionsSummaryPanel", [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {


        tmpl: flightOptionsSummaryPanelTmpl,
        templateString: "",
    	widgetsInTemplate : true,


        postMixInProperties: function () {
            this.inherited(arguments);
        },

    	buildRendering: function(){

    		if(this.model){
    			var listOfFlightViewData =  this.model.flightViewData;
                var flightIndex = this.flightIndexValue;


        		for(var i=0; i<listOfFlightViewData.length;i++){

        			for(var j = 0; j<listOfFlightViewData[i].flightViewData.length;j++){

        				if(j == flightIndex )
        					{
        					listOfFlightViewData[i].flightViewData = listOfFlightViewData[i].flightViewData[j];
        					break;
        					}

        			}


        		}
    		}
    		this.dojoConfig=dojoConfig;

    		this.templateString = this.renderTmpl(this.tmpl, this);
    		delete this._templateCache[this.templateString];
    		this.inherited(arguments);
    	},


        postCreate: function () {
        	this.attachEvents();
        	this.setFlightSwitcher();
            this.inherited(arguments);
            var selectBtn = query("button.button", this.domNode);
            for(var index = 0; index<selectBtn.length; index++){
            	this.tagElement(selectBtn[index], "Selection"+(index +1));
            }

        },

        attachEvents : function () {
        	this.summaryButtonHandlers =  on(query(".button", this.domNode), "click", lang.hitch(this, function(event){
        		if(event.target.value){
        		this.summaryButtonHandlers.remove();
        		topic.publish("flightoptions.summarypanel.button.clicked", event.target.value);
        		}
        		else{
        			topic.publish("flightoptions.alternateflights.close");
        		}

            }));
        },

        setFlightSwitcher: function() {

    		var getFlightDiv = query('.flight-options-section', this.domNode);


    		_.each(getFlightDiv,  function(item){

    			var buttons = query('.button', item);
    		_.each(buttons, function(button) {
    			dojo.connect(button, 'click', function(e) {

    				_.each(getFlightDiv,  function(item){
    					var buttons = query('.button', item);
    					_.each(buttons, function(button){
    						domClass.remove(button, 'selected');
    					});

    					domClass.remove(item, 'highlighted-div');

    				});

    				domClass.add(button, 'selected');
    				domClass.add(item, 'highlighted-div');

    				});
    			});
    		});
    	 	}
    });
});
