define('tui/villaAvailability/view/VillaAvailabilitySlide', [
  'dojo',
  'dojo/_base/fx',
  'tui/widgetFx/Slide',
  'tui/villaAvailability/view/VillaAvailabilityTransitionPaging'
], function(dojo, baseFx) {

  dojo.declare('tui.villaAvailability.view.VillaAvailabilitySlide', [tui.widgetFx.Slide, tui.villaAvailability.view.VillaAvailabilityTransitionPaging], {

      toggleDisableControls: function() {
        var slide = this;
        _.forEach(slide.controls, function(element, index) {
          var prop = (slide.isLimit(index)) ? 'addClass' : 'removeClass';
          dojo[prop](element, 'disable');
        });
      }
  });

  return tui.villaAvailability.view.VillaAvailabilitySlide;
});
