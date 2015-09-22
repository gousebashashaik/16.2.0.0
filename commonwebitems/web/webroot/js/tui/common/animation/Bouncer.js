define('tui/common/animation/Bouncer', [
  'tui/common/animation/DOM'
], function(DOM) {

  var BOUNCE_TIMING_FN = 'cubic-bezier(.49, .73, .45, .97)';

  function Bouncer(element, transformer, bounceWidth) {
    this.element = element;
    this.transformer = transformer;
    this.bounceWidth = bounceWidth;
    this.elementWidth = DOM.width(element);
    this.parentWidth = DOM.width(element.parentNode);
  }

  Bouncer.prototype.handle = function() {
    var bouncer = this;

    var element = bouncer.element;

    var transformer = bouncer.transformer;

    // Is there a better way to get the slided distance?
    var absoluteLeft = element.offsetLeft + _.pixelValue(transformer.transformValues().x);

    var elementWidth = bouncer.element.clientWidth;
    var parentWidth = bouncer.element.parentNode.clientWidth;

    var shouldBounceFromLeft = false;
    var shouldBounceFromRight = false;

    if (elementWidth <= parentWidth) {
      // 1st case: the element is smaller than the container.
      shouldBounceFromLeft = absoluteLeft < 0;
      shouldBounceFromRight = absoluteLeft + elementWidth > parentWidth;
    } else {
      // 2nd case: the element is wider than the container.
      shouldBounceFromLeft = absoluteLeft > 0;
      shouldBounceFromRight = absoluteLeft + elementWidth < parentWidth;
    }

    function freezeHandler() {
      transformer.freezx();
    }

    return function() {
      if (shouldBounceFromLeft) {
        //var bounceX = Math.abs(elementLeftPos) - (bouncer.elementWidth - bouncer.bounceWidth);
        transformer.transform(0, 0, 0, 300, BOUNCE_TIMING_FN, freezeHandler);
      } else if (shouldBounceFromRight) {
        transformer.transform(0, 0, 0, 300, BOUNCE_TIMING_FN, freezeHandler);
      } else {
        freezeHandler();
      }
    };
  };

  return {
    init: function(element, transformer, bounceWidth) {
      return new Bouncer(element, transformer, bounceWidth);
    }
  };
});
