define("tui/flights/widget/wherewefly/SeasonPullDown", [
  "dojo",
  "dojo/text!tui/flights/templates/SeasonPullDownTMPL.html",
  "dojo/query",
  'dojo/_base/html',
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/on",
  "dojo/dom-class",
  "dojo/_base/lang",
  "dojo/_base/connect",
  'dojo/dom-attr',
  "tui/flights/view/FlightScheduler",
  "tui/flights/view/FlightsMonthBar",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/_TuiBaseWidget"], function (dojo, seasonPullDownTmpl, query, html, domConstruct, domStyle, on, domClass, lang, connect, domAttr, flightScheduler, flightMonthBar) {
  dojo.declare("tui.flights.widget.wherewefly.SeasonPullDown", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: seasonPullDownTmpl,

	  seasonLength: dojo.global.seasonLength,

	  status: null,

	  months: [],

	  tempAvailableMonths: [],

	  constructor:function(){
	  	  var seasonPullDown = this;
	  	seasonPullDown.inherited(arguments);
  	  },

  	  postCreate: function(){
  		  var seasonPullDown = this;
  		  seasonPullDown.inherited(arguments);

  		  seasonPullDown.attachEventMonthPullDown();
  		  on(document.body, "click", function(evt){
	  			var evtId = evt.target || evt.srcElement;
	  			if(lang.trim(evtId.id) === "airports-lists-filter-when" || lang.trim(evtId.id) === "flying-from-filter-when"){
	  				domClass.add(query("#flying-from-filter-when")[0], "border-sel-active");
	  			}else{
	  				domClass.remove(query("#flying-from-filter-when")[0], "border-sel-active");
	  				seasonPullDown.hideMonthPullDown()
	  			}
	  	  });

  	  },

  	 onAfterTmplRender:function(){
		  var seasonPullDown = this;
		  seasonPullDown.inherited(arguments);

		  seasonPullDown.months = [];
	  },

  	 attachEventMonthPullDown: function(){
	  		var seasonPullDown = this;

	  		on(query("#airports-lists-filter-when")[0], "click", function(evt){
	  			seasonPullDown.validateFields();
			});

	  		on(query("#flying-from-filter-when")[0], "click", function(evt){
	  			seasonPullDown.validateFields();
			});

	  },

	  validateFields: function(){
		  var seasonPullDown = this;

		  seasonPullDown.months = [];

		  /*domClass.add(query("#flying-from-filter-when")[0], "border-sel-active");
		  var fromAirport = query("#flying-from-filter")[0],

		  if(fromAirport.value === "" && toAirport.value === ""){
			  seasonPullDown.populateMonths();
		  }else if(fromAirport.value === "" || toAirport.value === ""){
			  seasonPullDown.populateMonths();
		  }else if(fromAirport.value !== "" && toAirport.value !== ""){
			  seasonPullDown.renderMonthPullDown();
		  }*/

		  seasonPullDown.populateMonths();
	  },

  	  populateMonths: function(){
           var seasonPullDown = this;

	  	   var fs = new flightScheduler();
			for(var i = 0;i < seasonPullDown.seasonLength; i++){
			   var monthAndYear = fs.getFullMonths(i);
			   var monandyrinDigit = fs.getMonthSlicedIndex(i);
			   var arr = monandyrinDigit.split("@");
			   var obj = {available: true, month: monthAndYear};
			   obj.mon = arr[1]; obj.year = arr[0];
			   seasonPullDown.months.push(obj);
			}
			dojo.global.seasonPullDown  = seasonPullDown.months;
			if(query("#seasons-pull-down")[0]) domConstruct.destroy("seasons-pull-down");
			var renderedNode = domConstruct.toDom(seasonPullDown.render());
			var closestParent = query("div.inputfilters")[1];
			domConstruct.place(renderedNode, closestParent, "last");
			domStyle.set(query("#seasons-pull-down")[0], {
				  "top":"25px"
			});
			seasonPullDown.showMonthPullDown();
  	  },

  	 renderMonthPullDown: function(){
   		var seasonPullDown = this;
   		var fs = new flightScheduler();
   		dojo.xhrGet({
			  url: "ws/traveldates",
			  handleAs: "json",
			  sync: false,
			  load: function(data, ioargs){
   					seasonPullDown.tempAvailableMonths = data;
		   			for(var i = 0;i < seasonPullDown.seasonLength; i++){
		  			   var monthAndYear = fs.getFullMonths(i),
		  			       monandyrinDigit = fs.getMonthSlicedIndex(i),
		  			       arr = monandyrinDigit.split("@"),
		  			       obj = {available: false, month: monthAndYear},
		  			       tmpX = true;
		  			   _.each(seasonPullDown.tempAvailableMonths, function(elm){
		  				  		if(tmpX){
		  					   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
		  					   			obj.available = true;
		  					   			tmpX = false;
		  					   		}
		  				   		}
		  				});
		  			   obj.mon = arr[1]; obj.year = arr[0];
		  			 seasonPullDown.months.push(obj);
		  			}
		  			dojo.global.seasonPullDown  = seasonPullDown.months;
		  			if(query("#seasons-pull-down")[0]) domConstruct.destroy("seasons-pull-down");
		  			var renderedNode = domConstruct.toDom(monthPullDown.render());
		 			var closestParent = query("div.inputfilters")[1];
		 			domConstruct.place(renderedNode, closestParent, "last");
		  			domStyle.set(query("#seasons-pull-down")[0], {
		  				  "top":"25px"
		  			});
		  			seasonPullDown.showMonthPullDown();
   			  },
   			  error: function(error, ioargs){

   			  }
   		});
   	  },
   	  generateMonthPullDownData: function(){
     		var seasonPullDown = this;
     		var fs = new flightScheduler();
     		dojo.xhrGet({
  			  url: "ws/traveldates",
  			  handleAs: "json",
  			  sync: false,
  			  load: function(data, ioargs){
     				seasonPullDown.tempAvailableMonths = data;
  		   			for(var i = 0;i < seasonPullDown.seasonLength; i++){
  		  			   var monthAndYear = fs.getFullMonths(i),
  		  			       monandyrinDigit = fs.getMonthSlicedIndex(i),
  		  			       arr = monandyrinDigit.split("@"),
  		  			       obj = {available: false, month: monthAndYear},
  		  			       tmpX = true;
  		  			   _.each(seasonPullDown.tempAvailableMonths, function(elm){
  		  				  		if(tmpX){
  		  					   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
  		  					   			obj.available = true;
  		  					   			tmpX = false;
  		  					   		}
  		  				   		}
  		  				});
  		  			   obj.mon = arr[1]; obj.year = arr[0];
  		  			   seasonPullDown.months.push(obj);
  		  			}
  		  			dojo.global.seasonPullDown  = seasonPullDown.months;
  		  			if(query("#seasons-pull-down")[0]) domConstruct.destroy("seasons-pull-down");
  		  			var renderedNode = domConstruct.toDom(seasonPullDown.render());
  		 			var closestParent = query("div.inputfilters")[1];
  		 			domConstruct.place(renderedNode, closestParent, "last");


	  				  var fb = new flightMonthBar();
	  				  fb.generateMonths();

	  				  fa = new flightActions();
	  		  		  fa.showSearchBox();
     			  },
     			  error: function(error, ioargs){

     			  }
     		});
      },

  	  showMonthPullDown: function(){
  		  var seasonPullDown = this;
  		  domStyle.set(query("#seasons-pull-down")[0], {
  			  "display":"block"
  		  });
  		  seasonPullDown.hideMonthPullDownLists();
  	  },

  	 hideMonthPullDownLists: function(){
	  		var seasonPullDown = this;
	  		query("#seasons-pull-down li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	if(!domClass.contains(elm, "disable-list-select")){
			    		query("#flying-from-filter-when").attr( "value", lang.trim(query(elm).text()) );
			    		if(dojo.byId("ftselectedMonth"))domConstruct.destroy("ftselectedMonth");
			    		var mon = domAttr.get(this, "data-mon");
			    		var yr = domAttr.get(this, "data-yr");
			    		var hid = domConstruct.create("input", {
			    			type:"hidden",
			    			value:  domAttr.get(this, "data-code"),
			    			id:"ftselectedMonth"
			    		});
			    		domConstruct.place(hid, document.body, "last");
				    	seasonPullDown.hideMonthPullDown();
			    	}
			    });
			});
	  },

  	  hideMonthPullDown: function(){
		  if(query("#seasons-pull-down")[0]){
		  		domStyle.set(query("#seasons-pull-down")[0], {
					  "display":"none"
				 });
		  }
  	  },

  	  render: function(){
	  		var seasonPullDown = this;
			var html = seasonPullDown.renderTmpl(seasonPullDown.tmpl);
			return html;
	  }

  });

  return tui.flights.widget.wherewefly.SeasonPullDown;
});