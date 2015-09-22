define("tui/searchBPanel/view/DestinationGuideSearchBox", [
	"dojo",
	"dojo/on",
	"dojo/dom-attr",
	"dojo/_base/lang",
	"dojo/Stateful",
	"dojo/query",
	"dojo/_base/connect",
	"tui/searchPanel/view/ErrorPopup",
	'dojo/text!tui/searchBPanel/view/templates/DestinationGuideSearchBoxTmpl.html',
	"tui/searchBPanel/model/DestinationModel",
	"tui/searchBPanel/view/DestinationAutoComplete",
    "tui/widget/_TuiBaseWidget",
    "dijit/_TemplatedMixin"
],	function(dojo, on, domAttr, lang, Stateful, query, connect, ErrorPopup, searchBoxTmpl, DestinationModel) {

	dojo.declare("tui.searchBPanel.view.DestinationGuideSearchBox", [tui.widget._TuiBaseWidget, dijit._TemplatedMixin, Stateful], {

		templateString: searchBoxTmpl,

  		placeHolder: "Start typing a destination, hotel or collection name",

		autocomplete: tui.searchBPanel.view.DestinationAutoComplete,

		supportsPlaceHolder: true,

		autocompleteProps: null,

		selectedNode : null,

		duration: 500,

		postCreate:function () {
			var destinationGuideSearchBox = this;
			destinationGuideSearchBox.autocompleteProps = {};
			destinationGuideSearchBox.addTextEventlistener();
			destinationGuideSearchBox.supportsPlaceHolder = destinationGuideSearchBox.placeHolderSupport();
		    if(!destinationGuideSearchBox.supportsPlaceHolder) {
			  var searchBoxDomNode = query("input", destinationGuideSearchBox.domNode);
			  if(searchBoxDomNode.length) {
			  var curPlaceholder = domAttr.get(searchBoxDomNode[0],"placeholder");
		        if (curPlaceholder && curPlaceholder.length) {
		        	searchBoxDomNode.val(curPlaceholder);
		        	domAttr.set(searchBoxDomNode[0], {"old_color": searchBoxDomNode[0].style.color})
		            searchBoxDomNode[0].style.color = "#c0c0c0";

		        	on(searchBoxDomNode, "focus", function(){
		        		var _this = this;
		                if (this.originalTextbox) {
		                    _this = this.originalTextbox;
		                    _this.dummyTextbox = this;
		                    this.parentNode.replaceChild(this.originalTextbox, this);
		                    _this.focus();
		                }
		                _this.style.color = domAttr.get(searchBoxDomNode[0],"old_color");
		                if (_this.value === curPlaceholder)
		                    _this.value = "";
		    		});

		        	on(searchBoxDomNode, "blur", function() {
		        		var _this = this;
		                if (_this.value === "") {
		                    _this.style.color = "#c0c0c0";
		                    _this.value = curPlaceholder;
		                }
		        	});
		        }
			  }
		  }
            destinationGuideSearchBox.createAutocomplete();
			destinationGuideSearchBox.inherited(arguments);

			connect.subscribe('tui.searchBPanel.view.DestinationMultiFieldList.onTextboxInputFocus', function() {
				destinationGuideSearchBox.autocomplete.hideList();
				query("input",destinationGuideSearchBox.domNode).val("");
				 if(!destinationGuideSearchBox.supportsPlaceHolder) {
					 var searchBoxDomNode = query("input",destinationGuideSearchBox.domNode)[0];
		                if (searchBoxDomNode.value === "") {
		                	searchBoxDomNode.style.color = "#c0c0c0";
		                	searchBoxDomNode.value = domAttr.get(searchBoxDomNode,"placeholder");
		                }
				 }
			});

			 // Watch changes on Search Error Messages "to" property
			 var targetObject = lang.getObject("autocomplete.searchPanelModel.searchErrorMessages", false, destinationGuideSearchBox);
			 targetObject.watch("to", lang.hitch(destinationGuideSearchBox, "displayToErrorMessage"));

		},

		displayToErrorMessage: function (name, oldErrorInfo, newErrorInfo) {
		      var destinationGuideSearchBox = this;
		      // no errors to shows, so close popup if opened.
		      if (destinationGuideSearchBox.infoPopup) {
		        destinationGuideSearchBox.infoPopup.close();
		        destinationGuideSearchBox.infoPopup.destroyRecursive();
		        destinationGuideSearchBox.infoPopup = null;
		      }

		      if (!newErrorInfo || _.isEmpty(newErrorInfo)) {
		        return;
		      }

		      // create an error popup and display it.
		      destinationGuideSearchBox.infoPopup = new ErrorPopup({
		        tmpl: destinationGuideSearchBox.autocomplete.infoPopupTemplate,
		        error: destinationGuideSearchBox.autocomplete.error,
		        elementRelativeTo: destinationGuideSearchBox.domNode,
		        floatWhere: "position-bottom-left",
		        setPosOffset: function (position) {
		          var tooltips = this;
		          if (position === "position-bottom-left") {
		            tooltips.posOffset = {top: 8, left: 0};
		          }
		        }
		      }, null);

		      destinationGuideSearchBox.infoPopup.open();

		  var airportGuide = dojo.query('.airport-guide', destinationGuideSearchBox.infoPopup.popupDomNode),
          	  destGuide = dojo.query('.destination-guide', destinationGuideSearchBox.infoPopup.popupDomNode),
      	      datePicker = dojo.query('.datepicker', destinationGuideSearchBox.infoPopup.popupDomNode),
      	      multiSelect = dojo.query('.multi-select', destinationGuideSearchBox.infoPopup.popupDomNode);

	      var destGuideText = destinationGuideSearchBox.autocomplete.error.code === 'NO_MATCH_FOUND' ? 'See All Destinations NoMatch' : 'See All Destinations';
	      	  destinationGuideSearchBox.tagElement(destinationGuideSearchBox.infoPopup.popupDomNode, destinationGuideSearchBox.autocomplete.error.code);
      		  destGuide.length ? destinationGuideSearchBox.tagElement(destGuide[0], 'See Destinations From') : null;
      		  airportGuide.length ? destinationGuideSearchBox.tagElement(airportGuide[0], 'See Departure Airports For') : null;
      		  datePicker.length ? destinationGuideSearchBox.tagElement(datePicker[0], 'Selected Date No Fly') : null;
      		  multiSelect.length ? destinationGuideSearchBox.tagElement(multiSelect[0], multiSelect[0].innerHTML) : null;

      		  on(destinationGuideSearchBox.infoPopup.popupDomNode, on.selector('.airport-guide', "click"), function (event) {
	              var destination = null;

	              // add entry item to whereTo
					dojo.forEach(destinationGuideSearchBox.autocomplete.error.entry, function(entry){
						destination = dojo.mixin(new DestinationModel(), entry);
						destinationGuideSearchBox.setDataItem({
							key:destination.id,
							value:destination.name,
							listData:destination
						});
					});

	              // remove match items from whereFrom
	              dojo.forEach(destinationGuideSearchBox.autocomplete.error.matches, function(match){
	            	  destinationGuideSearchBox.searchPanelModel.from.remove(match.id);
	              });

	              // open airport expandable.
	              dojo.publish("tui.searchBPanel.view.AirportGuide.openExpandable");

	              // clear error which will close popup.
	              destinationGuideSearchBox.searchPanelModel.searchErrorMessages.set("to", {});
			});
		 },

		// summary:
 		//		Determines whether browser supports placeholder property
		placeHolderSupport : function() {
	        var textBox = document.createElement("input");
	        textBox.type = "text";
	        return (typeof textBox.placeholder !== "undefined");
	    },

	    // summary:
	    // 			Focuses cursor on quick find text box
	    focusTextBox : function() {
	    	this.textboxInput.focus();
	    },

	 // Add the selected data item to the model
	    setDataItem:function (selectData) {
			var destinationGuideSearchBox = this;
			if (selectData.value === 'destinationguide') {
				return;
			}
			destinationGuideSearchBox.searchPanelModel.to.add(selectData.listData);
	    },

		updateModel: function (destination, action) {
		  // summary:
		  //		Adds destination to model
			var destinationGuideSearchBox = this;
			destinationGuideSearchBox.searchPanelModel.to[action]((action === "remove") ? destination.id : destination);
		},

		createAutocomplete: function () {
            // summary:
            //		Creates autocomplete for searchbox.
            var destinationGuideSearchBox = this;
			destinationGuideSearchBox.autocompleteProps.searchPanelModel = destinationGuideSearchBox.searchPanelModel;
			destinationGuideSearchBox.autocompleteProps.widgetController = destinationGuideSearchBox.widgetController;
	        destinationGuideSearchBox.autocompleteProps.elementRelativeTo = destinationGuideSearchBox.domNode;
            destinationGuideSearchBox.autocompleteProps.duration = destinationGuideSearchBox.duration;
            destinationGuideSearchBox.autocompleteProps.arrow = false;

			destinationGuideSearchBox.autocompleteProps.onHighlightItem = function (element, data, index) {
				destinationGuideSearchBox.selectedNode = element;
			}

            destinationGuideSearchBox.autocompleteProps.onElementListSelection = function (selectedData, listData, listIndex) {
                if (selectedData) {
					 dojo.publish("tui.searchBPanel.view.DestinationGuide.updateSearchItem", [destinationGuideSearchBox.selectedNode, selectedData.listData, "add"]);
					 query("input",destinationGuideSearchBox.domNode).val("");
					 if(!destinationGuideSearchBox.supportsPlaceHolder) {
						 var searchBoxDomNode = query("input",destinationGuideSearchBox.domNode)[0];
			                if (searchBoxDomNode.value === "") {
			                	searchBoxDomNode.style.color = "#c0c0c0";
			                	searchBoxDomNode.value = domAttr.get(searchBoxDomNode,"placeholder");
			                }
					 }
					 destinationGuideSearchBox.tagElement(query("#" + this.id)[0],selectedData.key);
                } else {
                	// DE26157: Show only A-Z component
                	if(listData && listIndex && listIndex === (listData.length - 1)) {
                		connect.publish('tui.searchBPanel.view.DestinationGuide.ScrollToAZ');
                	}
                }
            };
            destinationGuideSearchBox.autocomplete = new destinationGuideSearchBox.autocomplete(destinationGuideSearchBox.autocompleteProps, destinationGuideSearchBox.textboxInput);
        },

		addTextEventlistener: function () {
			var destinationGuideSearchBox = this;

			on(destinationGuideSearchBox.textboxInput, "click", function (event) {
				dojo.stopEvent(event);
	            destinationGuideSearchBox.textboxInput.focus();
			});

		}
	});


	return tui.searchBPanel.view.DestinationGuideSearchBox;
});