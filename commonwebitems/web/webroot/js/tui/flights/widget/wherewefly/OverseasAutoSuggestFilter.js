define("tui/flights/widget/wherewefly/OverseasAutoSuggestFilter", [
  "dojo",
  "dojo/text!tui/flights/templates/OverseasAutoSuggestFilter.html",
  "dijit/registry",
  "dojo/query",
  "dojo/string",
  'dojo/_base/html',
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/_base/event",
  "dojo/on",
  "dojo/keys",
  "dojo/ready",
  "dojo/dom-class",
  "dijit/focus",
  "dojo/_base/lang",
  "dojo/dom-attr",
  "dojo/_base/connect",
  "tui/flights/widget/ErrorTooltip",
  "dojo/NodeList-traverse",
  "dojo/NodeList-manipulate",
  "dojo/date/locale",
  "tui/widget/_TuiBaseWidget"], function (dojo, overseasautosuggestFilterTmpl, registry, query, string, html, domConstruct, domStyle, event, on, keys, ready,domClass, focusUtil, lang, domAttr, connect, errorTooltip) {
  dojo.declare("tui.flights.widget.wherewefly.OverseasAutoSuggestFilter", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: overseasautosuggestFilterTmpl,

	  charNo: 2,

	  airports: [],

	  airportStore: null,

	  airportCodes: [],

	  airportsList: [],

	  tipAdvisor: null,

	  idx: 0,

	  constructor: function(params, srcNodeRef){
	  	 var overseasautoSuggestFilter = this;
  	  },

	  postCreate:function(){
	  	  var overseasautoSuggestFilter = this;
	  	overseasautoSuggestFilter.inherited(arguments);

  		  on(document.body, "click", function(evt){
  			  var targetObj = evt.target || evt.srcElement;
			  if(targetObj.id === "overseas-airport-filter"){
				  domClass.add(query("#overseas-airport-filter")[0], "border-sel-active");

				  dojo.xhrGet({
					  url: "/flight/WhereWeFlyFilter",
					  handleAs: "json",
					  sync: false,
					  load: function(data, ioargs){
				  		  overseasautoSuggestFilter.airportStore = data;
				  		  if(overseasautoSuggestFilter.airportStore.overseasAirportList){
				  			overseasautoSuggestFilter.airportCodes = overseasautoSuggestFilter.airportStore.overseasAirportList;
				  			_.each(overseasautoSuggestFilter.airportCodes, function(airportInfo){
				  				overseasautoSuggestFilter.airports.push(airportInfo);
				  			});
				  		  }

				  		 /*
				  		  overseasautoSuggestFilter.airportCodes = _.keys(overseasautoSuggestFilter.airportStore);
				  		  _.each(overseasautoSuggestFilter.airportCodes, function(code){
							_.each(overseasautoSuggestFilter.airportStore[code], function(airportInfo){
								overseasautoSuggestFilter.airports.push(airportInfo);
							});
				  		  });*/

						  overseasautoSuggestFilter.placeAirportList();
						  overseasautoSuggestFilter.eventHandlers();
						  if(targetObj.id !== "overseas-airport-filter") overseasautoSuggestFilter.showAutoSuggestList();
						  overseasautoSuggestFilter.searchAirportNamesOrCodes("", evt, 0);
						  focusUtil.focus(query("#overseas-airport-filter")[0]);
					  },
					  error: function(error, ioargs){

					  }
				  });
			  }else{
				  domClass.remove(query("#overseas-airport-filter")[0], "border-sel-active");
				  overseasautoSuggestFilter.hideAutoSuggestList();
			  }
		  });
	  },

	  onAfterTmplRender:function(){
		  var overseasautoSuggestFilter = this;
		  overseasautoSuggestFilter.inherited(arguments);
		  overseasautoSuggestFilter.airports = [];
	  },

	  pickPosition:function(){
		  var overseasautoSuggestFilter = this;
		  var pos = html.coords(overseasautoSuggestFilter.domNode);
		  return pos;
	  },

	  placeAirportList: function(){
		  var overseasautoSuggestFilter = this;

		  if(query("#overseas-auto-suggest-filter")[0])domConstruct.destroy("overseas-auto-suggest-filter");

		  var closestParent = query("div.overseasAirports")[0];

		  if(closestParent){
			  var node = domConstruct.toDom(overseasautoSuggestFilter.render());
			  domConstruct.place(node, closestParent, "last");
		  }

		  var pos = overseasautoSuggestFilter.pickPosition();

		  domStyle.set(query("#overseas-auto-suggest-filter")[0], {
			  "top":"25px"
		  });
	  },

	  eventHandlers: function(){
		  var overseasautoSuggestFilter = this;
		  overseasautoSuggestFilter.keyUpEvents();
	  },

	  hideAutoSuggestList: function(){
		  if(query("#overseas-auto-suggest-filter")[0]){
			  domStyle.set(query("#overseas-auto-suggest-filter")[0], {
			  	  "display":"none"
			  });
		  }
	  },

	  showAutoSuggestList:function(){
		  domStyle.set(query("#overseas-auto-suggest-filter")[0], {
		  	  "display":"block"
		  });
	  },

	  keyUpEvents: function(){
		  var overseasautoSuggestFilter = this;
		  var flyingFrom = query("#overseas-airport-filter")[0];

		  on(flyingFrom, "keypress", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if (charOrCode === keys.DOWN_ARROW || charOrCode === keys.UP_ARROW) {
		            evt.preventDefault();
		      } else if (charOrCode === keys.ENTER) {
		        	if(flyingFrom.value.length == 0) return;
		        	flyingFrom.value = lang.trim(query("li[class='airport-codes-li-visible hlight-airport-list']").text());
		        	overseasautoSuggestFilter.hideAutoSuggestList();
		      }
     	  });

		  on(query("#overseas-auto-suggest-filter")[0], "mouseover", function (evt) {
			    query("#overseas-auto-suggest-filter li.hlight-airport-list").removeClass("hlight-airport-list");
		  });

		  on(flyingFrom, "keyup", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.ENTER){
					return;
				}else if (lang.trim(flyingFrom.value) === "") {
					overseasautoSuggestFilter.hideAutoSuggestList();
			    }else {
			        if (charOrCode == keys.DOWN_ARROW && idx < query("#overseas-auto-suggest-filter li.airport-codes-li-visible").length - 1) {
			            idx++;
			        } else if (charOrCode == keys.UP_ARROW && idx > 0) {
			            idx--;
			        } else if (charOrCode !== keys.DOWN_ARROW && charOrCode !== keys.UP_ARROW) {
			            idx = 0;
			        }
			        if(flyingFrom.value.length > overseasautoSuggestFilter.charNo ){
			        	overseasautoSuggestFilter.showAutoSuggestList();
			        	overseasautoSuggestFilter.searchAirportNamesOrCodes(flyingFrom.value, evt, idx);
			        }
			    }
		  });

		  on(flyingFrom, "keydown", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.TAB){
				  if(query("#overseas-airport-filter")[0].value.length <= 2){
					  overseasautoSuggestFilter.throwErrorTooltip();
				  }
			  }
		  });
		  //attach events to each li airports
		  query("#overseas-auto-suggest-filter li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	flyingFrom.value = lang.trim(query(this).text());

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
		  var overseasautoSuggestFilter = this;
		  var count = 0;
			if (idx === undefined) {idx = 0;}
		    var list = query('#overseas-auto-suggest-filter'),
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
						domClass.add(elm, "airport-codes-li-visible");
			            if (visibleIdx === idx) {
			            	count = 1;
			                domClass.add(elm, "hlight-airport-list");
			            } else {
			                domClass.remove(elm, "hlight-airport-list");
			            }
			            visibleIdx++;
			        }else{
			        	domClass.remove(elm, "airport-codes-li-visible");
			        }

		        } else {
		        	/* var tipAdvise =  new errorTooltip({
			  			  connectId: "#flying-from",
			  			  errorMsg: "Try selecting from drop down menu or alternatively search by airport code.",
			  			  errorHeaderMsg: "No matches found",
			  			  id: "flyingFrom",
			  		 });*/
		        	domClass.remove(elm, "airport-codes-li-visible");
		        }
		    });

		    if(count == 0 ){
		    	overseasautoSuggestFilter.throwErrorTooltip();
		    	count = 1;
		  }
	  },
	  throwErrorTooltip: function(){
			var overseasautoSuggestFilter = this;
			/*if(query(".tooltip"))
				dojo.style(query(".tooltip"), {"display" :"none"});*/
    	 //dojo.style(dojo.query(".underline"), {"display" : "block"});
			/*dojo.forEach(dojo.query(".tooltip"), function(e){
            	dojo.style(e, {"display":"none"});
            });
			dojo.byId("overseas-auto-suggest-filter").style.display = "none";
			try{
				overseasautoSuggestFilter.errorTooltip = true;
				new errorTooltip({
		  			  connectId: "#overseas-airport-filter",
		  			  errorMsg: "Try slecting from the drop down menu or alternatively search by aiport code.",
		  			  errorHeaderMsg: "No Matches Found",
		  			  dynaId: "#flyingFromFilter"
		  		  });
			}catch(e){}*/

	  },

	  render: function(){
		  var overseasautoSuggestFilter = this;
		  var html = overseasautoSuggestFilter.renderTmpl(overseasautoSuggestFilter.tmpl);
		  return html;
	  }

  });

  return tui.flights.widget.wherewefly.OverseasAutoSuggestFilter;
});