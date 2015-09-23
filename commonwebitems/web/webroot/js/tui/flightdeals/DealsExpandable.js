define("tui/flightdeals/DealsExpandable", [
   "tui/widget/form/flights/Expandable",
   "dojo",
   "dojo/on",
   "dojo/_base/declare",
   "dojo/text!tui/flightdeals/templates/ICanFlyOut.html",
   "dojo/_base/fx",
   "dijit/registry",
   "dojo/query",
   "dojo/dom-class",
   "dojo/_base/xhr",
   "dojox/dtl/Context",
   "dojo/NodeList-traverse",
   "tui/flightdeals/model/DealsPanelModel"
   ],function(Expandable,dojo,on,declare,ICanFlyOutTmpl,fx,registry,query,domClass,xhr){

		declare("tui.flightdeals.DealsExpandable",[Expandable],{
			// ----------------------------------------------------------------------------- properties

	    	value: [],

	    	queryObj: {},

	    	standby: "",

	    	cal_months_labels : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],

	    	cal_months_shot_labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'July', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],

	    	flexibleChecked			: false,

	    	selectedData	: "",

	    	// ----------------------------------------------------------------------------- methods
	    	postCreate: function () {
	    		var DealsExpandable = this;
	    		DealsExpandable.inherited(arguments);
	    		DealsExpandable.tagElement(DealsExpandable.domNode, "FO_I_Can_Fly_Out");

	    	},
    	    onAfterTmplRender: function () {
    	    	var DealsExpandable = this;
	    		DealsExpandable.inherited(arguments);

	    		//parsing the template and placing inside the expandable dom node.
	    		target = dojo.query(".deals-wrapper",DealsExpandable.expandableDom)[0];
	    		dojo.html.set(target,ICanFlyOutTmpl,{
					parseContent: true
				});
	    		DealsExpandable.standby = DealsExpandable.standBy(dojo.query(".deals-wrapper div",DealsExpandable.expandableDom)[0]);
	    		//Getting the option from search panel monthsAndYears and created the options
	    		registry.byId("monthSelect").listData = dojo.global.monthsAndYears;
	    		registry.byId("monthSelect").appendOption("<span style='font-style:italic; color:#666; font-size:13.5px;' class='disable-list-select'>Please select</span>", "",0);
	    		registry.byId("monthSelect").renderList();
	    		registry.byId("monthSelect").disableItem(0);

	    		//Attach flyout specific events
	    		DealsExpandable.attachFlyOutEvents();
	    		DealsExpandable.attachExpandableEvent();
	    		DealsExpandable.populateSavedValues();


	    		DealsExpandable.tagElement(DealsExpandable.expandableDom, "Overall_I_Can_Fly_Out");

    	    },

    	    populateSavedValues :function(){
    	    	var dealsExpandable = this,monthSelector,selectedValue ;
    	    		monthSelector= dijit.byId("monthSelect");
    	    		selectedValue = dealsExpandable.dealsPanelModel.month + "/" + dealsExpandable.dealsPanelModel.year;
    	    		monthSelector.setSelectedValue(selectedValue);
    	    		if(!dealsExpandable.dealsPanelModel.dealsFilterStatus){
    	    			dealsExpandable.dealsPanelModel.disableDealsButton();
	    			}
    	    		if(dealsExpandable.dealsPanelModel.flexible){
    	    			dijit.byId("flexible").setChecked(true);
    	    		}

	    				switch(dealsExpandable.dealsPanelModel.days){
	    					case 7:
	    						if(!dijit.byId("sevenDays").disabled) dijit.byId("sevenDays").set("checked",true);
	    						break;
	    					case 14:
	    						if(!dijit.byId("twoWeeks").disabled) dijit.byId("twoWeeks").set("checked",true);
	    						break;
	    					case 43:
	    						if(!dijit.byId("sixWeeks").disabled) dijit.byId("sixWeeks").set("checked",true);
	    						break;
	    					case 93:
	    						if(!dijit.byId("threeMonths").disabled) dijit.byId("threeMonths").set("checked",true);
	    						break;
	    					default :
	    						dealsExpandable.updateIcanFlyView();
	    				}

    	    },


    	    updateIcanFlyView : function(){
    	    	var dealsExpandable = this, days,
	    	    	seasonLastObject,
	    	    	seasonLastMonth,
	    	    	seasonLastYear;
    	    		if(dealsExpandable.dealsPanelModel.month){
    	    			dealsExpandable.dealsPanelModel.month =parseInt(dealsExpandable.dealsPanelModel.month);

    	    			monthName = dealsExpandable.cal_months_labels[dealsExpandable.dealsPanelModel.month-1];
    	    			shotNameMonthName = dealsExpandable.cal_months_shot_labels[dealsExpandable.dealsPanelModel.month-1];

    	    			query(".placeholder",dealsExpandable.domNode)[0].innerHTML	= "<span>"+ monthName +" "+parseInt(dealsExpandable.dealsPanelModel.year) + "</span>";

    	    			if(dojo.byId("withinID")!==null && dojo.byId("withinID")!==undefined ){
    	    				dojo.byId("withinID").style.display="none";
    	    			}


    	    			if(dealsExpandable.dealsPanelModel.flexible){
    	    					var dt = new Date();
    	    					dealsExpandable.dealsPanelModel.month =parseInt(dealsExpandable.dealsPanelModel.month);
    	    					dealsExpandable.dealsPanelModel.year =parseInt(dealsExpandable.dealsPanelModel.year);

    	    					if(dt.getMonth() === (dealsExpandable.dealsPanelModel.month-1) && dt.getFullYear() === dealsExpandable.dealsPanelModel.year){
    	    						startMonth = dealsExpandable.cal_months_shot_labels[dealsExpandable.dealsPanelModel.month-1];
    	    					}else if(dealsExpandable.dealsPanelModel.month === 1){
    	    						startMonth = dealsExpandable.cal_months_shot_labels[11];
    	    					}
    	    					else{
    	    						startMonth = dealsExpandable.cal_months_shot_labels[dealsExpandable.dealsPanelModel.month-2];
    	    					}

    	    					seasonLastObject = dojo.global.monthsAndYears[dojo.global.monthsAndYears.length-1];
    	    					seasonLastMonth = parseInt(seasonLastObject.value.split("/")[0]);
    	    					seasonLastYear = parseInt(seasonLastObject.value.split("/")[1]);

    	    					if(dealsExpandable.dealsPanelModel.month === 12){
	    	    					endMonth = dealsExpandable.cal_months_shot_labels[0];
	    	    				}else if(dealsExpandable.dealsPanelModel.month === seasonLastMonth && dealsExpandable.dealsPanelModel.year === seasonLastYear){
	    	    					endMonth = dealsExpandable.cal_months_shot_labels[dealsExpandable.dealsPanelModel.month-1];
	    	    				}else{
	    	    					endMonth = dealsExpandable.cal_months_shot_labels[dealsExpandable.dealsPanelModel.month];
	    	    				}

    	    					startYear = (dealsExpandable.dealsPanelModel.month === 1) ? dealsExpandable.dealsPanelModel.year-1 :  dealsExpandable.dealsPanelModel.year
    	    					endYear = (dealsExpandable.dealsPanelModel.month === 12) ? dealsExpandable.dealsPanelModel.year+1 :  dealsExpandable.dealsPanelModel.year

    	    				if(dojo.byId("selectedDayDuration")!==null && dojo.byId("selectedDayDuration")!==undefined)dojo.byId("selectedDayDuration").innerHTML	=  startMonth +" "+ startYear +" - "+ endMonth +" "+ endYear ;
    	    			}else if(dojo.byId("selectedDayDuration")!==null && dojo.byId("selectedDayDuration")!==undefined){
    	    				dojo.byId("selectedDayDuration").innerHTML	=  shotNameMonthName +" "+ dealsExpandable.dealsPanelModel.year ;
    	    			}
    	    			//query("#dealSearchPanelContainer .dealsbutton").removeClass("disabled").addClass("cta");

    	    		}else if(dealsExpandable.dealsPanelModel.days) {
    	    				dealsExpandable.dealsPanelModel.days = parseInt(dealsExpandable.dealsPanelModel.days);

    	    			if(dealsExpandable.dealsPanelModel.days === 7){
    	    				days =" 7 days";
    	    			}else if(dealsExpandable.dealsPanelModel.days === 14 ){
    	    				days =" 2 weeks";
    	    			}else if(dealsExpandable.dealsPanelModel.days === 43 ){
    	    				days = " 6 weeks";
    	    			}else if (dealsExpandable.dealsPanelModel.days === 93){
    	    				days = " 3 months";
    	    			}

    	    			if(dojo.byId("inId")!==null && dojo.byId("inId")!==undefined){
    	    				dojo.byId("inId").style.display="none";
    	    			}

    	    			query(".placeholder",dealsExpandable.domNode)[0].innerHTML	= "<span> Within "+ days + "</span>";
    	    			if(dojo.byId("selectedDayDuration")!==null && dojo.byId("selectedDayDuration")!==undefined){
    	    				dojo.byId("selectedDayDuration").innerHTML	=  dealsExpandable.dealsPanelModel.days + " days";
    	    			}
    	    		}

    	    },


    	    closeExpandable: function(){
		    	var DealsExpandable = this;
		    	DealsExpandable.standby.hide();
		    	DealsExpandable.inherited(arguments);

		    },
    	    attachFlyOutEvents: function(){
    	    	var DealsExpandable = this;

    	    	dojo.query(".dijitRadio",DealsExpandable.expandableDom).on("click",function(evt){
    	    		query(".placeholder",DealsExpandable.domNode)[0].innerHTML = "<span>"+ query(registry.byNode(this).domNode).next("label").text() + "</span>";
    	    		DealsExpandable.dealsPanelModel.enableDealsButton();
    	    		registry.byId("monthSelect").setSelectedValue("");
    	    		DealsExpandable.selectedData =  registry.byNode(this).value;
    	    		 registry.byNode(this).set("checked",true);
    	    		//DealsExpandable.dealsPanelModel.updateBugetState();
    	    	});

    	    	on(registry.byId("monthSelect"),"change",function(){
    	    		if(this.selectNode.value == ""){
    	    			registry.byId("flexible").reset();
    	    			registry.byId("flexible").set("disabled",true);
    	    			DealsExpandable.dealsPanelModel.month = null;
    	    			DealsExpandable.dealsPanelModel.year = null;
    	    			DealsExpandable.dealsPanelModel.flexible = false;
    	    		} else {
    	    			registry.byId("flexible").set("disabled",false);
    	    			DealsExpandable.dealsPanelModel.enableDealsButton();

    	    			// reset radio options when selected.
    	    			dojo.query(".content .dijitRadio.dijitRadioChecked").forEach(function(item){
    	    				registry.byNode(item).set("checked",false);
    	    			});
    	    			DealsExpandable.selectedData = this.selectNode.value;
    	    			//DealsExpandable.dealsPanelModel.days = null;
    	    			query(".placeholder",DealsExpandable.domNode)[0].innerHTML	= "<span>"+ query(this.selectDropdownLabel).text()+ "</span>";
    	    			//DealsExpandable.dealsPanelModel.updateBugetState();
    	    		}
    	    	});

    	    	dojo.query("a.close",DealsExpandable.expandableDom).on("click",function(){
    	    		DealsExpandable.hideWidget(DealsExpandable.expandableDom);
    	    	})
    	    	on(registry.byId("flexible"),"change",function(){

    	    		if(this.checked){
    	    			DealsExpandable.flexibleChecked = true;
    	    		} else {
    	    			DealsExpandable.flexibleChecked = false;
    	    		}
    	    		if(!DealsExpandable.dealsPanelModel.dealsFilterStatus){
    	    			DealsExpandable.dealsPanelModel.enableDealsButton();
    	    		}
    	    	});

	    		DealsExpandable.attachBodyEvent();

    	    },

    	    onOpenExpandable: function(){
    	    	var DealsExpandable = this;
    	    	DealsExpandable.standby.show();
    	    	var def = DealsExpandable.doXhrPost("ws/flyout");
    	    	def.then(function(data){

    	    		var keys = Object.keys(data);
    	    		_.forEach(keys,function(item){
    	    			dijit.byId(item).set('disabled',!data[item]);

    	    		});

    	    		DealsExpandable.standby.hide();

    	    	})
    	    }


		});
		return tui.flightdeals.DealsExpandable;
})