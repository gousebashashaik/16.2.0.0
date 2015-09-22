define("tui/widget/booking/yourflight/view/YourFlights", ["dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/on",
  "dojo/fx",
  'dojo/query',
  'dojo/dom-class',
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/yourflight/view/Templates/YourFlightTmpl.html",
  "dojo/topic",
  "dojo/html",
  "dojo/parser",
  "tui/config/TuiConfig",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"],
  function (declare, _WidgetBase, Templatable, dtlTemplated, on, coreFx, query, domClass, domStyle, lang, dom, domConstruct, YourFlightTmpl, topic, html, parser, tuiConfig) {
    return declare("tui.widget.booking.yourflight.view.YourFlights", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

      tmpl: YourFlightTmpl,
      templateString: "",
      firstCruise:"",
      secondCruise:"",
      tuiConfig: tuiConfig,
      itemSelector : ".show-item-toggle",
      targetSelector:"3pfmessaging",
      thirdPartyFlightSelectionFlag:"",
      get3PFCarrierFlag:'',

      siteName : dojoConfig.site,

      postMixInProperties: function () {
			this.contentDisplay = "stay";

		},

	  generateDualBrandCode: function(){
		  var yourFlight = this,
		  	  dualBrandValue = "";
		  if(yourFlight.accomViewData && yourFlight.accomViewData.productRanges && yourFlight.accomViewData.productRanges.length){
			  yourFlight.accomViewData.productRanges[0].modifiedCode = yourFlight.accomViewData.productRanges[0].code;
			  if(dojoConfig.dualBrandSwitch  && yourFlight.tuiConfig[dojoConfig.site]){
				  dualBrandValue = yourFlight.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCode[yourFlight.accomViewData.productRanges[0].code.toLowerCase().replace(/\s/g,"")];
				  if(dualBrandValue){
					  yourFlight.accomViewData.productRanges[0].modifiedCode = dualBrandValue;
				  }
			  }
		  }
	  },

      buildRendering: function () {
    	this.generateDualBrandCode();
    	this.find3PFCarrier();
    	if (!_.isEmpty(this.jsonData.thirdPartyInfoMap)){
    		this.build3PFMap(this.jsonData.thirdPartyInfoMap);
    	}
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        var widget = this;
        var widgetDom = widget.domNode;
        var controller = null;
        widget.controller = dijit.registry.byId("controllerWidget");
        widget.controller.registerView(this);
        parser.parse(widget.domNode);
        this.inherited(arguments);
        widget.tagElements(query('a.change-flight'),"changeFlight");
        var changeLink =dom.byId("changeFlights");
        if(changeLink){
        this.clickhandler =  on(dom.byId("changeFlights"), "click", lang.hitch(this, function () {
        	topic.publish("flightOptions.overlay.show", this.jsonData);
        }));
        }
        /*3PF messaging related code - start */
        widget.showMoreInfoClicked();
        /*3PF messaging related code - end */
      },

      handleBTBItenaries:function(){
    	  if(this.packageTypeValue == "BTB"){
      		var str=this.accomViewData.accomName;
      		  var accom= str.split("|");
      		  this.firstCruise=accom[0];
        		this.secondCruise=accom[1];
        }

      },
        /*3PF messaging related code - start */

      build3PFMap : function (thirdPartyInfoMap){
    	  var getCarrierCode = "",
    	  widget = this;

    	  if(widget.jsonData.packageViewData.flightViewData.length){
    		  _.each(widget.jsonData.packageViewData.flightViewData, function(outboundCarrier){
    			  getCarrierCode = outboundCarrier.outboundSectors[0].carrierCode;
    			  if(thirdPartyInfoMap[getCarrierCode]){
    			  	widget.thirdPartyInfo = widget.get3PFDetailsMap(getCarrierCode,thirdPartyInfoMap[getCarrierCode]);
    			  }
    		  });
    	  }
    	  if(widget.jsonData.packageViewData.flightViewData.length){
    		  _.each(widget.jsonData.packageViewData.flightViewData, function(inboundCarrier){
      				getCarrierCode = inboundCarrier.inboundSectors[0].carrierCode;
      				if(thirdPartyInfoMap[getCarrierCode]){
      					widget.thirdPartyInboundInfo = widget.get3PFDetailsMap(getCarrierCode,thirdPartyInfoMap[getCarrierCode]);
      				}
      			});
      		}
      },

      get3PFDetailsMap : function(carrierCode, thirdPartyFlight){
    	  return {
				"introText1" 	: thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText1"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText1"] : "",
				"introText2"	: thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText2"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText2"] :"",
				"introText3"	: thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText3"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_IntroText3"] :"",
				"flightName"	: thirdPartyFlight[carrierCode + "_ThirdPartyFlightText"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlightText"] :"",
				"heading":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBoundHeading"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBoundHeading"] :"",
				"checkIn":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn"] :"",
				"checkIn1":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn1"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn1"] :"",
				"checkIn2": thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn2"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_CheckIn2"] :"",
				"bagDrop":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop"] :"",
				"bagDrop1":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop1"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop1"] :"",
				"bagDrop2":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop2"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop2"]  :"",
				"bagDrop3":thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop3"] ? thirdPartyFlight[carrierCode + "_ThirdPartyFlight_OutBound_BagDrop3"] :""
    	  }
      },

		find3PFCarrier: function () {
			var widget = this;
			widget.getinboundThirdParty = widget.jsonData.packageViewData.flightViewData[0].inboundThirdParty;
			widget.getoutBoundThirdParty = widget.jsonData.packageViewData.flightViewData[0].outBoundThirdParty;
			widget.thirdPartyFlightSelectionFlag=false;
			if (widget.jsonData.packageViewData.multicomThirdPartyFlight) {
				if(( widget.getinboundThirdParty==true || widget.getoutBoundThirdParty==true )){
					widget.thirdPartyFlightSelectionFlag = true;
					widget.get3PFCarrierFlag = "mixed_carrier";
				}
				if( widget.getinboundThirdParty==true && widget.getoutBoundThirdParty==true){
					widget.thirdPartyFlightSelectionFlag = true;
					widget.get3PFCarrierFlag = "single_carrier";
				}
			}
      },

      showMoreInfoClicked: function(){
    	  var widget = this;
    	  var getclassName = query(widget.itemSelector);
		  
		  var get3pfSection = dojo.byId(widget.targetSelector);

    	  if(get3pfSection){
    		  domStyle.set(get3pfSection, "display", "none");
    	  }


				
    	 
    	  query(widget.itemSelector).text(widget.defaultTxt);

      	  on(getclassName, "click", function(){
      		  widget.get_mod = ("none" === dojo.style(widget.targetSelector, "display")) ? "wipeIn" : "wipeOut";
      		  if(widget.get_mod=="wipeIn"){
      			  widget.FOwipeIn();
      		  }
      		  if (widget.get_mod === 'wipeOut'){
      			  widget.FOwipeOut();
      		  }
      	  });

      },

      FOwipeIn : function(){
    	  var widget = this;
      		coreFx.wipeIn({
    	      node: widget.targetSelector,
    	      onEnd: function(){
    	    		query(widget.itemSelector).text(widget.expandTxt);
				}
    	    }).play();
      },

      FOwipeOut : function(){
    	  var widget = this;
    	  coreFx.wipeOut({
    		  node: widget.targetSelector,
    		  onEnd: function(){
    			  query(widget.itemSelector).text(widget.defaultTxt);
    		  }
   	    }).play();

     	var getclassName = query(widget.itemSelector);

     }, /*3PF messaging related code - end */

      refresh: function (field, response) {
        var widget = this;
        widget.jsonData = response;
        widget.controller = dijit.registry.byId("controllerWidget");
        widget.flightViewData = widget.controller.refData(widget.jsonData.packageViewData.flightViewData, widget.flightIndexVal);
        widget.accomViewData = widget.controller.refData(widget.jsonData.packageViewData.accomViewData, widget.accomIndexVal);
        widget.generateDualBrandCode();
        widget.find3PFCarrier();
        if (!_.isEmpty(this.jsonData.thirdPartyInfoMap)){
        	this.build3PFMap(widget.jsonData.thirdPartyInfoMap);
        }
        topic.subscribe("flightoptions.alternateflightsOverlay.getData", lang.hitch(this, function () {
          dijit.byId("duration-tab").model = this.jsonData;
        }));
        this.packageTypeValue = this.jsonData.packageType;
        this.handleBTBItenaries();
        var html = widget.renderTmpl(widget.tmpl, widget);
        domConstruct.place(html, widget.domNode, 'only');
        parser.parse(widget.domNode);
        widget.showMoreInfoClicked();
        
		
        var get3pfSection = dojo.byId("3pfmessaging");
	  	if(get3pfSection){
	  		domStyle.set(get3pfSection, "display", "none");
	  	}
        var changeLink =dom.byId("changeFlights");
        if(changeLink){
        this.clickhandler.remove();
        this.clickhandler = on(dom.byId("changeFlights"), "click", lang.hitch(this, function () {
          topic.publish("flightOptions.overlay.show", this.jsonData);
        }));
      }
      }
    });
  });