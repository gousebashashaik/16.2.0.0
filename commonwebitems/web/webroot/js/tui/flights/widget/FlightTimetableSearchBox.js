define("tui/flights/widget/FlightTimetableSearchBox", [
  "tui/searchPanel/view/ErrorPopup",
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
  "tui/search/nls/Searchi18nable",
  "tui/searchPanel/config/SearchConfig",
  "dijit/form/CheckBox",
  "tui/widget/form/SelectOption",
  'dojo/NodeList-traverse',
  "tui/widget/_TuiBaseWidget"], function (ErrorPopup,dojo, flightTimetableSearchBoxTmpl, flightTinyCalendar, query, domConstruct, domStyle, domAttr, on, keys, ready, domClass, domGeom,event, parser, html, flightScheduler, registry, lang, tooltips, errorTooltip,Searchi18nable,SearchConfig) {
  dojo.declare("tui.flights.widget.FlightTimetableSearchBox", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable,Searchi18nable,SearchConfig], {


	  tmpl: flightTimetableSearchBoxTmpl,

	  elm: null,

	  adultLen: 10,

	  adultArr: [],

	  childArr: [],

	  infantAges :[],

	  childAges: [],

	  seasons: [],

	  seasonLength: 0,

	  childLen: 9,

	  elmX: null,

	  elmY: null,

	  errorTooltip: false,

       selectedCurDate: null,

	  selectedReturnDate: null,

	  selectedDepartureDate : null,

       constructor:function(params){
	  		var flightTimetableSearchBox = this,whenDate;
	  		flightTimetableSearchBox.inherited(arguments);
	  		flightTimetableSearchBox.elm = params.elm;
	  		flightTimetableSearchBox.selectedDepartureDate = params.selectedDepartureDate
            var parentTd = query(flightTimetableSearchBox.elm)[0];
            flightTimetableSearchBox.seasonLength = dojo.global.seasonLength;
	  		/*var tdIndex = parentTd.cellIndex;
			tdIndex += 1;

			query(parentTd).parent().prevAll().query("td:nth-child(" + tdIndex + ")").forEach(function(elm){
				if(query(elm).parent().prev().length === 0){
					domStyle.set(elm, {
						borderTop: "3px solid #feb512"
					});
				}
			});*/
	  		flightTimetableSearchBox.selectedDate = query(parentTd).query("input")[0].value;
	  		whenDate = new Date(parseInt(dojo.query(".timeTableMonthSelector .month-sel-active")[0].getAttribute("data-cur-yr")),parseInt(dojo.query(".timeTableMonthSelector .month-sel-active")[0].getAttribute("data-cur-mon")),parseInt(flightTimetableSearchBox.selectedDate))
	  		flightTimetableSearchBox.selectedCurDate = dojo.date.locale.format(whenDate, {selector: "date",datePattern: "yyy-MM-dd"});

	  		//flightTimetableSearchBox.selectedReturnDate



            flightTimetableSearchBox.findPos();
	  		setTimeout(function(){
	  			on(registry.byNode(query(".totalChilds1",flightTimetableSearchBox.elm).parent()[0]),"change",function(){
					query('.timetableChildPax .custom-dropdown').forEach(function(dropdown){
						dijit.byNode(dropdown).destroy();
						query(".childage-label .clear",flightTimetableSearchBox.elm).remove();
					});
					if(parseInt(this.selectNode.value) > 0) {
						if(registry.byNode(query(".timetableOneWay",flightTimetableSearchBox.elm).children()[0]).checked){
							domConstruct.place("<p class='clear'>CHILD AGES (ON FLYING DATE)</p>",query(".timetableChildPax",flightTimetableSearchBox.elm)[0],"before");
						}
						else{
							domConstruct.place("<p class='clear'>CHILD AGES (ON RETURN DATE)</p>",query(".timetableChildPax",flightTimetableSearchBox.elm)[0],"before");
						}
					}
					for(var i=0; i<parseInt(this.selectNode.value);i++){
						if(i==2||i==5){
							select = domConstruct.create("select",{'class': 'dropdown childSelect last','data-dojo-type':'tui.widget.form.SelectOption','data-dojo-props':'maxHeight:180'},query(".timetableChildPax",flightTimetableSearchBox.elm)[0]);
						}
						else{
							select = domConstruct.create("select",{'class': 'dropdown childSelect','data-dojo-type':'tui.widget.form.SelectOption','data-dojo-props':'maxHeight:180'},query(".timetableChildPax",flightTimetableSearchBox.elm)[0]);
						}
						for(var k=0;k<16;k++){
						 if(k==0){
							 var option = domConstruct.create("option",{"value":"-1","label":"-","selected":"selected","innerHTML":"-"},select);
						 }
							var option = domConstruct.create("option",{"value":k,"label":k,"innerHTML":k},select);
						}
					}
					parser.parse(query(".timetableChildPax",flightTimetableSearchBox.elm)[0]);
		    	});
	  		},500)
  	  },

  	  findPos: function(){
  		  var flightTimetableSearchBox = this,
  		  	  tinySearchBox = query("#tiny-search-box")[0],pos;
  		  pos = dojo.coords(flightTimetableSearchBox.elm);
  		  flightTimetableSearchBox.elmX = -238;
  		  flightTimetableSearchBox.elmY = 80;
  		 if(tinySearchBox){
  			flightTimetableSearchBox.destroyTinySearchBox();
  		  }
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
  		  flightTimetableSearchBox.regenerateObjPosition();
  		  flightTimetableSearchBox.elmntMon = parseInt(flightTimetableSearchBox.elmntMon);
  		  flightTimetableSearchBox.elmntYr = parseInt(flightTimetableSearchBox.elmntYr);
  		  query("#season-len-input")[0].value = txtVal;
  		  var initMon = query("#season-len ul li:first-child")[0];
  		  var curIndex = domAttr.get(initMon, "data-cur-idx");
   		  dojo.global.monCurIndx = curIndex;
  		  var schd = new flightTinyCalendar(flightTimetableSearchBox.elmntMon,flightTimetableSearchBox.elmntYr, flightTimetableSearchBox.selectedDate, flightTimetableSearchBox.selectedDepartureDate);
     	  schd.checkAvailableDates(flightTimetableSearchBox.elmntMon,flightTimetableSearchBox.elmntYr, flightTimetableSearchBox.selectedDate);
  		  schd.generateHTML();
  		  var searchTinyDatePicker = query(".search-tinydatepicker")[0];
  		  searchTinyDatePicker.innerHTML = schd.getHTML();
		 if(query(".curDay", searchTinyDatePicker)[0]) {
			 dojo.style(query(".curDay", searchTinyDatePicker).closest('td')[0],"backgroundColor","#fcb512");
		 }
		 if(query(".disabled", searchTinyDatePicker)[0]){
			 _.forEach(query(".disabled", searchTinyDatePicker).closest('td'),function(disabledCell){
	  			 dojo.style(disabledCell,"backgroundColor","#f7f8fa");
	  		});
		 }
		  schd.attachTooltipEvents();
  		  schd.attachClickEvent();
  		  parser.parse(query(".searchpopup")[0],flightTimetableSearchBox.elm);
  		  return pos;
  	  },

  	  regenerateObjPosition :  function(){
  		var flightTimetableSearchBox = this;

  			if(this.elm.cellIndex === 0){
  				dojo.style(dojo.byId("tiny-search-box"),"left",  "0px");
  				query(".arrow-tiny-search").style("right", "430px")
  			}else if( this.elm.cellIndex === 6){
  				dojo.style(dojo.byId("tiny-search-box"),"right", "0px");
  				query(".arrow-tiny-search").style("right", "65px")
  			}else if(this.elm.cellIndex === 1){
  				dojo.style(dojo.byId("tiny-search-box"),"left",  "-145px");
  				query(".arrow-tiny-search").style("right", "280px")
  			}else if( this.elm.cellIndex === 5){
  				dojo.style(dojo.byId("tiny-search-box"),"right", "-145px");
  				query(".arrow-tiny-search").style("right", "200px")
  			}else{
  				dojo.style(dojo.byId("tiny-search-box"),"left", "-190px");
  				query(".arrow-tiny-search").style("right", "242px");
  			}

  	  },
  	  postCreate: function(){
	  		var flightTimetableSearchBox = this,
	  			tinySearchBox = query("#tiny-search-box")[0];
	  		flightTimetableSearchBox.inherited(arguments);
	  		flightTimetableSearchBox.initSearchMessaging();
	  		flightTimetableSearchBox.attachEvents();
	  		/*query(document.body).on("click",function(evt){
	  			if(evt.target.id != "tiny-search-box" && !dojo.hasClass(evt.target,"schd-time")){
	  				if(query("#tiny-search-box")[0]){
	  					flightTimetableSearchBox.destroyTinySearchBox();
	  				}
	  			}
	  		});*/
	  		query("#tiny-search-box").on("click,mouseover,mouseout", function(evt){
	  			evt.stopPropagation();
	  			evtObj = evt.target || evt.srcElement;
	  			if(evt.type === "click"){
	  				if(flightTimetableSearchBox.errorTooltip){
	  					flightTimetableSearchBox.errorTooltip = false;
	  				}

	  				if(evtObj.id !== "return-search-flights" || lang.trim(evtObj.id) === ""){
	  					var errtips = query(".tooltip", tinySearchBox);
						if(errtips){

							query(".error-tool-tip").forEach(function(elm){
								domClass.remove(elm, "error-tool-tip");
							});

							errtips.forEach(domConstruct.destroy);

						}
	  				 }

	  				if(evtObj.id !== "pull-months" && evtObj.id !== "season-len-input")flightTimetableSearchBox.handleClickOnMonthBox();
	  				if(evtObj.id !== "pull-adults")flightTimetableSearchBox.handleClickOnAdultBox();
	  				if(evtObj.id !== "pull-childs")flightTimetableSearchBox.handleClickOnChildBox();
	  				if(evtObj.type === "checkbox")flightTimetableSearchBox.handleClickOnCheckBox(evtObj);
	  			}
	  		});
	  		query("#closeble-search-box").on("click", function(evt){
	  			flightTimetableSearchBox.destroyTinySearchBox();
	  		});
	//START ---
	  		dojo.query(".timetableOneWay .dijitCheckBoxInput").on("click", function(){
	  			flightTimetableSearchBox.disableOneWayReturn();
	  		});

	  		query(".timetableOneWay .tiny-search-label").on("mouseover,mouseout",function(evt){
	  				if(evt.type=="mouseover"){
	  					dojo.query(evt.target).addClass("per-radiospl");
	  					dojo.query(evt.target).prev(".dijitCheckBox").addClass("dijitCheckBoxHover dijitHover");
	  				}
	  				else{
	  					dojo.query(evt.target).removeClass("per-radiospl");
	  					dojo.query(evt.target).prev(".dijitCheckBox").removeClass("dijitCheckBoxHover dijitHover");
	  				}
	  		});

	  		dojo.query(".timetableOneWay .dijitCheckBox").on("mouseover,mouseout",function(evt){
	  			if(evt.type=="mouseover"){
	  				dojo.query(".dijitCheckBox").next(".tiny-search-label").addClass("per-radiospl");
		  		}
	  			else{
	  				dojo.query(".dijitCheckBox").next(".tiny-search-label").removeClass("per-radiospl");
	 	  		}
	  		});

	  		query(".timetableOneWay .tiny-search-label").on("click",function(evt){
	  			var checboxNode =dojo.query(evt.target).prev(".dijitCheckBox")[0],
	  			inputChecbox = dojo.query(".timetableOneWay .dijitCheckBoxInput")[0];
	  			if(!dojo.hasClass(checboxNode,"dijitCheckBoxChecked")){
	  				dijit.byId(inputChecbox.id).set("checked",true);
		  			dojo.query(".dijitCheckBox").next(".tiny-search-label").removeClass("per-radiospl");
	  			}
	  			else{
	  				dijit.byId(inputChecbox.id).set("checked",false);
	  			}
	  			flightTimetableSearchBox.disableOneWayReturn();
	  		});
// END ----
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
	  		query("#season-len-input").on("click", function(evt){
	  			domStyle.set(query("#season-len")[0], {display:"block"});
	  		});

	  		query("#season-len ul li").forEach(function (elm, i) {
			    on(elm, "click", function (evt) {
			    	if(query("#season-len-input")[0].value == query(elm).text()){
			    		domStyle.set(query("#season-len")[0], {display:"none"});
			    		return;
			    	}
			    	query("#season-len-input")[0].value = query(elm).text();
			    	var mnth = parseInt(domAttr.get(elm, "data-cur-mon")),
			    		yr = parseInt(domAttr.get(elm, "data-cur-year")),
			    		schd = new flightTinyCalendar(mnth,yr,flightTimetableSearchBox.selectedDate ,  dojo.global.selectedDepartureDate);
			    		schd.checkAvailableDates(mnth,yr,flightTimetableSearchBox.selectedDate);
			    		schd.generateHTML();
			    		var searchTinyDatePicker = query(".search-tinydatepicker")[0];
			    		searchTinyDatePicker.innerHTML = schd.getHTML();
			    		if(query(".curDay", searchTinyDatePicker)[0]) {
							 dojo.style(query(".curDay", searchTinyDatePicker).closest('td')[0],"backgroundColor","#fcb512");
						}
						if(query(".disabled", searchTinyDatePicker)[0]){
							  _.forEach(query(".disabled", searchTinyDatePicker).closest('td'),function(disabledCell){
					  			 dojo.style(disabledCell,"backgroundColor","#f7f8fa");
					  		 });
					   }
			    		domStyle.set(query("#season-len")[0], {display:"none"});
			    		dojo.global.monCurIndx = domAttr.get(elm, "data-cur-idx");

			    		flightTimetableSearchBox.calArrowsEnableDisable(elm);
			    		flightTimetableSearchBox.getLastMonthandLastYearData(dojo.global.monCurIndx-1, yr);

			    		schd.attachTooltipEvents();
			    		schd.attachClickEvent();
			    });
			 });

	  		//add next prev months
	  		flightTimetableSearchBox.nextPrevMonths();

	  		/*on(document.body, "click", function(evt){

	  			var evtElm = evt.target || evt.srcElement;

	  			if(evtElm.id === "pull-adults" || evtElm.id === "pull-childs")return;
	  			else{
	  				if(query("#adults-len")[0])domStyle.set(query("#adults-len")[0], {display:"none"});
	  				if(query("#child-len")[0])domStyle.set(query("#child-len")[0], {display:"none"});
	  			}

	  		});*/
	  		query(".button.search-submit",dojo.byId("tiny-search-box")).on("click",function(){

	  			if(flightTimetableSearchBox.doPaxValidation() && flightTimetableSearchBox.doRetDateValidation()){
	  				flightTimetableSearchBox.createFormData();
	  				query(".search-form")[0].submit();
	  			} else{
	  				return false
	  			};

			})



  	  },

  	  attachEvents: function(){
  		var flightTimetableSearchBox = this,oneWayCheckBox;

  		on(flightTimetableSearchBox.elm.children[flightTimetableSearchBox.elm.children.length-1],"click",function(evt){
			var elem=evt.target;
			/*if(!dojo.isMozilla){
	    		var evt = event || window.event;
		    	 elem = evt.target || evt.srcElement;
	    	}
	    	else{
	    		elem = window.element;
	    	}*/
			if(dojo.query(elem).closest(".custom-dropdown").length > 0) return;
			var dropDown = dojo.query(".custom-dropdown",flightTimetableSearchBox.elm);
			if(dropDown!=undefined){
    			_.forEach(dropDown,function(item){
    				if(registry.byNode(item)!=undefined)
    				registry.byNode(item).hideList();
				});
			}
			oneWayCheckBox = query(evt.target).closest(".dijitCheckBox");
			if(oneWayCheckBox.length > 0 || domClass.contains(evt.target,"tiny-search-label")){
				if(domClass.contains(evt.target,"tiny-search-label")) oneWayCheckBox = query(".timetableOneWay",flightTimetableSearchBox.elm).children();
				if(registry.byNode(oneWayCheckBox[0]).checked){
					query(".childage-label .clear").text("CHILD AGES (ON FLYING DATE)")
				} else {
					query(".childage-label .clear").text("CHILD AGES (ON RETURN DATE)")
				}

			}
  		});

  	  },


		doPaxValidation: function(){
			var flightTimetableSearchBox = this,infantAges=[],childAges=[],  adultSelectCount,childSelectCount,adultNode,childNode;
			adultNode = registry.byNode(query(".timeTableAdultDropDown",flightTimetableSearchBox.elm).parent()[0]);
			childNode = registry.byNode(query(".totalChilds1",flightTimetableSearchBox.elm).parent()[0]);
			//childNodeChild = registry.byNode(query(".childSelect",flightTimetableSearchBox.elm)[0]);
			adultSelectCount = parseInt(adultNode.selectNode.value);
			childSelectCount = parseInt(childNode.selectNode.value);

			query(".timetableChildPax .childSelect.custom-dropdown",flightTimetableSearchBox.elm).forEach(function(item){
				(function(item){
				var childSelectAge = parseInt(registry.byNode(item).selectNode.value);
				if( childSelectAge <= flightTimetableSearchBox.INFANT_AGE && childSelectAge != -1){
					infantAges.push(childSelectAge);

					childAges.push(childSelectAge);
				}else {
					childAges.push(childSelectAge);
				}
				})(item)

	  		});



			if (adultSelectCount === 0 && childSelectCount === 0) {
				flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.onePassenger,adultNode.domNode);
				dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");
				return false;
			}

			if(adultSelectCount + childSelectCount > flightTimetableSearchBox.MAX_ADULTS_NUMBER ){
				flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.partyLimit + flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.partyLimitHours,childNode.domNode);
				dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");

				if(adultSelectCount !== 0 && infantAges.length > adultSelectCount){
					childNodeChild = registry.byNode(query(".childSelect",flightTimetableSearchBox.elm)[0]);
					flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.infantLimit,childNodeChild.domNode);
					dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");
				}
				else flightTimetableSearchBox.SelectedChildValidaiton();
				return false;
			}



			if(adultSelectCount !== 0 && infantAges.length > adultSelectCount){
				childNodeChild = registry.byNode(query(".childSelect",flightTimetableSearchBox.elm)[0]);
				flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.infantLimit,childNodeChild.domNode);
				dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");
				return false;
			}

			/*flightTimetableSearchBox.SelectedChildValidaiton();*/

			if(adultSelectCount === 0 && childSelectCount > 0 ){
				flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.childOnly,adultNode.domNode);
				dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");
				if(!flightTimetableSearchBox.SelectedChildValidaiton());
				return false;
			}

			if(!flightTimetableSearchBox.SelectedChildValidaiton()) return false;

			flightTimetableSearchBox.adultSelectCount = adultSelectCount;
			flightTimetableSearchBox.childSelectCount = childAges.length - infantAges.length;
			flightTimetableSearchBox.childAges = childAges;
			flightTimetableSearchBox.infantAges = infantAges;
			return true;

  	  },
		SelectedChildValidaiton : function (){
			var flightTimetableSearchBox = this,flag = true;

			query(".timetableChildPax .childSelect.custom-dropdown",flightTimetableSearchBox.elm).forEach(function(item){
				 if(parseInt(registry.byNode(item).selectNode.value) === -1){
					 flag = false;
				 }
			});

			 if(!flag){
				 childNodeChild = registry.byNode(query(".childSelect",flightTimetableSearchBox.elm)[0]);
				if(dijit.byNode(query(".timetableOneWay .dijitCheckBox",flightTimetableSearchBox.elm)[0]).checked){
					 flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.childNoAgesOneWay,childNodeChild.domNode);
				 }
				 else{
					 flightTimetableSearchBox.showErrorPop(flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.childNoAges,childNodeChild.domNode);
				 }
				dojo.query(".custom-dropdown",flightTimetableSearchBox.elm).addClass("error");
			 }

			return flag;
		},
		showErrorPop: function(errorMessage,elementRelativeTo){
			var flightTimetableSearchBox = this,customDropodown;
			customDropodown = dojo.query(".custom-dropdown",flightTimetableSearchBox.elm);
			var x = new ErrorPopup({
		        arrow: false,
		        elementRelativeTo: elementRelativeTo,
		        errorPopupClass: "dealsPax",
		        floatWhere: "position-bottom-center",
		        errorMessage:errorMessage,
		        onClose:function(){
		        	customDropodown.removeClass("error");
		        }
		      });
			x.startup();
			x.open();
			on(customDropodown,"click",function(){
				domClass.remove(this,"error");
				dojo.query(".dealsPax").remove();


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
  		  	  txtI,currentMonth,prevMonth,nextMonth,mon,yr,curIdx;

  		  	query("#nextTinyPicker").on("click", function(evt){
  		  			 targetObj = evt.target || evt.srcElement;
  		  			 if(domClass.contains(targetObj,"disable-next-months")) return;
  		  			/*new tooltips({
  		  				text: "murali",
  		  				floatWhere: "position-top-center"
  		  			});*/

					curIdx = parseInt(dojo.global.monCurIndx);
					curIdx++;
					txtI = query("#season-len ul li[data-cur-idx=\'" + curIdx + "\']")[0];
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
						dojo.global.yearLastIndx = yr;
					}else{
						return;
					}

			});

			query("#prevTinyPicker").on("click", function(evt){
					targetObj = evt.target || evt.srcElement;
					if(domClass.contains(targetObj,"disable-prev-months")) return;
					curIdx = parseInt(dojo.global.monCurIndx);
				curIdx--;

				txtI = query("#season-len ul li[data-cur-idx=\'" + curIdx + "\']")[0];


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
					dojo.global.yearLastIndx = yr;
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

  		if(dojo.global.monLastIndx === '' || dojo.global.monLastIndx == undefined){
			dojo.global.monLastIndx = monthIdx;
		}

  		if(dojo.global.yearLastIndx  === '' || dojo.global.yearLastIndx == undefined){
			if(parseInt(dojo.global.monLastIndx) === 11){
				dojo.global.yearLastIndx = yearIdx - 1;
			}else{
				dojo.global.yearLastIndx = yearIdx;
			}
		}

  	  },

  	  renderTinyCalender: function(m, yr){
  		  var flightTimetableSearchBox = this;

		  var selectedDepartureDate = dojo.global.selectedDepartureDate


  		  var schd = new flightTinyCalendar(m, yr, flightTimetableSearchBox.selectedDate ,selectedDepartureDate);
  		  var searchTinyDatePicker = query(".search-tinydatepicker")[0];
  		  schd.checkAvailableDates(m,yr,flightTimetableSearchBox.selectedDate);
		  schd.generateHTML();
		  searchTinyDatePicker.innerHTML = schd.getHTML();
		  if(query(".curDay", searchTinyDatePicker)[0]) {
				 dojo.style(query(".curDay").closest('td')[0],"backgroundColor","#fcb512");
		 }
		 if(query(".disabled", searchTinyDatePicker)[0]){
			 _.forEach(query(".disabled").closest('td'),function(disabledCell){
	  			 dojo.style(disabledCell,"backgroundColor","#f7f8fa");
	  		});
		 }
		  schd.attachTooltipEvents();
		  schd.attachClickEvent();
  	  },

  	  destroySearchBox: function(){
  		var flightTimetableSearchBox = this;

  	  },

  	  render: function(){
  		  var flightTimetableSearchBox = this;
		  var html = flightTimetableSearchBox.renderTmpl(flightTimetableSearchBox.tmpl);
		  return html;
	  },

	  disableOneWayReturn : function(){
		  var flightTimetableSearchBox = this;
			var returningWhen =dojo.query(".calender-return")[0];
			var checboxNode=dojo.query(".timetableOneWay .dijitCheckBox")[0];
  			if((dojo.hasClass(checboxNode,"dijitCheckBoxChecked")) && (dojo.query(".calender-return").children(".section-overlay").length === 0)){
				 domConstruct.create("div",{"class":"section-overlay"},returningWhen,"first");
				 domClass.add(returningWhen,"disableAll");
				 dojo.query(".yellowBGClick").removeClass("yellowBGClick");
				 dojo.attr(dojo.byId("season-len-input"),"data-ret-date","");
          	}
			else{
				query(".section-overlay",returningWhen).remove();
				domClass.remove(returningWhen,"disableAll");
			}
	  },
		createFormData: function(){
		  var flightTimetableSearchBox = this,flyingFrom,flyingTo,depDate,returnDate,adults,children,infants,infantAge,isOneWay,childAge;

		  flightTimetableSearchBox.createInput("searchType", "pricegrid");
	      /*submitButton.createInput("sp", "true");*/
		  flightTimetableSearchBox.createInput("isFlexible", "Y");

		  flyingFrom= query("#from-airport").text().split("(")[1].replace(")","");
		  flightTimetableSearchBox.createInput("flyingFrom[]",flyingFrom);

		  flyingTo = query("#to-airport").text().split("(")[1].replace(")","");
		  flightTimetableSearchBox.createInput("flyingTo[]", flyingTo);

		  flightTimetableSearchBox.createInput("depDate", flightTimetableSearchBox.selectedCurDate);


		  returnDate = dojo.attr(dojo.byId("season-len-input"),"data-ret-date");

		  flightTimetableSearchBox.createInput("returnDate",returnDate);

		  flightTimetableSearchBox.createInput("adults",flightTimetableSearchBox.adultSelectCount.toString());

		  flightTimetableSearchBox.createInput("children",flightTimetableSearchBox.childSelectCount.toString());

		  flightTimetableSearchBox.createInput("infants",flightTimetableSearchBox.infantAges.length.toString());

		  flightTimetableSearchBox.createInput("isOneWay",registry.byNode(query(".timetableOneWay",flightTimetableSearchBox.elm).children()[0]).checked);

		  flightTimetableSearchBox.createInput("infantAge",flightTimetableSearchBox.infantAges.toString());

		  flightTimetableSearchBox.createInput("childAge",flightTimetableSearchBox.childAges.toString());

		},
		createInput: function (name, value) {
		// summary:
		//    creates input fields for search form
	      var flightTimetableSearchBox = this;
	      var input = dojo.create("input", {"type": "hidden", "name": name, "value": value}, query(".search-form")[0]);
	      console.log(input);
	   },
	   doRetDateValidation: function(){
		   var flightTimetableSearchBox = this;
		   if((dojo.attr(dojo.byId("season-len-input"),"data-ret-date") == null || dojo.attr(dojo.byId("season-len-input"),"data-ret-date") == "") && !registry.byNode(query(".timetableOneWay",flightTimetableSearchBox.elm).children()[0]).checked){
			   var msg = flightTimetableSearchBox.searchMessaging[dojoConfig.site].errors.emptyDate;
			   flightTimetableSearchBox.showErrorPop(msg,dojo.byId("season-len-input"));
			   return false;
		   } else {
			   return true;
		   	 }
	   },
	  destroyTinySearchBox: function(){
		  var flightTimetableSearchBox = this,cell,leftMostCell,topMostCell;
		  		tinySearchBox = query("#tiny-search-box");
		  		cell = query(tinySearchBox.closest("td"));
		  		leftMostCell =cell.siblings()[0];
		  		topMostCell = cell.parents(".schdbody").siblings().children().children();
				dojo.removeClass(cell[0],"click-active");
				dojo.removeClass(leftMostCell,"leftMostCell-click-active");
				dojo.removeClass(topMostCell[cell[0].cellIndex],"topMostCell-click-active");
				dojo.query(".dealsPax").remove();
				domConstruct.destroy(tinySearchBox[0]);
	  }
	 });

  return tui.flights.widget.FlightTimetableSearchBox;
});