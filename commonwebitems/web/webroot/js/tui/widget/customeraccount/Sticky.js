define('tui/widget/customeraccount/Sticky', [
    'dojo',
    'dojo/dom-geometry',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'dojo/_base/connect',
    'dojo/dom-construct',
    'tui/common/TuiUnderscore',
    'tui/widget/mobile/Widget'], function (dojo, geom, domClass, domStyle, domAttr) {
    dojo.declare('tui.widget.customeraccount.Sticky', [tui.widget.mobile.Widget], {
        hide: null,
        position: null,
        sortBarClone: _.once(function () {
            var widget = this;
            return dojo.query(widget.hide,widget.domNode.parentNode)[0];
        }),
        isSticked: function () {
            return domClass.contains(this.domNode, 'fade-in');
        },
        stick: function () {
            var widget = this;
            //domClass.add(widget.sortBarClone(), 'fade-out');
            domClass.remove(widget.domNode, 'hide');
            domClass.add(widget.domNode, 'fade-in');
        },
        unStick: function () {
            var widget = this;
            //domClass.remove(widget.sortBarClone(), 'fade-out');
            //domClass.add(widget.sortBarClone(), 'fade-in');
            domClass.remove(widget.domNode, 'fade-in');
            domClass.add(widget.domNode, 'hide');
        },
        startSticky: function (scrollPosition) {
            var widget = this;
            dojo.style(widget.domNode, 'top', scrollPosition+'px');
            !widget.isSticked() ? widget.stick() : '';
        },
        endSticky: function (scrollPosition) {
            var widget = this;
            widget.isSticked() ? widget.unStick() : '';
        },
        handleStickies: function () {
            var widget = this;
            function stickyHandler() {
                var scrollTop = (window.pageYOffset !== undefined) ? window.pageYOffset :
                    (document.documentElement || document.body.parentNode || document.body).scrollTop;
                var sortBarPos = geom.position(dojo.query('.top-summary', widget.sortBarClone())[0]);
                if (sortBarPos.y  <= 0) {
                    widget.startSticky(scrollTop - widget.position.y);
                }

                if (sortBarPos.y > 0) {
                    widget.endSticky();
                }
            }
            document.addEventListener('touchmove', stickyHandler, true);
            dojo.connect(window, 'onscroll', stickyHandler);
        },
        postCreate: function () {
            var widget = this;
            widget.position = geom.position(widget.domNode);
            widget.handleStickies();
            widget.inherited(arguments);
        }
    });
    return tui.widget.customeraccount.Sticky;
});
