define("tui/widget/booking/latecheckout/view/LateCheckoutDeal", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/on",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/latecheckout/view/templates/LateCheckoutTmpl.html",
  "dojo/text!tui/widget/booking/latecheckout/view/templates/LateCheckoutTmplFcTh.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/booking/latecheckout/view/LateCheckOutOverlay",
  "tui/widget/booking/latecheckout/view/LateCheckOutView",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/Expandable"

   ], function (declare, query, domClass, domStyle, dom, on, lang, domConstruct, LateCheckoutTmpl,LateCheckoutTmplFcTh,
              html, Evented, _TuiBaseWidget, dtlTemplate, Templatable,
              LateCheckOutOverlay,LateCheckOutView,BookflowUrl, Expandable ) {

  return declare("tui.widget.booking.latecheckout.view.LateCheckoutDeal",
    [ tui.widget.booking.Expandable, _TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: LateCheckoutTmpl,
	tmplFcTh: LateCheckoutTmplFcTh,
      templateString: "",
      widgetsInTemplate: true,
      controller: null,
      removeLinkFlag: false,

      model: null,

      postCreate: function () {
        var widget = this;
        this.intializeLCDView();
        this.controller = dijit.registry.byId("controllerWidget");
        this.controller.registerView(this);
        this.removeLink = query(".removeqty", this.domNode);
        this.attachEventsForRemoveLink();
       /* this.forTransitionEffect();*/
        this.inherited(arguments);
        widget.tagElements(dojo.query('button.singleroombutton'),"Late checkout room");
        if (this.autoTag) {
          this.tagElements(query(this.targetSelector, this.domNode), 'toggle');
        }
        dojo.subscribe("tui/widget/booking/displayContent", function () {
          widget.displayContent();
        });
        this.displayContent();
      },
		
		attachEventsForRemoveLink : function(){

			_.each(this.removeLink, lang.hitch(this, function(removeLink) {
				on(removeLink,'click', lang.hitch(this, this.handleRemoveButton, removeLink));
			}));
			var closeLink=dom.byId("lcdCloseLink");
			if(closeLink){
			on(dom.byId("lcdCloseLink"),'click', lang.hitch(this, this.lcdCloseLink));
			}
		},

		lcdCloseLink: function(){
			var getLCRRemoveText = query('.lateco-removed',this.domNode)[0];
			getLCRRemoveText.style.display = "none";
		},

		buildRendering: function(){
			
			if(tuiSiteName == 'firstchoice' || tuiSiteName == 'thomson'){
				 this.modifyJson();                				
				this.templateString = this.renderTmpl(this.tmplFcTh, this);
			}
			else{
			this.templateString = this.renderTmpl(this.tmpl, this);
			}
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		intializeLCDView : function () {

			if(this.lcdDetailsButton) {
				on(this.lcdDetailsButton, "click", lang.hitch(this, this.handleLCDButton));
			}else if(this.lcdChangeButton) {
				on(this.lcdChangeButton	, "click", lang.hitch(this, this.handleLCDButton));
			}else if(this.lcdAddSingleRooomButton) {
				on(this.lcdAddSingleRooomButton	, "click", lang.hitch(this, this.handleLCDAddButton));
			}
		},

		handleLCDAddButton : function () {
			var value=this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData[0].quantity;
			this.generateRequest(value);
		},
		handleRemoveButton : function () {
			var value="";
			this.removeLinkFlag= true;
			this.generateRequest(value);
			
		},
		
		removeLCR:function () {
			var getLCRRemoveText = query('.lateco-removed',this.domNode);
			getLCRRemoveText[0].style.display = "block";

			var fadeArgs = {
	                node: getLCRRemoveText[0],
	                duration: BookflowUrl.fadeOutDuration,
	                onEnd: function () {
	                	getLCRRemoveText[0].style.display = "none";
				}
	              };
	              dojo.fadeOut(fadeArgs).play();

		},
		generateRequest : function(value){
			var widget = this;
			var extraCategory = widget.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityCategoryCode;
			var requestData={extraCategory:extraCategory,extraCode:'LCD',quantity:value};
			var url =BookflowUrl.latecheckouturl;
			this.controller.generateRequest("latecheckout",url,requestData);
		},

		handleLCDButton : function () {
			if(this.lateCheckOutView != null) {
				this.lateCheckOutView.destroyRecursive();
				this.lateCheckOutView = null;
				//this.lateCheckOutOverlay.destroyRecursive();
				this.lateCheckOutOverlay= null;
			}
			this.lateCheckOutView = new LateCheckOutView ({
				"lcdData": this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData,
				"id" : "addrooms-lateCheckout",
				"extraCategory":this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityCategoryCode
			});
			domConstruct.place(this.lateCheckOutView.domNode, this.domNode, "last");
			this.lateCheckOutOverlay = new LateCheckOutOverlay({widgetId:this.lateCheckOutView.id, modal: true});
			this.lateCheckOutOverlay.open();
		},
		
		forTransitionEffect: function(){
			var widget= this;
			widget.transitionOptions.domNode = widget.domNode;
			widget.transition = widget.addTransition();
			// Tagging particular element.
			if(widget.autoTag) {
				widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), 'toggle');
			}

		},
		
		displayContent: function(){

			var widget = this;
			var widgetDom = widget.domNode;
			var items = dojo.query(".open",widgetDom);
			var allItems = dojo.query(".item",widgetDom);
			_.each(allItems,function(item){
				var header = dojo.query(".item-toggle",item)[0];
				var thresholdAvailability = dojo.query(".limitedLCDAvailabilityAccordian",item)[0];

				if(thresholdAvailability != null){
					domClass.remove(thresholdAvailability,"displayNone");
				}

			});
			_.each(items,function(item){
				var header = dojo.query(".item-toggle",item)[0];

				var thresholdAvailability = dojo.query(".limitedLCDAvailabilityAccordian",item)[0];

				if(thresholdAvailability != null){
					domClass.add(thresholdAvailability,"displayNone");
				}
			})
		},
		
		refresh : function(field,response) {
			var parentNode = this.domNode.parentElement;
			this.jsonData = response;
			this.destroyRecursive();
			/*this.forTransitionEffect();*/
			var node = domConstruct.create("div", null, dom.byId("lcdHolder"));
			this.create({
				"jsonData" : response,
				"transitionType" : 'WipeInOut'
			}, node);
			if(this.lateCheckOutView != null) {
				this.lateCheckOutView.destroyRecursive();
				this.lateCheckOutView = null;
			}

			if(this.removeLinkFlag == true){
				this.removeLinkFlag = false;
				this.removeLCR();
			}




			if(response.extraFacilityViewDataContainer.lateCheckOut.available == true){
				this.lateCheckOutView = new LateCheckOutView ({
					lcdData: response.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData
				});
			}
		
		},
		formatAMPM: function (timeString) {
		  console.log("input time:"+timeString);
		  if(timeString && timeString.indexOf(":") != -1){			
			//return  dojo.date.locale.format(new Date(Date.parse(new Date().getYear()+" "+new Date().getMonth()+" "+new Date().getDay()+" "+timeString)), {timePattern: "h:mma", selector: "time"});
			var timeString = timeString.toString().replace(" ", "");			
			var H = +timeString.substr(0, 2);
			var h = H % 12 || 12;
			var ampm = H < 12 ? "AM" : "PM";
			timeString = h + timeString.substr(2, 3) + ampm;
			console.log("output time:"+timeString);
			return timeString;
		  }
		},
		modifyJson: function(){
			this.timeDisplay = this.formatAMPM(this.jsonData.packageViewData.flightViewData[0].inboundSectors[0].schedule.depTime);
			var lcdData = this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData;
			this.lateCheckoutRoomsLength = 0;
			for (var i = 0; i < lcdData.length; i++) {
				var price = (this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData[i].currencyAppendedPerPersonPrice).replace(".00","");
				this.jsonData.extraFacilityViewDataContainer.lateCheckOut.extraFacilityViewData[i].currencyAppendedPerPersonPriceWithoutDecimal = price;
				for (var k = 0, j = 1; j <= lcdData[i].quantity; j++, k++) {
					this.lateCheckoutRoomsLength++;
				}
		}
			//jsonData.packageViewData.extraFacilityCategoryViewData.0.extraFacilityViewData.0.currencyAppendedPrice
			var extras = this.jsonData.packageViewData.extraFacilityCategoryViewData;
			for (var i = 0; i < extras.length; i++) {
				var facilityData = extras[i].extraFacilityViewData;			

				for( var j = 0; j < facilityData.length; j++ ){
					var roomPrice = facilityData[j].currencyAppendedPrice.replace(".00","");
					this.jsonData.packageViewData.extraFacilityCategoryViewData[i].extraFacilityViewData[j].currencyAppendedPriceWithoutDecimals = roomPrice;
				}
			}
		}
	});
});