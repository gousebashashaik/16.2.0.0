define ("tui/widget/search/ChildrenSelectOption", ["dojo", 
											   	   "tui/widget/form/SelectOption"], function(dojo){

	dojo.declare("tui.widget.search.ChildrenSelectOption", [tui.widget.form.SelectOption], {
		
		parentWidget: null,

        postMixInProperties: function(){
            var childrenSelectOption = this;
           // childrenSelectOption.srcNodeRef.options[0].selected = true;
            childrenSelectOption.inherited(arguments);
        },

		postCreate: function(){
			var childrenSelectOption = this;
			childrenSelectOption.inherited(arguments);
			childrenSelectOption.parentWidget = childrenSelectOption.getParent();
			childrenSelectOption.addChildDropdownEventlistener();
		},
		
		addChildDropdownEventlistener: function(){
    		var childrenSelectOption = this;
    		childrenSelectOption.connect(childrenSelectOption, "onChange", function(name, oldValue, newvalue){
				dojo.publish("tui/widget/search/ChildrenSelectOption/onChange", [childrenSelectOption.parentWidget , childrenSelectOption, newvalue.value]);
			})
    	}
	})

	return tui.widget.search.ChildrenSelectOption;
})