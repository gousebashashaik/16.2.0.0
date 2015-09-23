define("tui/flightdeals/FlyFromDealsExpandable", [
     "tui/widget/form/flights/Expandable",
     "dojo",
     "dojo/on",
     "dojo/_base/declare",
     "dojo/text!tui/flightdeals/templates/FlyFrom.html",
     "dojo/query",
     "dijit/registry",
     "dojo/_base/fx",
     "dojo/dom",
     "dojo/has",
     "dijit/focus",
     "dojo/NodeList-traverse",
     "dojo/_base/sniff",
     "tui/flightdeals/model/DealsPanelModel",
     "tui/searchPanel/view/flights/AirportListGrouping"
   ],function(Expandable,dojo,on,declare,flyFromTmpl,query,registry,fx,dom,has,focus){

		declare("tui.flightdeals.FlyFromDealsExpandable",[Expandable, tui.searchPanel.view.flights.AirportListGrouping],{
			//.............................................Properties
			 airportList: null,

			 ukairportList:null,

			 standby: "",

			 fromSelectedCount  :0,

			 fromDisabledCount  :0,

			 selectedUkAirports : [],

			 flyFromAllAirports : "",

			//.............................................methods
			 postCreate:function(){
				 var flyFromDealsExpandable = this;
				 flyFromDealsExpandable.inherited(arguments);
				 flyFromDealsExpandable.getAirports();
				 flyFromDealsExpandable.tagElement(flyFromDealsExpandable.domNode, "FO_Fly-From");

			 },
			 onAfterTmplRender: function(){
				 var flyFromDealsExpandable = this;
				 flyFromDealsExpandable.renderUKAirports();
				 flyFromDealsExpandable.standby = flyFromDealsExpandable.standBy(dojo.query(".deals-wrapper div",flyFromDealsExpandable.expandableDom)[0]);
				 flyFromDealsExpandable.tagElement(flyFromDealsExpandable.expandableDom, "Overall_FO_Fly-From");
			 },
			 closeExpandable: function(){
		    	var flyFromDealsExpandable = this;
		    		flyFromDealsExpandable.standby.hide();
		    		flyFromDealsExpandable.inherited(arguments);

		    },

		    updateFlyFromView : function(){
		    	var flyFromDealsExpandable = this,count;

		    		if(flyFromDealsExpandable.dealsPanelModel.fromAirports){
		    			count =flyFromDealsExpandable.dealsPanelModel.fromAirports.length;
		    		}else{
		    			count =0;
		    		}

			    	if(count > 1){
		    			query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span>" + count + " airports</span>";
		    		}else if(count === 1 && flyFromDealsExpandable.dealsPanelModel.fromAirportNames[0]){
		    			query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span>" + flyFromDealsExpandable.dealsPanelModel.fromAirportNames[0].name + "("+ flyFromDealsExpandable.dealsPanelModel.fromAirports[0] +")</span>";
		    		}else if(count === 0){
		    			query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span> Any UK airport </span>";
		    		}

		    },

		    populateSignleValue : function(){
				var flyFromDealsExpandable = this;
				_.each(flyFromDealsExpandable.airportList, function(airport){
          			if(airport.id === flyFromDealsExpandable.dealsPanelModel.fromAirports[0]){
          				query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span>" + airport.name + "("+ airport.id +")</span>";
          			}
          	});
			},


			getAirports:function(){
				var flyFromDealsExpandable = this,ukAirports,data,html;

				    	    	 //ukAirports = flyFromDealsExpandable.airportjson.GBR;
				var def = flyFromDealsExpandable.doXhrPost("ws/dealsdepartureairports");
				def.then(function(data){

					flyFromDealsExpandable.ukairportList = flyFromDealsExpandable.getDealsUKAiportList(data);

					if(query("#ukAirportName")[0]){
						setTimeout(function(){
							flyFromDealsExpandable.dealsPanelModel.groupedUKAiports = flyFromDealsExpandable.getGroupedUkAirport(data);
							flyFromDealsExpandable.getSelectedPreviouseGroupsDetails();
						}, 1000);

					}

				});
			},
			renderUKAirports: function(){
				var flyFromDealsExpandable = this;
				data = {
   			 		ukairportList : flyFromDealsExpandable.ukairportList,
   			 		dealsPanelModel : flyFromDealsExpandable.dealsPanelModel
			   	}
		   	 	html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flyFromTmpl));
			   	 target = dojo.query(".deals-wrapper",flyFromDealsExpandable.expandableDom)[0];
				     dojo.html.set(target,html,{
				    	parseContent: true
				     });
				    //flyFromDealsExpandable.setHeight();
				    flyFromDealsExpandable.attachExpandableEvent();
				   	flyFromDealsExpandable.attachEvents();
			},

			updateGroupHeadinginModel : function(){
				var flyFromDealsExpandable = this,checkedGrpCheckBox,grphead,countryCode,totalCount;

					checkedGrpCheckBox = dojo.query(".grpHeading .dijitChecked",flyFromDealsExpandable.expandableDom);
					flyFromDealsExpandable.dealsPanelModel.fromAirportGpName=[];
					//Group Selected Check box update to model
					if(checkedGrpCheckBox.length > 0){
						_.forEach(checkedGrpCheckBox,function(item){
							flyFromDealsExpandable.dealsPanelModel.fromAirportGpName.push(dijit.byNode(item).value);
						});
					}

					if(flyFromDealsExpandable.dealsPanelModel.fromAirportGpName.length === 1 && checkedGrpCheckBox.length === 1){

						grphead = dijit.byNode(checkedGrpCheckBox[0]);

						if(grphead.focusNode.dataset !== undefined){
							countryCode = grphead.focusNode.dataset.countrycode;
						} else {
							countryCode = grphead.focusNode.getAttribute("data-countrycode")
						}

						totalCount = dojo.query("." +countryCode).length;
						flyFromDealsExpandable.fromDisabledCount = dojo.query("." +countryCode + ".dijitDisabled").length;
						flyFromDealsExpandable.fromSelectedCount = (totalCount - flyFromDealsExpandable.fromDisabledCount) -1;
					}


					/*if(flyFromDealsExpandable.dealsPanelModel.fromAirportGpName.length === 1 && flyFromDealsExpandable.fromSelectedCount === flyFromDealsExpandable.dealsPanelModel.fromAirports.length){
						flyFromDealsExpandable.dealsPanelModel.fromAirportGpFlag = true;
					}else{
						flyFromDealsExpandable.dealsPanelModel.fromAirportGpFlag = false;
					}*/

			},

			attachEvents:function(){
				var flyFromDealsExpandable = this;

				flyFromDealsExpandable.disablingGroupHead();
				//Connecting event for group checkbox

				dojo.query('.grpHeading .dijitCheckBox').forEach(function(checkBox){
					on(dijit.byNode(checkBox),"click",function(evt){
						flyFromDealsExpandable.selectedUkAirports = [];
						flyFromDealsExpandable.fromSelectedCount = -1;
						flyFromDealsExpandable.fromDisabledCount = 0;

						var countryCode;
						if(this.focusNode.dataset !== undefined){
							countryCode = this.focusNode.dataset.countrycode;
						} else {
							countryCode = this.focusNode.getAttribute("data-countrycode")
						}

						flyFromDealsExpandable.fromDisabledCount = dojo.query("." +countryCode + ".dijitDisabled").length;
						flyFromDealsExpandable.fromSelectedCount = dojo.query("." +countryCode).length;

						 dojo.query("." +countryCode).forEach(function(c){
								if((dijit.byNode(checkBox).checked) && (dijit.byNode(c).disabled===false)){
									dijit.byNode(c).set("checked",true);
								}
								else{
									dijit.byNode(c).set("checked",false);
								}

							});
						var CheckedCheckBoxes = dojo.query(".childCountry .dijitChecked",flyFromDealsExpandable.expandableDom);

						if(CheckedCheckBoxes.length > 0){
							_.forEach(CheckedCheckBoxes,function(item){
								flyFromDealsExpandable.selectedUkAirports.push(dijit.byNode(item).value);
							});

						}

						//flyFromDealsExpandable.updateGroupHeadinginModel();

						flyFromDealsExpandable.updatePlaceHolder(CheckedCheckBoxes.length);

						//flyFromDealsExpandable.dealsPanelModel.updateBugetState();
					});
				});
				//connecting event for child checkbox
				dojo.query('.childCountry .dijitCheckBox').forEach(function(childCheckBox){
					var airportmodelGroups;
					if(dijit.byNode(childCheckBox).focusNode.dataset !== undefined){
						airportmodelGroups = dijit.byNode(childCheckBox).focusNode.dataset.airportmodelGroups;
					} else {
						airportmodelGroups = dijit.byNode(childCheckBox).focusNode.getAttribute("data-airportmodel-groups")
					}

					on(dijit.byNode(childCheckBox),"click",function(evt){
						flyFromDealsExpandable.selectedUkAirports = [];

						var grpCheckbox = dojo.query(".grpHeading ." + airportmodelGroups)[0],
						childCheckboxes=dojo.query(".childCountry ." +airportmodelGroups),
						disabledChildCheckBoxes=dojo.query(".childCountry ." +airportmodelGroups+".dijitCheckBoxDisabled"),
						enabledChildCheckBoxeslength=childCheckboxes.length-disabledChildCheckBoxes.length,
						checkedCheckBoxes = dojo.query(".childCountry .dijitChecked",flyFromDealsExpandable.expandableDom),
						checkedChildCheckboxes=dojo.query(".childCountry .dijitChecked." + airportmodelGroups);

						if(!dijit.byNode(childCheckBox).checked) {
							dijit.byNode(grpCheckbox).set("checked",false);
						}else{
							if(enabledChildCheckBoxeslength===checkedChildCheckboxes.length){
								var parent = dijit.byNode(grpCheckbox).set("checked",true);
									parent.set("checked",true);
							}
						}
						//Child Selected Check box update to model
						if(checkedCheckBoxes.length > 0){
							_.forEach(checkedCheckBoxes,function(item){
								flyFromDealsExpandable.selectedUkAirports.push(dijit.byNode(item).value);
							});

						}

						//flyFromDealsExpandable.updateGroupHeadinginModel();

						flyFromDealsExpandable.updatePlaceHolder(checkedCheckBoxes.length);
						//flyFromDealsExpandable.dealsPanelModel.updateBugetState();
					});
				});

				on(dijit.byId("anyUKAirport"),"click",function(){
						var anyUKAirport = this;
							flyFromDealsExpandable.selectedUkAirports = [];
							/*flyFromDealsExpandable.dealsPanelModel.fromAirports = [];
							flyFromDealsExpandable.dealsPanelModel.fromAirportNames = [];
							flyFromDealsExpandable.dealsPanelModel.fromAirportGpName = [];*/

							anyUKAirport.checked ? flyFromDealsExpandable.flyFromAllAirports = anyUKAirport.value : flyFromDealsExpandable.flyFromAllAirports = "" ;

							flyFromDealsExpandable.disabledAllAirport();

							flyFromDealsExpandable.updatePlaceHolder(0);
							flyFromDealsExpandable.disablingGroupHead();
							//flyFromDealsExpandable.dealsPanelModel.updateBugetState();
				});
			},

			disabledAllAirport : function(){
				var flyFromDealsExpandable = this;
					dojo.forEach(flyFromDealsExpandable.ukairportList,function(item){
						if(anyUKAirport.checked){
							dijit.byId(item.id+"-deals").set("checked",false);

							dijit.byId(item.id+"-deals").set("disabled",true);
							if(dojo.isIE == 8){
								query(dijit.byId(item.id+"-deals").domNode).next("label").style("color","#aeaeae");
							}
							dojo.query('.grpHeading .dijitCheckBox',flyFromDealsExpandable.expandableDom).forEach(function(item){
								dijit.byNode(item).set("checked",false);
								dijit.byNode(item).set("disabled",true);

								if(dojo.isIE == 8) query(item).next("label").style("color","#aeaeae");
							});
						} else {

								dijit.byId(item.id+"-deals").set("disabled",!item.available);
								if(dojo.isIE == 8 && item.available){
									query(dijit.byId(item.id+"-deals").domNode).next("label").style("color","#666");
								}
								dojo.query('.grpHeading .dijitCheckBox',flyFromDealsExpandable.expandableDom).forEach(function(item){
									dijit.byNode(item).set("disabled",false);
									if(dojo.isIE == 8) query(item).next("label").style("color","#666");
								})
						}

					})

			},

			updatePlaceHolder: function(value,doValidate){ //doValidate is boolean to check whethere show deals button needs to be enabled or not
				if(doValidate == undefined || doValidate == null) doValidate = true;
				var flyFromDealsExpandable = this;
				if(value == 1){
					var selectedNodeTxt = dojo.query('.childCountry .dijitCheckBoxChecked',flyFromDealsExpandable.expandableDom).next("label").text();
					query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span>"+selectedNodeTxt+"</span>";
				} else if(value == 0) {
					query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span> Any UK airport</span>";
				} else {
					query(".placeholder",flyFromDealsExpandable.domNode)[0].innerHTML = "<span>" + value + " airports</span>";
				}

				//Check if we are results page and enable show deals button on change of search criteria
		    	if(!flyFromDealsExpandable.dealsPanelModel.dealsFilterStatus && doValidate){
		    		flyFromDealsExpandable.dealsPanelModel.enableDealsButton();
		    	}

			},
			setHeight: function(){
				var flyFromDealsExpandable = this;
				if(flyFromDealsExpandable.isShowing()) return;
				var target = dojo.query(".deals-wrapper",flyFromDealsExpandable.expandableDom).parent()[0];

				var elem = dojo.query(".deals-wrapper",flyFromDealsExpandable.expandableDom)[0];

				var height = dojo.position(elem).h


				//dojo.style(target,"max-height",height+"px")
				if(has("ie")<10){
					dojo.style(target,"max-height","637px");
				} else {
					fx.animateProperty({
	        			node:target,
	        			 properties: {
	        				 maxHeight: 637
	        			 	}
	        		}).play();
				}
			},

			 onOpenExpandable: function(){
	    	    	var flyFromDealsExpandable = this;

	    	    	flyFromDealsExpandable.standby.show();
	    	    	dojo.addClass(flyFromDealsExpandable.standby.domNode, "flyfromDeals");
	    	    		var def = flyFromDealsExpandable.doXhrPost("ws/dealsdepartureairports");
		    	    	def.then(function(data){
		    	    		flyFromDealsExpandable.ukairportList = data.GBR;
		    	    		flyFromDealsExpandable.dealsPanelModel.fromAirports = flyFromDealsExpandable.dealsPanelModel.validateSavedSearch(flyFromDealsExpandable.ukairportList, flyFromDealsExpandable.dealsPanelModel.fromAirports);
		    	    		flyFromDealsExpandable.selectedUkAirports =flyFromDealsExpandable.dealsPanelModel.fromAirports;
		    	    		_.forEach(flyFromDealsExpandable.ukairportList, function(item){
		    	    			if(dijit.byId("anyUKAirport").checked!=true ){
		    	    				if(!item.available){
		    	    					dijit.byId(item.id+"-deals").set("checked",false);
			    	    				dijit.byId(item.id+"-deals").set("disabled",true);
			    	    			} else {
			    	    				;
			    	    				dijit.byId(item.id+"-deals").set("disabled",false);
			    	    			}
		    	    			}
		    	    		})

		    	    		flyFromDealsExpandable.updatePlaceHolder(dojo.query('.childCountry .dijitCheckBoxChecked',flyFromDealsExpandable.expandableDom).length,false);
		    	    		flyFromDealsExpandable.checkAllAirportCheckStatus();
		    	    		flyFromDealsExpandable.disablingGroupHead();
		    	    		flyFromDealsExpandable.standby.hide();
		    	    	})
	    	    },

	    	    checkAllAirportCheckStatus : function() {
	    	    	var flyFromDealsExpandable = this;
	    	    		if(dojo.query('.anyAirport #anyUKAirport')[0].checked){
	    	    			flyFromDealsExpandable.disabledAllAirport();
	    	    		}
	    	    },

	    	    disablingGroupHead:function () {
					var flyFromDealsExpandable = this;

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
					flyFromDealsExpandable.updateGroupHeadinginModel();
			},


			getSelectedPreviouseGroupsDetails : function(){
				var flyFromDealsExpandable = this,
					def,
					selectedAiports,
					groupedAirports,
					responseData,
					airportList =[],
					countries=[],
					allCountries=[],
					fromAirportGpName=[];
					fromSelectedGPAirports=[];

					def = flyFromDealsExpandable.doXhrPost("ws/dealsdepartureairports");
	    	    	def.then(function(data){
	    	    		groupedAirports = flyFromDealsExpandable.getAvailableGroupedUKAirport(data);
	    	    		selectedAiports = flyFromDealsExpandable.dealsPanelModel.fromAirports;
	    	    		responseData	= flyFromDealsExpandable.getAirportSwapList(data);
						responseData	= flyFromDealsExpandable.getUkAirports(responseData);

						_.each(selectedAiports, function(airports){
							_.each(responseData, function(tempAirport){
								if(airports === tempAirport.id){
									if(tempAirport.available){
										airportList.push(tempAirport);
									}
								}
							});
						});

						countries = flyFromDealsExpandable.getCountryNames(airportList);

						allCountries = flyFromDealsExpandable.setAirportMap(airportList ,countries);

						if(allCountries.length === 1){
							_.each(groupedAirports, function(gpAirport){
								_.each(allCountries, function(countries){
									if(gpAirport.countryName===countries.countryName && gpAirport.airports.length === countries.airports.length){
										fromAirportGpName.push(gpAirport.countryName);
										fromSelectedGPAirports.push(gpAirport);
									}
								});
							});
						}

						if(fromSelectedGPAirports.length === 1){
							dojo.byId("ukAirportName").innerHTML = "any "+ fromAirportGpName[0];
						}else if(selectedAiports.length === 1){
							dojo.byId("ukAirportName").innerHTML =  flyFromDealsExpandable.dealsPanelModel.fromAirportNames[0].name + " ("+ selectedAiports[0] +")";
						}else if(selectedAiports.length > 1){
							dojo.byId("ukAirportName").innerHTML = selectedAiports.length + " UK airports"
						}else if(selectedAiports.length === 0){
							dojo.byId("ukAirportName").innerHTML =  "any UK airport"
						}

						flyFromDealsExpandable.dealsPanelModel.fromAirportGpName = fromAirportGpName;

	    	    	});
			}



		});

		return tui.flightdeals.FlyFromDealsExpandable;
});