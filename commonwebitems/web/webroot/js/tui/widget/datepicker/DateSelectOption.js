define("tui/widget/datepicker/DateSelectOption", ["dojo",
												  "tui/widget/form/SelectOption"], function(dojo, query){

    dojo.declare("tui.widget.datepicker.DateSelectOption", [tui.widget.form.SelectOption], {

    	// summary:
    	//		Class which defines the behaviour for custom date selectOption.
    	//
    	// description:
    	//		DateSelectOption extends tui.widget.form.SelectOption, and defines the behaviour
    	//		for date selectOptions used in the tui.widget.datepicker.DatePicker.

        // ---------------------------------------------------------------- DateSelectOption methods

        postCreate: function() {

        	// summary:
        	//			Called after the DateSelectOption widget has been setup.
        	//
        	// description:
        	//			Once DateSelectOption widget has been setup,
        	//			we subscribe to listen to topic messages and set up eventlistners, to
        	//			objects and dom object.

            var dateselectoption = this;
            dateselectoption.inherited(arguments);
            dateselectoption._subscribeToTopic();

            dateselectoption.connect(dateselectoption.selectDropdown, "onclick", function(event){
                dojo.stopEvent(event);
                dojo.publish("tui/widget/datepicker/DateSelectOption/onclick", [dateselectoption, dateselectoption.selectNode]);
            })

            dateselectoption.connect(dateselectoption, "onChange", function(name, oldValue, newvalue){
                dojo.publish("tui/widget/datepicker/DateSelectOption/onchange", [dateselectoption, dateselectoption.selectNode, newvalue])
            })
        },

        _subscribeToTopic: function(){
        	// summary:
        	//		Subscribes to topics, and wait for topic messages to occur.
        	// tag:
        	//		private

            var dateselectoption = this;
            dateselectoption.subscribe("tui/widget/datepicker/DatePicker/close", function(datepicker){
            	// when the datepicker sends a close message we will hide the date selectOption
                dateselectoption.hideList();
            });

            dateselectoption.subscribe("tui/widget/form/SelectOption/onclick", function(currentSelection, event){
            	// when the date selectOption is not the current one select, will close it.
                if (currentSelection !== dateselectoption){
                    dojo.removeClass(dateselectoption.selectDropdown, "open");
                    dateselectoption.hideList();
                }
             })
        }
    })

    return tui.widget.datepicker.DateSelectOption;
})