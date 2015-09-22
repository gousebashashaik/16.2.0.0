define ("tui/widget/expand/LinkExpandable", ["dojo", "tui/widget/expand/Expandable"], function(dojo){

	dojo.declare("tui.widget.expand.LinkExpandable", [tui.widget.expand.Expandable], {
		
		defaultTxt: null,
		
		openTxt: "",
		
		postCreate: function(){
			var linkExpandable = this;
			linkExpandable.inherited(arguments);
			dojo.connect(linkExpandable.transition, "onWidgetTransitionEnd", 
				function(transition, itemContent, item, wipe, target){
					if (!linkExpandable.openTxt) return; 
					var txt = (wipe == "wipeIn") ? linkExpandable.openTxt : linkExpandable.defaultTxt
					dojo.query(target).text(txt);
				});
			dojo.connect(linkExpandable.transition, "onWidgetTransition",
				function(transition, itemContent, item, wipe, target){ 
					if (!linkExpandable.defaultTxt){
						linkExpandable.defaultTxt = dojo.query(target).text();
					}
				});
			linkExpandable.tagElements(dojo.query('a', linkExpandable.domNode), "seeAllXReviews");
		}
	})
	
	return tui.widget.expand.LinkExpandable;
})