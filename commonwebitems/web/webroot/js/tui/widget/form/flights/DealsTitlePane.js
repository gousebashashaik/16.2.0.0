define("tui/widget/form/flights/DealsTitlePane" ,[ "dojo",
        "dojo/_base/declare",
        "dijit/TitlePane",
        "dojo/text!./templates/dealstitlepane.html",
        "tui/widget/_TuiBaseWidget"
        ],function(dojo,declare, TitlePane, template, _TuiBaseWidget){
		declare("tui.widget.form.flights.DealsTitlePane",[TitlePane, _TuiBaseWidget],{

			templateString: template,

			secondTitle: "",

			postCreate: function(){
				var DealsTitlePane=this;
					DealsTitlePane.inherited(arguments);
					DealsTitlePane.tagElement(DealsTitlePane.domNode, "FO_Deals_Results_Heading");
			},

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