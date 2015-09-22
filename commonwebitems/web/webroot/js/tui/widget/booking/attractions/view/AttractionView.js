define("tui/widget/booking/attractions/view/AttractionView", [
                                                              "dojo/_base/declare",
                                                              "dojo/query",
                                                              "dojo/dom-attr",
                                                              "dojo/dom",
                                                              "dojo/dom-construct",
                                                              "dojo/dom-class",
                                                              "dojo/Evented",
                                                              "dojo/topic",
                                                              "dojo/_base/lang",
                                                              "dojo/dom-style",
                                                              "tui/widget/_TuiBaseWidget",
                                                              "dojox/dtl/_Templated",
                                                              "tui/widget/mixins/Templatable",
                                                              "dojo/text!tui/widget/booking/attractions/view/Templates/AttractionView.html",
                                                              "tui/widget/booking/constants/BookflowUrl",
                                                              "dojo/on",
                                                              "dojo/has",
                                                              "dojo/_base/json",
                                                              "tui/widget/form/SelectOption"


                                                              ], function (declare, query, domAttr,dom, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
                                                            		  dtlTemplate, Templatable, AttractionView,BookflowUrl, on,has, jsonUtil) {

	return declare('tui.widget.booking.attractions.view.AttractionView',
			[_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

		tmpl: AttractionView,
		templateString: "",
		widgetsInTemplate: true,
		extrasMap: {},
		totalPrice: 0,
		selectAdultData: [],
		selectChildData: [],
		categoryCodes:"",
		selectedIndex:"",
		impInfoCheckBox:"",
		childLable:false,



		postMixInProperties: function () {
		 for ( var index = 0; index < this.attractionsOptionsData.length; index++) {
				 var attraction = this.attractionsOptionsData[index].extraFacilityViewData;
		           for (var index2 = 0; index2 < attraction.length; index2++) {
		              var extrasStr = "";
		              for (var num = 0; num <= attraction[index2].quantity; num++) {
		                extrasStr = extrasStr + num;
		              }
		              attraction[index2].selectionData = extrasStr;
		              if(attraction[index2].paxType == 'Child' && attraction[index2].quantity > 0 ){
		            	  this.childLable = true;
		              }
		            }
		          }

			this.categoryCodes =  this.categoryCode;
			if(this.extraContent.extraContent.extraFacilityContent != null){
			if(!this.isEmpty(this.extraContent.extraContent.extraFacilityContent.included)){
				this.pointer = this.extraContent.extraContent.extraFacilityContent.included.split(" ");
				this.include = this.extraContent.extraContent.extraFacilityContent.included.split(this.pointer[0]);
			}

			if(!this.isEmpty(this.extraContent.extraContent.extraFacilityContent.notIncluded)){
				this.pointer = this.extraContent.extraContent.extraFacilityContent.notIncluded.split(" ");
				this.notinclude = this.extraContent.extraContent.extraFacilityContent.notIncluded.split(this.pointer[0]);
			}
			}

		},

		buildRendering: function () {
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		postCreate: function () {
			var widget = this;
			var widgetDom = widget.domNode;
			this.controller = dijit.registry.byId("controllerWidget");
			this.attachEvents();
			this.handleCheckBox();
			this.handleSelectionBox();
			if(this.AttractionSelectCSS){
				widget.cssSelection();
			}

			this.inherited(arguments);
			this.tagElements(dojo.query('.overview-tab', this.domNode),"Overview");

			var radioBtn = dojo.query('.radio-btn', this.domNode);
			for(var index =0; index < radioBtn.length; index++){
				this.tagElement(radioBtn[index],"select"+(index + 1));
			}

			var attractionDropdown = dojo.query('.infant-count', this.domNode);
			for(var index1 =0; index1 < attractionDropdown.length; index1++){
				if(attractionDropdown[index1].id == 'Adult'){
					this.tagElement(attractionDropdown[index1],"adults");
				}else{
					this.tagElement(attractionDropdown[index1],"children");
				}

			}
			this.tagElements(dojo.query('button.button', this.domNode),this.categoryCodes);

			if(this.isEmpty(this.extraContent.extraContent.galleryImages)){
					var viewport =  query(".viewport", this.domNode)[0];
					dojo.style(viewport, "width", "440px");
			}

		},

		attachEvents: function () {
			for (var index = 0; index < this.attractionsOptionsData.length; index++) {
				var superCategoryCode = this.attractionsOptionsData[index].extraFacilityCategoryCode;
				for(var i =0; i < this.attractionsOptionsData[index].extraFacilityViewData.length; i++){
					var attractionPrice = this.attractionsOptionsData[index].extraFacilityViewData[i];
					if(attractionPrice.quantity > 0){
						var selectionBox = this[attractionPrice.code + "SelectionBox"];
						on(this[attractionPrice.code + "SelectionBox"], "change", lang.hitch(this, this.handleSelectionBox));
					}
				}
				on(this["checkboxId"+superCategoryCode], "click", lang.hitch(this, this.handleCheckBox));
			}
			on(this.addButton, "click", lang.hitch(this, this.handleAddButton));
			on(this.overView, "click", lang.hitch(this, this.handleTab,"overView"));
			if(this.extraContent.extraContent.extraFacilityContent != null){
			if(this.extraContent.extraContent.extraFacilityContent.itinerary){
				on(this.itinerary, "click", lang.hitch(this, this.handleTab,"itinerary"));
			}
			}
			on(this.location, "click", lang.hitch(this, this.handleTab,"location"));
		},

		handleCheckBox: function () {
			for (var index = 0; index < this.attractionsOptionsData.length; index++) {
				var superCategoryCodes = this.attractionsOptionsData[index].extraFacilityCategoryCode;
				for(var i =0; i < this.attractionsOptionsData[index].extraFacilityViewData.length; i++){
					var attractionPrice = this.attractionsOptionsData[index].extraFacilityViewData[i];
					if(attractionPrice.quantity > 0){
						if(this["checkboxId"+superCategoryCodes].checked == true){
							this.totalPrice = 0;
							domAttr.set(this.totalCalculatedPrice, "innerHTML", this.totalPrice.toFixed(2));
							domStyle.set(this[superCategoryCodes+"Attrcation"], "display", "block");
							domClass.remove(query(".dropDown-divider ", this.domNode)[0], "disNone");
							if(query(".error-notation", this.impInfoCheckBox.domNode)[0]){
								domClass.add(query(".error-notation", this.impInfoCheckBox.domNode)[0], "disNone");
							}
						}else{
							this[attractionPrice.code + "SelectionBox"].renderList();
							domStyle.set(this[superCategoryCodes+"Attrcation"], "display", "none");
						}
					}
				}
			}
		},
		handleTab: function (tabName) {
			if(tabName == "overView"){
				domClass.remove(this.overViewData, "disNone");
				if(this.extraContent.extraContent.extraFacilityContent != null){
				if(this.extraContent.extraContent.extraFacilityContent.itinerary){
					domClass.add(this.itineraryData, "disNone");
					domClass.remove(this.itinerary, "active");
				}
				}
				domClass.add(this.locationData, "disNone");
				domClass.add(this.overView, "active");
				domClass.remove(this.description, "disNone");
				domClass.remove(this.location, "active");
			}else if(tabName == "itinerary"){
				domClass.add(this.overViewData, "disNone");
				if(this.extraContent.extraContent.extraFacilityContent != null){
				if(this.extraContent.extraContent.extraFacilityContent.itinerary){
					domClass.remove(this.itineraryData, "disNone");
					domClass.add(this.itinerary, "active");
				}
				}
				domClass.add(this.description, "disNone");
				domClass.add(this.locationData, "disNone");
				domClass.remove(this.overView, "active");
				domClass.remove(this.location, "active");
			}else {
				domClass.add(this.overViewData, "disNone");
				domClass.add(this.description, "disNone");
				domClass.remove(this.locationData, "disNone");
				domClass.remove(this.overView, "active");
				domClass.add(this.location, "active");
				if(this.extraContent.extraContent.extraFacilityContent != null){
					if(this.extraContent.extraContent.extraFacilityContent.itinerary){
						domClass.add(this.itineraryData, "disNone");
						domClass.remove(this.itinerary, "active");
					}
				this.initializeMap(this.extraContent.extraContent.extraFacilityContent.latitude, this.extraContent.extraContent.extraFacilityContent.longitude);
			}
			}


		},
		initializeMap: function(latitude,longitude){
			var myLatlng = new google.maps.LatLng(latitude,longitude);
			var mapProp = {
					center:myLatlng,
					zoom:4

			};
			/*var map=new google.maps.Map(this.locationData,mapProp);
			 */
			map = new google.maps.Map(this.locationData, {
				center: myLatlng,
				zoom: 4,
				zoomControlOptions: {
					position: google.maps.ControlPosition.TOP_RIGHT
				},
				panControl: false,
				mapTypeControl: false,
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				styles: [{
					featureType: "poi.business",
					elementType: "labels",
					stylers: [
					          { visibility: "off" }
					          ]
				}]
			});

			var marker = new google.maps.Marker({
				position: myLatlng,
				map: map,
				title: ''
			});
			//google.maps.event.addDomListener(window, 'load', initializeMap);
		},
		
		handleSelectionBox: function () {
			this.totalPrice = 0;
			for (var index = 0; index < this.attractionsOptionsData.length; index++) {
				for(var i =0; i < this.attractionsOptionsData[index].extraFacilityViewData.length; i++){
					var attractionPrice = this.attractionsOptionsData[index].extraFacilityViewData[i];
					if(attractionPrice.quantity > 0){
						var noOfItems = this[attractionPrice.code + "SelectionBox"].getSelectedData().value;
						var price = attractionPrice.currencyAppendedPrice.slice(1);
						this.totalPrice = this.totalPrice + (noOfItems * price);
					}
				}
			}
			domAttr.set(this.totalCalculatedPrice, "innerHTML", this.totalPrice.toFixed(2));

		},

		handleAddButton: function () {
			var responseObj = [],
			url =  BookflowUrl.attractionurl;
			var errorMgs = dom.byId("error-mgs");
			var AttractionFlag = false,
			radioBtnFlag = true;

			for (var index = 0; index < this.attractionsOptionsData.length; index++) {
				var superCategoryCode = this.attractionsOptionsData[index].extraFacilityCategoryCode;
				for(var i =0; i < this.attractionsOptionsData[index].extraFacilityViewData.length; i++){
					var attractionPrice = this.attractionsOptionsData[index].extraFacilityViewData[i];
					var selectionBox = this[attractionPrice.code + "SelectionBox"];
					if(this["checkboxId"+superCategoryCode].checked == true){
						if(attractionPrice.quantity > 0){
							var quantity = this[attractionPrice.code + "SelectionBox"].getSelectedData ().value;

							if(quantity == 0 && attractionPrice.paxType == 'Adult'){
								AttractionFlag = false;
								radioBtnFlag = false;
								domClass.remove(query(".error-notation", this.impInfoCheckBox.domNode)[0], "disNone");
								domAttr.set(errorMgs, "innerHTML", "Please select atleast one Adult");
								break;
							}else{
								AttractionFlag = true;
								selectionBox.resObj = {
										"categoryCode": superCategoryCode,
										"extraCode": attractionPrice.code,
										"aliasSuperCategoryCode":this.categoryCodes,
										"quantity":quantity
								};
								responseObj.push(selectionBox.resObj);
							}

						}
					}
				}
			}

				if(AttractionFlag){
					domClass.add(query(".error-notation", this.impInfoCheckBox.domNode)[0], "disNone");
					var requestData = {"attractionExtra": jsonUtil.toJson(responseObj)};
					this.controller.generateRequest("attraction", url, requestData);
					this.attractionOverlay.close();
				}else if(radioBtnFlag){
					domClass.remove(query(".error-notation", this.impInfoCheckBox.domNode)[0], "disNone");
					domAttr.set(errorMgs, "innerHTML", "Please select atleast one attraction");
				}
		},

		isEmpty : function(obj) {
			if(has("ie") == 8){
				if(obj === undefined || obj.length === 0){
					return true;
				}else{
					return false;
				}
			}else{
			for(var key in obj) {
				if(obj.hasOwnProperty(key))
					return false;
			}
			return true;
			}
		},

		cssSelection: function(){
			var widget = this;
			var widgetDom = widget.domNode;
			var radioButtons= query('input[type=radio]',widgetDom);
			_.each(radioButtons, function(radioButton) {
				dojo.connect(radioButton,'click', function(e){
					var items = query('.theme-park-heading', widgetDom);
					_.each(items, function(item) {
						domClass.remove(item, 'clicked-radio');
					});
					var checkedRadioButtons= query('input[type=radio]:checked',widgetDom);
					_.each(checkedRadioButtons, function(checkedRadioButton) {
						var checkedParent = dojo.query(checkedRadioButton).parents(".theme-park-heading");
						domClass.add(checkedParent[0], 'clicked-radio');
					});
				});
			});
		}


	});
});