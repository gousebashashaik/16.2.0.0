define("tui/widget/booking/insuranceB/TouchTooltip", [
	"dojo",
    "dojo/dom-style",
	"dojo/touch",
	"dojo/on",
	"dojo/text!tui/widget/booking/insuranceB/view/templates/tooltipTmpl.html",
	"tui/widget/popup/Tooltips"
], function (dojo, domStyle, touch, on, tooltipTmpl) {

		
	dojo.declare("tui.widget.booking.insuranceB.TouchTooltip", [tui.widget.popup.Tooltips], {

		// ---------------------------------------------------------------- properties

		tmpl: tooltipTmpl,

		floatWhere: "position-top-center",

		text: null,

		title: null,

		width: null,

		listItems: null,

        listClass: null,
		
		touchEvent: null,
		
        hideWidget: function () {
            var tooltips = this;
            if (tooltips.popupDomNode) domStyle.set(tooltips.popupDomNode, "display", "none");
        },
		
		istouchdevice: _.once(function() {
			  return 'ontouchstart' in window; // to detect the touch event is present or not(works on most browsers)
			}),

		open: function () {
		  var tooltips =this;
		  tooltips.inherited(arguments);
		  //attach touch event for body only if tooltip is open
		  tooltips.touchEvent = on(dojo.body(),dojo.touch.press,function (e) {
					 tooltips.close(); 
              });
		},
		
		close: function () {
            var tooltips = this;
			tooltips.inherited(arguments);
			tooltips.touchEvent.remove(); //disconnecting touch event on body after closing the tooltip
        },
		
		postCreate: function (){
			var tooltips = this;
			if(tooltips.istouchdevice()){
				tooltips.eventType = "click"; //click event for ipads
			}
			else{
				tooltips.eventType = "mouseover"; //mouseover event for desktops
			}
			tooltips.inherited(arguments);
		}

	});

	return tui.widget.booking.insuranceB.TouchTooltip;
});