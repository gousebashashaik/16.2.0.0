define("tui/widget/customeraccount/PopupBase", [
    "dojo",
    "dojo/query",
    "dojo/cache",
	"dojo/touch",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/mixins/FloatPosition",
    "tui/widget/mixins/Templatable",
    "tui/widget/mixins/Scrollable"], function (dojo, query, cache,touch) {

    dojo.declare("tui.widget.customeraccount.PopupBase", [tui.widget._TuiBaseWidget,
                                                tui.widget.mixins.FloatPosition,
                                                tui.widget.mixins.Scrollable,
                                                tui.widget.mixins.Templatable], {

        // ---------------------------------------------------------------- properties

        popupDomNode: null,
        state:null,
        eventType: "mouseover",
		delay:3000,
        resizeConnect: null,

        subscribableMethods: ["open", "close", "resize"],

        // ---------------------------------------------------------------- methods

        postCreate: function () {
            var popupBase = this;
            if (popupBase.tmplPath) {
                popupBase.tmpl = cache("tui.widget", popupBase.tmplPath);
            }
            popupBase.addPopupEventListener();

            popupBase.inherited(arguments);
        },

        addPopupEventListener: function () {
            var popupBase = this;
            if (!popupBase.domNode) return;
			
			//Logout tooltip functionality
			
			//dojo.query(".logged-out")[0].innerHTML
			var className = dojo.query(".logged-out").attr("class");
			
				if(className == "logged-out show"){ 
					console.log(popupBase.delay);					
					window.onload = function(){					
						setTimeout(function(){
						dojo.query(".logged-out").attr("class","logged-out hide");		
						},popupBase.delay);					
					}
				}
			
			if(popupBase.eventType == "logout"){
			/*
				popupBase.connect(popupBase.domNode, function (e) {					
					popupBase.open();
					window.onload = function(){					
						setTimeout(function(){
						popupBase.close();					
						},popupBase.delay);					
					}					
				});
			*/
			}
			else{
				popupBase.connect(popupBase.domNode, ["on", popupBase.eventType].join(""), function (e) {
					dojo.stopEvent(e);
					popupBase.open();
				});
				
				//event type if condition removed to enable on tab press focus show tooltip and focus out close tooltip
				//fix for DE12736
				popupBase.connect(popupBase.domNode, "onmouseout,focusout", function (e) {
						dojo.stopEvent(e);
						popupBase.close();
				});
				//touch support for ipad, ipad mini, iphone and hand-held devices
				dojo.query('.customer-form').on('touchstart', function(){
					if(popupBase.state){
						popupBase.close();
					}
			    });
			}
        },

        destroyRecursive: function () {
            var popupBase = this;
            if (popupBase.popupDomNode) {
                query(popupBase.popupDomNode).remove();
            }
            popupBase.inherited(arguments);
        },

        resize: function () {
            var popupBase = this;
            popupBase.posElement(popupBase.popupDomNode);
        },

        open: function () {
            var popupBase = this;
            if (!popupBase.popupDomNode) {
                popupBase.createPopup();
            }
            popupBase.posElement(popupBase.popupDomNode);
            popupBase.resizeConnect = popupBase.connect(window, "onresize", function () {
                popupBase.resize();
            });
            popupBase.showWidget(popupBase.popupDomNode);
            popupBase.onBeforeAddScrollerPanel(popupBase.popupDomNode, popupBase);
            popupBase.addScrollerPanel(popupBase.popupDomNode);
            popupBase.onOpen(popupBase.popupDomNode, popupBase);
            popupBase.state=true;
        },

        close: function () {
            var popupBase = this;
            popupBase.disconnect(popupBase.resizeConnect);
            popupBase.hideWidget(popupBase.popupDomNode);
            popupBase.onClose(popupBase.popupDomNode, popupBase);
            popupBase.state=false;
        },

        createPopup: function () {
            var popupBase = this;
            popupBase.setPosOffset(popupBase.floatWhere);

            popupBase.renderTmpl();
        },

        deletePopupDomNode: function () {
            var popupBase = this;
            if (popupBase.resizeConnect) {
                popupBase.disconnect(popupBase.resizeConnect);
                popupBase.resizeConnect = null;
            }
            dojo.destroy(popupBase.popupDomNode);
            popupBase.popupDomNode = null;
        },

        setPosOffset: function (position) {},

        onAfterTmplRender: function (html) {
            var popupBase = this;
            if (html) {
                popupBase.popupDomNode = dojo.place(dojo.trim(html), document.body)
            }
        },

        onClose: function (popupDomNode, popupBase) {},

        onOpen: function (popupDomNode, popupBase) {},

        onBeforeAddScrollerPanel: function(popupDomNode, popupBase) {}
    });

    return tui.widget.customeraccount.PopupBase;
});