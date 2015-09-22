define ("tui/widget/booking/ShowMoreExcursions", ["dojo",
                                            "dojo/_base/fx","dojo/text!tui/widget/Templates/ExcursionsOptionsComponentTmpl.html"], function(dojo, fx,ExcursionsOptionsComponentTmpl){

	dojo.declare("tui.widget.booking.ShowMoreExcursions", [tui.widget.popup.DynamicPopup], {

		tmpl:ExcursionsOptionsComponentTmpl,
		stopDefaultOnCancel: false,

		// ---------------------------------------------------------------- methods
        postCreate: function(){

            var ShowMoreExcursions = this;
            ShowMoreExcursions.inherited(arguments);
            var wipeInButton = dojo.byId("showMoreExcursions"),
			wipeTarget = dojo.byId("wipeTarget");

            dojo.connect(wipeInButton, "onclick", function(evt){
            	dojo.fx.wipeIn({ node: wipeTarget }).play();
            });
        }

      })

	return tui.widget.booking.ShowMoreExcursions;
})