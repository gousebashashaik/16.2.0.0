define("tui/widget/popup/Popup", [
    "dojo",
    "dojo/on",
    "dojo/_base/fx",
    "dojo/query",
    "tui/widget/popup/PopupBase"], function (dojo, on, baseFx, query) {

    dojo.declare("tui.widget.popup.Popup", [tui.widget.popup.PopupBase], {

        // ---------------------------------------------------------------- tui.widget.popup.Popup properties

        // DOM eventType for displaying popup.
        eventType: "mousedown",

        // Boolean for enabling and disabling popup modal.
        modal: false,

        // modal opacity.
        opacity: 0.4,

        // Reference to modal HTML DOM node. 
        modalDomNode: null,

        // Reference to modal connect event.
        modalConnect: null,

        // Selector for DOM close element
        closeSelector: ".close",
        cancelSelector: ".cancel",

        // Boolean for disabling prevent default, and event bubbling on close.
        stopDefaultOnCancel: true,

        // ---------------------------------------------------------------- tui.widget.mixins.FloatPosition properties

        // Position where popup will be displayed.
        floatWhere: tui.widget.mixins.FloatPosition.CENTER,

        // ---------------------------------------------------------------- tui.widget.popup.Popup methods

        displayModal: function (display, opacity, callback, popup) {
            var popup = this;
            if (popup.modal) {
                popup.modalDomNode = tui.widget.popup.Popup.Modal.display(display, opacity, callback, popup);
                popup.addEventModalListener();
            }
        },

        addEventModalListener: function () {
            var popup = this;
            if (popup.modalConnect) return;
            popup.modalConnect = popup.connect(popup.modalDomNode, "onclick", function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                popup.close();
            });
        },

        // ---------------------------------------------------------------- tui.widget.popup.PopupBase methods

        open: function () {
            // summary:
            //		Overides the default open method from popupbase, ensuring that modal DOM container
            //		if configured is displayed on open
            var popup = this;
            if (popup.modal) {
                var args = arguments;
                test=query("html").addClass("modalmode");
				dojo.setStyle(test, "overflow", "hidden");
				dojo.setStyle(test, "position", "fixed");
                popup.displayModal(true, popup.opacity, function () {
                    popup.inherited(args);
                    if(!_.isUndefined(popup.widgetRef)){
                    	popup.widgetRef.parentWidget.UpdateScroll()
                    	
                    }
                }, popup);
                dojo.publish("tui:channel=modalOpening");
                return;
            }
            popup.inherited(arguments);
        },

        close: function () {
            // summary:
            //		Overides the default close method from popupbase, ensuring that modal DOM container
            //		if configured is closed on close.
            var popup = this;
            if (popup.modal) {
                query("html").removeClass("modalmode");
            }
            popup.inherited(arguments);
            if (popup.modal) {
                popup.displayModal(false, 0, null, popup);
            }
        },

        onAfterTmplRender: function () {
            // summary:
            //		Overides onAfterTmplRender to add a close connect event listener for closing popup,
            //		when close link is selected.
            var popup = this;
            popup.inherited(arguments);
            query(popup.closeSelector, popup.popupDomNode).onclick(function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                popup.close();
            });
            query(popup.cancelSelector, popup.popupDomNode).onclick(function (event) {
                if (popup.stopDefaultOnCancel) {
                    dojo.stopEvent(event);
                }
                popup.close();
            });
        }

    });

    // ---------------------------------------------------------------- modal singleton

    tui.widget.popup.Popup.Modal = (function () {

        var modal = null;

        var popups = [];

        function aminateOpacity(display, opacity, callback) {
            var s = opacity, e = 0;
            if (display === "block") {
                s = 0;
                e = opacity;
            }
            baseFx.animateProperty({
                node: modal,
                properties: {
                    opacity: {start: s, end: e}
                },
                onEnd: function () {
                    dojo.style(modal, {display: display});
                    if (callback) {
                        callback();
                    }
                }
            }).play();
        }

        function addPopup(popup) {
            var index = _.indexOf(popups, popup);
            if (index === -1) {
                popups.push(popup);
            }
        }

        function removePopup(popup) {
            var index = _.indexOf(popups, popup);
            if (index !== -1) {
                popups.splice(index, 1);
            }
        }

        function display(display, opacity, callback, popup, args) {

            modal = modal || dojo.create("div", {
                className: "modal"
            }, document.body, "last");

            (!display) ? removePopup(popup) : addPopup(popup);

            if ((!display && popups.length === 0) ||
                (display && dojo.style(modal, "display") !== "block")) {
                display = (display) ? "block" : "none";
                dojo.style(modal, {opacity: 0, display: "block"});
                aminateOpacity(display, opacity, callback);
            } else {
                if (callback) {
                    callback(args);
                }
            }
            return modal;
        }

        return {
            display: display
        };
    })();

    return tui.widget.popup.Popup;
});