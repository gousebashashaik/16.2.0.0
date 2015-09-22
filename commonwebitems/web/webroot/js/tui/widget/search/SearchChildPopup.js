define ("tui/widget/search/SearchChildPopup", ["dojo",
											   "dojo/cookie",
                                               "tui/widget/form/SelectOption",
                                               "dojo/text!tui/widget/search/templates/mainsearch/ChildrenPopup.html",
											   "dojo/topic",
											   "tui/widget/popup/Popup",
											   "dojo/NodeList-traverse",
											   "dijit/registry",
											   "tui/widget/search/CookieSearchSave"], function(dojo, cookie, selectOption, childremPopTmpl){

	
	dojo.declare("tui.widget.search.SearchChildPopup", [tui.widget.popup.Popup, tui.widget.search.CookieSearchSave], {
		// summary:
  		// 		SearchChildPopup class is a popup which allow children ages to be defined and edited.
		
		childrenTotal: 0,
		
		childrenTmplTotal: null,
		
		childrens: null,
		
		roomId: null,
		
		roomNumber: 1,
		
		// ---------------------------------------------------------------- floatposition properties
		
		posOffset: {top: 9, left: 0},
		
		elementRelativeTo: null,
		
		floatWhere: tui.widget.mixins.FloatPosition.BOTTOM_CENTER,

        usecookie: true,
		
		// ---------------------------------------------------------------- popupbase properties
		
		tmpl:  childremPopTmpl,
		
		// ---------------------------------------------------------------- methods
		
		postCreate: function(){
			// summary:
  			// 		This method is called after the searchChildPopup widget is created and is available on the dom.
  			//		Here we initialise variables with the default values, and subscribe to channel ready for events. 
			var searchChildPopup = this;
			searchChildPopup.parentWidget = searchChildPopup.getParent();
			searchChildPopup.childrenTmplTotal = [];
			searchChildPopup.inherited(arguments);
			
			searchChildPopup.elementRelativeTo = dojo.query(".dropdown.child", dojo.query(searchChildPopup.domNode).parents(".item")[0])[0];
			
			searchChildPopup.subscribeToChannels();
			
			// Now we need to see if children ages are already saved. If so we need to populate form.
			searchChildPopup.childrens = [];

			var savedsearch = searchChildPopup.getSaveFormData(searchChildPopup.parentWidget.cookieName);
			savedsearch = (savedsearch) ? dojo.fromJson(savedsearch): null;

			if (savedsearch && searchChildPopup.usecookie){
				// see how many children have been selected.
				var childSelected  =  parseInt(savedsearch["search-children"][searchChildPopup.roomNumber - 1], 10);
				if(childSelected > 0){ 

					// display child edit link
					dojo.style(searchChildPopup.domNode, "display", "block");
					
					// lets get the children ages, there can only be a possibly 8 children in one room.
					var start = ((searchChildPopup.roomNumber - 1) * 8);
					var end  = ((searchChildPopup.roomNumber - 1) * 8 + childSelected);
					
					searchChildPopup.childrens = (!savedsearch) ? [] : savedsearch["ages"].slice(start,end);
					_.forEach(searchChildPopup.childrens, function(child, index){
						searchChildPopup.childrens[index] = ((child === "") || (child === "null")) ? 0 : parseInt(child, 10);
					})

					searchChildPopup.childrenTmplTotal.length = childSelected;
					searchChildPopup.childrenTotal = childSelected;
					searchChildPopup.createPopup();
				}
			}
		},

		subscribeToChannels: function(){
			var searchChildPopup = this;

           /* searchChildPopup.subscribe("tui/widget/search/deleteRoom/close", function(target){
                searchChildPopup.close();
            });*/

            searchChildPopup.subscribe("tui/widget/booking/GetPriceModal/close", function(target){
                searchChildPopup.close();
            });

			searchChildPopup.subscribe("tui/widget/search/SearchChildPopup/edit", function(target){
				if (target !== searchChildPopup) {
					searchChildPopup.close();
					return;
				}
                searchChildPopup._childrens = dojo.clone(searchChildPopup.childrens);
                var widgets = dijit.registry.findWidgets(searchChildPopup.popupDomNode);
                var index = 0;
                _.forEach(widgets, function(widget, i){
                    if (widget instanceof selectOption){
                        widget.setSelectedIndex(searchChildPopup.childrens[index] + 1);
                        index++;
                    }
                })
				searchChildPopup.open();
			});
			
			searchChildPopup.subscribe("tui/widget/search/ChildrenSelectOption/onChange", function(parent, target, childrenTotal){

                searchChildPopup._childrens = dojo.clone(searchChildPopup.childrens);
				// Seeing if widget from main search, or getprice made this channel called.
				if(parent != searchChildPopup.parentWidget) return;

				// Now lets ensure the childrenSelectOption selected is the one asscoiated with this popup.
				if(target.domNode === searchChildPopup.elementRelativeTo) {
					var childrenTotal = parseInt(childrenTotal, 10);
					// store the selected child ages into the childrens array, for use when rendering
					// children age pop up.
					if(searchChildPopup.childrenTotal != childrenTotal) {
						searchChildPopup.childrenTotal = childrenTotal;
						searchChildPopup.childrenTmplTotal.length = searchChildPopup.childrenTotal;
						for(var i = 0; i < searchChildPopup.childrenTotal; i++) {
							searchChildPopup.childrens[i] = (searchChildPopup.childrens[i] !== undefined) ? searchChildPopup.childrens[i] : -1;
						}
						var amountToDelete = searchChildPopup.childrens.length - searchChildPopup.childrenTotal;
						searchChildPopup.childrens.splice(searchChildPopup.childrenTotal, amountToDelete);

						if(searchChildPopup.popupDomNode) {
							searchChildPopup.deleteCustomSelectWidget();
							searchChildPopup.deletePopupDomNode();
						}
					}

					if(searchChildPopup.childrenTotal !== 0) {
						searchChildPopup.showWidget();
						if(!dojo.hasClass(searchChildPopup.parentWidget.domNode, "initialising")) {
							searchChildPopup.open();
						}
					} else {
						searchChildPopup.hideWidget();
					}
				} else {
					searchChildPopup.close();
				}
			})
		},

		
		deleteCustomSelectWidget: function(){
			var searchChildPopup = this;
			if (searchChildPopup.popupDomNode){
				var elements = dojo.query(".custom-dropdown", searchChildPopup.popupDomNode);
				_.forEach(elements, function(elem){
					dijit.registry.byNode(elem).destroyRecursive();
				})
			}
		},
		
		resetsChildData: function(){
			// summary:
			// 		Method which resets the child data.
			var searchChildPopup = this;
			searchChildPopup.childrenTotal = 0;
			searchChildPopup.childrenTmplTotal = [];
			searchChildPopup.childrens = [];
		},
		
		addCancelEventlistener: function(){
			var searchChildPopup = this;
			dojo.query(searchChildPopup.closeSelector, searchChildPopup.popupDomNode).onclick(function(){
                searchChildPopup.childrens = searchChildPopup._childrens;
                var childrenSize = searchChildPopup.childrens.length;
                var dropdown = dijit.registry.byNode(searchChildPopup.elementRelativeTo);
                dropdown.setSelectedIndex(childrenSize);
                searchChildPopup.close();
        	});
		},

        onAfterTmplRender: function(){
			// summary:
  			// 		After the child popup template is rendered, we will add the event listeners
  			//		for the age dropdown.
			var searchChildPopup = this;
            searchChildPopup.inherited(arguments);
			searchChildPopup.addCancelEventlistener()
        	dojo.parser.parse(searchChildPopup.popupDomNode);
			var dropdowns = dojo.query(".custom-dropdown", searchChildPopup.popupDomNode);
			_.forEach(dropdowns, function(dropdown, i){
				var value = searchChildPopup.childrens[i];
				var widget = dijit.registry.byNode(dropdown)
				widget.setSelectedValue(value);
				
				// add children ages to children age array.
				searchChildPopup.connect(widget, "onChange", function(name, oldValue, newvalue){
					searchChildPopup.childrens[i] = parseInt(newvalue.value, 10);
				});
          	});
          	
          	// Add eventlistener to OK button. 
          	var button = dojo.query(".button.ok", searchChildPopup.popupDomNode)[0];
          	searchChildPopup.connect(button, "onclick", function(){
          		if (searchChildPopup.isAgesValid()){
          			searchChildPopup.close();
          		}
			});

		},
		
		close:function(){
			var searchChildPopup = this;
			var error = dojo.query(".error", searchChildPopup.popupDomNode)[0];
			searchChildPopup.hideWidget(error);
			searchChildPopup.inherited(arguments);
		},
		
		isAgesValid: function(){
			var searchChildPopup = this;
			var index = _.indexOf(searchChildPopup.childrens, -1);
          	if (index > -1){
                var lastindex = _.lastIndexOf(searchChildPopup.childrens , -1);
          		var error = dojo.query(".error", searchChildPopup.popupDomNode)[0];
               // var age = (lastindex === index) ? "age" : "ages";
               // dojo.query(".plural", error).innerHTML(age);
          		searchChildPopup.showWidget(error);
          		return false;
          	}
          	return true;
		},
		
		updateAges: function(index) {
			// summary:
  			// 		Update the age fields on form, from given data in the searchChildPopup.childrens array.
  			var searchChildPopup = this;
  			
  			if (!searchChildPopup.isAgesValid()){
  				searchChildPopup.open();
  				return false;	
  			}
  			
  			// clear children ages.
  			searchChildPopup.clearAges();
			
			// set children ages
			var infant, children, age, childSelector, field, ageSelector, childIndex = 1, infantTotalAges = 0, childrenTotalAges = 0;
			for(var i = 0; i < searchChildPopup.childrens.length; i++) {
				age = searchChildPopup.childrens[i];
				childSelector = [".room", index , "-childrenAges", childIndex].join("");
				field = dojo.query(childSelector, searchChildPopup.parentWidget.domNode)[0];
				if(age < 2) {
					infantTotalAges++;
				} else {
					childrenTotalAges++;
                    childIndex++;
					field.value = age;
				}
				ageSelector = [".room", index , "-ages", (i + 1)].join("");
				field = dojo.query(ageSelector, searchChildPopup.parentWidget.domNode)[0];
				field.value = age;
			}

			// set total infant total
			infant = [".room", index, "-totalInfantsInRoom"].join("");
			infant = dojo.query(infant, searchChildPopup.parentWidget.domNode)[0];
			infant.value = (infantTotalAges > 0) ? infantTotalAges : "";
			
			// set children total
			children = [".room", index, "-totalChildrenInRoom"].join("");
			children = dojo.query(children, searchChildPopup.parentWidget.domNode)[0];
			children.value = (childrenTotalAges > 0) ? childrenTotalAges : "";
			return true;
		},
		
		clearAges: function(){
			var searchChildPopup = this;
			var roomGroup = [".", searchChildPopup.roomId,"-group"].join("")
			dojo.query(roomGroup, searchChildPopup.parentWidget.domNode).forEach(function(item, i){
				item.value = "";	
			})
		},
		
		addPopupEventListener: function(){
			var popupBase = this;
			dojo.style(popupBase.domNode, "cursor", "pointer");
			popupBase.connect(popupBase.domNode, ["on", popupBase.eventType].join(""), function(e){
 				popupBase.domNode.focus();
 				//dojo.stopEvent(e);
 				popupBase.editWidget(popupBase.popupDomNode);
			})
		},

        onBeforePosition: function(listPos, targetPos, windowPos){
            var searchChildPopup = this;
            if ((targetPos.y + targetPos.h) + listPos.h > windowPos.h ){
                searchChildPopup.floatWhere = tui.widget.mixins.FloatPosition.TOP_CENTER;
                dojo.addClass(searchChildPopup.popupDomNode, "down-arrow");
                return;
            }
            dojo.removeClass(searchChildPopup.popupDomNode, "down-arrow");
            searchChildPopup.floatWhere = tui.widget.mixins.FloatPosition.BOTTOM_CENTER;
        },

		editWidget: function(popupDomNode){
			var searchChildPopup = this;
			dojo.publish("tui/widget/search/SearchChildPopup/edit", [searchChildPopup, popupDomNode]);
		}	
	})

	return tui.widget.search.SearchChildPopup;
})