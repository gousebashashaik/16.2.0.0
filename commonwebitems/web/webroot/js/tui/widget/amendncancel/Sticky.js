define("tui/widget/amendncancel/Sticky", [
  "dojo",
  "dojo/on",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
    "tui/widgetFx/Transitiable",
    "tui/widgetFx/WipeTransitions"
  ], function(dojo, on, domStyle) {

  dojo.declare("tui.widget.amendncancel.Sticky", [tui.widget._TuiBaseWidget], {

    postCreate: function() {
      var sticky = this;
	  if(document.addEventListener) {
		document.addEventListener('touchmove', stickyHandler, true);
	  }

      on(window, 'scroll', stickyHandler);


	function stickyHandler() {
	  var stickyWrapper = dojo.query(".stickyWrapper")[0];
	  var footerPos = dojo.byId("inner-footer").offsetTop - 350;
      var stickyDomPos = sticky.domNode.offsetTop,
          stickOffPos = (stickyWrapper.offsetTop),
          pagePos = window.pageYOffset ? window.pageYOffset : document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;

      if (pagePos > stickOffPos) {
        dojo.addClass(sticky.domNode, "stick");
      }
      else if (pagePos < stickOffPos) {
        dojo.removeClass(sticky.domNode, "stick");
      }

	  if(pagePos > footerPos) {
		var footerOffsetDiff = footerPos - pagePos+"px";
		domStyle.set(sticky.domNode, "top", footerOffsetDiff);
	  }
	  if(pagePos <= footerPos) {
		domStyle.set(sticky.domNode, "top", "0px");
	  }

    }
  }

  });
  return tui.widget.amendncancel.Sticky;
});