define("tui/villaAvailability/view/VillaAvailabilityTransitionPaging", [
    "dojo",
    'dojo/_base/connect',
    "dojo/text!tui/widgetFx/paging/templates/carouselControlTmpl.html",
    "tui/widget/mixins/Templatable",
    "tui/widgetFx/Transition",
    "tui/widgetFx/paging/Paging"
], function (dojo, connect, controlTmpl) {

    dojo.declare("tui.villaAvailability.view.VillaAvailabilityTransitionPaging", [tui.widgetFx.paging.TransitionPaging, tui.widgetFx.Transition, tui.widgetFx.paging.Paging, tui.widget.mixins.Templatable], {

        attachTransitionEventListeners: function () {
            var transitionPaging = this;
            transitionPaging.inherited(arguments);
            _.forEach(transitionPaging.controls, function (element, index) {
                dojo.connect(element, "onclick", function (event) {
                    dojo.stopEvent(event);
                    //if(dojo.hasClass(element, 'disable')) return;
                    //transitionPaging.widgetTransition(transitionPaging, element);
                    //fire the request for paginating...
                    //connect.publish('tui/villaAvailability/paginate', []);
                    connect.publish('tui.villaAvailability.view.VillaAvailability.paginate', [element, index]);
                })
            })
        }
    });

    return tui.villaAvailability.view.VillaAvailabilityTransitionPaging;
});