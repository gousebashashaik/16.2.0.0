define([ "dojo",
        "dojo/_base/declare",
        "dijit/TitlePane",
        "dojo/text!./templates/dealstitlepane.html"
        ],function(dojo,declare, TitlePane, template){
		declare("tui.widget.form.flights.DealsTitlePane",[TitlePane],{

			templateString: template,

			secondTitle: "",

			_setSecondTitleAttr: { node: "secondTitleNode", type: "innerHTML" },	// override default where title becomes a hover tooltip,

			setSecondTitle: function(/*String*/ secondTitle){
				// summary:
				//		Deprecated.  Use set('title', ...) instead.
				// tags:
				//		deprecated
				kernel.deprecated("dijit.TitlePane.setSecondTitle() is deprecated.  Use set('secondTitle', ...) instead.", "", "2.0");
				this.set("secondTitle", secondTitle);
			},

			onShow: function(){
				var DealsTitlePane = this;
				dojo.style(DealsTitlePane.secondTitleNode,"visibility","hidden");
			},

			onHide: function(){
				var DealsTitlePane = this;
				dojo.style(DealsTitlePane.secondTitleNode,"visibility","visible");
			}
		})

		return tui.widget.form.flights.DealsTitlePane;
})