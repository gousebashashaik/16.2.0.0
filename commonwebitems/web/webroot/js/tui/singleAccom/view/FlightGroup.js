define("tui/singleAccom/view/FlightGroup", [
    "dojo",
    "dojo/on",
    "dojo/dom-style",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/dom-class",
    "dojo/_base/connect"], function (dojo, on, domStyle, domAttr, query, domClass, connect) {


    dojo.declare("tui.singleAccom.view.FlightGroup", [tui.widget._TuiBaseWidget], {

        priceToggleHandle : null,

        postCreate: function () {
            var widget = this;

            widget.priceToggleHandle = connect.subscribe("tui:channel=priceToggle", function (message) {
                domClass.add(widget.domNode, message.add);
                domClass.remove(widget.domNode, message.remove);
            	_.each(query('.cta-buttons .cta', widget.domNode), function(accomUrl){
            		var continueUrl = dojo.getAttr(accomUrl,"href");
            		 var strIndex = continueUrl.search("&price=");
            		    if(strIndex >= 0){
            		    	continueUrl = continueUrl.substring(0,strIndex)+"&price="+message.add;
            	        }
            		    else {
            		    	continueUrl = continueUrl+"&price="+message.add;
            		    }
            		    dojo.setAttr(accomUrl, "href", continueUrl);
            	});
            });

            on(dojo.query('h3', widget.domNode)[0], 'click', function () {
                domClass.toggle(widget.domNode, 'open');
            });

            widget.inherited(arguments);
        },

        destroy : function () {
            var widget = this;
            connect.unsubscribe(widget.priceToggleHandle);
            widget.inherited(arguments);
        }

    })
    return tui.singleAccom.view.FlightGroup;
})
