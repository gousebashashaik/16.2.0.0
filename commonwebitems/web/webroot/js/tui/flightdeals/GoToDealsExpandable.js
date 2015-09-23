define("tui/flightdeals/GoToDealsExpandable",[
        "tui/widget/form/flights/Expandable",
       "dojo",
       "dojo/on",
       "dojo/_base/declare",
       "dojo/query",
       "dojo/text!tui/flightdeals/templates/GoTo.html",
       "dijit/registry",
       "dojo/dom",
       "dojo/_base/fx",
       "dojo/has",
       "dojo/NodeList-traverse",
       "dojo/_base/sniff",
       "tui/flightdeals/model/DealsPanelModel",
       "tui/searchPanel/view/flights/AirportListGrouping"
       ],function(Expandable,dojo,on,declare,query,goToTmpl,registry,dom,fx,has){

	declare("tui.flightdeals.GoToDealsExpandable",[Expandable, tui.searchPanel.view.flights.AirportListGrouping],{
			//.............................................Properties
			overseasAirports:[],

			standby: "",

			toSelectedCount:"",

			toDisabledCount :"",

			selectedOverseasAirports : [],

			gotoAllAirports : "",


			//.............................................methods
			postCreate:function(){
				 var goToDealsExpandable = this;
				 goToDealsExpandable.inherited(arguments);
				 goToDealsExpandable.getAirports();
				 goToDealsExpandable.tagElement(goToDealsExpandable.domNode, "FO_Go_To");
			 },

			onAfterTmplRender:function () {
				var goToDealsExpandable = this;
				goToDealsExpandable.renderOverseasAirports();
				goToDealsExpandable.standby = goToDealsExpandable.standBy(dojo.query(".deals-wrapper div",goToDealsExpandable.expandableDom)[0]);
				goToDealsExpandable.tagElement(goToDealsExpandable.expandableDom, "Overall_FO_Go_To");


			},
			 closeExpandable: function(){
			    	var goToDealsExpandable = this;
			    	goToDealsExpandable.standby.hide();
			    	goToDealsExpandable.inherited(arguments);
			    },

			    updateGoToView : function(){
			    	var goToDealsExpandable = this,count;

			    		if(goToDealsExpandable.dealsPanelModel.toAirports){
			    			count =goToDealsExpandable.dealsPanelModel.toAirports.length;
			    		}else{
			    			count = 0;
			    		}

			    		if(count > 1){
			    			query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span>" + count + " airports</span>";
			    		}else if(count === 1 && goToDealsExpandable.dealsPanelModel.toAirportNames[0]){
			    			query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span>" + goToDealsExpandable.dealsPanelModel.toAirportNames[0].name + "("+ goToDealsExpandable.dealsPanelModel.toAirports[0] +")</span>";
			    		}else if(count === 0){
			    			query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span> Any destination </span>";
			    		}

			    },

			getAirports:function(){
				var goToDealsExpandable = this, data,html;

				var def = goToDealsExpandable.doXhrPost("ws/dealsdestinationairports");
				def.then(function(data){
						goToDealsExpandable.overseasAirports = goToDealsExpandable.getDealsOverseasAirportList(data);

						if(query("#destinationAirportName")[0]){
							setTimeout(function(){
								goToDealsExpandable.dealsPanelModel.groupedOverseasAiports = goToDealsExpandable.getGroupedOverseasAirport(data);
								goToDealsExpandable.getSelectedPreviouseGroupsDetails();
							}, 1000);


						}


				});

			},

			populateSignleValue : function(){
				var goToDealsExpandable = this;
				_.each(goToDealsExpandable.airportList, function(airport){
          			if(airport.id === goToDealsExpandable.dealsPanelModel.toAirports[0]){
          				query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span>" + airport.name + "("+ airport.id +")</span>";
          			}
          	});
			},

			renderOverseasAirports: function(){
				var goToDealsExpandable = this;

				data={
                		overseasAirportslist: goToDealsExpandable.overseasAirports,
                		dealsPanelModel : goToDealsExpandable.dealsPanelModel
                }
				html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, goToTmpl));
                target = dojo.query(".deals-wrapper",goToDealsExpandable.expandableDom)[0];
	    	    dojo.html.set(target,html,{
	    	    	parseContent: true
	    	    });
	    	    goToDealsExpandable.attachEvents();

	    	    goToDealsExpandable.attachExpandableEvent();

			},

			updateGroupHeadinginModel : function(){
				var goToDealsExpandable = this,checkedGrpCheckBox,grphead,countryCode,totalCount;
					goToDealsExpandable.dealsPanelModel.toAirportGpName = [];
					checkedGrpCheckBox = dojo.query(".grpHeading .dijitChecked",goToDealsExpandable.expandableDom);

					//Group Selected Check box update to model
					if(checkedGrpCheckBox.length > 0){
						_.forEach(checkedGrpCheckBox,function(item){
							goToDealsExpandable.dealsPanelModel.toAirportGpName.push(dijit.byNode(item).value);
						});
					}

					if(goToDealsExpandable.dealsPanelModel.toAirportGpName.length === 1 && checkedGrpCheckBox.length === 1){

						grphead = dijit.byNode(checkedGrpCheckBox[0]);

						if(grphead.focusNode.dataset !== undefined){
							countryCode = grphead.focusNode.dataset.countrycode;
						} else {
							countryCode = grphead.focusNode.getAttribute("data-countrycode")
						}

						totalCount = dojo.query("." +countryCode).length;
						goToDealsExpandable.toDisabledCount = dojo.query("." +countryCode + ".dijitDisabled").length;
						goToDealsExpandable.toSelectedCount = (totalCount - goToDealsExpandable.toDisabledCount) -1;
					}


					if(goToDealsExpandable.dealsPanelModel.toAirportGpName.length === 1 && goToDealsExpandable.toSelectedCount === goToDealsExpandable.dealsPanelModel.toAirports.length){
						goToDealsExpandable.dealsPanelModel.toAirportGpFlag = true;
					}else{
						goToDealsExpandable.dealsPanelModel.toAirportGpFlag = false;
					}
			},


			attachEvents:function(){
				var goToDealsExpandable = this;

				goToDealsExpandable.disablingGroupHead();
				//Connecting event for group checkbox
				dojo.query('.grpHeading .dijitCheckBox').forEach(function(checkBox){
					on(dijit.byNode(checkBox),"click",function(evt){
						goToDealsExpandable.selectedOverseasAirports = [];
						goToDealsExpandable.dealsPanelModel.toAirportNames = [];

						var countryCode;
						if(this.focusNode.dataset !== undefined){
							countryCode = this.focusNode.dataset.countrycode;
						} else {
							countryCode = this.focusNode.getAttribute("data-countrycode")
						}

							dojo.query("." +countryCode).forEach(function(c){
								if((dijit.byNode(checkBox).checked) && (dijit.byNode(c).disabled===false)){
									dijit.byNode(c).set("checked",true);
								}
								else{
									dijit.byNode(c).set("checked",false);
								}
							});
						var CheckedCheckBoxes = query(".childCountry .dijitChecked",goToDealsExpandable.expandableDom);
						if(CheckedCheckBoxes.length > 0){
							_.forEach(CheckedCheckBoxes,function(item){
								goToDealsExpandable.selectedOverseasAirports.push(dijit.byNode(item).value);
								goToDealsExpandable.dealsPanelModel.toAirportNames.push(dijit.byNode(item).title)
							});

						}
						goToDealsExpandable.updateGroupHeadinginModel();
						goToDealsExpandable.updatePlaceHolder(CheckedCheckBoxes.length);

						//goToDealsExpandable.dealsPanelModel.updateBugetState();
					});
				});
				//connecting event for child checkbox
				dojo.query('.childCountry .dijitCheckBox').forEach(function(childCheckBox){

					on(dijit.byNode(childCheckBox),"click",function(evt){
						goToDealsExpandable.selectedOverseasAirports = [];
						goToDealsExpandable.dealsPanelModel.toAirportNames = [];

						var airportmodelCountrycode;
						if(dijit.byNode(childCheckBox).focusNode.dataset !== undefined){
							airportmodelCountrycode = dijit.byNode(childCheckBox).focusNode.dataset.airportmodelCountrycode;
						} else {
							airportmodelCountrycode = dijit.byNode(childCheckBox).focusNode.getAttribute("data-airportmodel-countryCode")

						}


						var grpCheckbox = dojo.query(".grpHeading ." +	airportmodelCountrycode)[0],
						childCheckboxes=dojo.query(".childCountry ." + airportmodelCountrycode),
						disabledChildCheckBoxes=dojo.query(".childCountry ." +airportmodelCountrycode+".dijitCheckBoxDisabled"),
						enabledChildCheckBoxeslength=childCheckboxes.length-disabledChildCheckBoxes.length,
						checkedCheckBoxes = dojo.query(".childCountry .dijitChecked",goToDealsExpandable.expandableDom),
						checkedChildCheckbox=dojo.query(".childCountry .dijitChecked." +airportmodelCountrycode);

						if(!dijit.byNode(childCheckBox).checked) {
							dijit.byNode(grpCheckbox).set("checked",false);
						}else{
							if(enabledChildCheckBoxeslength===checkedChildCheckbox.length){
								var parent  = dijit.byNode(grpCheckbox);
									parent.set("checked",true);
									goToDealsExpandable.dealsPanelModel.toAirportGpName.push(parent.value);
							}
						}
						if(checkedCheckBoxes.length > 0){
							_.forEach(checkedCheckBoxes,function(item){
								goToDealsExpandable.selectedOverseasAirports.push(dijit.byNode(item).value);
								goToDealsExpandable.dealsPanelModel.toAirportNames.push(dijit.byNode(item).title)
							});

						}

						goToDealsExpandable.updateGroupHeadinginModel();


						goToDealsExpandable.updatePlaceHolder(checkedCheckBoxes.length);

						//goToDealsExpandable.dealsPanelModel.updateBugetState();
					});
				});
				on(dijit.byId("anyOSAirport"),"click",function(){
					//console.log(this.checked);
					var allChkBoxes = registry.findWidgets(goToDealsExpandable.expandableDom),
					anyOSAirport = this;
					goToDealsExpandable.dealsPanelModel.toAirports = [];
					goToDealsExpandable.dealsPanelModel.toAirportNames = [];
					goToDealsExpandable.dealsPanelModel.toAirportGpName = [];

					anyOSAirport.checked ? goToDealsExpandable.gotoAllAirports = anyOSAirport.value : goToDealsExpandable.gotoAllAirports="" ;

					goToDealsExpandable.disabledAllAirport();

					goToDealsExpandable.updatePlaceHolder(0);
					goToDealsExpandable.disablingGroupHead();

					//goToDealsExpandable.dealsPanelModel.updateBugetState();
			});

			},

			disabledAllAirport : function(){
				var goToDealsExpandable = this;
				_.each(goToDealsExpandable.overseasAirports,function(item){
					if(anyOSAirport.checked){
						dijit.byId(item.id+"-deals").set("checked",false);
						dijit.byId(item.id+"-deals").set("disabled",true);
						if(dojo.isIE == 8){
							query(dijit.byId(item.id+"-deals").domNode).next("label").style("color","#aeaeae");
						}
						dojo.query('.grpHeading .dijitCheckBox',goToDealsExpandable.expandableDom).forEach(function(item){

							dijit.byNode(item).set("checked",false);
							dijit.byNode(item).set("disabled",true);
							if(dojo.isIE == 8) query(item).next("label").style("color","#aeaeae");

						});
					} else {
						dijit.byId(item.id+"-deals").set("disabled",!item.available);
						if(dojo.isIE == 8 && item.available){
							query(dijit.byId(item.id+"-deals").domNode).next("label").style("color","#666");
						}
						dojo.query('.grpHeading .dijitCheckBox',goToDealsExpandable.expandableDom).forEach(function(item){
							dijit.byNode(item).set("disabled",false);
							if(dojo.isIE == 8) query(item).next("label").style("color","#666");
						});
					}

				})
			},

			sortByName :  function(array){
                return _.sortBy(array, "countryName");
            },
			updatePlaceHolder: function(value,doValidate){  //doValidate is boolean to check whethere show deals button needs to be enabled or not
				var goToDealsExpandable = this;
				if(doValidate == undefined || doValidate == null) doValidate = true;
				if(value == 1){
					var selectedNodeTxt = dojo.query('.childCountry .dijitCheckBoxChecked',goToDealsExpandable.expandableDom).next("label").text();
					query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span>"+ selectedNodeTxt + "</span>";
				} else if(value == 0){
					query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span> Any destination </span>";
				} else {
					query(".placeholder",goToDealsExpandable.domNode)[0].innerHTML = "<span>" + value + " airports</span>";
				}

				//Check if we are results page and enable show deals button on change of search criteria
	    		if(!goToDealsExpandable.dealsPanelModel.dealsFilterStatus && doValidate){
	    			goToDealsExpandable.dealsPanelModel.enableDealsButton();
		    	}
			},
			/*closeExpandable: function(){
		    	var goToDealsExpandable = this;
		    	goToDealsExpandable.inherited(arguments);
		    	goToDealsExpandable.setHeight()
			},*/
			setHeight: function(){
				var goToDealsExpandable = this;
				var elem = dojo.query(".deals-wrapper",goToDealsExpandable.expandableDom)[0];
				var target = dojo.query(".deals-wrapper",goToDealsExpandable.expandableDom).parent()[0];
				var height = dojo.position(elem).h

				//dojo.style(target,"max-height",height+"px")
				if(has("ie")<10){
					dojo.style(target,"max-height","1655px");
				} else {
					fx.animateProperty({
	        			node:target,
	        			//duration: dojo.isIE > 7 ? 1500 : 1000,
	        			 properties: {
	        				 maxHeight: 1655
	        			 	}
	        		}).play();
				}

			},
			onOpenExpandable: function(){
    	    	var goToDealsExpandable = this,def;
    	    	goToDealsExpandable.standby.show();
    	    	dojo.addClass(goToDealsExpandable.standby.domNode,"goToDeals");
    	    	def = goToDealsExpandable.doXhrPost("ws/dealsdestinationairports");
    	    	var target = dojo.query(".deals-wrapper",goToDealsExpandable.expandableDom).parent()[0];

    	    	def.then(function(data){

    	    		goToDealsExpandable.overseasAirports = goToDealsExpandable.getDealsOverseasAirportList(data);

    	    		 goToDealsExpandable.dealsPanelModel.toAirports = goToDealsExpandable.dealsPanelModel.validateSavedSearch(goToDealsExpandable.overseasAirports, goToDealsExpandable.dealsPanelModel.toAirports);
    	    		 goToDealsExpandable.selectedOverseasAirports = goToDealsExpandable.dealsPanelModel.toAirports;
    	    		 _.forEach(goToDealsExpandable.overseasAirports, function(item,index){
    	    			if(item.countryCode !== "GBR" && dijit.byId("anyOSAirport").checked != true){
    	    			//	console.log(item.countryCode,index);
	    	    			if(!item.available){
	    	    				dijit.byId(item.id+"-deals").set("checked",false);
	    	    				dijit.byId(item.id+"-deals").set("disabled",true);
	    	    			} else {
	    	    				dijit.byId(item.id+"-deals").set("disabled",false);
	    	    			}
    	    			}
    	    		})
    	    		goToDealsExpandable.updatePlaceHolder(dojo.query('.childCountry .dijitCheckBoxChecked',goToDealsExpandable.expandableDom).length,false);

    	    		goToDealsExpandable.checkAllAirportCheckStatus();
    	    		goToDealsExpandable.disablingGroupHead();
    	    		goToDealsExpandable.standby.hide();
    	    	})
    	    },

    	    checkAllAirportCheckStatus : function() {
    	    	var goToDealsExpandable = this;
    	    		if(dojo.query('.anyAirport #anyOSAirport')[0].checked){
    	    			goToDealsExpandable.disabledAllAirport();
    	    		}
    	    },

    	    disablingGroupHead:function () {
				var goToDealsExpandable = this;

				dojo.query('.grpHeading .dijitCheckBox').forEach(function(checkBox){
					var grphead=dijit.byNode(checkBox);
					var countryCode;
						if(grphead.focusNode.dataset !== undefined){
							countryCode = grphead.focusNode.dataset.countrycode;
						} else {
							countryCode = grphead.focusNode.getAttribute("data-countrycode")
						}
					if(grphead!==undefined){

						var disabledlength=dojo.query("." +countryCode + ".dijitDisabled").length;
						var grouplength=dojo.query("." +countryCode).length;
						var checkedlength=dojo.query("." +countryCode + ".dijitChecked").length;


						dojo.query("." +countryCode).forEach(function(c){
							var grpCheckbox = dojo.query('.grpHeading .' +countryCode );
							var childCheckbox = dojo.query('.childCountry .' +countryCode );
							var childCheckboxlength = childCheckbox.length;
							var childCheckedlength = dojo.query('.childCountry .dijitChecked.' +countryCode ).length;
							var childDisabledlength = dojo.query('.childCountry .dijitDisabled.' +countryCode ).length;

					if((childCheckboxlength)===(childDisabledlength)){
						dijit.byNode(grpCheckbox[0]).set("checked",false);
						dijit.byNode(grpCheckbox[0]).set("disabled",true);
						if(dojo.isIE == 8) query(dijit.byNode(grpCheckbox[0]).domNode).next("label").style("color","#aeaeae");
					}else if(childCheckboxlength===(childCheckedlength+childDisabledlength)){
						(dijit.byNode(grpCheckbox[0])).set("checked",true);
					}else if((childCheckboxlength)!=(childDisabledlength)){
						dijit.byNode(grpCheckbox[0]).set("disabled",false);
						dijit.byNode(grpCheckbox[0]).set("checked",false);
					}
					else{
						(dijit.byNode(grpCheckbox[0])).set("checked",false);
						if(dojo.isIE == 8) query(dijit.byNode(grpCheckbox[0]).domNode).next("label").style("color","#666");
					}
						});

						}
				});
				goToDealsExpandable.updateGroupHeadinginModel();
		},


		getSelectedPreviouseGroupsDetails : function(){
			var goToDealsExpandable = this,
				def,
				selectedAiports,
				groupedAirports,
				responseData,
				airportList =[],
				countries=[],
				allCountries=[],
				toAirportGpName=[];
				toSelectedGPAirports=[];

				def = goToDealsExpandable.doXhrPost("ws/dealsdestinationairports");
				def.then(function(data){

					groupedAirports = goToDealsExpandable.getAvailableGroupedOverseasAirport(data);
					selectedAiports = goToDealsExpandable.dealsPanelModel.toAirports;
					responseData	= goToDealsExpandable.getAirportSwapList(data);
					responseData	= goToDealsExpandable.getOverseasairport(responseData);

					_.each(selectedAiports, function(airports){
						_.each(responseData, function(tempAirport){
							if(airports === tempAirport.id){
								if(tempAirport.available){
									airportList.push(tempAirport);
								}
							}
						});
					});

					countries = goToDealsExpandable.getCountryNames(airportList);

					allCountries = goToDealsExpandable.setAirportMap(airportList ,countries);

					if(allCountries.length === 1){
						_.each(groupedAirports, function(gpAirport){
							_.each(allCountries, function(countries){
								if(gpAirport.countryName===countries.countryName && gpAirport.airports.length === countries.airports.length){
									toAirportGpName.push(gpAirport.countryName);
									toSelectedGPAirports.push(gpAirport);
								}
							});
						});
					}

					if(toSelectedGPAirports.length === 1){
						dojo.byId("destinationAirportName").innerHTML =  toAirportGpName[0];
					}else if(selectedAiports.length === 1){
						dojo.byId("destinationAirportName").innerHTML =  goToDealsExpandable.dealsPanelModel.toAirportNames[0].name + " ("+ selectedAiports[0] +")";
					}else if(selectedAiports.length > 1){
						dojo.byId("destinationAirportName").innerHTML = selectedAiports.length + " airports"
					}else if(selectedAiports.length === 0){
						dojo.byId("destinationAirportName").innerHTML =  "any destination"
					}

					goToDealsExpandable.dealsPanelModel.toAirportGpName = toAirportGpName;


				});

		}


		});

	return tui.flightdeals.GoToDealsExpandable;
});