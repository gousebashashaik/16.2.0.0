define("tui/widget/booking/excursion/view/ExcursionsOptionsComponent", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/on",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/excursion/view/templates/ExcursionsOptionsComponentTmpl.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/excursion/view/ExcursionsOptionsOverlay",
  "tui/widget/booking/excursion/view/ExcursionsOptionsView",
  "dojo/_base/json",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/dom-attr"
], function (declare, query, domClass, domStyle, dom, on, lang, domConstruct, ExcursionsOptionsComponentTmpl, html,
             Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable, ExcursionsOptionsOverlay,
             ExcursionsOptionsView, jsonUtil,BookflowUrl,domAttr) {

  return declare("tui.widget.booking.excursion.view.ExcursionsOptionsComponent",
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: ExcursionsOptionsComponentTmpl,
      templateString: "",
      controller: null,
      excursionOverlayData:"",
      model:null,
      flagExcursion:false,
      categoryCode:"",
      feedBackData:"",
  	  code: "",
  	  totalPrice:"",
   	  flagButton:false,
  	  removeLinkFlag:false,
  	  removeCategory:"",


      postMixInProperties: function () {
    	  this.model = this.jsonData.extraFacilityViewDataContainer.excursionOptions;
    	  this.setFlags();
    	  _.each(this.model, lang.hitch(this, function (obj) {
    		  this.flag = false;
    		  _.each(obj.extraContent.galleryImages, lang.hitch(this, function (imgObj) {
    			  	if(imgObj.size === "small"  &&  !this.flag){
    	   		  		obj.extraContent.extraFacilityContent.imgUrl = imgObj.mainSrc;
    	   		  		this.flag = true;
    	   		  	}
    		  }));
    	  }));
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
    	this.intializeExcursionsView();
    	this.controller = dijit.registry.byId("controllerWidget");
        this.controller.registerView(this);
        this.removeLinks =	query(".prebook-removeqty", this.domNode);
        this.attachEventsForRemoveLinks();
        dojo.parser.parse(this.domNode);
        this.inherited(arguments);
        var excursionBtn = dojo.query('button.button', this.domNode);
        for(var index = 0; index < excursionBtn.length ; index++){
        	this.tagElement(excursionBtn[index],excursionBtn[index].id);
        }
        // Tagging particular element.
        if (this.autoTag) {
          this.tagElements(query(this.targetSelector, this.domNode), 'toggle');
        }
      },


      refresh: function (field, response) {
    	  this.jsonData = response;
          this.model = response.extraFacilityViewDataContainer.excursionOptions;
          this.destroyRecursive();
          var node = domConstruct.create("div", null, dom.byId("excursionOptionsHolder"));
          this.create({
            "jsonData": response,
            "transitionType": 'WipeInOut'
          }, node);
          this.setFlags();
          if(this.removeLinkFlag == true){
				this.removeLinkFlag = false;
				this.removeLCR(this.removeCategory,dom.byId("excursion-mgs"),query('.excursion-removed',this.domNode)[0]);
			}
      },


      attachEventsForRemoveLinks: function () {
          _.each(this.removeLinks, lang.hitch(this, function (removeLink) {
            on(removeLink, 'click', lang.hitch(this, this.handleRemoveLinks, removeLink));
          }));

          on(dom.byId("excursionCloseLink"),'click', lang.hitch(this, this.excursionCloseLink));

        },

        excursionCloseLink: function(){
			var getLCRRemoveText = query('.excursion-removed',this.domNode)[0];
			getLCRRemoveText.style.display = "none";
		},
      intializeExcursionsView: function () {
    	  for(var index = 0 ; index < this.model.length ; index++){
    		  var data = this.model[index].extraFacilityCategoryCode;
    		  if(data.indexOf(",") != -1){
	    	      var lengthText = (data.split(",").length - 1);
	    		  for(var i = 1 ; i <= lengthText; i++){
	    			  console.log(i);
	    			  data = data.replace(",", "");
	    		  }
    		  }
    		  var button = this[data+"Button"];
    		  var change = this[data+"Change"];
    		  this.excursionOverlayData = this.model[index].extraFacilityViewData;
    		  this.extraContent = this.model[index].extraContent;
    		  if (button) {
	            on(button, "click", lang.hitch(this, this.handleViewDetailsButton,this.excursionOverlayData,this.model[index].extraFacilityCategoryCode, this.extraContent));
	          } else if (change) {
	        	on(change, "click", lang.hitch(this, this.handleViewDetailsButton,this.excursionOverlayData,this.model[index].extraFacilityCategoryCode, this.extraContent));
	          }
    	  }
    	  if(this.flagButton){
    	  on(this.showMoreExcursions,'click', lang.hitch(this, this.showMoreExcursion, "more"));
    	  on(this.lessExcursions,'click', lang.hitch(this, this.showMoreExcursion, "less"));
      }
      },

      handleViewDetailsButton: function (excursionData,categoryCode, extraContent) {
    	  if (this.excursionsOptionsView && this.excursionsOptionsView !== null) {
	          this.excursionsOptionsView.destroyRecursive();
	          this.excursionsOptionsView = null;
	          this.excursionsOptionsOverlay.destroyRecursive();
	          this.excursionsOptionsOverlay = null;
	        }
	        this.excursionsOptionsView = new ExcursionsOptionsView({
	          "excursionsOptionsData": excursionData,
	          "id": "dolphinEncounter",
	          "categoryCode":categoryCode,
	          "extraContent": extraContent,
	          "jsonData":this.jsonData,
	          "excursionsOptionsOverlay" : this.excursionsOptionsOverlay

	        });
	        domConstruct.place(this.excursionsOptionsView.domNode, this.domNode, "last");
	        this.excursionsOptionsOverlay = new ExcursionsOptionsOverlay({"widgetId": this.excursionsOptionsView.id, modal: true});
	        this.excursionsOptionsView.excursionsOptionsOverlay = this.excursionsOptionsOverlay;
	        this.excursionsOptionsOverlay.jsonData = extraContent;
	        this.excursionsOptionsOverlay.open();
      },

      handleRemoveLinks: function (removeLink) {
          var keyValue = removeLink.id;
          var category = removeLink.name;
          var adultCode = keyValue.slice(0, 3);
          var childCode = keyValue.slice(4, 7);
          var parentRemoveLink = query(removeLink).parents(".prebook-confirmation");
          var quantity = 0;
          var removeObj = [
               {categoryCode:category,extraCode:adultCode,quantity:quantity},
               {categoryCode:category,extraCode:childCode,quantity:quantity}
          ];
          parentRemoveLink[0].style.display = "none";
          var url =BookflowUrl.excursionurl ;
          var requestData = {"excursionExtra": jsonUtil.toJson(removeObj)};
          this.removeLinkFlag= true;
          this.removeCategory=category;
          this.controller.generateRequest("excursion", url, requestData);

        },

        showMoreExcursion: function (wipeIn) {
        	var showMore = query(".altExcursion", this.domNode);
        	if(wipeIn === 'more'){
        		_.each(showMore, lang.hitch(this, function (showMores) {
    				domClass.add(this.showMoreExcursions, "disNone");
    				domClass.remove(showMores, "disNone");
    				domClass.remove(this.lessExcursions, "disNone");

    			}));
	
        	}else{
        		_.each(showMore, lang.hitch(this, function (showMores) {
					domClass.add(this.lessExcursions, "disNone");
					domClass.add(showMores, "disNone");
					domClass.remove(this.showMoreExcursions, "disNone");

				}));
        	}
        	
          },
          
        removeLCR:function (message, excursionMgs, getLCRRemoveText) {
			var widget = this;
			domAttr.set(excursionMgs.id, "innerHTML", message + " "+ "has been removed");
			getLCRRemoveText.style.display = "block";

			var fadeArgs = {
	                node: getLCRRemoveText,
	                duration: BookflowUrl.fadeOutDuration,
	                onEnd: function () {
	                	getLCRRemoveText.style.display = "none";
	                }
	              };
	              dojo.fadeOut(fadeArgs).play();
		},


        setFlags: function () {
        	this.flagExcursion = false;
        	this.categoryCode = "";
        	this.feedBackData = "";
        	this.code = "";
        	this.totalPrice = "";
        	if(this.jsonData.extraFacilityViewDataContainer.excursionOptions.length > 3){
        		this.flagButton=true;
        	}

        	for(var index = 0 ; index < this.model.length ; index++){
        		for(var i=0 ; i < this.model[index].extraFacilityViewData.length; i++){
        			if (this.model[index].extraFacilityViewData[i].selected == true) {
        				this.flagExcursion = true;
        				this.categoryCode = this.model[index].extraFacilityCategoryCode;
        				break;
        			}
        		}
        	}

        	for(var j=0; j < this.jsonData.packageViewData.extraFacilityCategoryViewData.length; j++){
        		var packageData = this.jsonData.packageViewData.extraFacilityCategoryViewData[j];
        		 if(packageData.superCategoryCode === 'Excursion'){
        			 this.totalPrice = packageData.currencyAppendedCategoryTotalPrice;
        			 for(var k=0; k < packageData.extraFacilityViewData.length; k++){
        				 var data = packageData.extraFacilityViewData[k];
        				 if (data.selected && data.paxType === 'Adult'){
        					 	this.feedBackData = this.feedBackData + data.categoryCode + " "+ "x" + data.selectedQuantity + " " + data.paxType;
        					 	this.code = this.code + data.code;
        				  }else if(data.selected && data.paxType === 'Child'){
        					   this.feedBackData = this.feedBackData + "," + "x" + data.selectedQuantity + " " + data.paxType;
        					   this.code = this.code + "+" + data.code;
        				  }
        			 }
        		 }
        	}
        }

    });
});