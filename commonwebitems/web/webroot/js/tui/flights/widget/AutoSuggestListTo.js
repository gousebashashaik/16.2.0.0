define("tui/flights/widget/AutoSuggestListTo", [
  "dojo",
  "dojo/text!tui/flights/templates/AutoSuggestListTo.html",
  "dijit/registry",
  "dojo/query",
  "dojo/string",
  'dojo/_base/html',
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/_base/event",
  "dojo/on",
  "tui/flights/store/FlightAirportToStore",
  "dojo/keys",
  "dojo/ready",
  "dojo/dom-class",
  "dijit/focus",
  "dojo/_base/lang",
  "dojo/dom-attr",
  "dojo/_base/connect",
  "tui/flights/widget/ErrorTooltip",
  "dojo/NodeList-traverse",
  "dojo/date/locale",
  "tui/widget/_TuiBaseWidget"], function (dojo, autosuggestToTmpl, registry, query, string, html, domConstruct, domStyle, event, on, flightAirportToStore, keys, ready,domClass, focusUtil, lang, domAttr, connect, errorTooltip) {
  dojo.declare("tui.flights.widget.AutoSuggestListTo", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: autosuggestToTmpl,

	  charNo: 2,

	  airports: [],

	  airportStore: null,

	  airportCodes: [],

	  airportsList: [],

	  idx: 0,

	  errorTooltip: false,

	  constructor: function(params, srcNodeRef){
	  	 var autoSuggestListTo = this;
  	  },

	  postCreate:function(){
	  	  var autoSuggestListTo = this;
  		  autoSuggestListTo.inherited(arguments);

  		  on(document.body, "click", function(evt){
  			  var targetObj = evt.target || evt.srcElement;
  			  if(autoSuggestListTo.errorTooltip) {
  				  query("#flying-to")[0].value = "";
  				  autoSuggestListTo.errorTooltip = false;
  			  }
			  if(targetObj.id === "airports-lists-to" || targetObj.id === "flying-to"){
				  domClass.add(query("#flying-to")[0], "border-sel-active");

				  var fromAir = query("#flying-from")[0].value;
				  var toAir = query("#flying-to")[0].value;
				  var fromAirValue = "";
				  var toAirValue = "",
				  	  whenMon = "";
				 if(fromAir.length > 0)
					if(fromAir.indexOf("(") !== -1) fromAirValue = fromAir.split("(")[1].split(")")[0];
				 if(toAir.length > 0)
					 if(toAir.indexOf("(") !== -1) toAirValue = toAir.split("(")[1].split(")")[0];
				 /*if(lang.trim(query("#whenCal")[0].value) !== "" && lang.trim(query("#flying-from")[0].value) !== "")query("#whenCal")[0].value="";*/
				 whenMon = dojo.byId("ftselectedMonth");
				 if(whenMon){
					 whenMonValue = whenMon.value;
				 }else{
					 whenMonValue = "";
				 }

				 var targetURL = "ws/timetablearrival?from[]="+fromAirValue+"&when="+whenMonValue;

				  dojo.xhrGet({
					  url: targetURL,
					  handleAs: "json",
					  sync: false,
					  load: function(data, ioargs){
				  		  autoSuggestListTo.airportStore = data.items;
				  		  //autoSuggestListTo.airportCodes = _.keys(autoSuggestListTo.airportStore);
				  		  //_.each(autoSuggestListTo.airportCodes, function(code){

				  		  var ukAirportList = [], overseasAirportList = [];

				  		  _.each(autoSuggestListTo.airportStore, function(airportInfo){
								if(airportInfo.available) {
									if(airportInfo.countryCode == "GBR"){
										ukAirportList.push(airportInfo);
									}else{
										overseasAirportList.push(airportInfo);
									}
								}
							});

				  		  autoSuggestListTo.airports=ukAirportList.concat(overseasAirportList);

				  		  //});

						  autoSuggestListTo.placeAirportList();
						  autoSuggestListTo.eventHandlers();
						  /*if(targetObj.id !== "flying-to"){
							  autoSuggestListTo.showAutoSuggestList();
						  }*/
						  autoSuggestListTo.showAutoSuggestList();

						  autoSuggestListTo.searchAirportNamesOrCodes("", evt, 0);

						  focusUtil.focus(query("#flying-to")[0]);
					  },
					  error: function(error, ioargs){

					  }
				  });
			  }else{
				  domClass.remove(query("#flying-to")[0], "border-sel-active");
				  autoSuggestListTo.hideAutoSuggestList();
			  }
		  });
	  },

	  onAfterTmplRender:function(){
		  var autoSuggestListTo = this;
		  autoSuggestListTo.inherited(arguments);

		  autoSuggestListTo.airports = [];
	  },

	  pickPosition:function(){
		  var autoSuggestListTo = this;
		  var pos = html.coords(autoSuggestListTo.domNode);
		  return pos;
	  },

	  placeAirportList: function(){
		  var autoSuggestListTo = this;

		  if(query("#auto-suggest-list-to")[0])domConstruct.destroy("auto-suggest-list-to");

		  var closestParent = query("div.inputholders")[1];

		  if(closestParent){
			  var node = domConstruct.toDom(autoSuggestListTo.render());
			  domConstruct.place(node, closestParent, "last");
		  }

		  var pos = autoSuggestListTo.pickPosition();

		  domStyle.set(query("#auto-suggest-list-to")[0], {
			  "top":"25px"
		  });
	  },

	  eventHandlers: function(){
		  var autoSuggestListTo = this;
		  autoSuggestListTo.keyUpEvents();
	  },

	  hideAutoSuggestList: function(){
		  if(query("#auto-suggest-list-to")[0]){
			  domStyle.set(query("#auto-suggest-list-to")[0], {
			  	  "display":"none"
			  });
		  }
	  },

	  showAutoSuggestList:function(){
		  domStyle.set(query("#auto-suggest-list-to")[0], {
		  	  "display":"block"
		  });
	  },

	  keyUpEvents: function(){
		  var autoSuggestListTo = this;
		  var flyingTo = query("#flying-to")[0];

		  on(flyingTo, "keypress", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if (charOrCode === keys.DOWN_ARROW || charOrCode === keys.UP_ARROW) {
		            evt.preventDefault();
		      }else if (charOrCode === keys.ENTER) {
		        	if(flyingTo.value.length == 0) return;
		        	flyingTo.value = lang.trim(query("li[class='airport-codes-to-li-visible hlight-airport-list-to']").text());
		        	autoSuggestListTo.hideAutoSuggestList();
		       }
     	  });

		  on(query("#auto-suggest-list-to")[0], "mouseover", function (evt) {
			    query("#auto-suggest-list-to li.hlight-airport-list-to").removeClass("hlight-airport-list-to");
		  });


		  on(flyingTo, "keyup", function(evt){
			  var errtips = query(".tooltip", query(".inputholders")[1]);
				if(errtips){
					domClass.remove("flying-to", "error-tool-tip");
					errtips.forEach(domConstruct.destroy);
				}
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.ENTER){
					return;
				}else if (lang.trim(flyingTo.value) === "") {
					autoSuggestListTo.hideAutoSuggestList();
			    }else {
			        if (charOrCode == keys.DOWN_ARROW && idx < query("#auto-suggest-list-to li.airport-codes-to-li-visible").length - 1) {
			            idx++;
			        } else if (charOrCode == keys.UP_ARROW && idx > 0) {
			            idx--;
			        } else if (charOrCode !== keys.DOWN_ARROW && charOrCode !== keys.UP_ARROW) {
			            idx = 0;
			        }

			        if(flyingTo.value.length > autoSuggestListTo.charNo ){
			        	autoSuggestListTo.showAutoSuggestList();
			        	autoSuggestListTo.searchAirportNamesOrCodes(flyingTo.value, evt, idx);
			        }
			    }

		  });

		  /**
		   * handling tab related event
		   */
		  on(flyingTo, "keydown", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.TAB){
				  if(query("#flying-to")[0].value.length <= 2){
					autoSuggestListTo.throwErrorTooltip();
				  }
				  query("#whenCal")[0].value = "";
			  }
		  });

		  //attach events to each li airports
		  query("#auto-suggest-list-to li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	flyingTo.value = lang.trim(query(this).text());

			    	var hidn = domConstruct.create("input", {
		    			type:"hidden",
		    			value:  domAttr.get(elm, "data-country-code"),
		    			id:"ftselectedCountry"
		    		});


		    		domConstruct.place(hidn, document.body, "last");

			    });
			});
	  },

	  /*----filter out the list of airports-----*/
	  searchAirportNamesOrCodes: function(val, evt, idx){
		  var autoSuggestListTo = this;
		  var count = 0;

			if (idx === undefined) {idx = 0;}
		    var list = query('#auto-suggest-list-to'),
		        visibleIdx = 0;
		    dojo.forEach(list.query('li'), function (elm, i) {
		        var found = false;
		        try{
		        	var regExp = new RegExp(val, 'i');
		    	}catch(e){}
		        var elmText = lang.trim(query(elm).text());
		        try{
		        	var regText = regExp.test(elmText);
		        }catch(e){}
		        if (regText) {
		        	var testVal = elmText.substring(elmText.indexOf("(")+1, elmText.lastIndexOf(")")).toLowerCase();
		        	if(elmText.toLowerCase().search(val.toLowerCase()) === 0 || testVal === val.toLowerCase()){
		        		var newStr = elmText.replace(new RegExp('(' + val + ')', 'i'), '<strong>$1</strong>');
						elm.innerHTML = newStr;
						domClass.add(elm, "airport-codes-to-li-visible");
			            if (visibleIdx === idx) {
			            	count = 1;
			                domClass.add(elm, "hlight-airport-list-to");
			            } else {
			                domClass.remove(elm, "hlight-airport-list-to");
			            }
			            visibleIdx++;
			        }else{
			        	domClass.remove(elm, "airport-codes-to-li-visible");
			        }
		        } else {
		        	domClass.remove(elm, "airport-codes-to-li-visible");
		        }
		    });

		    if(count == 0 ){
		    	autoSuggestListTo.throwErrorTooltip();
		    	count = 1;
		  }
	  },
	  throwErrorTooltip: function(){
			var autoSuggestListTo = this;
			dojo.byId("auto-suggest-list-to").style.display = "none";
			try{
				autoSuggestListTo.errorTooltip = true;
				var errtips = query(".tooltip", query(".inputholders")[1]);
				if(errtips){
					errtips.forEach(domConstruct.destroy);
				}
					new errorTooltip({
		  			  connectId: "#flying-to",
		  			  errorMsg: "Try slecting from the drop down menu or alternatively search by aiport code.",
		  			  errorHeaderMsg: "No Matches Found",
		  			  dynaId: "#flyingTo"
		  		  });
			}catch(e){}

	  },

	  render: function(){
		  var autoSuggestListTo = this;
		  var html = autoSuggestListTo.renderTmpl(autoSuggestListTo.tmpl);
		  return html;
	  }

  });

  return tui.flights.widget.AutoSuggestListTo;
});