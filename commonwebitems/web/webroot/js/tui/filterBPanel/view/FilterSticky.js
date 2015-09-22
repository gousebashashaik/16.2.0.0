define('tui/filterBPanel/view/FilterSticky', [
  'dojo',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/topic',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/_base/connect',
  "dojo/dom-geometry",
  'dojo/_base/window',
  'tui/widget/_TuiBaseWidget'], function(dojo, domClass, domConstruct, topic, on, domAttr, domStyle, connect, domGeom, win) {

  function getIndex(node) {
    var parent = node.parentElement || node.parentNode, i = -1, child;
    while(parent && (child = parent.childNodes[++i])) if(child == node) return i;
    return -1;
  }

  function reposition(x) {
    domConstruct.place(x['element'], x['parent'], parseInt(x['index']));
    return x;
  }

  function actualLocation(element) {
    return {
      'element': element,
      'parent': element.parentElement || element.parentNode,
      'index': getIndex(element)
    };
  }

  function targetLocation(element, index) {
    return {
      'element': element,
      'parent': dojo.query('body')[0],
      'index': index
    };
  }


  dojo.declare('tui.filterBPanel.view.FilterSticky', [tui.widget._TuiBaseWidget], {

    controllerID: null,

    stickPosition: null,

    unstickPosition: null,

    scroller: null,
	
	originalStickyPos : 0,

    scrollConfig: {'scrollX': false, 'scrollY': true},

    isSticked: function() {
      return domClass.contains(this.domNode, 'fix-me');
    },

    stick: function() {
      var widget = this;
      widget.stickPosition ? [reposition(widget.stickPosition)] : '';
      domClass.add(widget.domNode, 'fix-me');
      connect.publish("tui/filterPanel/filterSticked", true);
    },


    unStick: function() {
      var widget = this;
      widget.unstickPosition ? [reposition(widget.unstickPosition)] : '';
      domClass.remove(widget.domNode, 'fix-me');
      connect.publish("tui/filterPanel/filterSticked", false);
    },

    startSticky: function() {
      var widget = this;
      !widget.isSticked() ? widget.stick() : '';
    },

    endSticky: function() {
      var widget = this;
      widget.isSticked() ? widget.unStick() : '';
    },

    stickUnstick: function(){
    	var widget = this,
			scrollYPosition = win.global.scrollY ? win.global.scrollY : document.documentElement.scrollTop;
    	if(scrollYPosition >= widget.originalStickyPos.y){
            widget.startSticky();
          }
          else {
              	widget.endSticky();
          }

    },
    postCreate: function() {
      var widget = this;
      var controllerId = widget.controllerID || 'mediator';
      window.scrollTo(0, 0);
      widget.originalStickyPos = domGeom.position(widget.domNode);
      var controller = dijit.registry.byId(controllerId);
      controller.registerControls(widget);
      //on scrolling
      on(window, 'scroll', function(){
          widget.stickUnstick();
      });
      on(window, 'touchmove', function(){
          widget.stickUnstick();
      });

      topic.subscribe('tui/widget/sticky/initiateScroller', function(message) {
        if(domClass.contains(widget.domNode, message.id)) {
          // refresh method needs a delay for dom to refresh
          if(widget.scroller === null) {
            _.delay(function() {
              widget.scroller = new IScroll(dojo.query('.body', widget.domNode)[0], widget.scrollConfig);
            }, 200);
          } else {
            // refresh method needs a delay for dom to refresh
            _.delay(function() {
              widget.scroller.refresh()
            }, 200);
          }
        }
      })
      topic.subscribe('tui/widget/sticky/deactivateScroller', function(message) {
        if(domClass.contains(widget.domNode, message.id)) {
          widget.scroller !== null ? [widget.scroller.scrollTo(0, 0), widget.scroller.destroy(), widget.scroller = null] : '';
        }
      })

      topic.subscribe("tui/searchBPanel/view/SearchSummary?view=changed", function(m){
    	  widget.originalStickyPos = domGeom.position(widget.domNode, true);
      });

      widget.stickPosition = targetLocation(widget.domNode, (getIndex(controller.domNode) + 1));
      widget.unstickPosition = actualLocation(widget.domNode);
      widget.inherited(arguments);
    }

  });
});
