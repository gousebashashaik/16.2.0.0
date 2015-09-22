define(["dojo/_base/declare",
  "dojo",
  // Parent classes
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  // General application modules
  "dojo/_base/lang",
  "dojo/on",
  "dojo/fx",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/query",
  "dojo/dom-construct",
  "dojo/_base/connect",
  "dojo/topic",
  "dojo/_base/xhr",
  "dojo/dom-attr",
  "dojo/_base/array",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/InsuranceFamilyCover.html",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/InsuranceIndDetails.html",
  "tui/widget/booking/insuranceB/InsuranceModel",
  "tui/widget/booking/insuranceB/InsuranceSummary",
  "tui/widget/booking/insuranceB/PassengerAgeProfileSelect",
  "tui/widget/expand/Expandable"
],

  function (declare, dojo, _WidgetBase, Templatable, _Templated, lang, on, coreFx, domClass, domGeom, domStyle, dom, query, domConstruct, connect, topic, xhr, domAttr, array, BookflowUrl, insuranceFamilyCoverTmpl, insuranceIndividualDetails, InsuranceModel) {

    return declare("tui.widget.booking.insuranceB.InsuranceFamilyCover", [_WidgetBase, _Templated, Templatable], {

      /** Widget template HTML string */


      tmpl: insuranceFamilyCoverTmpl,
      insuranceIndividualDetails : insuranceIndividualDetails,
      templateString: "",
      widgetsInTemplate: true,
      resultDomNode: null,
      selectedFlag:null,
      insStaticTooltipData:null,
      insuranceRestrictionsToolTip:null,
      addExcessFlag:null,
      ewflag:null,
      isDetailsSectionBuilt : false,
      targetUrl : BookflowUrl.insuranceBaddurl,
      removeUrl : BookflowUrl.insuranceBremoveurl,
      confirmationFlag : false,
      iTotalPrice : 0,
      familyPackagePresent : false,
      insuranceAdded : false,
      excessWaiverFlag : true,


      postMixInProperties: function () {
        var insuranceFamilyCover = this
	        insuranceFamilyCover.adtPrice = this.adtPrice;
	        insuranceFamilyCover.chdPrice = this.chdPrice;
	        insuranceFamilyCover.familyTitle = this.title;
	        insuranceFamilyCover.insViewObject = this.insViewObject;
	        insuranceFamilyCover.code = this.code;
	        insuranceFamilyCover.resultDomNode = this.resultDomNode;
	        insuranceFamilyCover.selectedFlag = this.selectedFlag;
	        insuranceFamilyCover.addExcessFlag=this.addExcessFlag;
	        insuranceFamilyCover.ewflag=this.ewflag;
	        insuranceFamilyCover.insStaticTooltipData = this.insStaticTooltipData;
	        insuranceFamilyCover.insuranceRestrictionsToolTip = this.insuranceRestrictionsToolTip;
	        insuranceFamilyCover.buttonRefNode= this.familygq
	        insuranceFamilyCover.jsonData = this.jsonData
	        insuranceFamilyCover.familyPackagePresent = this.familyPackagePresent;
	        this.inherited(arguments);
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      // After addition of insurance cover
      afterSuccess: function (response) {
          var widget = this;
    	  dijit.registry.byId("controllerWidget").publishToViews("extraoptions", response);
    	  if(!widget.familyPackagePresent){
	        	widget.collapseDetails();
	        	widget.insuranceAdded = true;
	          	domClass.remove(widget.addedIndividualBtn, "disNone");
	          	domClass.add(widget.hideDetailsBtn, "hide");
	          	domClass.add(widget.paxTable, "hide");
	          	domClass.add(widget.showDetailsBtn, "hide");
          } else {
        	  _.each(response.insuranceContainerViewData.insViewData, function(insData){
        		if(!insData.familyInsPresent){
                	widget.totalPriceSummary = insData.totalPrice;
        		}
        	});
          	widget.showAddedButton(response);
          }
    	  topic.publish("tui/widget/booking/insurnaceB/InsuranceIndividualCover/disabledFam");
    	  if(response.insuranceContainerViewData.coveredAllPassenger){
    	  topic.publish("tui/booking/insuranceB/disableAdequateInsuranceCheck");
    	  }
      },

      postCreate: function () {
        var insuranceFamilyCover = this;
         this.inherited(arguments);
         if(insuranceFamilyCover.selected){
        	 insuranceFamilyCover.showAddedButton();
         }
         on(insuranceFamilyCover.showDetailsBtn,"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.expandDetails));
         on(insuranceFamilyCover.hideDetailsBtn,"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.collapseDetails));
         insuranceFamilyCover.tweakAgeProfiles();

         topic.subscribe("tui/widget/booking/insurnaceB/isuranceFamilyCover/disabledInd", function(){
        	 insuranceFamilyCover.collapseDetails();
        	 domClass.add("ind-ins-block","indBlock");
         });

         topic.subscribe("tui/widget/booking/insurnaceB/isuranceFamilyCover/enableInd", function(){
        	 //insuranceFamilyCover.showDetailsBtn.disabled=false;
        	 //domClass.remove("ind-ins","Insopacity");
        	 //domClass.remove(insuranceFamilyCover.showDetailsBtn,"ins-cursor");
        	 domClass.remove("ind-ins-block","indBlock");
         });

         topic.subscribe("tui/widget/booking/insuranceB/insuranceFamilyCover/updatePrice", function(){
        	 insuranceFamilyCover.updatePrice();
         });

         topic.subscribe("tui/widget/booking/insuranceB/insuranceFamilyCover/updateInfantCheck", function(){
        	 insuranceFamilyCover.updateInfantCheck();
         });

         this.tagElements(query('button.button', this.domNode),this.title);
         topic.subscribe("tui/widget/booking/insuranceB/remove", function(){
        	 insuranceFamilyCover.showDetailsButton();
         });
      },


      updateInfantCheck : function(){
    	  var insuranceFamilyCover = this,
    	  	  checkedBoxes;
    	  _.each(query(".requiredPassengers input", insuranceFamilyCover.individualExpanded), function(checkbox){
	    		  if(domClass.contains(checkbox,"adult")){
	    			   checkedBoxes = _.filter(query(".requiredPassengers input.adult", insuranceFamilyCover.individualExpanded),function(adultCheckbox){
	           		   return adultCheckbox.checked;
	           	   });

	    			   totalCheckedBoxes = checkedBoxes ? checkedBoxes.length : 0;
	   			   _.each(query(".requiredPassengers input.infant", insuranceFamilyCover.individualExpanded), function(infantCheckbox, infantIndex){
	   				   infantCheckbox.checked = false;
	   				   if(infantIndex < totalCheckedBoxes){
	               			  infantCheckbox.checked = true;
	   				    }
	           		});
	    		  }
    		});
      },

      // Displays show details button by hiding all other buttons
      showDetailsButton : function() {
    	  var insuranceFamilyCover = this;
          domClass.add(insuranceFamilyCover.addedIndividualBtn, "disNone");
          domClass.remove(insuranceFamilyCover.showDetailsBtn, "hide");
          domClass.remove(insuranceFamilyCover.priceDetail, "disNone");
          domClass.remove(insuranceFamilyCover.insuranceTitleSection, "full-width");
      },

      // Displays added button with insurance summary and an option to remove cover
      showAddedButton : function(response) {
        var insuranceFamilyCover = this,
        	insPrice = insuranceFamilyCover.totalPriceSummary;
        domClass.remove(insuranceFamilyCover.addedIndividualBtn, "disNone");
        domClass.add(insuranceFamilyCover.showDetailsBtn, "hide");
        domClass.add(insuranceFamilyCover.priceDetail, "disNone");
        domClass.add(insuranceFamilyCover.insuranceTitleSection, "full-width");
        if(!response){
        	if(insuranceFamilyCover.jsonData.insuranceContainerViewData.coveredAllPassenger){
                 setTimeout(function(){
                    topic.publish("tui/booking/insuranceB/disableAdequateInsuranceCheck");
                 }, 1500);
        	} else {
        		setTimeout(function(){
	            	topic.publish("tui/booking/insuranceB/enableAdequateInsuranceCheck");
	            }, 1500);
        	}
        }
        if(insuranceFamilyCover.familyPackagePresent){
	        var insuranceSummary = new tui.widget.booking.insuranceB.InsuranceSummary({
	        	jsonData : insuranceFamilyCover.jsonData,
	        	titleName : insuranceFamilyCover.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.ind_ins_info_one_displayName,
	        	summaryDescription : insuranceFamilyCover.title,
	        	addExcessFlag : insuranceFamilyCover.ewflag,
	        	price : insPrice,
	        	famCode: insuranceFamilyCover.code,
	        	getBasicPrice : insuranceFamilyCover.basicPrice,
	            indAddObject: insuranceFamilyCover.insViewObject1,
	            overlayObj: insuranceFamilyCover.insuranceInformationOverlay,
	            resultDomNode: insuranceFamilyCover.resultDomNode,
	            jsonData:this.jsonData,
	            isFamButtonAdded:'false'
	        });
	        insuranceFamilyCover.collapseDetails();
        } else {
        	 on(_.first(query("a.prebook-removeqty",insuranceFamilyCover.removeSection)),"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.removeInsurance));
        	 domClass.remove(insuranceFamilyCover.removeSection, "hide");
        	 domAttr.set(_.first(query(".insured-price", insuranceFamilyCover.removeSection)), "innerHTML", insuranceFamilyCover.totalPriceSummary);
        }
      },

      // Adds age profile with corresponding price
      tweakAgeProfiles : function () {
    	  var insuranceFamilyCover = this,
    	  	  individualInsurance = null;
    	  individualInsurance = _.filter(insuranceFamilyCover.insViewObject.insViewData, function(viewData){
    		  return !viewData.familyInsPresent;
    	  });
    	  individualInsurance = _.first(individualInsurance);
    	  insuranceFamilyCover.indiviualInsurance = JSON.parse(JSON.stringify(individualInsurance));
    	  _.each(insuranceFamilyCover.insViewObject.insPasViewData, function(passenger){
    		  _.each(passenger.ageProfile, function(ageProfile){
    			  switch(ageProfile.ageCode.toLowerCase()){
    			  		case "adult" : ageProfile.tweakedProfile = ageProfile.ageRange.toLowerCase()+" <strong>("+ individualInsurance.frmAdtPrice +")</strong>";
    			  			break;
    			  		case "senior" :  ageProfile.tweakedProfile = ageProfile.ageRange.toLowerCase()+" <strong>("+ individualInsurance.frmSeniorPrice +")</strong>";
			  				break;
    			  		case "supersenior" :ageProfile.tweakedProfile = ageProfile.ageRange.toLowerCase()+" <strong>("+ individualInsurance.frmSuperSeniorPrice +")</strong>";
		  					break;
    			  		case "not applicable" : ageProfile.tweakedProfile = ageProfile.ageRange.toLowerCase();
    			  		    break;
    			  }
    		  });
    	  });
      },

      // Gets the passenger who are been insured
      getInsuredPax : function() {
    	  var widget = this;
    	  var insuredPax = {};
    	  var selectBox = null;
    	  var childLabel = null;

    	  _.each(query(".travelling-passenger-section input"), function(checkbox){
    		  if(checkbox.checked){
    			  selectBox = _.first(query("select", checkbox.parentElement));
    			  if(selectBox){
    				  insuredPax[domAttr.get(checkbox,"id").split("checkboxId")[1]] = selectBox.value;
    			  } else {
    				  childLabel = _.first(query("label.child", checkbox.parentElement));
    				  if(childLabel){
    					  insuredPax[domAttr.get(childLabel,"id").split("label")[1]]= domAttr.get(childLabel,"data-label-value");
    				  }
    			  }
    		  }
    	  });
    	  return insuredPax;
      },

     // Adds the insurance cover for selected pax
     addInsurance : function(event){
      var  widget = this,
      	   insuredPax = null,
      	   addButton = null;
      if(widget.confirmationFlag){
    	  if(widget.iTotalPrice){
    		  if(event.target){
    			  addButton = event.target;
    		  } else {
    			  addButton = event.srcElement;
    		  }
	    	  widget.ewflag = JSON.parse(domAttr.get(addButton,"data-excess-waiver"));
		      insuredPax =  widget.getInsuredPax();
	          domClass.add("top", 'updating');
	          domClass.add("main", 'updating');
	          domClass.add("right", 'updating');
	    	  domAttr.set(_.first(query(".insured-price", widget.removeSection)), "innerHTML", widget.ewflag ? widget.totalPriceWithEw : widget.totalPrice);
		      var jsonRequestData = {
		              "insuranceCode": widget.code,
		              "excessWaiver": widget.ewflag,
		              "selected": insuredPax
		      };
		      var results = xhr.post({
		          url: widget.targetUrl,
		          content: {insuranceCriteria: dojo.toJson(jsonRequestData)},
		          handleAs: "json",
		          error: function (err) {
		             // error handling
		          }
		        });

		        dojo.when(results, function (response) {
		        	widget.afterSuccess(response);
		            domClass.remove("top", 'updating');
		            domClass.remove("main", 'updating');
		            domClass.remove("right", 'updating');
		        });
    	  } else {
    		  domClass.remove("impInfoCheckbox_Error77", "hide");
    	  }
      } else {
    	  domClass.remove("impInfoCheckbox_Error22", "disNone");
    	  domAttr.set("impInfoCheckbox_Error22", "innerHTML", "Please confirm that you have checked the terms and conditions");
      }
    },

    // Removes added insurance cover
    removeInsurance : function(event){
        var  widget = this;
        var jsonRequestData = {
	              "insuranceCode": widget.code
	    };
        domClass.add("top", 'updating');
        domClass.add("main", 'updating');
        domClass.add("right", 'updating');
        var results = xhr.post({
            url: widget.removeUrl,
            content: {insuranceCriteria: dojo.toJson(jsonRequestData)},
            handleAs: "json",
            error: function (err) {
               // error handling
            }
          });
          dojo.when(results, function (response) {
        	widget.afterRemoveSuccess(response);
            domClass.remove("top", 'updating');
            domClass.remove("main", 'updating');
            domClass.remove("right", 'updating');
          });

      },

      afterRemoveSuccess : function(response){
    	  var widget = this;
    	  domClass.add(widget.addedIndividualBtn, "disNone");
    	  domClass.remove(widget.priceDetail, "disNone");
    	  dijit.registry.byId("controllerWidget").publishToViews("extraoptions", response);
    	  if(widget.domNode.parentElement && domClass.contains(widget.domNode.parentElement,"individual-family")){
          	domClass.remove(widget.showDetailsBtn, "hide");
        	domClass.remove(widget.paxTable, "hide");
    	  } else {
    	    widget.collapseDetails();
        	domClass.add(widget.removeSection, "hide");
    	  }
    	  topic.publish("tui/widget/booking/insurnaceB/InsuranceIndividualCover/enableFam");
		  topic.publish("tui/booking/insuranceB/enableAdequateInsuranceCheck");
      },

      // Expands insurance component - triggered on click of show details button
      expandDetails : function(){
    	  var insuranceFamilyCover = this,
    	  	  expandedSectionDim = null;
    	  domClass.add(insuranceFamilyCover.paxTable, "hide");
    	  domClass.add(insuranceFamilyCover.showDetailsBtn, "hide");
    	  domClass.remove(insuranceFamilyCover.hideDetailsBtn, "hide");
    	  domClass.add(insuranceFamilyCover.coverageIndividual, "background-none");
    	  domClass.add(insuranceFamilyCover.coverageIndividual, "section-expanded");
    	  if(insuranceFamilyCover.familyPackagePresent){
	    		 domClass.remove(insuranceFamilyCover.extraBorder, "hide");
	    		 domClass.add("ind-ins","expanded");
	      }
    	  topic.publish("tui/widget/booking/insurnaceB/InsuranceIndividualCover/disabledFam");
    	  if(!insuranceFamilyCover.isDetailsSectionBuilt){
        	  insuranceFamilyCover.buildIndividualDetailsSection();
        	  on(_.first(query("div.simple-add-section button",insuranceFamilyCover.individualExpanded)),"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.addInsurance));
        	  on(_.first(query("div.excess-excess-waiver-section button",insuranceFamilyCover.individualExpanded)),"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.addInsurance));
        	  on(_.first(query("a.prebook-removeqty",insuranceFamilyCover.removeSection)),"click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.removeInsurance));
        	  _.each(query(".requiredPassengers input", insuranceFamilyCover.individualExpanded), function(checkbox){
	        		if(!domClass.contains(checkbox,"infant")){
	        		    on(checkbox, "click", function(){
		                       var check = this,
		                       	   checkedBoxes = [],
		                       	   totalCheckedBoxes = 0,
		                       	   checkIndex = 0,
		                       	   adultIndex = 0,
		                       	   selectDropDown = null,
		                       	   totalOptions = 0;
		                       //hack-for IE browser else change event will not fire
		                       checkbox.blur();
		                       checkbox.focus();
		             		  if(domClass.contains(checkbox,"adult")){
		             			   checkedBoxes = _.filter(query(".requiredPassengers input.adult", insuranceFamilyCover.individualExpanded),function(adultCheckbox){
		                    		   return adultCheckbox.checked;
		                    	   });

		             			   totalCheckedBoxes = checkedBoxes ? checkedBoxes.length : 0;
                    			   _.each(query(".requiredPassengers input.infant", insuranceFamilyCover.individualExpanded), function(infantCheckbox, infantIndex){
                    				   infantCheckbox.checked = false;
                    				   if(infantIndex < totalCheckedBoxes){
			                    			  infantCheckbox.checked = true;
                    				    }
		                    		});
		                    	   checkIndex = check.id.split("checkboxId")[1];
		                    	   selectDropDown = insuranceFamilyCover["selectDropdown"+checkIndex];
			                       if(check.checked){
			                    	   if(selectDropDown){
			                    		   _.each(selectDropDown.listItems, function(listItem, listIndex){
			                    			   domClass.remove(listItem, "disabled");
			                    			   domClass.remove(listItem, "active");
			                    			   totalOptions = selectDropDown.selectNode.options.length;
			                    			   if(!listIndex && selectDropDown.selectNode.value.toLowerCase() === "not applicable"){
			                    				   domAttr.remove(selectDropDown.selectNode.options[totalOptions - 1],"selected");
			                    				   domClass.add(listItem, "active");
			                    				   selectDropDown.selectDropdownLabel.innerHTML = listItem.innerHTML;
			                    				   domAttr.set(selectDropDown.selectNode.options[listIndex],"selected","selected");
			                    				   selectDropDown.selectNode.value =  domAttr.get(selectDropDown.selectNode.options[listIndex],"value"); //DE44422, DE44425 and DE44426
			                    				   domClass.add(_.first(query(".travelling-passenger-section .select-div-" + checkIndex, insuranceFamilyCover.individualExpanded)), "disNone");
			                    			   }
			                    		   });
			                    	   }
			                       } else {
			                    	   if(selectDropDown){
			                    		   _.each(selectDropDown.listItems, function(listItem){
			                    			   domClass.add(listItem, "disabled");
			                    		   });
			                    	   }
			                       }
		             		  }
		             		 if(check.checked){
		             			 domClass.add("impInfoCheckbox_Error77", "hide");
		             		 }
		                       insuranceFamilyCover.updatePrice();
		        		  });
	        		}
        	  });
        	  on(_.first(query(".agreement-box input",insuranceFamilyCover.individualExpanded)), "click", function(event){
        		                  var checkbox = this;
        		                  checkbox.blur();
        		                  checkbox.focus();
				                  insuranceFamilyCover.confirmationFlag = checkbox.checked;
				                  if (checkbox.checked) {
				                    domClass.add("impInfoCheckbox_Error22", "disNone");
				                  }
        		  	});
    	  }
    	  domClass.remove(insuranceFamilyCover.individualExpanded, "hide");
    		coreFx.wipeIn({
      	      node: insuranceFamilyCover.individualExpanded,
      	      onEnd: function(){
  				}
      	    }).play();

      },

      // Updates the total price when age profile/ pax is selected-deselected
      updatePrice : function(){
    	  var insuranceFamilyCover = this,
    	  	  totalPrice = 0,
    	  	  totalPriceWithEw = 0,
    	  	  excessWaiver = 0,
    	  	  selectElement = null;
    	  _.each(query(".travelling-passenger-section input", insuranceFamilyCover.individualExpanded), function(checkbox){
    		  if(checkbox.checked && !domClass.contains(checkbox,"infant")){
    			  if(!domClass.contains(checkbox,"child")){
    				  selectElement = _.first(query("select", checkbox.parentElement));
    				  switch(selectElement.value.toLowerCase()){
    				  	case "adult" : totalPrice += insuranceFamilyCover.adultPrice;
    				  			break;
    				  	case "senior" : totalPrice += insuranceFamilyCover.seniorPrice;
		  				        break;
    				  	case "supersenior" : totalPrice += insuranceFamilyCover.superSeniorPrice;
    				  	        break;
    				  }

    			  } else {
    				  totalPrice += insuranceFamilyCover.childPrice;
    			  }
    			  excessWaiver  += insuranceFamilyCover.excessWaiverPrice;
    		  }
    	  });
    	  if(excessWaiver){
    		  insuranceFamilyCover.excessWaiverFlag = false;
    		  totalPriceWithEw = totalPrice + excessWaiver;
    	  }
    	  domAttr.set(_.first(query(".simple-add-section strong.insuranceOnlyPrice")), "innerHTML", currency + totalPrice);
    	  domAttr.set(_.first(query(".excess-excess-waiver-section strong.insuranceWithExcessWaiverPrice")), "innerHTML", currency + totalPriceWithEw);
    	  insuranceFamilyCover.totalPrice = currency +  totalPrice;
    	  insuranceFamilyCover.totalPriceWithEw = currency + totalPriceWithEw;
    	  insuranceFamilyCover.iTotalPrice = totalPrice;

      },

      // Builds the expanded section for individual insurance cover
      buildIndividualDetailsSection : function() {
    	  var insuranceFamilyCover = this,
    	  	  html = "";
    	  html = insuranceFamilyCover.renderTmpl(insuranceFamilyCover.insuranceIndividualDetails, this);
    	  domConstruct.place(html, insuranceFamilyCover.individualExpanded,"only");
    	  var detailSectionWidgets = dojo.parser.parse(insuranceFamilyCover.individualExpanded);
    	  _.each(detailSectionWidgets, function(detailWidget){
    		    if(detailWidget.dojoAttachPoint){
    		    	insuranceFamilyCover[detailWidget.dojoAttachPoint] = detailWidget;
    		    }
    	  });
    	  insuranceFamilyCover.isDetailsSectionBuilt = true;
    	  if(insuranceFamilyCover.familyPackagePresent){
    		  domConstruct.place(insuranceFamilyCover.individualExpanded, "fam-ins", "after");
    	  }
      },

      // Collapses expanded section on click of hide details button
      collapseDetails : function () {
    	  var insuranceFamilyCover = this;
    	  domClass.remove(insuranceFamilyCover.paxTable, "hide");
    	  domClass.remove(insuranceFamilyCover.showDetailsBtn, "hide");
    	  domClass.add(insuranceFamilyCover.hideDetailsBtn, "hide");
    	  domClass.remove(insuranceFamilyCover.coverageIndividual, "background-none");
    	  domClass.remove(insuranceFamilyCover.coverageIndividual, "section-expanded");
    	  domClass.remove("ind-ins","expanded");
    	  if(!insuranceFamilyCover.familyPackagePresent){
    		  domStyle.set(insuranceFamilyCover.individualExpanded,"top","-10px");
    	  }
    	  topic.publish("tui/widget/booking/insurnaceB/InsuranceIndividualCover/enableFam");
    		coreFx.wipeOut({
      	      node: insuranceFamilyCover.individualExpanded,
      	      onEnd: function(){
      	    	  domClass.add(insuranceFamilyCover.individualExpanded, "hide");
      	    	  domClass.add(insuranceFamilyCover.extraBorder, "hide");
      	    	  domClass.remove(insuranceFamilyCover.domNode.parentElement, "bbnone");
	      	    	if(!insuranceFamilyCover.familyPackagePresent){
	          		  domStyle.set(insuranceFamilyCover.individualExpanded,"top","-20px");
	          		  if(insuranceFamilyCover.insuranceAdded){
	      	        	domClass.remove(insuranceFamilyCover.removeSection, "hide");
	      	        	insuranceFamilyCover.insuranceAdded = false;
	          		  }
	          	    }
  				}
      	  }).play();

      }
    });
  });
