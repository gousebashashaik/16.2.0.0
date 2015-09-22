define("tui/flights/widget/FlightTimetableSearchBox", [
  "dojo",
  "dojo/text!tui/flights/templates/FlightTimetableSearchBox.html",
  "tui/flights/view/FlightTinyCalendar",
  "dojo/query",
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/on",
  "dojo/keys",
  "dojo/ready",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/_base/event",
  "dojo/parser",
  'dojo/_base/html',
  "tui/flights/view/FlightScheduler",
  "dijit/registry",
  "dojo/_base/lang",
  "tui/widget/popup/Tooltips",
  "tui/flights/widget/ErrorTooltip",
  'dojo/NodeList-traverse',
  "tui/widget/_TuiBaseWidget"], function (dojo, flightTimetableSearchBoxTmpl, flightTinyCalendar, query, domConstruct, domStyle, domAttr, on, keys, ready, domClass, domGeom,event, parser, html, flightScheduler, registry, lang, tooltips, errorTooltip) {
  dojo.declare("tui.flights.widget.FlightTimetableSearchBox", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
	  
	    
	  tmpl: flightTimetableSearchBoxTmpl,
	  
	  elm: null,
	  
	  adultLen: 10,
	  
	  adultArr: [],
	  
	  childArr: [],
	  
	  childAges: [],
	  
	  seasons: [],
	  
	  seasonLength: 18,
	  
	  childLen: 9,
	  
	  elmX: null,
	  
	  elmY: null,
	  
	  errorTooltip: false,
	  
	  constructor:function(params){
	  		var flightTimetableSearchBox = this;
	  		flightTimetableSearchBox.inherited(arguments);
	  		
	  		flightTimetableSearchBox.elm = params.elm;
	  		
	  		var parentTd = query(flightTimetableSearchBox.elm).parent().parent()[0];
	  		
	  		var tdIndex = parentTd.cellIndex;
			tdIndex += 1;
			
			query(parentTd).parent().prevAll().query("td:nth-child(" + tdIndex + ")").forEach(function(elm){
				if(query(elm).parent().prev().length === 0){
					domStyle.set(elm, {
						borderTop: "3px solid #feb512"
					});
				}
			});
	  		flightTimetableSearchBox.selectedDate = query(parentTd).query("input")[0].value;
	  		
	  		flightTimetableSearchBox.findPos();
  	  },
  	  
  	  findPos: function(){
  		  var flightTimetableSearchBox = this;
  		
  		  var pos = dojo.coords(flightTimetableSearchBox.elm);
  		  
  		  flightTimetableSearchBox.elmX = -238;
  		  flightTimetableSearchBox.elmY = 80;
  		  
  		  if(query("#tiny-search-box")[0]) domConstruct.destroy(query("#tiny-search-box")[0]);
  		  
  		  var fs = new flightScheduler();
  		  
  		  flightTimetableSearchBox.adultArr = [];
  		  flightTimetableSearchBox.childArr = [];
  		  flightTimetableSearchBox.seasons = [];
  		  flightTimetableSearchBox.childAges = [];
  		  
  		  for(var i=0; i < flightTimetableSearchBox.adultLen; i++){
  			  	flightTimetableSearchBox.adultArr.push(i);
  		  }
  		  
  		  for(var i=0; i < flightTimetableSearchBox.childLen; i++){
			  	flightTimetableSearchBox.childArr.push(i);
		  }
  		  
  		  var elmnt = query(".month-sel-active")[0];
  		  
  		  flightTimetableSearchBox.elmntMon = domAttr.get(elmnt, "data-cur-mon");
  		  flightTimetableSearchBox.elmntYr = domAttr.get(elmnt, "data-cur-yr");
  		  flightTimetableSearchBox.elmntIdx = domAttr.get(elmnt, "data-cur-idx");
  		  
  		  flightTimetableSearchBox.elmntMon = parseInt(flightTimetableSearchBox.elmntMon);
  		  
  		  flightTimetableSearchBox.elmntIdx = parseInt(flightTimetableSearchBox.elmntIdx);
  		  
  		  var ij = 0;
  		  var mnthIdx = flightTimetableSearchBox.elmntMon; 
  		  var curIndx = mnthIdx;
  		  for(var j=flightTimetableSearchBox.elmntIdx; j < flightTimetableSearchBox.seasonLength; j++){
  			  var arr = [];
  			  var monthsplit = (new flightScheduler()).getMnthYear(j);
  			  arr = monthsplit.split("@");
  			  var obj = {};
  			  obj.month  = arr[1] + "  " + arr[0];
  			  
  			  if(mnthIdx <= 11){
  				  obj.idx = mnthIdx;
  				  mnthIdx++;
  			  }else if(mnthIdx > 11){
  				  obj.idx = ij;
  				  ij++;
  			  }
  			  	
  			  obj.yr = parseInt(arr[0]);
  			  obj.curIdx = curIndx++;
			  flightTimetableSearchBox.seasons.push(obj);
  		  }
  		  for(var k=0; k<18; k++){
  			  flightTimetableSearchBox.childAges.push(k);
  		  }
  		  var curMonandYr = (new flightScheduler()).getMnthYear(flightTimetableSearchBox.elmntIdx).split("@");
  		  var txtVal = curMonandYr[1] + "  " + curMonandYr[0];
  		  
  		  var renderedHTML = flightTimetableSearchBox.render();
  		  
  		  domConstruct.place(renderedHTML, flightTimetableSearchBox.elm, "last");
  		  
  		  flightTimetableSearchBox.elmntMon = parseInt(flightTimetableSearchBox.elmntMon);
  		  flightTimetableSearchBox.elmntYr = parseInt(flightTimetableSearchBox.elmntYr);
  		  
  		  query("#season-len-input")[0].value = txtVal;
  		  
  		  var initMon = query("#season-len ul li:first-child")[0];
  		  
  		  var curIndex = domAttr.get(initMon, "data-cur-idx");
  		  
  		  dojo.global.monCurIndx = curIndex; 
  		  
  		  var schd = new flightTinyCalendar(flightTimetableSearchBox.elmntMon,flightTimetableSearchBox.elmntYr, flightTimetableSearchBox.selectedDate);
  		   		  
     	  schd.checkAvailableDates(flightTimetableSearchBox.elmntMon,flightTimetableSearchBox.elmntYr, flightTimetableSearchBox.selectedDate);
  		  
  		  schd.generateHTML();
  		  query(".search-tinydatepicker")[0].innerHTML = schd.getHTML(); 
  		  schd.attachTooltipEvents();
  		  return pos;
  	  },
  	  
  	  postCreate: function(){
	  		var flightTimetableSearchBox = this;
	  		flightTimetableSearchBox.inherited(arguments);
	  	
	  		
	  		query("#tiny-search-box").on("click,mouseover,mouseout", function(evt){
	  			evt.stopPropagation();
	  			evtObj = evt.target || evt.srcElement;
	  			if(evt.type === "click"){
	  				if(flightTimetableSearchBox.errorTooltip){
	  					flightTimetableSearchBox.errorTooltip = false;
	  				}
	  				
	  				if(evtObj.id !== "return-search-flights" || lang.trim(evtObj.id) === ""){
	  					var errtips = query(".tooltip", query("#tiny-search-box")[0]);
						if(errtips){

							query(".error-tool-tip").forEach(function(elm){
								domClass.remove(elm, "error-tool-tip");
							});
							
							errtips.forEach(domConstruct.destroy);
							
						}
	  				 }
	  				
	  				if(evtObj.id !== "pull-months")flightTimetableSearchBox.handleClickOnMonthBox();
	  				if(evtObj.id !== "pull-adults")flightTimetableSearchBox.handleClickOnAdultBox();
	  				if(evtObj.id !== "pull-childs")flightTimetableSearchBox.handleClickOnChildBox();
	  				if(evtObj.type === "checkbox")flightTimetableSearchBox.handleClickOnCheckBox(evtObj);
	  			}
	  		});
	  		
	  		query("#closeble-search-box").on("click", function(evt){
	  			 domConstruct.destroy(query("#tiny-search-box")[0]);
	  		});
	  		query("#oneway-date-search").on("click", function(evt){
	  			domConstruct.destroy(query("#tiny-search-box")[0]);
	  		});
	  		
	  		query("#pull-adults").on("click", function(evt){
	  			domStyle.set(query("#adults-len")[0], {display:"block"});
	  		});
	  		
	  		query("#adults-len ul li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	query("#cnt-adults")[0].value = query(elm).text();
			    	domStyle.set(query("#adults-len")[0], {display:"none"});
			    });
			 });
	  		
	  		query("#child-len ul li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	query("#cnt-childs")[0].value = query(elm).text();
			    	domStyle.set(query("#child-len")[0], {display:"none"});
			    });
			 });
	  		
	  		query("#pull-childs").on("click", function(evt){
	  			domStyle.set(query("#child-len")[0], {display:"block"});
	  		});
	  		
	  		query("#pull-months").on("click", function(evt){
	  			domStyle.set(query("#season-len")[0], {display:"block"});
	  		});
	  		
	  		query("#season-len ul li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	query("#season-len-input")[0].value = query(elm).text();
			    	var mnth = parseInt(domAttr.get(elm, "data-cur-mon")),
			    		yr = parseInt(domAttr.get(elm, "data-cur-year")),
			    		schd = new flightTinyCalendar(mnth,yr,flightTimetableSearchBox.selectedDate);
			    		schd.checkAvailableDates(mnth,yr,flightTimetableSearchBox.selectedDate);
			    		schd.generateHTML();
			    		query(".search-tinydatepicker")[0].innerHTML = schd.getHTML();
			    		domStyle.set(query("#season-len")[0], {display:"none"});
			    		dojo.global.monCurIndx = domAttr.get(elm, "data-cur-idx");		    	
			    				    	
			    		flightTimetableSearchBox.calArrowsEnableDisable(elm);
			    		flightTimetableSearchBox.getLastMonthandLastYearData(dojo.global.monCurIndx-1, yr);			    	
			    						
			    		schd.attachTooltipEvents();
			    });
			 });
	  		
	  		//add next prev months 
	  		flightTimetableSearchBox.nextPrevMonths();
	  		
	  		query("#return-search-flights").on("click", function(){
	  			/*if(query("#cnt-adults")[0].value === "0" && query("#cnt-childs")[0].value > "0"){
	  				new errorTooltip({
	  					connectId: "#cnt-adults",
	  					errorMsg: "",
	  					errorHeaderMsg:"Please select at least one Adult passenger before proceeding.",
	  					id:"errorId"
	  				});
	  			}*/
	  			
	  			
	  			//Please enter the ages of the children on return of your holiday.
	  			
	  			// < query("#cnt-childs")[0].value
	  			
	  			if(query("#cnt-adults")[0].value === "0"){
	  				new errorTooltip({
	  					connectId: "#cnt-adults",
	  					errorMsg: "",
	  					errorHeaderMsg:"Please select at least one Adult passenger before proceeding.",
	  					dynaId:"errorId"
	  				});
	  				flightTimetableSearchBox.errorTooltip = true;
	  			}
	  			
	  			var isChildAgeEmpty = false;
	  			query("input.input-child-ages").forEach(function(elm, i){
	  				var isDisplayed = domStyle.get(query(elm).parent()[0], "display");
		  				if(lang.trim(elm.value) === "" && isDisplayed !=="none" ){
		  					domClass.add(elm, "error-tool-tip");
		  					isChildAgeEmpty = true;
			  				return false;	
		  				}
	  			});
	  			
	  			if(isChildAgeEmpty) {
	  				new errorTooltip({
	  					connectId: "#cnt-childs1",
	  					errorMsg: "",
	  					errorHeaderMsg:"Please enter the ages of the children on return of your holiday.",
	  					dynaId:"errorId"
	  				});
	  			}
	  			
	  			isChildAgeEmpty = false;
	  			
	  			var countP = parseInt(query("#cnt-adults")[0].value) + parseInt(query("#cnt-childs")[0].value);
	  			
	  			if(countP > 9){
	  				new errorTooltip({
	  					connectId: "#cnt-adults",
	  					errorMsg: "",
	  					errorHeaderMsg:"We are unable to take bookings for more than 9 passengers through our website. Please call 0871 664 0137 for group bookings.",
	  					dynaid:"errorIdMoreCount"
	  				});
	  				flightTimetableSearchBox.errorTooltip = true;
	  			}
	  			
	  			
	  			var chkAdultsCount = 0;
	  			var adultCnt = parseInt(query("#cnt-adults")[0].value);
	  			
	  			query("input.input-child-ages").forEach(function(elm){
	  				if(parseInt(elm.value) <= 1){
	  					chkAdultsCount++;
	  				}
	  			});
	  			
	  			if(chkAdultsCount > adultCnt){
	  				new errorTooltip({
	  					connectId: "#cnt-childs1",
	  					errorMsg: "",
	  					errorHeaderMsg:"For legal reasons, the number of infants needs to be the same as or less than the number of Adults.",
	  					dynaid:"errorIdMoreCount"
	  				});
	  				flightTimetableSearchBox.errorTooltip = true;
	  			}
	  			
	  		});
	  		
	  		
	  		on(document.body, "click", function(evt){
	  			
	  			var evtElm = evt.target || evt.srcElement;
	  			
	  			if(evtElm.id === "pull-adults" || evtElm.id === "pull-childs")return;
	  			else{
	  				if(query("#adults-len")[0])domStyle.set(query("#adults-len")[0], {display:"none"});
	  				if(query("#child-len")[0])domStyle.set(query("#child-len")[0], {display:"none"});
	  			}
	  			
	  		});
  	  },
  	  
  	  onAfterTmplRender: function(){
  		var flightTimetableSearchBox = this;
  	  },
  	  
  	  handleClickOnMonthBox: function(){
  		  var seasonLen = query("#season-len")[0];
	  		  if(seasonLen){
		  		  domStyle.set(seasonLen, {
		  			  "display":"none"
		  		  });
	  		  }
  	  },
  	  
  	  handleClickOnAdultBox: function(){
  		var adultLen = query("#adults-len")[0];
		  if(adultLen){
	  		  domStyle.set(adultLen, {
	  			  "display":"none"
	  		  });
		  }
  	  },
  	  
  	  handleClickOnChildBox: function(){
  		try{
	  		var childLen = query("#child-len")[0];
	  		var noOfChilds = query("#cnt-childs")[0].value;
  		}catch(e){}
  		
  		for(i=1; i<=8; i++){
  			if(dojo.byId("age"+i)){
  				dojo.byId("age"+i).style.display = "none";
  			}
  		}
  		for(i=1; i<=noOfChilds; i++){
  			if(dojo.byId("age"+i)){
  				dojo.byId("age"+i).style.display = "inline-block";
  			}
  		}
  		
		if(childLen){
	  		  domStyle.set(childLen, {
	  			  "display":"none"
	  		  });
		}
		
		query("img.pull-child-ages").on("click", function(evt){
			event.stop(evt);
  			evtObj = evt.target || evt.srcElement;
  			var obj = query(evtObj).parent().query("div.childages-len")[0];
  			var inputTxt = query(evtObj).parent().query("input")[0];
  			domStyle.set(obj, {
  				"display":"block"
  			});
  			query("ul li", obj).on("click", function(){
  				inputTxt.value =  query(this).text();
  				domStyle.set(obj, {
  	  				"display":"none"
  	  			});
  			});
		});
		query("div.childages-len").forEach(function(elm, i){
  			domStyle.set(elm, {display:"none"});
  		});
		
		
  	  },
  	  
  	  handleClickOnCheckBox: function(evtObj){
  		  // do nothing
  		  
  	  },
  	  
  	  handleMouseOverOnBox: function(){
  		
  	  },
  	  
  	  showSearchBox: function(){
  		  var flightTimetableSearchBox = this;
  	  },
  	  
  	  nextPrevMonths: function(){
  		  
  		  var flightTimetableSearchBox = this;
  		  var nextMonth = query("#nextTinyPicker")[0],
  		  	  prevMonth = query("#prevTinyPicker")[0],
  		  	  targetObj,
  		  	  txtI,mon,yr,curIdx;
  		  	  
  		  	query("#nextTinyPicker").on("click", function(evt){
  		  			 targetObj = evt.target || evt.srcElement;  		  				
  		  				
  		  			/*new tooltips({
  		  				text: "murali",
  		  				floatWhere: "position-top-center"
  		  			});*/
  		  			
					curIdx = parseInt(dojo.global.monCurIndx);
										
					curIdx++;					
				
					txtI = query("#season-len ul li[data-cur-idx=" + curIdx + "]")[0];
					yr = parseInt(domAttr.get(txtI, "data-cur-year"));
					
					flightTimetableSearchBox.getLastMonthandLastYearData(dojo.global.monCurIndx, yr);
					
					flightTimetableSearchBox.calArrowsEnableDisable(txtI);
					
					/*
					try{
						nextObj = query(txtI).next()[0];
					}catch(e){}
					
					if(!nextObj){
						if(domClass.contains(targetObj, "enable-next-months")) domClass.remove(targetObj, "enable-next-months");
						domClass.add(targetObj, "disable-next-months");
						if(domClass.contains(prevMonth, "disable-prev-months")) domClass.remove(prevMonth, "disable-prev-months");
						domClass.add(prevMonth, "enable-prev-months");
					}else{
						if(domClass.contains(prevMonth, "disable-prev-months")) domClass.remove(prevMonth, "disable-prev-months");
						domClass.add(prevMonth, "enable-prev-months");
					}*/
					
					if(txtI){
						query("#season-len-input")[0].value = query(txtI).text();
						mon = parseInt(domAttr.get(txtI, "data-cur-mon"));
						//var yr = parseInt(domAttr.get(txtI, "data-cur-year"));
						flightTimetableSearchBox.renderTinyCalender(mon, yr);
						dojo.global.monCurIndx = curIdx;
					}else{
						return;
					}
					
			});
			
			query("#prevTinyPicker").on("click", function(evt){
					targetObj = evt.target || evt.srcElement;
					
					curIdx = parseInt(dojo.global.monCurIndx);
				
				curIdx--;				
				
				txtI = query("#season-len ul li[data-cur-idx=" + curIdx+ "]")[0];
				
				
				flightTimetableSearchBox.calArrowsEnableDisable(txtI);
				
				/*
				 try{
					prevObj = query(txtI).prev()[0];
				}catch(e){}
				
				if(!prevObj){
					if(domClass.contains(targetObj, "enable-prev-months")) domClass.remove(targetObj, "enable-prev-months");
					domClass.add(targetObj, "disable-prev-months");
					if(domClass.contains(nextMonth, "disable-next-months")) domClass.remove(nextMonth, "disable-next-months");
					domClass.add(nextMonth, "enable-next-months");
				}else{
					if(domClass.contains(nextMonth, "disable-next-months")) domClass.remove(nextMonth, "disable-next-months");
					domClass.add(nextMonth, "enable-next-months");
				}
				*/
				if(txtI){
					query("#season-len-input")[0].value = query(txtI).text();
					mon = parseInt(domAttr.get(txtI, "data-cur-mon"));
					yr = parseInt(domAttr.get(txtI, "data-cur-year"));
					flightTimetableSearchBox.renderTinyCalender(mon, yr);
					dojo.global.monCurIndx = curIdx;
				}else{
					return;
				}
			});
			
  	  },
  	  calArrowsEnableDisable:function(obj){
  		var flightTimetableSearchBox = this,
  			nextObj,
  			prevObj,
  			nextMonth = query("#nextTinyPicker")[0],
  			prevMonth = query("#prevTinyPicker")[0];
  		
	  		try{
				 nextObj = query(obj).next()[0];
				 prevObj = query(obj).prev()[0];
			}catch(e){}
			
		
  			if(!nextObj){
	  			if(domClass.contains(nextMonth, "enable-next-months")) domClass.remove(nextMonth, "enable-next-months");
				domClass.add(nextMonth, "disable-next-months");
				if(domClass.contains(prevMonth, "disable-prev-months")) domClass.remove(prevMonth, "disable-prev-months");
				domClass.add(prevMonth, "enable-prev-months");
			}else if(!prevObj){			
				if(domClass.contains(prevMonth, "enable-prev-months")) domClass.remove(prevMonth, "enable-prev-months");
				domClass.add(prevMonth, "disable-prev-months");
				if(domClass.contains(nextMonth, "disable-next-months")) domClass.remove(nextMonth, "disable-next-months");
				domClass.add(nextMonth, "enable-next-months");
			} else{
				domClass.remove(prevMonth, "disable-prev-months");
				domClass.remove(nextMonth, "disable-next-months");
				
				domClass.add(prevMonth, "enable-prev-months");
				domClass.add(nextMonth, "enable-next-months");
			}
  		  
  	  },
  	  
  	  getLastMonthandLastYearData: function(monthIdx, yearIdx){
  		var flightTimetableSearchBox = this;
  		
  		if(dojo.global.monLastIndx === ''){
			dojo.global.monLastIndx = monthIdx;
		}
  		
  		if(dojo.global.yearLastIndx  === ''){
			if(parseInt(dojo.global.monLastIndx) === 11){
				dojo.global.yearLastIndx = yearIdx - 1;
			}else{
				dojo.global.yearLastIndx = yearIdx;
			}
		}		
  		
  	  },
  	  
  	  renderTinyCalender: function(m, yr){
  		  var flightTimetableSearchBox = this;
  		  var schd = new flightTinyCalendar(m, yr, flightTimetableSearchBox.selectedDate);
  		  schd.checkAvailableDates(m,yr,flightTimetableSearchBox.selectedDate);
		  schd.generateHTML();
		  query(".search-tinydatepicker")[0].innerHTML = schd.getHTML();
		  schd.attachTooltipEvents();
  	  },
  	  
  	  destroySearchBox: function(){
  		var flightTimetableSearchBox = this;
  		
  	  },
  	  
  	  render: function(){
  		  var flightTimetableSearchBox = this;
		  var html = flightTimetableSearchBox.renderTmpl(flightTimetableSearchBox.tmpl);
		  return html;
	  }
	  
  });
  
  return tui.flights.widget.FlightTimetableSearchBox;
});