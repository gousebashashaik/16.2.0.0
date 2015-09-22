define('tui/filterBPanel/view/ScrollControls', [
  'dojo',
  'dojo/on',
  'dojo/dom-style',
  'dojo/_base/window',
  'dojo/_base/connect',
  'dojo/dom-geometry',
  'dojo/dom-class',
  'tui/widget/_TuiBaseWidget'], function(dojo, on, domStyle, win, connect, geom, domClass) {

  dojo.declare('tui.filterBPanel.view.ScrollControls', [tui.widget._TuiBaseWidget], {



    handleSticky: function(){
      var widget = this;
      window.scrollTo(0, 0);
      widget.originalStickyPos = geom.position(widget.controls.domNode);
      //TODO: find out if there is a better way to do this.
      function stickyHandler(){
        var stickies = _.safeArray([widget.controls]);
        var currentStickyPos = geom.position(widget.controls.domNode);
        if (win.global.scrollY >= widget.originalStickyPos.y){
          _.each(stickies, function(sticky){
            domStyle.set(widget.resultsView.domNode, 'marginTop', currentStickyPos.h + 'px');
            sticky.startSticky();
            domClass.contains(sticky.domNode, 'active') ? domStyle.set(widget.resultsView.domNode, 'paddingTop', '') : '';
          });
        } else{
          _.each(stickies, function(sticky){
            domStyle.set(widget.resultsView.domNode, 'marginTop', '');
            sticky.endSticky();
          });
        }
      }

      document.addEventListener('touchmove', stickyHandler, true);
      on(window, 'scroll', stickyHandler);
      connect.subscribe('map/fullscreenexit', function(){
        widget.controls.endSticky();
      })
    },

    postCreate: function() {
      var widget = this;
      widget.inherited(arguments);
    }

  });
});
