define("tui/cruise/deck/controller/DeckController", [
    'dojo',
    "dojo/_base/declare",
    'dojo/on',
    'dojo/text!tui/cruise/deck/view/templates/deckLinkTmpl.html',
    "dojo/query",
    "dojo/dom-geometry",
    "dojo/dom-style",
    "dojo/_base/xhr",
    "tui/cruise/deck/view/DeckLink",
    "tui/widget/mixins/Templatable",
    'tui/widget/_TuiBaseWidget'], function (dojo, declare, on, deckLinkTmpl, query, domGeom, domStyle,  xhr) {

	var TARGET_URL = dojoConfig.paths.webRoot + "/deckplans";

	return declare("tui.cruise.deck.controller.DeckController", [tui.widget._TuiBaseWidget,  tui.widget.mixins.Templatable], {

        data: null,

        title: null,

        type: null,

		code: null,

        shipCode : null,

        tmpl: deckLinkTmpl,

        popup: null,

        decks: [],

        response: null,

        deckDropDown: null,

        deckOverlay: true,

        postCreate: function () {
            var deckController = this;
            deckController.inherited(arguments);

            //iterate over data and assign necessary data to links
            var i = 0;
            _.each(deckController.data, function (deck) {
                var deckData = {"deckData": deck, "no":deck.no, "title": deckController.title};
                var key = deckController.title + deck.no;
                deckController.decks[key] = deckData;
                var html = deckController.renderTmpl(null, deckData);
                dojo.place(html, query("ul", deckController.domNode)[0], "last");
                var links = query("a", deckController.domNode)[i];
                dojo.connect(links, 'onclick', function (event) {
                	var type = deckController.type ? deckController.type : "";
					var code = deckController.code ? deckController.code : "";
                	var shipCode = deckController.shipCode ? deckController.shipCode : "";
                	var xhrReq = xhr.get({
	                    url: TARGET_URL+"?shipCode="+shipCode+"&deckNo="+deckData.no+"&type="+type+"&code="+code,
	                    handleAs: "json",
	                    load: function (response, options) {
	                    	deckController.handleResults(response, deckData.no, key, event);
	                    },
	                    error: function (err) {
	                      deckController.handleError(err);
	                    }
	                  });
                });
                i++;
            });

            dojo.connect(window, 'orientationchange', function() {
         		deckController.alignOverlay();
		  	});
            dojo.destroy(query(".deck-availability-loader", deckController.domNode)[0]);
        },

        handleResults: function (response, deckNo, key, event) {
        	var deckController = this;
        	deckController.response = response
        	 dojo.publish("tui/widget/popup/cruise/DeckPopup/updateDropdown",
        			 		{	"decks":deckController.data, "key":key, "deckNo":deckNo, "title": deckController.title,
        		 				"type": deckController.type, "code": deckController.code ,"shipCode":deckController.shipCode}
        	 			);
        	 deckController.popup.data = response;
        	 deckController.popup.touchSupport = deckController.touchSupport
        	 deckController.popup.scrollToNode = event.target;
             deckController.popup.open();
             setTimeout(function(){ deckController.alignOverlay(); }, 500);
             var scroller = dijit.byId(query(".hz-scroller", deckController.popup.popupDomNode)[0].id);
             deckController.popup.popupDomNode.style.display = "block";
             scroller && scroller.update();
        },

       alignOverlay: function(){
        	var deckController = this;
        	var	deckOverLay = domGeom.position(deckController.popup.popupDomNode, true),
        		deckDomNodeInfo = domGeom.position(query(".content", deckController.popup.popupDomNode)[0], true),
        		lastX = (lastX ?lastX : 0) + deckOverLay.x - deckDomNodeInfo.x + (deckOverLay.w - deckDomNodeInfo.w) / 2,
        		lastY = (lastY?lastY : 0) + deckOverLay.y - deckDomNodeInfo.y + (deckOverLay.h - deckDomNodeInfo.h) / 2;
        	if( lastY <= 10 ){
        		lastY = 10;
        	}
            domStyle.set(query(".content", deckController.popup.popupDomNode)[0], {  left: lastX + "px", top: lastY + "px"  });
        },

        handleError: function(error){
       	 console.log("AJAX Error message: "+error);
       }
    });

});