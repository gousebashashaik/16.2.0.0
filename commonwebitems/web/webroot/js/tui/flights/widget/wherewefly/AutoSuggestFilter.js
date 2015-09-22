define("tui/flights/widget/wherewefly/AutoSuggestFilter", [
  "dojo",
  "dojo/text!tui/flights/templates/AutoSuggestFilter.html",
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
  "tui/widget/_TuiBaseWidget"], function (dojo, autosuggestFilterTmpl, registry, query, string, html, domConstruct, domStyle, event, on, keys, ready,domClass, focusUtil, lang, domAttr, connect, errorTooltip) {
  dojo.declare("tui.flights.widget.wherewefly.AutoSuggestFilter", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
	  
	  tmpl: autosuggestFilterTmpl,
	  
	  charNo: 2,
	  
	  airports: [],
	  
	  airportStore: null, 
	  
	  airportCodes: [],
	  
	  airportsList: [],
	  
	  tipAdvisor: null,
	  
	  idx: 0,
	 	  
	  constructor: function(params, srcNodeRef){
	  	 var autoSuggestFilter = this;
  	  },
  	  
	  postCreate:function(){
	  	  var autoSuggestFilter = this;
  		  autoSuggestFilter.inherited(arguments);
  		  
  		  on(document.body, "click", function(evt){
  			  var targetObj = evt.target || evt.srcElement;
			  if(targetObj.id === "airports-lists-filter" || targetObj.id === "flying-from-filter"){
				  domClass.add(query("#flying-from-filter")[0], "border-sel-active");
			    	  
				  dojo.xhrGet({
					  url: "/flights/WhereWeFlyFilter",
					  handleAs: "json",
					  sync: false,
					  load: function(data, ioargs){
				  		  autoSuggestFilter.airportStore = data;
				  		  if(autoSuggestFilter.airportStore.ukAirportList){
				  			autoSuggestFilter.airportCodes = autoSuggestFilter.airportStore.ukAirportList;
				  			_.each(autoSuggestFilter.airportCodes, function(airportInfo){
				  				autoSuggestFilter.airports.push(airportInfo);
				  			});
				  		  }
				  		  
				  		 /* 
				  		  autoSuggestFilter.airportCodes = _.keys(autoSuggestFilter.airportStore);
				  		  _.each(autoSuggestFilter.airportCodes, function(code){
							_.each(autoSuggestFilter.airportStore[code], function(airportInfo){
								autoSuggestFilter.airports.push(airportInfo);
							});
				  		  });*/
				  		  
						  autoSuggestFilter.placeAirportList();
						  autoSuggestFilter.eventHandlers();
						  if(targetObj.id !== "flying-from-filter") autoSuggestFilter.showAutoSuggestList();
						  autoSuggestFilter.searchAirportNamesOrCodes("", evt, 0);
						  focusUtil.focus(query("#flying-from-filter")[0]);
					  },
					  error: function(error, ioargs){
					   
					  }
				  });
			  }else{
				  domClass.remove(query("#flying-from-filter")[0], "border-sel-active");
				  autoSuggestFilter.hideAutoSuggestList();
			  }
		  });
	  },
	  
	  onAfterTmplRender:function(){
		  var autoSuggestFilter = this;
		  autoSuggestFilter.inherited(arguments);
		  autoSuggestFilter.airports = [];
	  },
	  
	  pickPosition:function(){
		  var autoSuggestFilter = this;
		  var pos = html.coords(autoSuggestFilter.domNode);
		  return pos;
	  },
	  
	  placeAirportList: function(){
		  var autoSuggestFilter = this;
		  
		  if(query("#auto-suggest-list-filter")[0])domConstruct.destroy("auto-suggest-list-filter");
		  
		  var closestParent = query("div.inputfilters")[0];
		  
		  if(closestParent){
			  var node = domConstruct.toDom(autoSuggestFilter.render());
			  domConstruct.place(node, closestParent, "last");
		  }
		  
		  var pos = autoSuggestFilter.pickPosition();
		  
		  domStyle.set(query("#auto-suggest-list-filter")[0], {
			  "top":"25px"
		  });
	  },
	  
	  eventHandlers: function(){
		  var autoSuggestFilter = this;
		  autoSuggestFilter.keyUpEvents();
	  },
	  
	  hideAutoSuggestList: function(){
		  if(query("#auto-suggest-list-filter")[0]){
			  domStyle.set(query("#auto-suggest-list-filter")[0], {
			  	  "display":"none"
			  });
		  }
	  },
	  
	  showAutoSuggestList:function(){
		  domStyle.set(query("#auto-suggest-list-filter")[0], {
		  	  "display":"block"
		  });
	  },
	  
	  keyUpEvents: function(){
		  var autoSuggestFilter = this;
		  var flyingFrom = query("#flying-from-filter")[0];
		  
		  on(flyingFrom, "keypress", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if (charOrCode === keys.DOWN_ARROW || charOrCode === keys.UP_ARROW) {
		            evt.preventDefault();
		      } else if (charOrCode === keys.ENTER) {
		        	if(flyingFrom.value.length == 0) return;
		        	flyingFrom.value = lang.trim(query("li[class='airport-codes-li-visible hlight-airport-list']").text());
		        	autoSuggestFilter.hideAutoSuggestList();
		      }
     	  });
		  
		  on(query("#auto-suggest-list-filter")[0], "mouseover", function (evt) {
			    query("#auto-suggest-list-filter li.hlight-airport-list").removeClass("hlight-airport-list");
		  });
		  
		  on(flyingFrom, "keyup", function(evt){
			  var errtips = query(".tooltip", query(".inputfilters")[0]);
				if(errtips){
					domClass.remove("flying-from-filter", "error-tool-tip");
					errtips.forEach(domConstruct.destroy);
				}
				
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.ENTER){
					return;
				}else if (lang.trim(flyingFrom.value) === "") {
					autoSuggestFilter.hideAutoSuggestList();
			    }else {
			        if (charOrCode == keys.DOWN_ARROW && idx < query("#auto-suggest-list-filter li.airport-codes-li-visible").length - 1) {
			            idx++;
			        } else if (charOrCode == keys.UP_ARROW && idx > 0) {
			            idx--;
			        } else if (charOrCode !== keys.DOWN_ARROW && charOrCode !== keys.UP_ARROW) {
			            idx = 0;
			        }
			        if(flyingFrom.value.length > autoSuggestFilter.charNo ){
			        	autoSuggestFilter.showAutoSuggestList();
			        	autoSuggestFilter.searchAirportNamesOrCodes(flyingFrom.value, evt, idx);
			        }
			    }
		  });
		  
		  on(flyingFrom, "keydown", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.TAB){
				  if(query("#flying-from-filter")[0].value.length <= 2){
					  autoSuggestFilter.throwErrorTooltip();
				  }
			  }
		  });
		  //attach events to each li airports
		  query("#auto-suggest-list-filter li").forEach(function (elm, i) {
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
		  var autoSuggestFilter = this;
		  var count = 0;
			if (idx === undefined) {idx = 0;}
		    var list = query('#auto-suggest-list-filter'),
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
		    	autoSuggestFilter.throwErrorTooltip();
		    	count = 1;
		  }
	  },
	  throwErrorTooltip: function(){
			var autoSuggestFilter = this;
			dojo.byId("auto-suggest-list-filter").style.display = "none";
			
			try{
				autoSuggestFilter.errorTooltip = true;
				
				var errtips = query(".tooltip", query(".inputfilters")[0]);
				if(errtips){
					errtips.forEach(domConstruct.destroy);
				}
				
				new errorTooltip({
		  			  connectId: "#flying-from-filter",
		  			  errorMsg: "Try slecting from the drop down menu or alternatively search by aiport code.",
		  			  errorHeaderMsg: "No Matches Found",
		  			  dynaId: "#flyingFromErr"
		  		});
				
			}catch(e){}
	  },
		
	  render: function(){
		  var autoSuggestFilter = this;
		  var html = autoSuggestFilter.renderTmpl(autoSuggestFilter.tmpl);
		  return html;
	  }
  
  });
  
  return tui.flights.widget.wherewefly.AutoSuggestFilter;
});