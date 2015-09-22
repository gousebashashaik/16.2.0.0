define("tui/widget/popup/PopupBase", [
    "dojo",
    "dojo/query",
    "dojo/cache",
    "dojo/dom-construct",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/mixins/FloatPosition",
    "tui/widget/mixins/Templatable",
    "tui/widget/mixins/Scrollable"], function (dojo, query, cache, domConstruct) {

    dojo.declare("tui.widget.popup.PopupBase", [tui.widget._TuiBaseWidget,
                                                tui.widget.mixins.FloatPosition,
                                                tui.widget.mixins.Scrollable,
                                                tui.widget.mixins.Templatable], {

        // ---------------------------------------------------------------- properties

        popupDomNode: null,

        eventType: "mouseover",

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
            popupBase.connect(popupBase.domNode, ["on", popupBase.eventType].join(""), function (e) {
                dojo.stopEvent(e);
                popupBase.open();
            });

            if ((popupBase.eventType == "mouseenter") || (popupBase.eventType == "mouseover")) {
                popupBase.connect(popupBase.domNode, "onmouseout", function (e) {
                    dojo.stopEvent(e);
                    popupBase.close();
                });
            }
        },

        destroyRecursive: function () {
            var popupBase = this;
            if (popupBase.popupDomNode) {
                domConstruct.destroy(popupBase.popupDomNode);
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
        },

        close: function () {
            var popupBase = this;
            popupBase.disconnect(popupBase.resizeConnect);
            popupBase.hideWidget(popupBase.popupDomNode);
            popupBase.onClose(popupBase.popupDomNode, popupBase);
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

    return tui.widget.popup.PopupBase;
});