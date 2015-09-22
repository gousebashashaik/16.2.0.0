define("tui/widget/booking/attractions/view/AttractionOptionsComponentPanel", [
                                                                               "dojo/_base/declare",
                                                                               "dojo/query",
                                                                               "dojo/dom-class",
                                                                               "dojo/dom-style",
                                                                               "dojo/dom",
                                                                               "dojo/on",
                                                                               "dojo/_base/lang",
                                                                               "dojo/dom-construct",
                                                                               "dojo/text!tui/widget/booking/attractions/view/Templates/AttractionOptionsComponentPanel.html",
                                                                               "dojo/html",
                                                                               "dojo/Evented",
                                                                               "tui/widget/_TuiBaseWidget",
                                                                               "dojox/dtl/_Templated",
                                                                               "tui/widget/mixins/Templatable",
                                                                               "tui/widget/expand/Expandable",
                                                                               "tui/widget/booking/attractions/view/AttractionView",
                                                                               "tui/widget/booking/attractions/view/AttractionOverlay",
                                                                               "tui/widget/booking/constants/BookflowUrl",
                                                                               "dojo/_base/json",
                                                                               "tui/widget/booking/Expandable",
                                                                               "dojo/dom-attr"


                                                                               ], function (declare, query, domClass, domStyle, dom, on, lang, domConstruct, AttractionOptionsComponentTmpl, html,
                                                                            		   Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable, AttractionView, AttractionOverlay,BookflowUrl, jsonUtil,domAttr) {

	return declare("tui.widget.booking.attractions.view.AttractionOptionsComponentPanel",
			[_TuiBaseWidget, dtlTemplate, Templatable, Evented], {



		tmpl:AttractionOptionsComponentTmpl,
		controller:  null,
		attractionsOverlayData:"",
		templateString: "",
		flagAttraction:false,
		feedBackData:"",
		code: "",
		totalPrice:"",
		categoryCode:"",
		superCategoryCode:"",
		removeLinkFlag:false,
		removeCategory:"",


		postMixInProperties: function () {
			this.model = this.jsonData.extraFacilityViewDataContainer.attractionOptions;
			_.each(this.model, lang.hitch(this , function(item, index){
				this.flag = false;
				_.each(item[0].extraContent.galleryImages, lang.hitch(this , function(imgObj){
					if(imgObj.size == "small"  &&  !this.flag){
						item[0].extraContent.extraFacilityContent.imgUrl = imgObj.mainSrc;
    	   		  		this.flag = true;
    	   		  	}


				}));
			}));

		},

		buildRendering: function(){
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},


		postCreate : function() {
			this.intializeAttractionView();
			this.controller = dijit.registry.byId("controllerWidget");
			this.controller.registerView(this);
			this.removeLinks =	query(".prebook-removeqty", this.domNode);
			this.attachEventsForRemoveLinks();
			this.setFlags();
			dojo.parser.parse(this.domNode);
			this.inherited(arguments);

			//ToDo Need to fixed for showing...
			  var infantSectionInfoTexts = query(".infant-section-infoText", this.domNode);
			  _.each(infantSectionInfoTexts, lang.hitch(this, function (infantSectionInfoText) {
					  infantSectionInfoText.innerHTML = infantSectionInfoText.innerHTML.substr(0,250);
					  infantSectionInfoText.innerHTML = infantSectionInfoText.innerHTML.concat("...");
				}));
			var attractionBtn = dojo.query('button.button', this.domNode);
		    for(var index = 0; index < attractionBtn.length ; index++){
		       	this.tagElement(attractionBtn[index],attractionBtn[index].id);
		    }
		},

		refresh: function (field, response) {
			this.jsonData = response;
			this.model = response.extraFacilityViewDataContainer.attractionOptions;
			this.setFlags();
			this.destroyRecursive();
			var node = domConstruct.create("div", null, dom.byId("attractionContainer"));
			this.create({
				"jsonData": response,
				"transitionType": 'WipeInOut'
			}, node);
			 if(this.removeLinkFlag == true){
					this.removeLinkFlag = false;
					this.removeLCR(this.removeCategory,dom.byId("attraction-mgs"),this[this.removeCategory+"remove"]);
				}
		},


		attachEventsForRemoveLinks: function () {
			_.each(this.removeLinks, lang.hitch(this, function (removeLink) {
				on(removeLink, 'click', lang.hitch(this, this.handleRemoveLinks, removeLink));
			}));
		},

		intializeAttractionView: function () {
			_.each(this.model, lang.hitch(this , function(item, index){
				var button = this[index+"Button"];
				var change = this[index+"Change"];
				if (button) {
					on(button, "click", lang.hitch(this, this.handleAttractionOverlayButton,item,index,item[0]));
				}
				if (change) {
					on(change, "click", lang.hitch(this, this.handleAttractionOverlayButton,item,index,item[0]));
				}
			}));
		},

		handleAttractionOverlayButton: function (attractionData,categoryCode,extraContent) {

			if (this.attractionView && this.attractionView != null) {
				this.attractionView.destroyRecursive();
				this.attractionView = null;
				this.attractionOverlay.destroyRecursive();
				this.attractionOverlay = null;
			}
			this.attractionView = new AttractionView({
				"attractionsOptionsData": attractionData,
				"id": "themeParkOverlay",
				"categoryCode":categoryCode,
				"extraContent":extraContent
			});
			domConstruct.place(this.attractionView.domNode, this.domNode, "last");
			this.attractionOverlay = new AttractionOverlay({"widgetId": this.attractionView.id, modal: true});
			this.attractionView.attractionOverlay = this.attractionOverlay;
		    this.attractionOverlay.jsonData = extraContent;
			this.attractionOverlay.open();
		},

		handleRemoveLinks: function (removeLink) {
			this.removeLinkFlag= true;
			var superCategoryCodes = removeLink.name.split('|');
			var category = superCategoryCodes[0];
			var superCategoryCode = superCategoryCodes[1];
			var keyValue = [];
			_.each(this.model, lang.hitch(this , function(item, index){
				for(var i=0 ; i < item.length; i++){
					for(var j=0; j < item[i].extraFacilityViewData.length; j++){
						if (item[i].aliasSuperCategoryCode == superCategoryCode && item[i].extraFacilityCategoryCode == category) {
							keyValue.push(item[i].extraFacilityViewData[j].code);
						}
					}
				}

			}));
			var adultCode = keyValue[0];
			var childCode = keyValue[1];
			var parentRemoveLink = query(removeLink);
			var quantity = 0;
			var removeObj = [];
			if(childCode == ""){
				removeObj = [
			                 {categoryCode:category,extraCode:adultCode,quantity:quantity,aliasSuperCategoryCode:superCategoryCode}
			                 ];
			}else{
				removeObj = [
			                 {categoryCode:category,extraCode:adultCode,quantity:quantity,aliasSuperCategoryCode:superCategoryCode},
			                 {categoryCode:category,extraCode:childCode,quantity:quantity,aliasSuperCategoryCode:superCategoryCode}
			                 ];
			}
			parentRemoveLink[0].style.display = "none";
			var url =BookflowUrl.attractionurl ;
			var requestData = {"attractionExtra": jsonUtil.toJson(removeObj)};
			  this.removeLinkFlag= true;
	          this.removeCategory=superCategoryCode;
			this.controller.generateRequest("attraction", url, requestData);

		},

		 removeLCR:function (message, excursionMgs, getLCRRemoveText) {
				var widget = this;
				var thisNode=this[message+"attraction-mgs"];
				thisNode.innerHTML = message+" " +"has been removed.";
				domClass.remove(getLCRRemoveText,"disNone");
				var thiscloseNode=this[message+"close"];
				 on(thiscloseNode,'click', lang.hitch(this, function(){
					 domClass.add(getLCRRemoveText,"disNone");
				 }));
				var fadeArgs = {
		                node: getLCRRemoveText,
		                duration: BookflowUrl.fadeOutDuration,
		                onEnd: function () {
		                domClass.add(getLCRRemoveText,"disNone");
		                }
		              };
		              dojo.fadeOut(fadeArgs).play();
			},


		setFlags: function () {
			_.each(this.model, lang.hitch(this , function(item, index){
				for(var i=0 ; i < item.length; i++){
					if (item[i].selected == true) {
							domClass.add(this[index+"View"], "disNone");
							domClass.remove(this[index+"Added"], "disNone");
							break;
						}else{
							domClass.remove(this[index+"View"], "disNone");
							domClass.add(this[index+"Added"], "disNone");
						}

				}
			}));


		}


	});
});