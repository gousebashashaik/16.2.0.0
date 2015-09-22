define('tui/widget/mixins/Swipable', [
  'dojo',
  'tui/common/Util',
  'dojo/on',
  'dojox/gesture/swipe',
  'dojox/gesture/tap',
  'dojo/_base/fx',
  'dojo/dom-style',
  'dojo/query',
  'dojox/dtl',
  'dojox/dtl/Context',
  'dojo/_base/fx',
  'dojo/query',
  'dijit/_Widget'
], function(dojo) {

  var SWIPE_VELOCITY = 2;

  dojo.declare('tui.widget.mixins.Swipable', [dijit._Widget], {

    data: null,

    viewPort: null,

    initSwipable: null,

    swiping: null,

    swiped: null,

    numberOfPages: 0,

    currentPage: 0,

    windowSlidedListener: function(previousWindow, currentWindow) {
      var widget = this;
      widget.currentPage = currentWindow;
      widget.children.forEach(function(child) {
        child.pageChanged(previousWindow, currentWindow);
      });
    },

    postCreate: function() {
      var widget = this;
      var viewPort = widget.getViewPortContent() ? widget.getViewPortContent() : widget.domNode;
      var hammer = Hammer(viewPort);

      hammer.options['swipe_velocity'] = SWIPE_VELOCITY;
      hammer.options['drag_block_vertical'] = false;
      hammer.options['swipe'] = false;
      hammer.options['drag_lock_to_axis'] = true;
      hammer.options['drag_min_distance'] = 75;

      hammer.on('drag', function(ev) {
        if (ev.gesture) {
          widget.dragging(ev.gesture['deltaX']);
        }
      });

      hammer.on('dragend', function(ev) {
        if (ev.gesture) {
          widget.dragged(ev.gesture['deltaX']);
        }
      });

      hammer.on('swipe', function(ev) {
        if (ev.gesture) {
          widget.swiped(ev.gesture['deltaX']);
        }
      });

      widget.initSwipable();
    }
  });
});
