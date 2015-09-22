define("tui/widget/expand/SimpleExpandable", [
    "dojo",
    "dojo/on",
    "tui/widget/_TuiBaseWidget",
    "tui/dtl/Tmpl",
    "tui/utils/CancelBlurEventListener" ], function (dojo, on) {

    dojo.declare("tui.widget.expand.SimpleExpandable", [tui.widget._TuiBaseWidget, tui.utils.CancelBlurEventListener], {

        // ----------------------------------------------------------------------------- properties

        tmpl: null,

        expandableDom: null,

        closeSelector: ".close",

        expandableParentDom: null,

        expandableProp: null,

        targetSelector: null,

        // ----------------------------------------------------------------------------- methods

        postCreate: function () {
            var simpleExpandable = this;
            simpleExpandable.attachOpenEvent();
            simpleExpandable.attachEventListeners();

            // set default to expand-vertically
            simpleExpandable.expandableProp = simpleExpandable.expandableProp ? simpleExpandable.expandableProp : "expand-vertical";

            simpleExpandable.inherited(arguments);
        },

        attachEventListeners: function () {
            // summary:
            //		Add an event listener to the destination guide dom node.
            //		Which closes destination guide on a blur event.
            var simpleExpandable = this;
            on(simpleExpandable.domNode, "blur", function (event) {
                dojo.stopEvent(event);
                simpleExpandable.performAfterBlur(function () {
                    simpleExpandable.closeExpandable();
                });
            });
        },

        attachOpenEvent: function () {
            var simpleExpandable = this;
            on(simpleExpandable.domNode, "click", function (event) {
                dojo.stopEvent(event);
                if (simpleExpandable.expandableDom === null ||
                    !simpleExpandable.isShowing(simpleExpandable.expandableDom)) {
                    simpleExpandable.openExpandable();
                }
            });
        },

        isShowing: function (element) {
            var _tuiBaseWidget = this;
            element = element || _tuiBaseWidget.domNode;
            return (dojo.hasClass(element, "open"));
        },

        openExpandable: function () {
            var simpleExpandable = this;
            if (!simpleExpandable.expandableDom) {
                simpleExpandable.createExpandable();
            }

            simpleExpandable.showWidget(simpleExpandable.expandableDom);

            simpleExpandable.domNode.focus();
        },

        closeExpandable: function () {
            var simpleExpandable = this;
            if (simpleExpandable.expandableDom && simpleExpandable.isShowing(simpleExpandable.expandableDom)) {
                simpleExpandable.hideWidget(simpleExpandable.expandableDom);
            }
        },

        createExpandable: function () {
            var simpleExpandable = this;
            if (simpleExpandable.tmpl) {
                var html = tui.dtl.Tmpl.createTmpl(simpleExpandable, simpleExpandable.tmpl);
                simpleExpandable.expandableDom = simpleExpandable.place(html);

                on(simpleExpandable.expandableDom, "mousedown", function (event) {
                    dojo.stopEvent(event);
                    simpleExpandable.cancelBlur();
                });
                simpleExpandable.onAfterTmplRender();
            }
        },

        place: function (html) {
            return dojo.place(html, document.body, "last");
        },

        onAfterTmplRender: function () {
            var simpleExpandable = this;

            on(simpleExpandable.expandableDom, on.selector(simpleExpandable.closeSelector, "click"), function (event) {
                dojo.stopEvent(event);
                simpleExpandable.closeExpandable();
            });
        },

        showWidget: function (element) {
            var simpleExpandable = this,
                elementToShow = (element || simpleExpandable.domNode),
                wrapper = dojo.query('.wrapper', simpleExpandable.expandableDom)[0],
                setHeight = simpleExpandable.expandableProp === 'expand-vertical',
                height = setHeight ? _.pixels(dojo.position(wrapper).h) : 0;
                if(simpleExpandable.maxHeight){
                    height =  simpleExpandable.maxHeight;
                }

            setTimeout(function () {
                setHeight ? dojo.style(elementToShow, 'maxHeight', height) : null;
                dojo.addClass(elementToShow, "open");
            }, 1);
            on.once(simpleExpandable.expandableDom, "transitionend, webkitTransitionEnd, mozTransitionEnd, otransitionend, oTransitionEnd", function(){
                dojo.addClass(elementToShow, "open-anim-done");
            });
        },

        hideWidget: function (element) {
            var simpleExpandable = this,
                elementToShow = (element || simpleExpandable.domNode),
                setHeight = simpleExpandable.expandableProp === 'expand-vertical';

            setHeight ? dojo.style(elementToShow, 'maxHeight', 0) : null;
            dojo.removeClass(elementToShow, "open");
            dojo.removeClass(elementToShow, "open-anim-done");
        }

    });

    return tui.widget.expand.SimpleExpandable;
});