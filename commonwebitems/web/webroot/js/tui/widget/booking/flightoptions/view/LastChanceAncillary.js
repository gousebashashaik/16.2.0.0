define("tui/widget/booking/flightoptions/view/LastChanceAncillary", [
  "dojo",
  "dojo/_base/declare",
  "dojo/dom-class",
  "dojo/on",
  "dojo/cookie",
   'tui/widget/popup/Popup',
   "dojo/dom-construct",
  "dojo/text!tui/widget/booking/flightoptions/view/templates/LastChanceAncillary.html",
  "dojo/_base/lang",
  "dojo/has",
  "dojo/NodeList-dom",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget"
  ],
  //Holiday Village Kos has multiple seat option, URL below
  //http://fc.localhost.co.uk:9001/holiday/bookaccommodation?productCode=047406&tab=overview&noOfAdults=2&noOfChildren=0&childrenAge=&duration=7&flexibleDays=3&airports[]=LGW|LTN|SEN|STN&flexibility=true&noOfSeniors=0&when=31-07-2014&units[]=&packageId=047406HVMUCT140693760000014070240000004692140762880000014076288000004693DY158&index=4&multiSelect=true&brandType=F&finPos=4
  //http://fc.uktapp30-hybrisa-sp/destinations/destinations.html
    function (dojo, declare, domClass, on, cookie, popup, domConstruct, ancillaryTmpl,lang,has) {

    return declare("tui.widget.booking.flightoptions.view.LastChanceAncillary", [
          tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.popup.Popup], {

	modal: true,
	showSySTmplFlag: true,
	showbaggAllTmplFlag: true,
	navigateTo:'',
	tmpl: ancillaryTmpl,
	baggAllwDomNode: '',
	thirdPartyFlightSelectionFlag: false,
	infantSelectionFlag: false,
	standardBagg: '',
	standardSeat: '',
	allExtraBaggAvailable: [],
	allExtraSeatAvailable: [],
	userData: {},
	skipPopup: false,
	modalConnect: true,
	stopDefaultOnCancel: false,
	tmplData:'',
	includeScroll: true,
	oneWay:false,
	singleBagInclude:true,
	noLabel : false,
		postCreate: function () {
			var ancillary = this;
			ancillary.dojoConfig = dojoConfig;
			ancillary.navigateTo = ancillary.jsonData.pageResponse.targetPageUrl;
			ancillary.controller = dijit.registry.byId("controllerWidget").registerView(ancillary);
			ancillary.setVariables();
			ancillary.getStandardSeat();
			ancillary.getStandardBagg();
			ancillary.setPosOffset(ancillary.floatWhere);
			ancillary.renderTmpl(ancillary.tmpl);
			if( !ancillary.baggAllwDomNode ){
				ancillary.baggAllwDomNode = dojo.query(".baggage-overlay", ancillary.popupDomNode)[0];
				ancillary.SySDomNode = dojo.query(".seating-overlay", ancillary.popupDomNode)[0];
			}
			ancillary.addEventListners();
			//data-dojo-attach-event="onmouseenter:_onHover,onmouseleave:_onUnhover,ondijitclick:_onClick"
			ancillary.inherited(arguments);
		},

		setVariables: function(){
			var ancillary = this;
			ancillary.userData.extraSeatAry=[];
			ancillary.userData.extraBaggAry=[];

			_.each(ancillary.jsonData.packageViewData.extraFacilityCategoryViewData, function(userData){
				if(userData.extraFacilityCategoryCode == "SEAT"){
					ancillary.userData.extraSeatAry = userData.extraFacilityViewData;  // for extra seat related query
				}
				if(userData.extraFacilityCategoryCode == "BAG"){
					ancillary.userData.extraBaggAry = userData.extraFacilityViewData;  // for extra seat related query
				}
			});
			ancillary.allExtraBaggAvailable = ancillary.jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData;
			ancillary.allExtraSeatAvailable = ancillary.jsonData.extraFacilityViewDataContainer.seatOptions.extraFacilityViewData;
			ancillary.tmplData = ancillary.jsonData.flightOptionsStaticContentViewData.flightContentMap;
			//ancillary.userData.noOfInfants = ancillary.jsonData.packageViewData.paxViewData.noOfInfants;
			//ancillary.flightViewData = ancillary.jsonData.packageViewData.flightViewData; //For flight inbound & outbound related query

			ancillary.flightViewData = dijit.registry.byId("controllerWidget").refData(ancillary.jsonData.packageViewData.flightViewData, ancillary.flightIndexValue);

			//ancillary.infantSelectionFlag = ( ancillary.userData.noOfInfants ) ? true : false;
			ancillary.thirdPartyFlightSelectionFlag = false;
			if( ! _.isEmpty(ancillary.flightViewData.inboundSectors )){
				ancillary.thirdPartyFlightSelectionFlag = ancillary.flightViewData.inboundSectors[0].carrierCode == "TOM" ? false : true;
			}
			if( !ancillary.thirdPartyFlightSelectionFlag && ancillary.flightViewData.outboundSectors ){
				ancillary.thirdPartyFlightSelectionFlag = ancillary.flightViewData.outboundSectors[0].carrierCode == "TOM" ? false : true;
			}
		},

		getStandardBagg: function(){
			var ancillary = this;
			var baggExtraFecilityAry = ancillary.allExtraBaggAvailable;
			var stdBagg = false;

		        if (_.isEmpty(ancillary.flightViewData.inboundSectors)){
		        	ancillary.oneWay = true;
		        }

			_.each(baggExtraFecilityAry, function(baggExtras, indx){
				if( !stdBagg && baggExtras.selection == "Included" && ancillary.jsonData.packageType != "FO"){
					stdBagg = true;
					this.noLabel = true;
					ancillary.standardBagg = baggExtras;
					var tmplBaggPromoData = baggExtraFecilityAry[indx+1];
					ancillary.tmplData.Ancillary_Baggage_WeightDet = ancillary.tmplData.Ancillary_Baggage_WeightDet.replace('{{DATA}}', ancillary.standardBagg.weightCode);
					ancillary.tmplData.Ancillary_Baggage_ExtraWeight = !tmplBaggPromoData ? "" : ancillary.tmplData.Ancillary_Baggage_ExtraWeight.replace('{{DATA}}', tmplBaggPromoData.weightCode);
					ancillary.tmplData.Ancillary_Baggage_ExtraPrice = !tmplBaggPromoData? "" : ancillary.tmplData.Ancillary_Baggage_ExtraPrice.replace('{{DATA}}', tmplBaggPromoData.price);
				}
				if( !stdBagg && baggExtras.selection == "Selectable" && baggExtras.selected == false && ancillary.jsonData.packageType == "FO"){
					stdBagg = true;
					this.noLabel = false;
					ancillary.standardBagg = baggExtras;
					var tmplBaggPromoData = baggExtraFecilityAry[indx];
					//ancillary.tmplData.Ancillary_Baggage_WeightDet = ancillary.tmplData.Ancillary_Baggage_WeightDet.replace('{{DATA}}', ancillary.standardBagg.weightCode);
					ancillary.tmplData.Ancillary_Baggage_ExtraWeight = !tmplBaggPromoData ? "" : ancillary.tmplData.Ancillary_Baggage_ExtraWeight.replace('{{DATA}}', tmplBaggPromoData.weightCode);
					if(ancillary.oneWay){
						ancillary.tmplData.Ancillary_Baggage_ExtraPrice_OneWay = !tmplBaggPromoData? "" : ancillary.tmplData.Ancillary_Baggage_ExtraPrice_OneWay.replace('{{DATA}}', tmplBaggPromoData.price);
					}
					else{
						ancillary.tmplData.Ancillary_Baggage_ExtraPrice = !tmplBaggPromoData? "" : ancillary.tmplData.Ancillary_Baggage_ExtraPrice.replace('{{DATA}}', tmplBaggPromoData.price);
					}

				}
				if(!stdBagg && baggExtras.selection == "Included" && baggExtras.weightCode != "0" && ancillary.jsonData.packageType == "FO"){
					stdBagg = true;
					this.noLabel = true;
					ancillary.standardBagg = baggExtras;
					if(baggExtraFecilityAry.length == 1){
						var tmplBaggPromoData = baggExtraFecilityAry[indx];
					}
					else{
						var tmplBaggPromoData = baggExtraFecilityAry[indx+1];
					}

					ancillary.tmplData.Ancillary_Baggage_WeightDet = ancillary.tmplData.Ancillary_Baggage_WeightDet.replace('{{DATA}}', ancillary.standardBagg.weightCode);
					ancillary.tmplData.Ancillary_Baggage_ExtraWeight = !tmplBaggPromoData ? "" : ancillary.tmplData.Ancillary_Baggage_ExtraWeight.replace('{{DATA}}', tmplBaggPromoData.weightCode);
					if(ancillary.oneWay){
						ancillary.tmplData.Ancillary_Baggage_ExtraPrice_OneWay = !tmplBaggPromoData? "" : ancillary.tmplData.Ancillary_Baggage_ExtraPrice_OneWay.replace('{{DATA}}', tmplBaggPromoData.price);
					}
					else{
						ancillary.tmplData.Ancillary_Baggage_ExtraPrice = !tmplBaggPromoData? "" : ancillary.tmplData.Ancillary_Baggage_ExtraPrice.replace('{{DATA}}', tmplBaggPromoData.price);
					}
				}
			});
			//For Premium Seats, standard baggage only will be available
			if( ancillary.jsonData.baggageSectionDisplayed === false ){
				ancillary.allExtraBaggAvailable = [ ancillary.standardBagg ];
			}
		},

		getStandardSeat: function(){
			var ancillary = this;
			var seatExtraFecilityAry = ancillary.allExtraSeatAvailable;
			var stdSeat = false;
			   if (_.isEmpty(ancillary.flightViewData.inboundSectors)){
				   ancillary.oneWay = true;
		        }

			_.each(seatExtraFecilityAry, function(seatExtras, indx){
				if( !stdSeat && seatExtras.selection == "Included"){
					stdSeat = true;
					ancillary.standardSeat = seatExtras;
					var tmplSeatPromoData = seatExtraFecilityAry[indx+1];
					ancillary.tmplData.Ancillary_Seat_PerAdult = !tmplSeatPromoData ? "" : ancillary.tmplData.Ancillary_Seat_PerAdult.replace('{{DATA}}', tmplSeatPromoData.adultPrice);
					if(ancillary.oneWay){
						ancillary.tmplData.Ancillary_Seat_PerChild_OneWay = !tmplSeatPromoData ? "" : ancillary.tmplData.Ancillary_Seat_PerChild_OneWay.replace('{{DATA}}', tmplSeatPromoData.childPrice);
					}
					else{
						ancillary.tmplData.Ancillary_Seat_PerChild = !tmplSeatPromoData ? "" : ancillary.tmplData.Ancillary_Seat_PerChild.replace('{{DATA}}', tmplSeatPromoData.childPrice);
					}

				}
			});
		},

		//Will be called from 'FlightOptionController.js -> publishToViews()'
		refresh: function (field, response) {
			var ancillary = this;
			if(cookie("BggExOpt") || cookie("noTnxOpt")){  return; }
			ancillary.jsonData = response;
			ancillary.setVariables();
			ancillary.getStandardSeat();
			ancillary.getStandardBagg();
			domConstruct.destroy(ancillary.popupDomNode);
			ancillary.popupDomNode = null;
			ancillary.renderTmpl(ancillary.tmpl);
			ancillary.baggAllwDomNode = dojo.query(".baggage-overlay", ancillary.popupDomNode)[0];
			ancillary.SySDomNode = dojo.query(".seating-overlay", ancillary.popupDomNode)[0];
			ancillary.addEventListners();
		},

		addEventListners: function(){
			var ancillary = this;
			 /* Below method  commented because of multiple request sending to the server
			  * on(this.domNode, on.selector(".ensLinkTrack", "click, keyup"), lang.hitch(this, function (event) {
	            if (event.keyCode && event.keyCode !== keys.ENTER) return;
			        this.open();
	        }));*/
			on(dojo.query(".close", ancillary.baggAllwDomNode)[0], "click", function(){
				cookie("BggExOpt", "1");
				cookie("SeatExOpt", "1");
			});
			on(dojo.query(".close", ancillary.SySDomNode)[0], "click", function(){
				cookie("SeatExOpt", "1");
				cookie("BggExOpt", "1");
			});

			on(dojo.query("#thanks", ancillary.popupDomNode)[0], "click", function(){
				cookie("noTnxOpt", "1");
				window.location.href = ancillary.navigateTo;
				return;
			});
		},

		checkBaggageScenarios: function(){
			var ancillary = this;
			if( ancillary.thirdPartyFlightSelectionFlag ){

				ancillary.skipPopup = true;
			}
			if( ancillary.thirdPartyFlightSelectionFlag && ancillary.jsonData.packageType == "FO"){
				//Passenger has selected third party flight, hense skipping the popup
				ancillary.skipPopup = false;
			}
		},

		checkPopupSkipScenarios: function(){
			var ancillary = this;
			ancillary.skipPopup = false;
			ancillary.singleBagInclude = true;
			if( cookie("BggExOpt") && cookie("SeatExOpt") ){
				//Passenger already clikced on both the add buttons, So nothing to show him now.
				ancillary.skipPopup = true;
			}else if( !ancillary.jsonData.baggageSectionDisplayed ){
				//for skipping the popup when premium club got selected for Long Haul
				ancillary.skipPopup = true;
			}else
			if( ancillary.thirdPartyFlightSelectionFlag ){
				//Passenger has selected third party flight, hense skipping the popup
				ancillary.skipPopup = true;
				if(ancillary.thirdPartyFlightSelectionFlag && ancillary.jsonData.packageType == "FO"){
					_.each(ancillary.allExtraBaggAvailable, function(baggExtra){
						if(baggExtra.selected){
							ancillary.singleBagInclude = false;
							}
						});

					if(ancillary.singleBagInclude){
						ancillary.skipPopup = false;
						ancillary.showbaggAllTmpl(); // This change will affect only for Flights Only .
					}

				}
			}else{
				/*if( ancillary.infantSelectionFlag ){
					//Passenger has infant in their party, hense skipping the popup
					ancillary.skipPopup = true;
				}else{*/
					//Check extra ancillary (seat) is selected or not.
					var standardSeat = true, showSySTmpl = true;
					_.each(ancillary.userData.extraSeatAry, function(seatExtra){

						if( standardSeat && seatExtra.code != ancillary.standardSeat.code){
							//Extra ancillary (seat) is selected, hense should not show the SySTmpl
							showSySTmpl = standardSeat = false;
							cookie("SeatExOpt", "1");//Now passenger selcted the seat extrs,hense setting the cookie
						}
					});

					//Check extra ancillary (baggage) is selected or not.
					var standardBagg = true, showBaggAllowTmpl = true;
					_.each(ancillary.userData.extraBaggAry, function(baggExtra){

						if( standardBagg && baggExtra.code != ancillary.standardBagg.code && baggExtra.weightCode !="0"){
								//Extra ancillary (baggage) is selected, hense should not show the baggAllowTmpl
								showBaggAllowTmpl = standardBagg = false;
								cookie("BggExOpt", "1");//Now passenger selcted the baggage extrs,hense setting the cookie
							}
						});

					if( cookie("SeatExOpt") && !standardBagg){
						//Now seat cookie has set, So no more Seat popup. As non standard baggage option is selected, we can skip baggage popup also
						// Hense skipping the popup
						ancillary.skipPopup = true;
					}
					else if( cookie("BggExOpt") && !standardSeat){
						//Now bagg cookie has set, So no more Baggage popup. As non standard seat option is selected, we can skip seat popup also
						// Hense skipping the popup
						ancillary.skipPopup = true;
					}

					//TODO: not sure the below length check is proper or not
					//Length is checking for identifying whether extra baggage or seat is available or not
					else if( standardSeat && standardBagg && ancillary.allExtraSeatAvailable.length <= 1 && ancillary.allExtraBaggAvailable.length <= 1 ){

						ancillary.skipPopup = true;

					//Passenger has selected standard seat and baggage, and extra ancillaries are not at all available to select, hense skipping the popup

					}else
					if( !standardSeat && !standardBagg){
						//Passenger already selected extra ancillaries (both seat & Baggage), skipping the popup
						ancillary.skipPopup = true;
					}else{
						//Popup skip scenario is over and now, heading for template show/hide scenarios
						ancillary.hideAllTmpls();

						if( standardSeat && standardBagg ){
							//Passenger has selected standard seat and baggage
							if( ancillary.allExtraSeatAvailable.length > 1 && ancillary.allExtraBaggAvailable.length > 1 ){
								//As extra seat and baggage is avilable to select, passenger will see both the templates
								ancillary.showSySTmpl();
								ancillary.showbaggAllTmpl();
							}else if( ancillary.allExtraSeatAvailable.length <= 1 && ancillary.allExtraBaggAvailable.length > 1  ){
								//As extra seat is not available, but baggage is avilable to select, passenger will see only the baggage templates
								ancillary.showbaggAllTmpl();
							}else if( ancillary.allExtraSeatAvailable.length > 1 && ancillary.allExtraBaggAvailable.length <= 1  ){
								//As extra baggage option is not available, but seat option is avilable to select, passenger will see only the seat templates
								ancillary.showSySTmpl();
								if(ancillary.allExtraBaggAvailable.length == 1 && ancillary.jsonData.packageType == "FO" &&  standardBagg ){
									_.each(ancillary.allExtraBaggAvailable, function(baggExtra){
										if(baggExtra.selected){
											ancillary.singleBagInclude = false;
											}
										});
									if(ancillary.singleBagInclude || ancillary.thirdPartyFlightSelectionFlag){
										ancillary.showbaggAllTmpl(); // This change will affect only for Flights Only as we do not get default baggage.
									}

								}

							}
						}else if( standardSeat && !standardBagg ){
							//Passenger has selected a standard seat and an extra baggage option
							if( ancillary.allExtraSeatAvailable.length > 1 ){
								//As the passenger already selected the extra baggage option, the baggage template will not be displayed
								//As extra seat option is available to selct, passenger will see only the seat template
								ancillary.showSySTmpl();
							}else{ ancillary.skipPopup = true; }
						}else if( !standardSeat && standardBagg ){
							//Passenger has selected a standard baggage and an extra seat option
							if( ancillary.allExtraBaggAvailable.length > 1 ){
								//As the passenger already selected the extra seat, the seat template will not be displayed
								//As extra baggage option is available to selct, passenger will see only the baggage template
								ancillary.showbaggAllTmpl();
							}else{ ancillary.skipPopup = true; }
						}
						if(ancillary.allExtraBaggAvailable.length == 1 && ancillary.jsonData.packageType == "FO" &&  !standardBagg ){
							if( ancillary.thirdPartyFlightSelectionFlag ){ancillary.skipPopup = false;}
							ancillary.skipPopup = false;// This change will affect only for Flights Only as we do not get default baggage.
						}

					}


				//}
			}



			return ancillary.skipPopup;
		},
		open: function () {
            var ancillary = this;
            if(  cookie("noTnxOpt") || ancillary.checkPopupSkipScenarios() ){
				window.location.href= ancillary.navigateTo;
				return;
			}
			domClass.remove(ancillary.popupDomNode, "baggage-popup seat-popup");
			if( domClass.contains(ancillary.SySDomNode, "hide") ){
				domClass.add(ancillary.popupDomNode, "baggage-popup");
				dojo.query("p.button", ancillary.popupDomNode).attr("analytics-id", "Baggage Popup");
			}else
			if( domClass.contains(ancillary.baggAllwDomNode, "hide")){
				domClass.add(ancillary.popupDomNode, "seat-popup");
				dojo.query("p.button", ancillary.popupDomNode).attr("analytics-id", "Seats Popup");
			}
			else{
				dojo.query("p.button", ancillary.popupDomNode).attr("analytics-id", "Baggage and Seats Popup");
			}
			ancillary.inherited(arguments);
        },

		showSySTmpl: function(){
			var ancillary = this;
			if( !cookie("SeatExOpt") ){
				domClass.remove(ancillary.SySDomNode, "hide");
			}
		},

		showbaggAllTmpl: function(){
			var ancillary = this;
			if(  !cookie("BggExOpt") ){
				domClass.remove(ancillary.baggAllwDomNode, "hide");
			}
		},

		hideAllTmpls: function(){
			var ancillary = this;
			domClass.add(ancillary.SySDomNode, "hide");
			domClass.add(ancillary.baggAllwDomNode, "hide");
		}
    });
  });