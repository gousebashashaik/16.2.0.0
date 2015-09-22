    define ("tui/searchPanel/view/cruise/CruiseAndStayPicker", [ "dojo", "dojo/on", "dojo/has","dojo/dom-attr",
    "dojo/text!tui/searchPanel/view/cruise/templates/CruiseAndStayTmpl.html",
    "dojo/topic",
    'dojo/query',
    "dojo/dom-style",
    "dojo/dom-class",
    "tui/searchPanel/store/cruise/CruiseAndStayStore",
    "tui/searchPanel/view/SearchGuide"], function(dojo, on, has, domAttr, cruiseAndStayTmpl, topic, query, domStyle, domClass){

    dojo.declare("tui.searchPanel.view.cruise.CruiseAndStayPicker", [tui.searchPanel.view.SearchGuide], {

        tmpl: cruiseAndStayTmpl,

        hotelList: null,

        hotelOption: null,

        expandableProp:null,

        optionType: null,

        cruiseAndStayPickerStore: null,

        analyticText: 'cruisestay',

        placeholder: null,

        cruiseAndStayText : {
            //"placeholder": "No hotel stay",
            "0": "No hotel stay",
            "3:CS": "3 nights stay",
            "4:CS": "4 nights stay",
            "7:CS": "7 nights stay",
            "7:SC": "7 nights stay"
        },

        stay: {
            "CS": "Cruise then stay",
            "SC": "Stay then Cruise"
        },

        subscribableMethods: ["pulse", "openExpandable", "closeExpandable", "openGlobalExpandable"],


        // ----------------------------------------------------------------------------- methods

        postCreate: function () {

            var cruiseAndStayPicker = this;

            cruiseAndStayPicker.placeholder = dojo.query(".placehold", cruiseAndStayPicker.domNode)[0];


            cruiseAndStayPicker.inherited(arguments);



            dojo.subscribe("tui:channel=modalOpening", function(){
                cruiseAndStayPicker.closeExpandable();
            });
            dojo.subscribe("tui.searchPanel.view.ChildSelectOption.updateChildAgeValues", function(){
               cruiseAndStayPicker.updatePlaceholder();
            });

            cruiseAndStayPicker.connect(document.body, "onclick", function (event) {
                if (document.activeElement === cruiseAndStayPicker.domNode) return;
                if (cruiseAndStayPicker.expandableDom === null || !cruiseAndStayPicker.isShowing(cruiseAndStayPicker.expandableDom)) {
                    return;
                }
                cruiseAndStayPicker.closeExpandable();
            });
            cruiseAndStayPicker.tagElement(cruiseAndStayPicker.domNode, cruiseAndStayPicker.analyticText);

        },

        showWidget: function (element) {
            var airportGuide = this;
            if(airportGuide.widgetController.searchApi === "getPrice") {
                // TODO: fix width being sent, this is arbitrary
                dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 505);
                // ie7 has issues with getPrice guide
                // TODO: fix this at some point
                if (has("ie") === 7) dojo.setStyle(element, {"width": "auto"});
            }
            var elementToShow = (element || airportGuide.domNode);
            dojo.addClass(elementToShow, "open");
            dojo.addClass(elementToShow, "open-anim-done");
            airportGuide.inherited(arguments);
        },

        hideWidget: function (element) {
            var airportGuide = this;
            if(airportGuide.widgetController.searchApi === "getPrice") {
                // TODO: fix width being sent, this is arbitrary
                dojo.publish("tui.searchGetPrice.view.GetPriceModal.resize", 299);
                // ie7 has issues with getPrice guide
                // TODO: fix this at some point
                if (has("ie") === 7) dojo.setStyle(element, {"width": 0});
            }
            airportGuide.inherited(arguments);
        },

        attachOpenEvent: function () {
            var cruiseAndStayPicker = this;
            on(cruiseAndStayPicker.domNode, "click", function (event) {
                if (dojo.hasClass(cruiseAndStayPicker.domNode, "loading")) {
                    return;
                }

                dojo.removeClass(cruiseAndStayPicker.domNode, "inactive");
                dojo.addClass(cruiseAndStayPicker.domNode, "active");

                if (cruiseAndStayPicker.expandableDom === null || !cruiseAndStayPicker.isShowing(cruiseAndStayPicker.expandableDom)) {
                    setTimeout(function () {
                        cruiseAndStayPicker.openExpandable();
                    }, 350);
                } else {
                    cruiseAndStayPicker.closeExpandable();
                }

            });
        },


        openExpandable: function () {
            // summary:
            //		Extend default expandable behaviour,
            //		only open once airport guide data is available and returned from the server.
            var cruiseAndStayPicker = this;
            var args = arguments;
            dojo.addClass(cruiseAndStayPicker.domNode, "loading");

            // publish opening event for widgets that need to close
            cruiseAndStayPicker.publishMessage("searchPanel/searchOpening");

            dojo.publish("tui.searchPanel.view.cruise.AirportMultiFieldList.highlight", [true, cruiseAndStayPicker.widgetController]);
            dojo.when(cruiseAndStayPicker.cruiseAndStayPickerStore.requestData(cruiseAndStayPicker.searchPanelModel.generateQueryObject(), false), function (responseData) {
                dojo.removeClass(cruiseAndStayPicker.domNode, "loading");

                cruiseAndStayPicker.hotelList = responseData;

                cruiseAndStayPicker.inherited(args);

                // Update the view to conform to the latest datafeed.
                cruiseAndStayPicker.updateCruiseStayCheckboxes(responseData);

                if (cruiseAndStayPicker.widgetController.searchApi === "getPrice") {
                    dojo.addClass(cruiseAndStayPicker.widgetController.domNode, "open-guide");
                }
            });
        },


        onAfterTmplRender: function () {
            // summary:
            //		After the expandable dom has been created,
            //		we attach event listeners to the checkboxes and links
            var cruiseAndStayPicker = this;
            cruiseAndStayPicker.inherited(arguments);

            on(query("span.night-options", cruiseAndStayPicker.expandableDom), "label:click", function (event) {

                dojo.stopEvent(event);
                var label, checkbox;
                if (event.target.tagName.toUpperCase() === "INPUT") {
                    checkbox = event.target;
                    label = event.target.parentElement;
                } else if (event.target.tagName.toUpperCase() === "SPAN") {
                    checkbox = event.target.parentElement.children[0];
                    label = event.target.parentElement;
                } else {
                    checkbox = event.target.children[0];
                    label = event.target;
                }


                if (dojo.hasClass(label, "disabled") || dojo.hasClass(label, "manually-disabled")) {
                    return;
                }



                var colTwoLabels = dojo.query("span.night-options label", cruiseAndStayPicker.expandableDom);
                _.each(colTwoLabels, function(input, i){
                    if (dojo.hasClass(input, "selected")){
                        dojo.removeClass( input, "selected");
                    }
                });

                cruiseAndStayPicker.optionType = checkbox.value;
                dojo.addClass(label, "selected");
                cruiseAndStayPicker.searchPanelModel.addAStay = cruiseAndStayPicker.optionType;
                cruiseAndStayPicker.updatePlaceholder(checkbox);
                dojo.publish("tui:channel=updateResponse", ['cs-stay']);
            });

            // Radio button event delegation
            on(query("div.col-first", cruiseAndStayPicker.expandableDom)[0], "label:click", function (event) {
                dojo.stopEvent(event);
                var label, checkbox;
                if (event.target.tagName.toUpperCase() === "INPUT") {
                    checkbox = event.target;
                    label = event.target.parentElement;
                } else if (event.target.tagName.toUpperCase() === "SPAN") {
                    checkbox = event.target.parentElement.children[0];
                    label = event.target.parentElement;
                } else {
                    checkbox = event.target.children[0];
                    label = event.target;
                }

                if (dojo.hasClass(label, "disabled") || dojo.hasClass(label, "manually-disabled")) {
                    return;
                }

                if (cruiseAndStayPicker.hotelOption === checkbox.value) {
                    return;
                }
                var csClicked = checkbox.value.indexOf('CS') > -1;
                if (csClicked){
                    var colOneLabels = dojo.query("span.night-options label", cruiseAndStayPicker.expandableDom);
                    _.each(colOneLabels, function(input, i){
                        dojo.removeClass(input, "disabled");
                    });
                }
                else{
                    var colOneLabels = dojo.query("span.night-options label", cruiseAndStayPicker.expandableDom);
                    _.each(colOneLabels, function(input, i){
                        dojo.addClass(input, "disabled");
                    });
                }

                var selectedValue = dojo.getAttr(label, 'data-value');

                cruiseAndStayPicker.hotelOption = checkbox.value;

                var colOneLabels = dojo.query("div.col-first label", cruiseAndStayPicker.expandableDom);
                _.each(colOneLabels, function(input, i){
                        dojo.removeClass(input, "selected");
                });

             // Start the orange pulse on the field.
                dojo.publish("tui.searchPanel.view.cruise.AirportMultiFieldList.pulse", [cruiseAndStayPicker.widgetController]);
                cruiseAndStayPicker.updateToPanel(checkbox);
                cruiseAndStayPicker.cancelBlur();

            	 domClass.add( label, "selected");
                 if(selectedValue.indexOf('CS') > -1){
                     _.each(colOneLabels, function(label, i){
                         if(dojo.getAttr(label, 'data-value') === selectedValue)
                            dojo.addClass(label, "selected");
                     });
                 }

            	 var colTwoLabels = dojo.query("div.col-second label", cruiseAndStayPicker.expandableDom);

            	 if(cruiseAndStayPicker.hotelOption != "0"){
            		 _.each(colTwoLabels, function(input, i){
                         dojo.removeClass(input, "disabled");
            		 });
            	 } else {
            		 _.each(colTwoLabels, function(input, i){
                         dojo.addClass(input, "disabled");
                         if (dojo.hasClass(input, "selected")){
                        	 dojo.removeClass( input, "selected");
                         }
            		 });
            	 }

                 cruiseAndStayPicker.searchPanelModel.addAStay = cruiseAndStayPicker.hotelOption;
                cruiseAndStayPicker.updatePlaceholder(checkbox);
                dojo.publish("tui:channel=updateResponse", ['cs']);
            });

            //cruiseAndStayPicker.searchPanelModel.searchErrorMessages.set("addAStay",{""+cruiseAndStayPicker.hotelOption+":"+ccruiseAndStayPicker.optionType+""});

            //pre populate saved search if applicable
            var colOneLabels = dojo.query("div.col-first label", cruiseAndStayPicker.expandableDom);
            var selectedStayOption = _.find(colOneLabels, function(label){ return dojo.hasClass(label, "selected"); });
            if(!selectedStayOption){
              //update the right label node with 'selected' class
              var list = _.filter(colOneLabels, function(label){ return dojo.getAttr(label, 'data-value') === cruiseAndStayPicker.searchPanelModel.addAStay; });
              _.each(list, function(modify){ domClass.add( modify, "selected");});
              if((cruiseAndStayPicker.searchPanelModel.addAStay.indexOf('CS')>-1) && _.size(list) === 1){
                  domClass.add( dojo.query(".cruise-n-stay", cruiseAndStayPicker.expandableDom)[0], "selected");
              }
            }
        },

        updateToPanel: function (clickedCheckbox) {
            // summary:
            //		Adds/removes selected airport to/from airport model.
            var cruiseAndStayPicker = this;

            var clickedLabel = dojo.query(clickedCheckbox).parent()[0];

            // Ignore the click if the label is disabled.
            if (dojo.hasClass(clickedLabel, "disabled")) {
                return;
            }

            // We look to the parent label tag class to see if selected or not.
            var add = !(dojo.hasClass(clickedLabel, "selected"));
            var action = (add) ? "addClass" : "removeClass";

            if (add) {
            	 cruiseAndStayPicker.animatePill(clickedLabel, dojo.query(".cruise-and-stay", cruiseAndStayPicker.widgetController.domNode)[0], function () {
                     // Execute the action after the animation.
                     cruiseAndStayPicker.updatePlaceholder(clickedCheckbox);
                 });
                return;
            }

        },

        updatePlaceholder: function (clickedCheckbox) {
            var cruiseAndStayPicker = this;
            cruiseAndStayPicker.placeholder.innerHTML = cruiseAndStayPicker.cruiseAndStayText[cruiseAndStayPicker.searchPanelModel.addAStay];
          },



        updateCruiseStayCheckboxes: function (responseData) {
            // summary:
            //    Changes item state to enabled/disabled depending on server response
            var cruiseAndStayPicker = this;
            if (!cruiseAndStayPicker.expandableDom) {
                return;
            }

            var colOneLabels = dojo.query("span.night-options label", cruiseAndStayPicker.expandableDom);
            _.each(colOneLabels, function(input, i){
                  if(cruiseAndStayPicker.searchPanelModel.addAStay.indexOf('CS') > -1) {
                      dojo.removeClass(input, "disabled");
                  }else{
                      dojo.addClass(input, "disabled");
                  }

            });

            var leftHandSide = _.filter(dojo.query("div.col-first label", cruiseAndStayPicker.expandableDom), function(label){ return dojo.hasClass(label, "cruise-n-stay") || dojo.hasClass(label, "stay-and-cruise"); });
            var invalidStay = _.find(responseData, function(stay){ return stay.disabled; });
            if(!_.isUndefined(invalidStay)) {
                //Disable all other than No Hotel stay
                _.each(leftHandSide, function(modify){ domClass.add( modify, "disabled");});
            }else{
                _.each(leftHandSide, function(modify){ domClass.remove( modify, "disabled");});
            }

        },

        cancelBlur: function () {
            var cruiseAndStayPicker = this;
            cruiseAndStayPicker.inherited(arguments);
            cruiseAndStayPicker.domNode.focus();
        },

        closeExpandable: function () {
            var cruiseAndStayPicker = this;
            cruiseAndStayPicker.inherited(arguments);
            dojo.publish("tui.searchPanel.view.AirportMultiFieldList.highlight", [false, cruiseAndStayPicker.widgetController]);

            if (cruiseAndStayPicker.widgetController.searchApi === "getPrice") {
                dojo.removeClass(cruiseAndStayPicker.widgetController.domNode, "open-guide");
                setTimeout(function(){
                	 dojo.publish("tui.searchBPanel.view.DropDownSelector", [true]);
                }, 280);
            }

            	dojo.addClass(cruiseAndStayPicker.domNode, "inactive");
	    	   	dojo.removeClass(cruiseAndStayPicker.domNode, "active");
        },

        place: function (html) {
            var cruiseAndStayPicker = this;
            return dojo.place(html, cruiseAndStayPicker.domNode, "after");
        }

    });
    return tui.searchPanel.view.cruise.CruiseAndStayPicker;
});