define("tui/widget/booking/infantoptions/view/PrebookInfantEquipment", [
    "dojo/_base/declare",
    "dojo/query",
    "dojo/dom-class",
    "dojo/dom-attr",
    "dojo/dom-style",
    "dojo/dom",
    "dojo/on",
    "dojo/_base/lang",
    "dojo/dom-construct",
    "dojo/text!tui/widget/booking/infantoptions/view/templates/InfantHotelOptionsTmpl.html",
    "dojo/html",
    "dojo/Evented",
    "tui/widget/_TuiBaseWidget",
    "dojox/dtl/_Templated",
    "tui/widget/mixins/Templatable",
    "tui/widget/expand/Expandable",
    "tui/widget/booking/infantoptions/view/InfantHotelOptionsOverlay",
    "tui/widget/booking/infantoptions/view/InfantSelectOptionView",
    "tui/widget/booking/infantoptions/view/CrecheOptionsOverlay",
    "tui/widget/booking/infantoptions/view/CrecheSpaceView",
    "tui/widget/booking/constants/BookflowUrl",
    "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
    "dojo/_base/json"



    ], function(declare, query, domClass,domAttr, domStyle, dom, on, lang, domConstruct, InfantHotelOptionsTmpl,
                                                                        		html, Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable,
                                                                        		InfantHotelOptionsOverlay,InfantSelectOptionView,CrecheOptionsOverlay,CrecheSpaceView,BookflowUrl,Bookflowi18nable, jsonUtil) {

	return declare("tui.widget.booking.infantoptions.view.PrebookInfantEquipment",
			[_TuiBaseWidget, dtlTemplate, Templatable,  Evented, Bookflowi18nable], {

		tmpl:InfantHotelOptionsTmpl,
		templateString: "",
		widgetsInTemplate : true,
		controller:  null,
		infantFlag: false,
		flagCreche: false,
		model: null,
		removeInfantLinkFlag:false,
		removeCrecheLinkFlag:false,
		removeCategory:"",
		categoryInfantType:"",
		categoryCrecheType:"",
		crechOverlayData1:"",
		obj1Url:BookflowUrl.infantoptionurl,
		obj2Url:BookflowUrl.hoteloptionremoveurl,
		Obj2OLUrl:BookflowUrl.crechespaceurl,
		contentRefObjPath:"extraOptionsContentViewData.extraContentViewData",



		postMixInProperties: function () {
			this.model = this.jsonData.extraFacilityViewDataContainer;
			this.setFlags();
			this.initBookflowMessaging();
			this.setContentValues();

		},
		setContentValues: function(){
			this.contentObject = this.jsonData ;
			var objetRef = this.bookflowMessaging["InfantAndcelebStye"];
			this.heading = objetRef.heading = "For the little ones";
			this.overlayHeading = objetRef.overlayHeading = "Prebook infant equipment";
			this.genericObj={};
			this.genericObj.classNam= this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.classDiff;
			this.genericObj.buttonClasNam= this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.buttonClassName;
			this.imageSRC1	=	objetRef["obj1"].imgSrc = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.infant_equip_info_key_value_pair_2_small;
			this.strapLine1 = objetRef["obj1"].strapLine = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.infant_equip_info_StrapLine;
			this.intro1 = objetRef["obj1"].intro = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.infant_equip_info_Intro;

			this.buttonText = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.buttonText;
			this.staticContentPer = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.statciContent;

			this.imageSRC2	=	objetRef["obj2"].imgSrc = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Key_Value_One_small;
			this.strapLine2 = objetRef["obj2"].strapLine = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Key_Value_One_Title;
			this.intro2 = objetRef["obj2"].intro = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Key_Value_One_Body;
			this.crechOverlayData1 = objetRef["obj2"].crechOverlayData={"imageSrc":this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Intro_medium ,
					"usp":this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_usp,
					"descriptionTitle":this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Key_Value_One_Title,
					"descriptionIntro":this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.creche_Intro
					};

		},

		buildRendering: function(){
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		postCreate : function() {
			this.intializeCrecheView();
			this.intializeInfantOptionsView();
			this.removeInfantLink = query(".prebook-removeqty",this.domNode);
			this.removeCrecheLink= query(".creche-removeqty", this.domNode);
			this.attachEventsForRemoveLinks();
			this.inherited(arguments);
			this.tagElements(dojo.query('.infantOptions', this.domNode),"Pre-book infant equipment");
			this.tagElements(dojo.query('.crecheOptions', this.domNode),"Secure a creche space");
			// Tagging particular element.
			if(this.autoTag) {
				this.tagElements(query(this.targetSelector, this.domNode), 'toggle');
			}
			this.controller = dijit.registry.byId("controllerWidget");
			this.controller.registerView(this);

		},

		attachEventsForRemoveLinks : function(){

			_.each(this.removeInfantLink, lang.hitch(this, function(removeLink) {
				on(removeLink,'click', lang.hitch(this, this.handleInfantRemoveLinks, removeLink));
			}));
			_.each(this.removeCrecheLink, lang.hitch(this, function (removeLink) {
				on(removeLink, 'click', lang.hitch(this, this.handleCrecheRemoveLink, removeLink));
			}));

		},

		intializeInfantOptionsView : function () {
			if(this.infantOptionsButton) {
				on(this.infantOptionsButton, "click", lang.hitch(this, this.handleInfantOptionsButton));
			}else if(this.infantChangeButton) {
				on(this.infantChangeButton, "click", lang.hitch(this, this.handleInfantOptionsButton));
			}
		},

		intializeCrecheView: function () {
			if (this.obj2.extrasToPassengerMapping && this.obj2.extrasToPassengerMapping.length > 0) {
				if (this.crecheDetailsButton) {
					on(this.crecheDetailsButton, "click", lang.hitch(this, this.handleCrecheButton));
				} else if (this.crecheChangeButton) {
					on(this.crecheChangeButton, "click", lang.hitch(this, this.handleCrecheButton));
				}
			}
		},
		getContentFunction: function(dataPathRef , contextObj){
	        return lang.getObject(dataPathRef, false ,contextObj ) ;
	     },

		handleInfantOptionsButton : function () {

			if(this.infantSelectOptionView && this.infantSelectOptionView != null) {
				this.infantSelectOptionView.destroyRecursive();
				this.infantSelectOptionView = null;
				this.infantHotelOptionsOverlay.destroyRecursive();
				this.infantHotelOptionsOverlay = null;
			}

			var contentObj = this.getContentFunction(this.contentRefObjPath,this.jsonData);

			this.infantSelectOptionView = new InfantSelectOptionView ({
				"infantSelectOptionData": this.obj1,
				"id" : "prebook-infant-section",
				"jsonData": this.jsonData,
				"contentObj":contentObj,
				"headingContent": this.overlayHeading,
				"url":this.obj1Url,
				"genericContent":this.genericObj,
				"overlayRef":null
			});
			domConstruct.place(this.infantSelectOptionView.domNode, this.domNode, "last");
			this.infantHotelOptionsOverlay = new InfantHotelOptionsOverlay({widgetId:this.infantSelectOptionView.id, modal: true});
			this.infantSelectOptionView.overlayRef = this.infantHotelOptionsOverlay;
			this.infantHotelOptionsOverlay.open();
		},

		handleCrecheButton: function () {
			if (this.crecheSpaceView && this.crecheSpaceView !== null) {
				this.crecheSpaceView.destroyRecursive();
				this.crecheSpaceView = null;
				this.crecheOverlay.destroyRecursive();
				this.crecheOverlay = null;
			}
			this.crecheSpaceView = new CrecheSpaceView({
				"crecheOptionsData": this.obj2,
				"id": "crechespaceoverlay",
				"jsonData": this.jsonData,
				"infantContent":this.crechOverlayData1,
				"url":this.Obj2OLUrl,
				"overlayRef":null
			});
			domConstruct.place(this.crecheSpaceView.domNode, this.domNode, "last");
			this.crecheOverlay = new CrecheOptionsOverlay({"widgetId": this.crecheSpaceView.id, "modal": true});
			this.crecheSpaceView.overlayRef = this.crecheOverlay;
			this.crecheOverlay.open();
		},


		handleInfantRemoveLinks : function (removeLink) {
			  this.removeInfantLinkFlag= true;
			var keyValue = removeLink.id.split('|');
			var key=keyValue[0]
			var price = keyValue[1];
			this.categoryInfantType=price;
			var extraCode = removeLink.name;
			var parentRemoveLink = dojo.query(removeLink).parents(".prebook-confirmation");
			parentRemoveLink[0].style.display = "none";
			value=0;

			 var removeObj = [
			                  {extraCode: key, quantity: value, extraCategoryCode: extraCode}
			                ];
			var url = this.obj1Url;
			 this.ajaxRequestFunc("infant","equipments",url,removeObj);

		},

		ajaxRequestFunc: function(field,requestDataName,url,ajaxObj){

			var requestData={};
			requestData[requestDataName] = jsonUtil.toJson(ajaxObj);
			this.controller.generateRequest(field,url,requestData);
		},

		handleCrecheRemoveLink: function (removeLink) {
			this.removeCrecheLinkFlag= true;
			var keyValue = removeLink.id.split('|');
			var key=keyValue[0]
			var price = keyValue[1];
			this.categoryInfantType=price;
	        var categoryCodes = removeLink.name.split('|');
	        var paxID = categoryCodes[0];
	        var extraCode = categoryCodes[1];
			var parentRemoveLink = query(removeLink).parents(".prebook-confirmation");
			var quantity = 0;
			var removeObj = [
	          {passengerId: paxID, extraCode: key, quantity: quantity, extraCategoryCode: extraCode}
			                 ];
			parentRemoveLink[0].style.display = "none";
			var url =this.obj2Url;
			this.ajaxRequestFunc("stage","removeExtra",url,removeObj);



		},

		removeLCR:function (price, prebookMgs, getLCRRemoveText) {
			var widget = this;
			domAttr.set(prebookMgs.id, "innerHTML", price);
			getLCRRemoveText[0].style.display = "block";
				var thiscloseNode=query('.closeLink',this.domNode);
				 on(thiscloseNode,'click', lang.hitch(this, function(){
					 getLCRRemoveText[0].style.display = "none";
				 }));
			var fadeArgs = {
	                node: getLCRRemoveText[0],
	                duration: BookflowUrl.fadeOutDuration,
	                onEnd: function () {
	                	getLCRRemoveText[0].style.display = "none";
	                }
	              };
	              dojo.fadeOut(fadeArgs).play();
		},


		setFlags : function() {
			this.flagCreche = false;
			this.infantFlag=false;

			if (this.obj2.extrasToPassengerMapping) {
				for (var i = 0; i < this.obj2.extrasToPassengerMapping.length; i++) {
					for (var j = 0; j < this.obj2.extrasToPassengerMapping[i].extras.length; j++) {
						if (this.obj2.extrasToPassengerMapping[i].extras[j].selected == true) {
							this.flagCreche = true;
							break;
						}
					}
					if (this.flagCreche === true) {
						break;
					}
				}
			}


			for(var j=0;j<this.obj1.extraFacilityViewData.length;j++){
				if(this.obj1.extraFacilityViewData[j].selected == true){
					this.infantFlag = true;
					break;
				}
			}

		},

		refresh : function(field,response) {
			this.jsonData = response;
			this.model = response.extraFacilityViewDataContainer;
			this.obj1 = response.extraFacilityViewDataContainer.infantOptions;
			this.obj2 = response.extraFacilityViewDataContainer.crecheOptions;
			this.destroyRecursive();
			var node = domConstruct.create("div", null, dom.byId("infantOptionsHolder"));
			this.create({
				"jsonData" : response,
				"obj1": response.extraFacilityViewDataContainer.infantOptions,
				"obj2": response.extraFacilityViewDataContainer.crecheOptions,
				"transitionType" : 'WipeInOut'
			}, node);
			 if(this.removeInfantLinkFlag == true){
					this.removeInfantLinkFlag = false;
					var prebookMgs = dom.byId("prebook-mgs");
					var getLCRRemoveText = query('.prebook-removed',this.domNode);
					this.removeLCR(this.categoryInfantType+" "+"has been removed", prebookMgs,getLCRRemoveText);
				}
			 if(this.removeCrecheLinkFlag == true){
					this.removeCrecheLinkFlag = false;
					var crecheMgs = dom.byId("creche-mgs");
					 var getLCRRemoveText = query('.creche-removed',this.domNode);
				       this.removeLCR(this.categoryInfantType+" "+ "has been removed", crecheMgs, getLCRRemoveText);

				}

		}

	});
});