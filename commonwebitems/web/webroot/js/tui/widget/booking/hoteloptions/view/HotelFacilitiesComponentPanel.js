define("tui/widget/booking/hoteloptions/view/HotelFacilitiesComponentPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/on",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/hoteloptions/view/templates/HotelFacilitiesComponentPanel.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/hoteloptions/view/SwimAcademyOverlay",
  "tui/widget/booking/hoteloptions/view/SoccerAcademyOverlay",
  "tui/widget/booking/hoteloptions/view/StageAcademyOverlay",
  "tui/widget/booking/hoteloptions/view/SwimmingAcademyView",
  "tui/widget/booking/hoteloptions/view/SoccerAcademyView",
  "tui/widget/booking/hoteloptions/view/StageAcademyView",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/_base/json",
  "tui/widget/expand/Expandable"


], function (declare, query, domClass, domStyle, dom, on, lang, domConstruct, HotelFacilitiesComponentPanel, html,
             Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable, SwimAcademyOverlay,
             SoccerAcademyOverlay, StageAcademyOverlay,SwimmingAcademyView,
             SoccerAcademyView, StageAcademyView,BookflowUrl, jsonUtil) {

  return declare("tui.widget.booking.hoteloptions.view.HotelFacilitiesComponentPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: HotelFacilitiesComponentPanel,
      templateString: "",
      widgetsInTemplate: true,
      controller: null,
      flagSwim: false,
      flagSoccer: false,
      flagStage: false,
      model: null,
      removeLinkFlag: false,


      postMixInProperties: function () {
        this.model = this.jsonData.extraFacilityViewDataContainer;
        this.setFlags();
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        this.intializeSwimView();
        this.intializeSoccerView();
        this.intializeStageView();
        this.controller = dijit.registry.byId("controllerWidget");
        this.controller.registerView(this);
        this.removeLinks =
          query(".stage-removeqty, .soccer-removeqty,.swim-removeqty", this.domNode);
        this.fadeCloseLinks =
            query(".fadeCloseLinks", this.domNode);
        this.attachEventsForRemoveLinks();
        this.inherited(arguments);
        this.tagElements(dojo.query('.swim-academy', this.domNode),"Swim Academy");
        this.tagElements(dojo.query('.soccer-academy', this.domNode),"Soccer Academy");
        this.tagElements(dojo.query('.stage-academy', this.domNode),"Stage Academy");
        // Tagging particular element.
        if (this.autoTag) {
          this.tagElements(query(this.targetSelector, this.domNode), 'toggle');
        }

      },

      refresh: function (field, response) {
        this.jsonData = response;
        this.model = response.extraFacilityViewDataContainer;
        this.destroyRecursive();
        var node = domConstruct.create("div", null, dom.byId("hotelOptionsHolder"));
        this.create({
          "jsonData": response,
          "transitionType": 'WipeInOut'
        }, node);
        if(this.removeLinkFlag == true){
			this.removeLinkFlag = false;
			this.removeLCR(field);
		}

      },

      attachEventsForRemoveLinks: function () {

        _.each(this.removeLinks, lang.hitch(this, function (removeLink) {
          on(removeLink, 'click', lang.hitch(this, this.handleRemoveLinks, removeLink));
        }));
        _.each(this.fadeCloseLinks, lang.hitch(this, function (fadeCloseLink) {
            on(fadeCloseLink, 'click', lang.hitch(this, this.fadeCloseLink , fadeCloseLink));
          }));
      },
      fadeCloseLink: function(fadeCloseLink){
    	  var className = "."+fadeCloseLink.id;
    	  var getLCRRemoveText = query(className, this.domNode);

          getLCRRemoveText[0].style.display = "none";
      },
      intializeSwimView: function () {
        if (this.model.swimOptions.extrasToPassengerMapping
              && this.model.swimOptions.extrasToPassengerMapping.length > 0) {
          if (this.swimDetailsButton) {
            on(this.swimDetailsButton, "click", lang.hitch(this, this.handleSwimButton));
          } else if (this.swimChangeButton) {
            on(this.swimChangeButton, "click", lang.hitch(this, this.handleSwimButton));
          }
        }
      },

      intializeSoccerView: function () {
        if (this.model.soccerOptions.extrasToPassengerMapping
              && this.model.soccerOptions.extrasToPassengerMapping.length > 0) {
          if (this.soccerDetailsButton) {
            on(this.soccerDetailsButton, "click", lang.hitch(this, this.handleSoccerButton));
          } else if (this.soccerChangeButton) {
            on(this.soccerChangeButton, "click", lang.hitch(this, this.handleSoccerButton));
          }
        }
      },

      intializeStageView: function () {
        if (this.model.stageOptions.extrasToPassengerMapping
              && this.model.stageOptions.extrasToPassengerMapping.length > 0) {
          if (this.stageDetailsButton) {
            on(this.stageDetailsButton, "click", lang.hitch(this, this.handleStageButton));
          } else if (this.stageChangeButton) {
            on(this.stageChangeButton, "click", lang.hitch(this, this.handleStageButton));
          }
        }
      },

      handleSwimButton: function () {

        if (this.swimmingAcademyView && this.swimmingAcademyView !== null) {
          this.swimmingAcademyView.destroyRecursive();
          this.swimmingAcademyView = null;
          this.swimAcademyOverlay.destroyRecursive();
          this.swimAcademyOverlay = null;
        }
        this.swimmingAcademyView = new SwimmingAcademyView({
          "swimOptionsData": this.jsonData.extraFacilityViewDataContainer.swimOptions,
          "id": "swimAcademy",
          "jsonData":this.jsonData,
          "overlayRef": null
        });   
        domConstruct.place(this.swimmingAcademyView.domNode, this.domNode, "last");
        this.swimAcademyOverlay = new SwimAcademyOverlay({"widgetId": this.swimmingAcademyView.id, modal: true});
        this.swimmingAcademyView.overlayRef = this.swimAcademyOverlay;
        this.swimAcademyOverlay.open();
      },

      handleSoccerButton: function () {
        if (this.soccerAcademyView && this.soccerAcademyView !== null) {
          this.soccerAcademyView.destroyRecursive();
          this.soccerAcademyView = null;
          this.soccerAcademyOverlay.destroyRecursive();
          this.soccerAcademyOverlay = null;
        }
        this.soccerAcademyView = new SoccerAcademyView({
          "soccerOptionsData": this.jsonData.extraFacilityViewDataContainer.soccerOptions,
          "id": "soccerAcademy",
          "jsonData": this.jsonData,
          "overlayRef": null
        });
        domConstruct.place(this.soccerAcademyView.domNode, this.domNode, "last");
        this.soccerAcademyOverlay = new SoccerAcademyOverlay({"widgetId": this.soccerAcademyView.id, "modal": true});
        this.soccerAcademyView.overlayRef = this.soccerAcademyOverlay;
        this.soccerAcademyOverlay.open();
      },

      handleStageButton: function () {
        if (this.stageAcademyView && this.stageAcademyView !== null) {
          this.stageAcademyView.destroyRecursive();
          this.stageAcademyView = null;
          this.stageAcademyOverlay.destroyRecursive();
          this.stageAcademyOverlay = null;
        }
        this.stageAcademyView = new StageAcademyView({
          "stageOptionsData": this.jsonData.extraFacilityViewDataContainer.stageOptions,
          "id": "stageAcademy",
          "jsonData": this.jsonData,
          "overlayRef": null
        });
        domConstruct.place(this.stageAcademyView.domNode, this.domNode, "last");
        this.stageAcademyOverlay = new StageAcademyOverlay({"widgetId": this.stageAcademyView.id, "modal": true});
        this.stageAcademyView.overlayRef = this.stageAcademyOverlay;
        this.stageAcademyOverlay.open();
      },

      setFlags: function () {
        this.flagSwim = false;
        this.flagStage = false;
        this.flagSoccer = false;
       
        if (this.model.soccerOptions.extrasToPassengerMapping) {
          for (var i = 0; i < this.model.soccerOptions.extrasToPassengerMapping.length; i++) {
            for (var j = 0; j < this.model.soccerOptions.extrasToPassengerMapping[i].extras.length; j++) {
              if (this.model.soccerOptions.extrasToPassengerMapping[i].extras[j].selected == true) {
                this.flagSoccer = true;
                break;
              }
            }
            if (this.flagSoccer === true) {
              break;
            }
          }
        }

        if (this.model.swimOptions.extrasToPassengerMapping) {
          for (var i = 0; i < this.model.swimOptions.extrasToPassengerMapping.length; i++) {
            var isSelected = false;
            for (var j = 0; j < this.model.swimOptions.extrasToPassengerMapping[i].extras.length; j++) {
              if (this.model.swimOptions.extrasToPassengerMapping[i].extras[j].selected == true) {
                this.flagSwim = true;
                isSelected = true;
                break;
              }
            }
            if (isSelected === false) {
              if (this.model.swimOptions.extrasToPassengerMapping[i].passenger.swimOrStageExtraSelected) {
                this.model.swimOptions.extrasToPassengerMapping[i].passenger.isStageSelected = true;
              } else {
                this.model.swimOptions.extrasToPassengerMapping[i].passenger.isStageSelected = false;
              }
            }
          }
        }

        if (this.model.stageOptions.extrasToPassengerMapping) {
          for (var i = 0; i < this.model.stageOptions.extrasToPassengerMapping.length; i++) {
            var isSelected = false;
            for (var j = 0; j < this.model.stageOptions.extrasToPassengerMapping[i].extras.length; j++) {
              if (this.model.stageOptions.extrasToPassengerMapping[i].extras[j].selected == true) {
                this.flagStage = true;
                isSelected = true;
                break;
              }
            }
            if (isSelected === false) {
              if (this.model.stageOptions.extrasToPassengerMapping[i].passenger.swimOrStageExtraSelected) {
                this.model.stageOptions.extrasToPassengerMapping[i].passenger.isSwimSelected = true;
              } else {
                this.model.stageOptions.extrasToPassengerMapping[i].passenger.isSwimSelected = false;
              }
            }
          }
        }
      },

      handleRemoveLinks: function (removeLink) {
        var keyValue = removeLink.id;
        var categoryCodes = removeLink.name.split('|');
        var paxID = categoryCodes[0];
        var extraCode = categoryCodes[1];
        var parentRemoveLink = query(removeLink).parents(".prebook-confirmation");
        var quantity = 0;
        var removeObj = [
          {passengerId: paxID, extraCode: keyValue, quantity: quantity, extraCategoryCode: extraCode}
        ];
        parentRemoveLink[0].style.display = "none";
        var categoryName = removeLink.parentNode.id;
        var url =BookflowUrl.hoteloptionremoveurl ;
        var requestData = {"removeExtra": jsonUtil.toJson(removeObj)};
        this.removeLinkFlag = true;
        this.controller.generateRequest(categoryName, url, requestData);
        //this.removeLCR();
      },

      removeLCR:function (field) {
    	  var classname = "."+field;
    	  console.log(classname);
    	  var getLCRRemoveText = query(classname, this.domNode);

        getLCRRemoveText[0].style.display = "block";

			var fadeArgs = {
	                node: getLCRRemoveText[0],
	                duration: BookflowUrl.fadeOutDuration,
	                onEnd: function () {
	                	getLCRRemoveText[0].style.display = "none";
          }
	              };
	              dojo.fadeOut(fadeArgs).play();

      }


    });
});