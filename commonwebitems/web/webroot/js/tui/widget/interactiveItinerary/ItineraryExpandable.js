define("tui/widget/interactiveItinerary/ItineraryExpandable", [
  'dojo/_base/declare',
  "tui/widget/expand/SlideExpandable"
], function (declare) {

  return declare("tui.widget.interactiveItinerary.ItineraryExpandable", [tui.widget.expand.SlideExpandable], {

    openDirection: 'top',

    subscribableMethods: ['toggleOpen']

  });
});