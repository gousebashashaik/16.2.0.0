define('tui/widget/popup/FaqPopup', [
    'dojo',
    "dojo/query",
    "dojo/parser",
    "dojo/dom-style",
    'tui/widget/popup/Popup'], function(dojo, query, parser, domStyle) {
    dojo.declare('tui.widget.popup.FaqPopup', [tui.widget.popup.Popup], {

        // ----------------------------------------------------------------------------- properties

        tmpl: "",
        
        modal: true,


        // ----------------------------------------------------------------------------- singleton

        
        addPopupEventListener: function () {
            var FaqPopup = this;
            if (!FaqPopup.domNode) return;
            
            FaqPopup.connect(FaqPopup.domNode, ["on", FaqPopup.eventType].join(""), function (e) {
                dojo.stopEvent(e);
                if( !FaqPopup.tmpl ){
                	
		                dojo.xhrGet({
		                		url: FaqPopup.href,
		                		load: function(tmplStr){
		                			var body = dojo.create("html", {innerHTML: tmplStr}).children[1];
		                			//var context= '<div class="popup modal-window remote-content">' + body.innerHTML + '<a href="javascript:void(0);" class="close sprite-img-grp-1">Close</a>'+'</div>'
									var context= '<div class="popup modal-window remote-content faq-popupclose">' + body.innerHTML + '<a href="javascript:void(0);" class="close sprite-img-grp-1">Close</a>'+'</div>'
		                			FaqPopup.tmpl = context;  
		                			FaqPopup.open();
		                		}
		                });
                }else{
                	FaqPopup.open();
                }
                
            });

            if ((FaqPopup.eventType == "mouseenter") || (FaqPopup.eventType == "mouseover")) {
                FaqPopup.connect(FaqPopup.domNode, "onmouseout", function (e) {
                    dojo.stopEvent(e);
                    FaqPopup.close();
                });
            }
        },
        
        onOpen: function () {
        var FaqPopup = this;
        window.scrollTo(0, 0);
       }

    });

    return tui.widget.popup.FaqPopup;
});