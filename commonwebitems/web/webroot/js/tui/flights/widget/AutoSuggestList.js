define("tui/flights/widget/AutoSuggestList", [
  "dojo",
  "dojo/text!tui/flights/templates/AutoSuggestList.html",
  "dijit/registry",
  "dojo/query",
  "dojo/string",
  'dojo/_base/html',
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/_base/event",
  "dojo/on",
  "tui/flights/store/FlightAirportStore",
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
  "tui/widget/_TuiBaseWidget"], function (dojo, autosuggestTmpl, registry, query, string, html, domConstruct, domStyle, event, on, flightAirportStore, keys, ready,domClass, focusUtil, lang, domAttr, connect, errorTooltip) {
  dojo.declare("tui.flights.widget.AutoSuggestList", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  /*templatePath: dojo.moduleUrl("tui/flights","templates/AutoSuggestList.html"),*/

	  tmpl: autosuggestTmpl,

	  charNo: 2,

	  airports: [],

	  airportStore: null,

	  airportCodes: [],

	  airportsList: [],

	  idx: 0,

	  errorTooltip: false,

	  constructor: function(params, srcNodeRef){
	  	 var autoSuggestList = this;
  	  },

	  postCreate:function(){
	  	  var autoSuggestList = this;

  		  autoSuggestList.inherited(arguments);
  		  on(document.body, "click", function(evt){
  			  if(autoSuggestList.errorTooltip) {
				  query("#flying-from")[0].value = "";
				  autoSuggestList.errorTooltip = false;
			  }
  			  var targetObj = evt.target || evt.srcElement;
			  if(targetObj.id === "airports-lists" || targetObj.id === "flying-from"){
				  domClass.add(query("#flying-from")[0], "border-sel-active");

				  var fromAir = query("#flying-from")[0].value;
				  var toAir = query("#flying-to")[0].value;
				  var fromAirValue = "";
				  var toAirValue = "",whenMon = "", whenMonValue= "";
				 if(fromAir.length > 0)
					if(fromAir.indexOf("(") !== -1) fromAirValue = fromAir.split("(")[1].split(")")[0];
				 if(toAir.length > 0)
					 if(toAir.indexOf("(") !== -1) toAirValue = toAir.split("(")[1].split(")")[0];
				 /*if(lang.trim(query("#whenCal")[0].value) !== "" && lang.trim(query("#flying-to")[0].value) !== "")query("#whenCal")[0].value="";*/
				 whenMon = dojo.byId("ftselectedMonth");
				 if(whenMon){
					 whenMonValue = whenMon.value;
				 }else{
					 whenMonValue = "";
				 }

				 var targetURL = "ws/timetabledeparture?to[]="+toAirValue+"&when="+whenMonValue;

				  dojo.xhrGet({
					  url: targetURL,
					  handleAs: "json",
					  sync: false,
					  load: function(data, ioargs){
				  		  autoSuggestList.airportStore = data.items;
				  		  /*autoSuggestList.airportCodes = _.keys(autoSuggestList.airportStore);*/
				  		  /*_.each(autoSuggestList.airportCodes, function(code){*/
				  		var ukAirportList = [], overseasAirportList = [];

				  		  _.each(autoSuggestList.airportStore, function(airportInfo){
								if(airportInfo.available) {
									if(airportInfo.countryCode == "GBR"){
										ukAirportList.push(airportInfo);
									}else{
										overseasAirportList.push(airportInfo);
									}
								}
							});

				  		autoSuggestList.airports=ukAirportList.concat(overseasAirportList);
				  		  /*});*/
						  autoSuggestList.placeAirportList();
						  autoSuggestList.eventHandlers();
						  //if(targetObj.id !== "flying-from") autoSuggestList.showAutoSuggestList();
						  autoSuggestList.showAutoSuggestList();
						  autoSuggestList.searchAirportNamesOrCodes("", evt, 0);
						  focusUtil.focus(query("#flying-from")[0]);
					  },
					  error: function(error, ioargs){

					  }
				  });
			  }else{
				  domClass.remove(query("#flying-from")[0], "border-sel-active");
				  autoSuggestList.hideAutoSuggestList();
			  }
		  });
	  },

	  onAfterTmplRender:function(){
		  var autoSuggestList = this;
		  autoSuggestList.inherited(arguments);
		  autoSuggestList.airports = [];
	  },

	  pickPosition:function(){
		  var autoSuggestList = this;
		  var pos = html.coords(autoSuggestList.domNode);
		  return pos;
	  },

	  placeAirportList: function(){
		  var autoSuggestList = this;

		  if(query("#auto-suggest-list")[0])domConstruct.destroy("auto-suggest-list");

		  var closestParent = query("div.inputholders")[0];

		  if(closestParent){
			  var node = domConstruct.toDom(autoSuggestList.render());
			  domConstruct.place(node, closestParent, "last");
		  }

		  var pos = autoSuggestList.pickPosition();

		  domStyle.set(query("#auto-suggest-list")[0], {
			  "top":"25px"
		  });
	  },

	  eventHandlers: function(){
		  var autoSuggestList = this;
		  autoSuggestList.keyUpEvents();
	  },

	  hideAutoSuggestList: function(){
		  if(query("#auto-suggest-list")[0]){
			  domStyle.set(query("#auto-suggest-list")[0], {
			  	  "display":"none"
			  });
		  }
	  },

	  showAutoSuggestList:function(){
		  domStyle.set(query("#auto-suggest-list")[0], {
		  	  "display":"block"
		  });
	  },

	  keyUpEvents: function(){
		  var autoSuggestList = this;
		  var flyingFrom = query("#flying-from")[0];
		  on(flyingFrom, "keypress", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if (charOrCode === keys.DOWN_ARROW || charOrCode === keys.UP_ARROW) {
		            evt.preventDefault();
		      } else if (charOrCode === keys.ENTER) {
		        	if(flyingFrom.value.length == 0) return;
		        	flyingFrom.value = lang.trim(query("li[class='airport-codes-li-visible hlight-airport-list']").text());
		        	autoSuggestList.hideAutoSuggestList();
		        }
     	  });

		  on(query("#auto-suggest-list")[0], "mouseover", function (evt) {
			    query("#auto-suggest-list li.hlight-airport-list").removeClass("hlight-airport-list");
		  });


		  on(flyingFrom, "keyup", function(evt){
			  	var errtips = query(".tooltip", query(".inputholders")[0]);
				if(errtips){
					domClass.remove("flying-from", "error-tool-tip");
					errtips.forEach(domConstruct.destroy);
				}
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.ENTER){
					return;
				}else if (lang.trim(flyingFrom.value) === "") {
					autoSuggestList.hideAutoSuggestList();

			    }else {
			        if (charOrCode == keys.DOWN_ARROW && idx < query("#auto-suggest-list li.airport-codes-li-visible").length - 1) {
			            idx++;
			        } else if (charOrCode == keys.UP_ARROW && idx > 0) {
			            idx--;
			        } else if (charOrCode !== keys.DOWN_ARROW && charOrCode !== keys.UP_ARROW) {
			            idx = 0;
			        }

			        if(flyingFrom.value.length > autoSuggestList.charNo ){

			        	autoSuggestList.showAutoSuggestList();
			        	autoSuggestList.searchAirportNamesOrCodes(flyingFrom.value, evt, idx);

			        }
			    }

		  });

		  /**
		   * handling tab related event
		   */
		  on(flyingFrom, "keydown", function(evt){
			  var charOrCode = evt.charCode || evt.keyCode;
			  if(charOrCode === keys.TAB){
				  if(query("#flying-from")[0].value.length <= 2){
					  autoSuggestList.throwErrorTooltip();
				  }
				  query("#whenCal")[0].value = "";

			  }
		  });

		  //attach events to each li airports
		  query("#auto-suggest-list li").forEach(function (elm, i) {
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
		  var autoSuggestList = this;
		  var count = 0;
			if (idx === undefined) {idx = 0;}
		    var list = query('#auto-suggest-list'),
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
		        	domClass.remove(elm, "airport-codes-li-visible");
		        }
		    });

		    if(count == 0 ){
		    	autoSuggestList.throwErrorTooltip();
		    	count = 1;
		  }
	  },
	  throwErrorTooltip: function(){
			var autoSuggestList = this;
			dojo.byId("auto-suggest-list").style.display = "none";

			try{
				autoSuggestList.errorTooltip = true;

				var errtips = query(".tooltip", query(".inputholders")[0]);
				if(errtips){
					errtips.forEach(domConstruct.destroy);
				}

				new errorTooltip({
		  			  connectId: "#flying-from",
		  			  errorMsg: "Try slecting from the drop down menu or alternatively search by aiport code.",
		  			  errorHeaderMsg: "No Matches Found",
		  			  dynaId: "#flyingFrom"
		  		  });
			}catch(e){}

	  },

	  render: function(){
		  var autoSuggestList = this;
		  var html = autoSuggestList.renderTmpl(autoSuggestList.tmpl);
		  return html;
	  }

  });

  return tui.flights.widget.AutoSuggestList;
});