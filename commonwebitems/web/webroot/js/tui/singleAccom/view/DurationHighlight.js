define ("tui/singleAccom/view/DurationHighlight", ["dojo",
    "dojo/on",
    "dojox/fx/scroll",
    "tui/widget/_TuiBaseWidget"], function(dojo, on){

    dojo.declare("tui.singleAccom.view.DurationHighlight", [tui.widget._TuiBaseWidget], {

        //-----------------------------------------------------properties

        target: 'nav',

        targetId: null,

        duration: 1000,

        //-----------------------------------------------------methods

        postCreate: function() {
            var durationHighlight = this;
            durationHighlight.inherited(arguments);
            durationHighlight.targetId = dojo.byId(durationHighlight.target);
            durationHighlight.addEventListener();
            durationHighlight.tagElement(durationHighlight.domNode, "seeDurations");
        },

        addEventListener: function() {
            var durationHighlight = this;
            on(durationHighlight.domNode, "click", function(){
                var scrollTop = (window.pageYOffset !== undefined) ?
                                window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop;

                if(scrollTop <= dojo.position(dojo.byId(durationHighlight.targetId)).y) {
                    durationHighlight.highlightDuration();
                    return;
                }

                dojox.fx.smoothScroll({
                    node: durationHighlight.targetId,
                    win: window,
                    duration: durationHighlight.duration,
                    onEnd: durationHighlight.highlightDuration()
                }).play();

            });
        },

        highlightDuration: function () {
            dojo.publish("tui.searchResults.view.HolidayDurationView.displayInfoPopup", true);
            dojo.publish("tui.searchResults.view.InfoPopup.reposition");
        }
    });

    return tui.singleAccom.view.DurationHighlight;
});