define ("tui/widget/expand/Accordion", ["dojo", "tui/widget/expand/Expandable"], function(dojo){
	
	dojo.declare("tui.widget.expand.Accordion", [tui.widget.expand.Expandable], {
			
		postCreate: function(){
			var acccordion = this;
			dojo.mixin(acccordion.transitionOptions, {closeOnClick: false});
			acccordion.inherited(arguments);
		}
	})

	return tui.widget.expand.Accordion;
})