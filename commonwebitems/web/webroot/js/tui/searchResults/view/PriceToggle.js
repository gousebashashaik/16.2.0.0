define("tui/searchResults/view/PriceToggle", [
  "dojo",
  "dojo/dom-class",
  "dojo/_base/connect",
  "dojo/topic",
  "tui/widget/form/ToggleSwitch"], function (dojo, domClass, connect, topic) {

	function hide(element) {
	    domClass.add(element, 'close');
	  }

	  function show(element) {
	    domClass.remove(element, 'close');
	  }
	
  dojo.declare("tui.searchResults.view.PriceToggle", [tui.widget.form.ToggleSwitch], {

    // ----------------------------------------------------------------------------- properties

	stateMap: {
	  'pp' : 'on',
	  'total' : 'off',
	  'off': 'pp',
	  'on': 'total'
	},
	    
    // ----------------------------------------------------------------------------- methods

	postCreate: function () {
	      //		Sets default values for class properties
		var PriceToggle = this;
			PriceToggle.inherited(arguments);
	    	PriceToggle.tagElement(PriceToggle.toggleSlide, "Maintoggle");
	},
    onAfterToggle: function(oldValue, newValue) {
        var PriceToggle = this;
        PriceToggle.togglePriceView(newValue);
      },
      
      togglePriceView: function (view) {
          var PriceToggle = this;
          PriceToggle.priceView = view;
          switch(view) {
          
            case "total":           
              connect.publish("tui:channel=priceToggle", {add : 'total', remove : 'pp'});
            	 //budgetToggle.model.searchRequest.priceView = 'total';
              break;
            case "pp":              
              connect.publish("tui:channel=priceToggle", {remove : 'total', add : 'pp'});
            	 //budgetToggle.model.searchRequest.priceView = 'pp';
              break;
          }
        },
        subscribeToChannels: function () {
		      var PriceToggle = this;
		      // toggle price
		      topic.subscribe("tui:channel=refreshPriceToggle", function () {
		    	  PriceToggle.refresh();
		      });

		     
		    },
        refresh: function () {
            var PriceToggle = this;
            PriceToggle.toggleAll('total','pp','off','on');
          }

  });

  return tui.searchResults.view.PriceToggle;
});