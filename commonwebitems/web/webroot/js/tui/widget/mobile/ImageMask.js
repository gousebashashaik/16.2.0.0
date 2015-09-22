define('tui/widget/mobile/ImageMask', [
  'dojo',
  'dojo/query',
  'dojo/dom-style',
  'dojo/dom-construct'
], function(dojo, query, domStyle, domConstruct) {

  /*function detectVw() {
    // We create a dummy HTML element with styling.
    var elem = domConstruct.create('div', {style: 'width:50vw'}, dojo.body());

    // We make sure that 50vw is half the width of the window.
    var width = parseInt(window.innerWidth / 2, 10);
    var compStyle = parseInt(domStyle.getComputedStyle(elem).width, 10);

    // Don't forget to remove the dummy element.
    domConstruct.destroy(elem);

    return compStyle === width;
  }

  var supportVw = detectVw();*/
  var supportVw = false;
  var maskDiv = query('.image-mask');

  if (maskDiv.length && !supportVw) {
    maskDiv = maskDiv[0];
    var image = query('img', maskDiv)[0];

    function adapt() {
      var windowWidth = window.innerWidth;
      if (windowWidth <= 579) {
        domStyle.set(maskDiv, 'height', 'auto');
        domStyle.set(image, 'top', '0');
        return;
      }

      // These values must stayed in sync with the ones in components/components/hero.less.
      var height = windowWidth * 0.314;
      var top = windowWidth * 0.10875;
      domStyle.set(maskDiv, 'height', _.pixels(height));
      domStyle.set(image, 'top', _.pixels(_.negate(top)));
    }

    adapt();
    var throttledAdapt = _.throttle(adapt, 1000, {leading: false, trailing: true});
    dojo.connect(window, 'onresize', throttledAdapt);
  }
});
