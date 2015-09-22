define("tui/searchPanel/view/flights/DestinationGuideSelectOption", [
  "dojo",
  "dojo/on",
  "tui/widget/form/SelectOption"
], function (dojo, on) {

  dojo.declare("tui.searchPanel.view.flights.DestinationGuideSelectOption", [tui.widget.form.SelectOption], {

    subscribableMethods: ["setSelectedValue"],

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationGuideSelectOption = this;
      destinationGuideSelectOption.inherited(arguments);
      destinationGuideSelectOption.addDropdownEventlistener();
    },

    addDropdownEventlistener: function () {
      var destinationGuideSelectOption = this;

      destinationGuideSelectOption.connect(destinationGuideSelectOption, "onChange", function (name, oldValue, newValue) {
        dojo.publish('tui.searchPanel.view.flights.DestinationGuide.onDestinationGuideSelectOptionChange', [newValue]);
      });

      on(destinationGuideSelectOption.selectDropdown, "click", function (event) {
        dojo.stopEvent(event);
        dojo.publish('tui.searchPanel.view.flights.DestinationGuide.cancelBlur');
      });

      if (destinationGuideSelectOption.scrollPanels) {
        on(destinationGuideSelectOption.scrollPanels[0].vTrack, "click", function () {
          dojo.publish('tui.searchPanel.view.flights.DestinationGuide.cancelBlur');
        });

        on(destinationGuideSelectOption.scrollPanels[0].vThumb, "click", function () {
          dojo.publish('tui.searchPanel.view.flights.DestinationGuide.cancelBlur');
        });
      }
    },

    onDisableSelection: function () {
      dojo.publish('tui.searchPanel.view.flights.DestinationGuide.cancelBlur');
    }
  });

  return tui.searchPanel.view.flights.DestinationGuideSelectOption;
});
