define("tui/widget/datepicker/ReturnDateSelectOption", ["dojo", 
												  "tui/widget/form/SelectOption"], function(dojo, query){

    dojo.declare("tui.widget.datepicker.ReturnDateSelectOption", [tui.widget.form.SelectOption], {
    	
    	// summary:
    	//		Class which defines the behaviour for custom date selectOption.
    	//
    	// description:
    	//		ReturnDateSelectOption extends tui.widget.form.SelectOption, and defines the behaviour
    	//		for date selectOptions used in the tui.widget.datepicker.ArrivalDatePicker.

        // ---------------------------------------------------------------- ReturnDateSelectOption methods

        postCreate: function() {
        	
        	// summary:
        	//			Called after the ReturnDateSelectOption widget has been setup.
        	//
        	// description:
        	//			Once ReturnDateSelectOption widget has been setup, 
        	//			we subscribe to listen to topic messages and set up eventlistners, to
        	//			objects and dom object. 
            
            var returnDateselectoption = this;
            returnDateselectoption.inherited(arguments);
            returnDateselectoption._subscribeToTopic();
            
            returnDateselectoption.connect(returnDateselectoption.selectDropdown, "onclick", function(event){
                dojo.stopEvent(event);
                dojo.publish("tui/widget/datepicker/ReturnDateSelectOption/onclick", [returnDateselectoption, returnDateselectoption.selectNode]);
            })
            
            returnDateselectoption.connect(returnDateselectoption, "onChange", function(name, oldValue, newvalue){
                dojo.publish("tui/widget/datepicker/ReturnDateSelectOption/onchange", [returnDateselectoption, returnDateselectoption.selectNode, newvalue])
            })
        },

        _subscribeToTopic: function(){
        	// summary:
        	//		Subscribes to topics, and wait for topic messages to occur.
        	// tag:
        	//		private
        	
            var returnDateselectoption = this;
            returnDateselectoption.subscribe("tui/widget/datepicker/ArrivalDatePicker/close", function(datepicker){
            	// when the datepicker sends a close message we will hide the date selectOption 
                returnDateselectoption.hideList();
            });

            returnDateselectoption.subscribe("tui/widget/form/SelectOption/onclick", function(currentSelection, event){
            	// when the date selectOption is not the current one select, will close it.
                if (currentSelection !== returnDateselectoption){
                    dojo.removeClass(returnDateselectoption.selectDropdown, "open");
                    returnDateselectoption.hideList();
                }
             })
        }
    })

    return tui.widget.datepicker.ReturnDateSelectOption;
})