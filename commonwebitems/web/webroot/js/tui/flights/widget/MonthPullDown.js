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
  "dijit/registry",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/_TuiBaseWidget"], function (dojo, monthPullDownTmpl, query, html, domConstruct, domStyle, on, domClass, lang, connect, domAttr, flightScheduler, flightMonthBar, monthStore,registry) {
  dojo.declare("tui.flights.widget.MonthPullDown", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: monthPullDownTmpl,

	  status: null,

	  months: [],

	  tempAvailableMonths: [],

	  selected: null,

	  seasonLength: 0,

	  constructor:function(){
	  	  var monthPullDown = this;
  	  },
  	  postCreate: function(){
  		  var monthPullDown = this;
  		monthPullDown.seasonLength =  dojo.global.seasonLength;
  		/* monthPullDown.attachEventMonthPullDown();
  		  on(document.body, "click", function(evt){
	  			var evtId = evt.target || evt.srcElement;
	  			if(lang.trim(evtId.id) === "month-pull-down-arrow" || lang.trim(evtId.id) === "whenCal"){}
	  			else{
	  				domClass.remove(query("#whenCal")[0], "border-sel-active");
		  			monthPullDown.hideMonthPullDown();
	  			}
	  	 });*/
      },
  	 onAfterTmplRender:function(){
		  var monthPullDown = this;
		  monthPullDown.inherited(arguments);

		  monthPullDown.months = [];
	  },
  	/* attachEventMonthPullDown: function(){
	  		var monthPullDown = this;
	  		on(query("#month-pull-down-arrow")[0], "click", function(evt){
				monthPullDown.validateFields();
			});
	  		on(query("#whenTimeTable")[0], "click", function(evt){
				monthPullDown.validateFields();
			});

	  },*/
	  validateFields: function(){
		  var monthPullDown = this;
		  monthPullDown.months = [];
		var fromAirport = registry.byId("timeTableFlyFrom").getValue(),
		      toAirport = registry.byId("timeTableFlyTo").getValue();
		//if(fromAirport !== "" || toAirport !== ""){
			  monthPullDown.renderMonthPullDown();
		  //}
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
			var closestParent = query("div.inputholders")[0];
			domConstruct.place(renderedNode, closestParent, "last");
			domStyle.set(query("#months-pull-down")[0], {
				  "top":"38px"
			});
			monthPullDown.showMonthPullDown();
  	  },
  	 renderMonthPullDown: function(){
   		var monthPullDown = this,fromAir,toAir,_targetURL;
   		fs = new flightScheduler();
		fromAir = registry.byId("timeTableFlyFrom").getValue();
		toAir = registry.byId("timeTableFlyTo").getValue();
		_targetURL = "";
		// called when clicking on "view" button to populate Month Bar and Big calendar
  		//  if(fromAir.length != 0 || toAir.length != 0){
			  var fromAirValue = fromAir;
			  var toAirValue   =   toAir;
			  _targetURL = "ws/traveldates?from[]="+fromAirValue+"&to[]="+toAirValue;

  		// }
   		dojo.xhrGet({
			  url: _targetURL,
			  handleAs: "json",
			  sync: false,
			  load: function(data, ioargs){
   					monthPullDown.tempAvailableMonths = data;
   					monthPullDown.months =[];
   					dojo.global.monthPullDown=[];
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
		  			monthPullDown.render();
		  			//existed code---don't delete
		  			/*var renderedNode = domConstruct.toDom(monthPullDown.render());
		 			var closestParent = query("div.inputholders")[0];
		 			domConstruct.place(renderedNode, closestParent, "last");
		  			domStyle.set(query("#months-pull-down")[0], {
		  				  "top":"38px"
		  			});
		  			monthPullDown.showMonthPullDown();*/
   			  },
   			  error: function(error, ioargs){

   			  }
   		});
   	  },
   	  generateMonthPullDownData: function(fromAirValue, toAirValue){
     		var monthPullDown = this,fromAir,toAir,_targetURL;
       		fs = new flightScheduler();

    		_targetURL = "ws/traveldates?to[]="+fromAirValue+"&from[]="+toAirValue;



     		dojo.xhrGet({
  			  url: _targetURL,
  			  handleAs: "json",
  			  sync: false,
  			  load: function(data, ioargs){
     				monthPullDown.tempAvailableMonths = data;
     				monthPullDown.months =[];
   					dojo.global.monthPullDown=[];
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
  		  		dojo.query(".timeTableMonthSelector").empty();
  		  	/*var selectedDate = data[0].split("-");
  		  			query("#ftselectedMonth")[0].value = selectedDate[1] + ":" + selectedDate[2];
  		  			if(query("#months-pull-down")[0]) domConstruct.destroy("months-pull-down");
  		  			var renderedNode = domConstruct.toDom(monthPullDown.render());
  		 			var closestParent = query("div.inputholders")[0];
  		 			domConstruct.place(renderedNode, closestParent, "last");*/


	  				  var fb = new flightMonthBar();
	  				  fb.generateMonths();

     			  },
     			  error: function(error, ioargs){

     			  }
     		});
      },


      getAvailableMonths: function(){
   		var monthPullDown = this,fromAir,toAir,_targetURL;
     		fs = new flightScheduler();
  		fromAir = registry.byId("timeTableFlyFrom").getValue();
  		toAir = registry.byId("timeTableFlyTo").getValue();
  		_targetURL = "";
  		// called when clicking on "view" button to populate Month Bar and Big calendar
    		  if(fromAir.length != 0 || toAir.length != 0){
  			  var fromAirValue = fromAir;
  			  var toAirValue   =   toAir;
  			  _targetURL = "ws/traveldates?from[]="+fromAirValue+"&to[]="+toAirValue;

    		 }

   		dojo.xhrGet({
			  url: _targetURL,
			  handleAs: "json",
			  sync: false,
			  load: function(data, ioargs){
   				monthPullDown.tempAvailableMonths = data;
   				monthPullDown.months =[];
 					dojo.global.monthPullDown=[];
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
		  		dojo.query(".timeTableMonthSelector").empty()
		  	/*var selectedDate = data[0].split("-");
		  			query("#ftselectedMonth")[0].value = selectedDate[1] + ":" + selectedDate[2];
		  			if(query("#months-pull-down")[0]) domConstruct.destroy("months-pull-down");
		  			var renderedNode = domConstruct.toDom(monthPullDown.render());
		 			var closestParent = query("div.inputholders")[0];
		 			domConstruct.place(renderedNode, closestParent, "last");*/


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
  		var monthPullDown = this,availableMonths = [],index;

  		_.each(monthPullDown.months,function(month){
  			var noResult="";
  			if(!month.available){
  				noResult =" - No Flights";
  			}

  			availableMonths.push({
  	          value: month.mon + "/" + month.year,
  	          text: month.month + noResult ,
  	          label: month.month + noResult ,
  	          disabled: !month.available
  	        });
  			});
  		index = registry.byId("whenTimeTable").getIndexFromValue(registry.byId("whenTimeTable").getSelectedData().value);
  		registry.byId("whenTimeTable").listData = availableMonths;
  		registry.byId("whenTimeTable").appendOption("<span class='disable-list-select'>Select a month</span>", "",0);
		registry.byId("whenTimeTable").renderList();
		registry.byId("whenTimeTable").disableItem(0);

		if(index > 0) registry.byId("whenTimeTable").setSelectedIndex(index);

		//existed code---don't delete
		/*  var html = monthPullDown.renderTmpl(monthPullDown.tmpl);
		  return html;*/
	  }

  });

  return tui.flights.widget.MonthPullDown;
});
