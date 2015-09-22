define('tui/widget/common/Scroller', [
  'dojo',
  'dojo/dom-style',
  'dojo/dom-attr'], function (dojo, domStyle, domAttr) {

  var EXTRA_SPACE = 100;
  
  var ARROW_WIDTH = 10;

  function computedWidth() {
	return document.documentElement.clientWidth + EXTRA_SPACE;
  }
  
  function computChildWidth() {
	  var newWidth = 0;
		var breadCrumb = dojo.query('.bread-viewport-list');
		var count = 0;
		_.each(dojo.query('.bread-viewport-list > li'), function (breadList) {
			newWidth += breadList.offsetWidth + ARROW_WIDTH;			
		});		
		return newWidth;
	  }

  _.each(dojo.query('.scroller_new'), function (element) {
	    var ul = dojo.query('ul', element)[0];
	    var dynamicWidth = dojo.fromJson(domAttr.get(element, 'data-scroll-dw') || "true");
	    if (ul && dynamicWidth) {
	      domStyle.set(ul, 'width', computChildWidth() + 'px');
	    }
	    new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
	  });
  
  
  _.each(dojo.query('.scroll'), function (element) {
    var ul = dojo.query('ul', element)[0];
    var dynamicWidth = dojo.fromJson(domAttr.get(element, 'data-scroll-dw') || "true");
    if (ul && dynamicWidth) {
      domStyle.set(ul, 'width', computedWidth() + 'px');
    }
    new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
  });

  
  
});
