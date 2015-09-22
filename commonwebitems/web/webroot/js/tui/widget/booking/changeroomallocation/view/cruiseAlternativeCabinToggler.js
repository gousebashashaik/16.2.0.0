define('tui/widget/booking/changeroomallocation/view/cruiseAlternativeCabinToggler', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  'dojo/dom',
  'dojo/dom-construct',
  'dojo/on',
  "dojo/_base/lang",
  'dojo/topic',
  "dojo/_base/xhr",
  "dojo/window",
  'tui/widget/booking/changeroomallocation/modal/RoomModel',
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/changeroomallocation/view/deckOverlay",
  "tui/widget/booking/changeroomallocation/view/deckOverlayView",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "tui/widget/ScrollHorizontal",
  "dijit/registry",
  'tui/widget/booking/changeroomallocation/view/RoomOptionsButtonToggler',
   'tui/widget/mixins/Templatable',
   'tui/widget/booking/Expandable'
   ], function(dojo, query, domClass, dom, domConstruct, on, lang, topic, xhr, win, roomModel,BookflowUrl, deckOverlay, deckOverlayView,Bookflowi18nable,scrollHorizontal,registry) {

	dojo.declare('tui.widget.booking.changeroomallocation.view.cruiseAlternativeCabinToggler', [tui.widget._TuiBaseWidget,tui.widget.booking.Expandable,tui.widget.booking.changeroomallocation.view.RoomOptionsButtonToggler,Bookflowi18nable], {

		transitionType: "WipeInOut",
		changeRoom: null,
		 deckOverlay: false,
		roomRefNum:3,
		postCreate: function() {
			var widget = this;
			var widgetDom = widget.domNode;
			widget.forTransitionEffect();
			widget.changeRoom ={};
			widget.jsonData = roomModel.alternateRooms ;
			widget.refAccomIndex =roomModel.accomIndex;
			console.log("heyllo"+ widget.jsonData);
			console.log(widget.jsonData);
			widget.shipCode =widget.jsonData.packageViewData.accomViewData[widget.refAccomIndex].accomCode;

			widget.initBookflowMessaging();

			dojo.subscribe("tui/widget/booking/displayContent", function(){
			widget.displayContent();
		});
			widget.displayContent();

			dojo.subscribe("tui/widget/ScrollPanel/deckPlan", function(scrollPanel){

				if(_.isUndefined(roomModel.deckViewPortSize)){

					roomModel.deckViewPortSize = scrollPanel.viewport.scrollLeft;
				}


			});


			var items = query(".item",widgetDom);

				_.each(items, function(item, i){
					var countRef = i+1;



					var cabinsId = dojo.query(".adults-logo",widgetDom)[0];
					 var caninId = dojo.attr(cabinsId, "id");
					 var itemId = dojo.attr(item, "id");
					var radioButtons = query("input", item);
					var selectButtons = query(".unselect-seat", item);
					var deckTags = query(".decktagcontainer",item);

					var moreDetail = query(".moreDetails",item)[0];


					on(moreDetail,'click', function(e){

						widget.scrollReserveCabin();
					});


					_.each(deckTags,function(deckTag){
						var deckAncTags = query(".ensLinkTrack",deckTag);

						_.each(deckAncTags,function(deckAncTag){

							on(deckAncTag,'click', function(e){

							widget.deckSlectButtonClick(deckAncTag,deckTag,caninId,itemId);
						});
					});

				});



					_.each(selectButtons,function(selectButton){

							on(selectButton,'click', function(e){

							widget.handleSelectButtonClick(selectButton,caninId,itemId)
						});
					});

				});

					widget.cruiseItemDisplay();

				widget.attachShowHideEvents();

		},
		cruiseItemDisplay: function(){
			var widget = this;
			var widgetDom = widget.domNode;
			var allItems = dojo.query(".item",widgetDom);

			_.each(allItems,function(item,i){
				var showItem = false;
				 var itemToggleHead = dojo.query(".item-toggle",item)[0];

				 var radioContainers = dojo.query(".radioContainer",item);


				 _.each(radioContainers,function(radioContainer){


					 if(domClass.contains(radioContainer, "selected")){
						 showItem = true;
					 }

				 });

				 if(i+1 > widget.roomRefNum && !showItem){
						domClass.remove(item,"open");
						showItem = false;
					}


			})

		},
		scrollReserveCabin: function(){

				window.scrollTo(0,dom.byId("reserve_cabin").offsetTop);
			 /*win.scrollIntoView(dom.byId("reserve_cabin"));*/
		},
		displayContent: function(){
			var widget = this;
			var widgetDom = widget.domNode;
			var items = dojo.query(".open",widgetDom);
			var allItems = dojo.query(".item",widgetDom);

			var showAllAlternativeButton = dojo.query(".btnRight",widgetDom)[0];
			var showAll = dojo.query(".showAll",showAllAlternativeButton)[0];

			showAll.innerHTML = this.bookflowMessaging[dojoConfig.site].showroomText;


			//for displaying the content based on condition(Depends on the transition state).
			_.each(allItems,function(item){
				var containerSec = dojo.query(".text_container",item)[0]

				if(containerSec != null){
					domClass.remove(containerSec,"displayNone");
				}

			})
			_.each(items,function(item){
				var containerSec = dojo.query(".text_container",item)[0]


				if(containerSec != null){
					domClass.add(containerSec,"displayNone");
				}

			})
		},


		deckSlectButtonClick: function(deckAncTag,deckId ,itemId , cabinTypeIndex){



			if(this.deckOverlayView && this.deckOverlayView != null) {
				this.deckOverlayView.destroyRecursive();
				this.deckOverlayView = null;
				this.deckOverlay.destroyRecursive();
				this.deckOverlay = null;
			}

			var widget = this;
			var decktagId = dojo.attr(deckAncTag, "id").split(" ");


			 var deckrefId = dojo.attr(deckId, "id");
			 var returnedObj = this.returnSelectedObj(deckrefId , itemId , cabinTypeIndex)
			 console.log(returnedObj);
			 console.log(decktagId[1]);
			 console.log(this.shipCode)
			 var codeType = "";

			 var tragetUrl = BookflowUrl.cruiseDeckPlan;


			 widget.deckSlection = {"deckNo":decktagId[1],"shipCode":this.shipCode,"code":codeType}
			var request={deckPlanCriteria: dojo.toJson(widget.deckSlection)};


			 var results = xhr.post({
	             url: tragetUrl,
	             content: request,
	             handleAs: "json",
	             headers: {Accept: "application/javascript, application/json"},
	             error: function (jxhr,err) {
	                 if (dojoConfig.devDebug) {
	                     console.error(jxhr);
	                 }
	                 console.log(err.xhr.responseText);
	                 widget.afterFailure(err.xhr.responseText);

	             }
	         });


	    	 dojo.when(results,function(response){


	    		 if (response == null)
				 {

				 dojo.removeClass(dom.byId("top"), 'updating');
	    		 dojo.removeClass(dom.byId("main"), 'updating');
	    		 dojo.removeClass(dom.byId("right"), 'updating');
				 }else
					 {
			 widget.renderOverlayWithContent(response,decktagId[1]);
			 dojo.removeClass(dom.byId("top"), 'updating');
			 dojo.removeClass(dom.byId("main"), 'updating');
			 dojo.removeClass(dom.byId("right"), 'updating');
					 }

	    	 });


		},
		displayCabinContents : function(){

		},

		renderOverlayWithContent: function(response,deckNo){
			this.deckOverlayView = new deckOverlayView ({

				"id" : "deck-selection",
				"deckNo": deckNo,
				"shipCode":	this.shipCode,
				"deckResponseData":response,
				"parentWidget":this
			});
			domConstruct.place(this.deckOverlayView.domNode, this.domNode, "last");
			this.deckOverlay = new deckOverlay({widgetId:this.deckOverlayView.id,widgetRef:this.deckOverlayView, modal: true});
			this.deckOverlayView.overlayRef = this.deckOverlay;
			this.deckOverlay.open();


		},
		UpdateScroll: function(){
			var childWidgets = registry.findWidgets(this.deckOverlayView.domNode);

			 _.each(childWidgets, lang.hitch(this, function(childWidget) {

				 if(childWidget instanceof scrollHorizontal){

					if(childWidget.viewport.scrollLeft != 0){
						roomModel.deckViewPortSize = childWidget.viewport.scrollLeft;
					}

					childWidget.viewport.scrollLeft = roomModel.deckViewPortSize
					childWidget.topOnUpdate = false;
					 childWidget.update();


				 }

			 }));
		},
        returnSelectedObj: function(deckId, cabinId, cabinTypeId)
		{
        	var widget = this;
		return widget.jsonData.roomOptionsViewData[cabinId].listOfCabinTypeViewData[cabinTypeId].listOfCabinViewData[deckId];
		},

		handleRadioButtonClick: function(){

		},
		handleSelectButtonClick: function(selectButtonObj ,itemId , cabinTypeIndex){
			console.log(selectButtonObj.id);
			console.log(itemId);
			var returnedObj = this.returnSelectedObj(selectButtonObj.id , itemId , cabinTypeIndex)
			console.log(returnedObj);
			this.generateRequest(itemId, selectButtonObj.id,returnedObj)

		},

		forTransitionEffect: function(){
			var widget= this;
			widget.transitionOptions.domNode = widget.domNode;
			widget.transition = widget.addTransition();
			console.log(widget.jsonData);
			// Tagging particular element.
			if(widget.autoTag) {
				widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), 'toggle');
			}
		},

		refresh : function(field,response){

		} ,

		generateRequest : function(cabinId,alternativeCabinid, returnedObj) {
			var field = "Cabins";
			var widget= this;
			// Getting the values to be sent as ajax parameters.
			var sellingCode= returnedObj.sellingCode;
			var roomCode = returnedObj.roomCode;
			var roomIndex = widget.jsonData.roomOptionsViewData[cabinId].roomIndex;
			widget.changeRoom = {"roomCode":roomCode,"sellingCode":sellingCode,"roomIndex":roomIndex}
			var request={roomSelectionCriteria: dojo.toJson(widget.changeRoom)};
			var url = BookflowUrl.cabinbuttontogglerurl;
			var controller= dijit.registry.byId("controllerWidget").generateRequest(field,url,request);
			return request;
		}
	});
	return tui.widget.booking.changeroomallocation.view.cruiseAlternativeCabinToggler;
});