define ("tui/widget/popup/OverflowTooltip", ["dojo",
                                             "dojo/text!tui/widget/Templates/OverflowTooltip.html",
                                             "dojo/html",
											 "tui/widget/popup/PopupBase"], function(dojo, tmpl, html){

    dojo.declare("tui.widget.popup.OverflowTooltip", [tui.widget.popup.PopupBase], {

        tmpl: tmpl,

        max: 22,

        text: null,

        floatWhere: "position-over",

        addPopupEventListener: function(){
            var overflowTooltip = this;
            dojo.connect(overflowTooltip.elementRelativeTo, "onmouseover", function(){
                overflowTooltip.checkValue();
            });
        },

        checkValue: function(){
            var overflowTooltip = this;
            overflowTooltip.text = overflowTooltip.elementRelativeTo.value;
            if(!overflowTooltip.text) return;

            if(overflowTooltip.text.length > overflowTooltip.max){
                if(document.activeElement != overflowTooltip.elementRelativeTo){
                    overflowTooltip.open();
                }
            }
        },

        open: function(){
            var overflowTooltip = this;
            if (overflowTooltip.popupDomNode){
                html.set(overflowTooltip.popupDomNode, overflowTooltip.text);
            }
            overflowTooltip.inherited(arguments);
        },

        onAfterTmplRender: function(){
            var overflowTooltip = this;
            overflowTooltip.inherited(arguments);
            overflowTooltip.connect(overflowTooltip.popupDomNode, "onmouseout", function(e){
                overflowTooltip.close();
            });
            overflowTooltip.connect(overflowTooltip.popupDomNode, "onclick", function(e){
                dojo.stopEvent(e);
                overflowTooltip.close();
                overflowTooltip.elementRelativeTo.focus();
            });
        },

        showWidget: function(){
            var overflowTooltip = this;
            dojo.setStyle(overflowTooltip.popupDomNode, {
                "display": "block",
                "opacity": "0"
            });
            dojo.fadeIn({
                node: overflowTooltip.popupDomNode,
                duration: 200
            }).play();
        },

        hideWidget: function(){
            var overflowTooltip = this;
            dojo.fadeOut({
                node: overflowTooltip.popupDomNode,
                duration: 200,
                onEnd: function(){
                    dojo.setStyle(overflowTooltip.popupDomNode, "display", "none");
                }
            }).play();
        }

    });

    return tui.widget.popup.OverflowTooltip;
})