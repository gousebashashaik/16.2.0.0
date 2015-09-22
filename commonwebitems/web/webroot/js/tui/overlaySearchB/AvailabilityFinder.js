define("tui/overlaySearchB/AvailabilityFinder", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/query",
  "dojo/on",
  "dojo/dom-attr",
  "tui/widget/_TuiBaseWidget"
], function (declare, lang, query, on, domAttr) {

  return declare("tui.overlaySearchB.AvailabilityFinder", [tui.widget._TuiBaseWidget], {

    searchModal: null,

    searchConfig: null,

    itemSelector: null,

    postCreate: function () {
      var availabilityFinder = this;
      availabilityFinder.inherited(arguments);
      availabilityFinder.attachEventListeners();
    },

    attachEventListeners: function () {
      var availabilityFinder = this,
          target;
      on(availabilityFinder.domNode, on.selector(availabilityFinder.itemSelector, "click"), function(e){
        target = e.target;
        if(target.tagName.toLowerCase() !== 'tr') {
          target = query(target).parents('tr')[0];
        }
        availabilityFinder.openModal(target);
      });
    },

    openModal: function (domNode) {
      var availabilityFinder = this,
          from = [{
            id: domAttr.get(domNode, 'data-airport-code'),
            name: domAttr.get(domNode, 'data-airport')
          }],
          when = domAttr.get(domNode, 'data-dep-date');

      availabilityFinder.searchModal.open({
        from:  from,
        when: when,
        noOfAdults: availabilityFinder.searchConfig.MIN_ADULTS_NUMBER
      });
    }

  });
});