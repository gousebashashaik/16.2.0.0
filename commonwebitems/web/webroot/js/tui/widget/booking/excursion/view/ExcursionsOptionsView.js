define("tui/widget/booking/excursion/view/ExcursionsOptionsView", [
                                                                   "dojo/_base/declare",
                                                                   "dojo/query",
                                                                   "dojo/dom",
                                                                   "dojo/dom-attr",
                                                                   "dojo/dom-construct",
                                                                   "dojo/dom-class",
                                                                   "dojo/Evented",
                                                                   "dojo/topic",
                                                                   "dojo/_base/lang",
                                                                   "dojo/dom-style",
                                                                   "tui/widget/_TuiBaseWidget",
                                                                   "dojox/dtl/_Templated",
                                                                   "tui/widget/mixins/Templatable",
                                                                   "dojo/text!tui/widget/booking/excursion/view/templates/ExcursionsOptionsViewTmpl.html",
                                                                   "dojo/on",
                                                                   "dojo/has",
                                                                   "dojo/_base/json",
                                                                   "tui/widget/booking/constants/BookflowUrl",
                                                                   "tui/widget/form/SelectOption",
                                                                   "tui/widget/LazyLoadImage",
                                                                   "tui/widget/media/HeroCarousel"
                                                                   ], function (declare, query,dom, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
                                                                		   dtlTemplate, Templatable, ExcursionsOptionsViewTmpl, on,has, jsonUtil,BookflowUrl) {

	return declare('tui.widget.booking.excursion.view.ExcursionsOptionsView',
			[_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

		tmpl: ExcursionsOptionsViewTmpl,
		templateString: "",
		widgetsInTemplate: true,
		selectAdultData: [],
		selectChildData: [],
		categoryCodes:"",
		type:"",
		impInfoCheckBox:"",
		addButton:"",


		postMixInProperties: function () {
			for ( var index = 0; index < this.excursionsOptionsData.length; index++) {
				var extrasStr = "";
				for (var num = 0; num <= this.excursionsOptionsData[index].quantity; num++) {
					extrasStr = extrasStr + num;
				}
				this.excursionsOptionsData[index].selectionData = extrasStr;
			}

			this.categoryCodes =  this.categoryCode;
			if(this.extraContent.extraFacilityContent){						
			if(!this.isEmpty(this.extraContent.extraFacilityContent.included)){
				this.pointer = this.extraContent.extraFacilityContent.included.split(" ");
				this.include = this.extraContent.extraFacilityContent.included.split(this.pointer[0]);
			}

			if(!this.isEmpty(this.extraContent.extraFacilityContent.notIncluded)){
				this.pointer = this.extraContent.extraFacilityContent.notIncluded.split(" ");
				this.notinclude = this.extraContent.extraFacilityContent.notIncluded.split(this.pointer[0]);
			}
			}


		},

		buildRendering: function () {
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		postCreate: function () {
			this.controller = dijit.registry.byId("controllerWidget");
			this.attachEvents();
			this.handleSelectionBox();
			this.inherited(arguments);
			//domAttr.set(this.itineraryData, "innerHTML", this.extraContent.extraFacilityContent.itinerary);

			this.tagElements(query('a.overview-tab', this.domNode),"Overview");
			this.tagElements(query('a.itinerary-tab', this.domNode),"Itinerary");
			this.tagElements(query('a.location-tab', this.domNode),"Location");
			var excursionDropdown = dojo.query('.infant-count', this.domNode);
			for(var index =0; index < excursionDropdown.length; index++){
				if(excursionDropdown[index].id == 'Adult'){
					this.tagElement(excursionDropdown[index],"adults");
				}else{
					this.tagElement(excursionDropdown[index],"children");
				}

			}
			this.tagElements(dojo.query('button.button', this.domNode),this.categoryCodes);
			this.tagElements(dojo.query('.excursion-map', this.domNode),"mapView");

			for(var i = 0; i< this.jsonData.extraFacilityViewDataContainer.excursionOptions.length; i++){
				var galleryImage = this.jsonData.extraFacilityViewDataContainer.excursionOptions[i].extraContent.galleryImages;
				if(this.isEmpty(galleryImage)){
					var viewport =  query(".viewport", this.domNode)[0];
					dojo.style(viewport, "width", "440px");

				}
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

		attachEvents: function () {
			for (var index = 0; index < this.excursionsOptionsData.length; index++) {
				var excursionPrice = this.excursionsOptionsData[index];
				this.type = excursionPrice.paxType;
				if(excursionPrice.quantity > 0){
					this[excursionPrice.code + "SelectionBox"].resObj = {
							"categoryCode":  this.categoryCodes,
							"extraCode": excursionPrice.code
					};

					on(this[excursionPrice.code + "SelectionBox"], "change", lang.hitch(this, this.handleSelectionBox));
				}
			}
			on(this.addButton, "click", lang.hitch(this, this.handleAddButton,this.type));
			on(this.overView, "click", lang.hitch(this, this.handleTab,"overView"));
			if(this.extraContent.extraFacilityContent.itinerary){
				on(this.itinerary, "click", lang.hitch(this, this.handleTab,"itinerary"));
			}
			on(this.location, "click", lang.hitch(this, this.handleTab,"location"));
		},

		handleSelectionBox: function () {
			this.totalPrice = 0;
			for (var index = 0; index < this.excursionsOptionsData.length; index++) {
				var excursionPrice = this.excursionsOptionsData[index];
				if(excursionPrice.quantity > 0){
					var noOfItems = this[excursionPrice.code + "SelectionBox"].getSelectedData().value;
					var price = excursionPrice.currencyAppendedPrice.slice(1);
					this.totalPrice = this.totalPrice + (noOfItems * price);
				}
			}
			domAttr.set(this.totalCalculatedPrice, "innerHTML", this.totalPrice.toFixed(2));

		},

		handleAddButton: function (type) {
			var responseObj = [],
			url =  BookflowUrl.excursionurl;
			var lableBtn = dom.byId("impInfoCheckbox_Error");
			var excursionFlag = true;
			var check = false;
			_.each(this._attachPoints, lang.hitch(this, function (attachPoint) {
				var selectionAttachPoint = this[attachPoint];
				if (selectionAttachPoint instanceof tui.widget.form.SelectOption) {
					var valueObj = selectionAttachPoint.resObj;
					var quantity = selectionAttachPoint.getSelectedData().value;
					domClass.add(query(".error-notation-excursion", this.impInfoCheckBox.domNode)[0], "disNone");
					if(quantity == 0 && selectionAttachPoint.id == 'Adult' ){
						check =true;
						excursionFlag = false;
						domClass.remove(query(".error-notation-excursion", this.impInfoCheckBox.domNode)[0], "disNone");
						valueObj.quantity = quantity;
						responseObj.push(valueObj);
					}
					else if(selectionAttachPoint.id == 'Child' && quantity > 0 && check == true ){
						excursionFlag = false;
						check=false;
						domClass.remove(query(".error-notation-excursion", this.impInfoCheckBox.domNode)[0], "disNone");
						//domAttr.set(lableBtn, "innerHTML", "An adult must accompany a child on an excursion");
					}
					else if(selectionAttachPoint.id == 'Child' && quantity == 0 && check == true ){
						excursionFlag = false;
						check=false;
						for(var i = 0; i< this.jsonData.extraFacilityViewDataContainer.excursionOptions.length; i++){
							if(this.jsonData.extraFacilityViewDataContainer.excursionOptions[i].selected){
								excursionFlag = true;

							}
						}

						valueObj.quantity = quantity;
						responseObj.push(valueObj);
						this.excursionsOptionsOverlay.close();
					}
					else{
						valueObj.quantity = quantity;
						responseObj.push(valueObj);
					}
				}

			}));
			if(check)
			{
				for(var i = 0; i< this.jsonData.extraFacilityViewDataContainer.excursionOptions.length; i++){
					if(this.jsonData.extraFacilityViewDataContainer.excursionOptions[i].selected){
						excursionFlag = true;
					}
				}
				this.excursionsOptionsOverlay.close();
			}

			if(excursionFlag)
			{
				var requestData = {"excursionExtra": jsonUtil.toJson(responseObj)};
				this.controller.generateRequest("excursion", url, requestData);
				this.excursionsOptionsOverlay.close();
			}

		},

		handleTab: function (tabName) {
			if(tabName == "overView"){
				domClass.remove(this.overViewData, "disNone");
				if(this.extraContent.extraFacilityContent){		
				if(this.extraContent.extraFacilityContent.itinerary){
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
				if(this.extraContent.extraFacilityContent){		
				if(this.extraContent.extraFacilityContent.itinerary){
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
				if(this.extraContent.extraFacilityContent){		
				if(this.extraContent.extraFacilityContent.itinerary){
					domClass.add(this.itineraryData, "disNone");
					domClass.remove(this.itinerary, "active");
				}
				}
				domClass.add(this.description, "disNone");
				domClass.remove(this.locationData, "disNone");
				domClass.remove(this.overView, "active");
				domClass.add(this.location, "active");
				if(this.extraContent.extraFacilityContent){		
				this.initializeMap(this.extraContent.extraFacilityContent.latitude, this.extraContent.extraFacilityContent.longitude);
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
		}

	});
});