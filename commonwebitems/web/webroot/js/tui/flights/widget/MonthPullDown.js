define("tui/flights/widget/MonthPullDown", [
  "dojo",
  "dojo/text!tui/flights/templates/MonthPullDown.html",
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
  "tui/flights/store/MonthPullDownStore",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/_TuiBaseWidget"], function (dojo, monthPullDownTmpl, query, html, domConstruct, domStyle, on, domClass, lang, connect, domAttr, flightScheduler, flightMonthBar, monthStore) {
  dojo.declare("tui.flights.widget.MonthPullDown", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
	  
	  tmpl: monthPullDownTmpl,
	  
	  seasonLength: 18,
	  
	  status: null,
	  
	  months: [],
	  
	  tempAvailableMonths: [],
	  
	  constructor:function(){
	  	  var monthPullDown = this;
	  	  
		  monthPullDown.inherited(arguments);
		  
		//  monthPullDown.defaultMonths = params.isDefault;
		 
  	  },
  	  
  	  postCreate: function(){
  		  var monthPullDown = this;
  		  monthPullDown.inherited(arguments);
  		  
  		  monthPullDown.attachEventMonthPullDown();
  		  on(document.body, "click", function(evt){
	  			var evtId = evt.target || evt.srcElement;
	  			if(lang.trim(evtId.id) === "month-pull-down-arrow" || lang.trim(evtId.id) === "whenCal"){}
	  			else{
	  				domClass.remove(query("#whenCal")[0], "border-sel-active");
		  			monthPullDown.hideMonthPullDown();
	  			}
	  	 });
	  		
  	  },
  	  
  	 onAfterTmplRender:function(){
		  var monthPullDown = this;
		  monthPullDown.inherited(arguments);
		  
		  monthPullDown.months = [];
	  },
  	  
  	 attachEventMonthPullDown: function(){
	  		var monthPullDown = this;
	  		
	  		on(query("#month-pull-down-arrow")[0], "click", function(evt){
				monthPullDown.validateFields();
			});
	  		
	  		on(query("#whenCal")[0], "click", function(evt){
				monthPullDown.validateFields();
			});

	  },
  	  
	  validateFields: function(){
		  var monthPullDown = this;
		  
		  monthPullDown.months = [];
		  domClass.add(query("#whenCal")[0], "border-sel-active");
		  var fromAirport = query("#flying-from")[0],
			toAirport = query("#flying-to")[0];
		  if(fromAirport.value === "" && toAirport.value === ""){
			  monthPullDown.populateMonths();
		  }else if(fromAirport.value === "" || toAirport.value === ""){
			  monthPullDown.populateMonths();
		  }else if(fromAirport.value !== "" && toAirport.value !== ""){
			  monthPullDown.renderMonthPullDown();
		  }
	  },
  	  
  	  populateMonths: function(){
           var monthPullDown = this;
		   
	  	   var fs = new flightScheduler();
			  
			for(var i = 0;i < monthPullDown.seasonLength; i++){
				
			   var monthAndYear = fs.getFullMonths(i);
			   var monandyrinDigit = fs.getMonthSlicedIndex(i);
			   var arr = monandyrinDigit.split("@");
			   
			   var obj = {available: true, month: monthAndYear};
			   obj.mon = arr[1]; obj.year = arr[0];
			   monthPullDown.months.push(obj);
			}
			dojo.global.monthPullDown  = monthPullDown.months;
			if(query("#months-pull-down")[0]) domConstruct.destroy("months-pull-down");
			var renderedNode = domConstruct.toDom(monthPullDown.render());
			var closestParent = query("div.inputholders")[2];
			domConstruct.place(renderedNode, closestParent, "last");
			domStyle.set(query("#months-pull-down")[0], {
				  "top":"25px"
			});
			monthPullDown.showMonthPullDown();
  	  },
  	  
  	 renderMonthPullDown: function(){
   		var monthPullDown = this;
   		var fs = new flightScheduler();
		var fromAir = query("#flying-from")[0].value;
		var toAir = query("#flying-to")[0].value;
		var _targetURL = "";
		
		
			// called when clicking on "view" button to populate Month Bar and Big calendar 
  		  if(fromAir.length != 0 && toAir.length != 0){
			  var fromAirValue = fromAir.split("(")[1].split(")")[0];
			  var toAirValue   =   toAir.split("(")[1].split(")")[0];
			  _targetURL = "ws/traveldates?from[]="+fromAirValue+"&to[]="+toAirValue;
			 
  		  }
   		dojo.xhrGet({
			  url: _targetURL,
			  handleAs: "json",
			  sync: false,
			  load: function(data, ioargs){
   					monthPullDown.tempAvailableMonths = data;
		   			for(var i = 0;i < monthPullDown.seasonLength; i++){
		  			   var monthAndYear = fs.getFullMonths(i),
		  			       monandyrinDigit = fs.getMonthSlicedIndex(i),
		  			       arr = monandyrinDigit.split("@"),
		  			       obj = {available: false, month: monthAndYear},
		  			       tmpX = true;
		  			   _.each(monthPullDown.tempAvailableMonths, function(elm){
		  				  		if(tmpX){
		  					   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
		  					   			obj.available = true;
		  					   			tmpX = false;
		  					   		}
		  				   		}
		  				});			   
		  			   obj.mon = arr[1]; obj.year = arr[0];
		  			   monthPullDown.months.push(obj);
		  			}
		  			dojo.global.monthPullDown  = monthPullDown.months;
		  			if(query("#months-pull-down")[0]) domConstruct.destroy("months-pull-down");
		  			var renderedNode = domConstruct.toDom(monthPullDown.render());
		 			var closestParent = query("div.inputholders")[2];
		 			domConstruct.place(renderedNode, closestParent, "last");
		  			domStyle.set(query("#months-pull-down")[0], {
		  				  "top":"25px"
		  			});
		  			monthPullDown.showMonthPullDown();
   			  },
   			  error: function(error, ioargs){
   				  
   			  }
   		});
   	  },
   	  generateMonthPullDownData: function(){
     		var monthPullDown = this;
     		var fs = new flightScheduler();
     		monthPullDown.months = [];
     		var fromAir = query("#from-airport")[0].innerHTML;
    		var toAir = query("#to-airport")[0].innerHTML;
    		var _targetURL = "";
    
            var fromAirValue = fromAir.split("(")[1].split(")")[0];
   			var toAirValue   =   toAir.split("(")[1].split(")")[0];
   			_targetURL = "ws/traveldates?from[]="+toAirValue+"&to[]="+fromAirValue;
      		
     		dojo.xhrGet({
  			  url: _targetURL,
  			  handleAs: "json",
  			  sync: false,
  			  load: function(data, ioargs){
     				monthPullDown.tempAvailableMonths = data;
     				if(data.length === 0) return;
  		   			for(var i = 0;i < monthPullDown.seasonLength; i++){
  		  			   var monthAndYear = fs.getFullMonths(i),
  		  			       monandyrinDigit = fs.getMonthSlicedIndex(i),
  		  			       arr = monandyrinDigit.split("@"),
  		  			       obj = {available: false, month: monthAndYear},
  		  			       tmpX = true;
  		  			   _.each(monthPullDown.tempAvailableMonths, function(elm){
  		  				  		if(tmpX){
  		  					   		if(arr[1] === elm.substring(3,5) && arr[0] === elm.substring(6,10)){
  		  					   			obj.available = true;
  		  					   			tmpX = false;
  		  					   		}
  		  				   		}
  		  				});			   
  		  			   obj.mon = arr[1]; obj.year = arr[0];
  		  			   monthPullDown.months.push(obj);
  		  			}
  		  			dojo.global.monthPullDown  = monthPullDown.months;
  		  			var selectedDate = data[0].split("-");
  		  			query("#ftselectedMonth")[0].value = selectedDate[1] + ":" + selectedDate[2];
  		  			if(query("#months-pull-down")[0]) domConstruct.destroy("months-pull-down");
  		  			var renderedNode = domConstruct.toDom(monthPullDown.render());
  		 			var closestParent = query("div.inputholders")[2];
  		 			domConstruct.place(renderedNode, closestParent, "last");
  		 			
  		 		  
	  				  var fb = new flightMonthBar();
	  				  fb.generateMonths();
	  				  
     			  },
     			  error: function(error, ioargs){
     				  
     			  }
     		});
      },
  	  
  	  showMonthPullDown: function(){
  		  var monthPullDown = this;
  		  domStyle.set(query("#months-pull-down")[0], {
  			  "display":"block"
  		  });
  		  monthPullDown.hideMonthPullDownLists();
  	  },
  	  
  	 hideMonthPullDownLists: function(){
	  		var monthPullDown = this; 
	  		query("#months-pull-down li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	if(!domClass.contains(elm, "disable-list-select")){
			    		query("#whenCal").attr( "value", lang.trim(query(elm).text()) );
			    		if(dojo.byId("ftselectedMonth"))domConstruct.destroy("ftselectedMonth"); 
			    		var mon = domAttr.get(this, "data-mon");
			    		var yr = domAttr.get(this, "data-yr");
			    		var hid = domConstruct.create("input", {
			    			type:"hidden",
			    			value:  mon+":"+yr,
			    			id:"ftselectedMonth"
			    		});
			    		domConstruct.place(hid, document.body, "last");
				    	monthPullDown.hideMonthPullDown();
			    	}
			    });
			});
	  },
	  
  	  hideMonthPullDown: function(){
		  if(query("#months-pull-down")[0]){
		  		domStyle.set(query("#months-pull-down")[0], {
					  "display":"none"
				 });
		  }
  	  },
  	  render: function(){
  		var monthPullDown = this;
		  var html = monthPullDown.renderTmpl(monthPullDown.tmpl);
		  return html;
	  }
	  
  });
  
  return tui.flights.widget.MonthPullDown;
});
