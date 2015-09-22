define ("tui/searchBResults/view/ThirdPartyInfoPopup", ['dojo',
                                     'dojo/ready',
                                     'dojo/_base/json',
                                     'dojo/text!tui/searchBResults/view/templates/thirdPartyInfoTmpl.html',
									 'tui/widget/popup/Popup'], function(dojo,ready,json,thirdPartyInfoTmpl,popup){

	dojo.declare("tui.searchBResults.view.ThirdPartyInfoPopup",  [tui.widget.popup.Popup], {

		modal: true,

		tmpl: thirdPartyInfoTmpl,

		outBound : false,

		inBound : false,

		itinerary : null,

		outboundInfo: false,

		inboundInfo: false,

		thirdParyInfoOutbound: null,

		staticText : null,

		floatWhere: tui.widget.mixins.FloatPosition.CENTER,

		includeScroll: true,

		postCreate: function() {
			var thirdPartyInfoPopup = this;
			thirdPartyInfoPopup.itinerary = JSON.parse(thirdPartyInfoPopup.itinerary.replace(/\\'/g,"\'"));
			thirdPartyInfoPopup.outBound = thirdPartyInfoPopup.itinerary.outbounds.length ? true : false;
			thirdPartyInfoPopup.inBound = thirdPartyInfoPopup.itinerary.inbounds.length ? true : false;
			thirdPartyInfoPopup.inherited(arguments);
	    }


	})

	return tui.searchBResults.view.ThirdPartyInfoPopup;
});
