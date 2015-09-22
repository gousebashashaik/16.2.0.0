define("tui/widget/ScrollPanel", ["dojo",
    "dojo/_base/window",
    "dojo/dom-style",  
	"dojo/text!tui/widget/Templates/ScrollPanelTmpl.html",
	"tui/widgetFx/MoveableLimit",
	"tui/widgetFx/VerticalMover",
	"tui/widget/_TuiBaseWidget",
	"tui/widget/mixins/ContentLoader"], function (dojo, win, domStyle, scrollPanelTmpl, moveableLimit, verticalMover) {

	dojo.declare("tui.widget.ScrollPanel", [tui.widget._TuiBaseWidget, tui.widget.mixins.ContentLoader], {

		height: 0,

		wheel: 8,

		vhandle: null,

		vScroll: true,

		vTrack: null,

		maxThumbSize: 15,

		offset: 16,

		mover: null,

		hideThumb: false,

		touchClass: false,

		contentSelector: ".viewport",

		topOnUpdate: true,

		//---------------------------------------------------------------- methods

		postMixInProperties: function () {
			var scrollPanel = this;
			var children = dojo.query(scrollPanel.srcNodeRef).children();
			dojo.place(scrollPanelTmpl, children[0], "before");
			var viewport = dojo.query(scrollPanel.contentSelector, scrollPanel.srcNodeRef)[0];
			_.forEach(children, function (item) {
				dojo.place(item, viewport, "last");
			})
		},

		postCreate: function () {
			var scrollPanel = this;
			scrollPanel.onBeforeScrollPanelInit(scrollPanel);
			scrollPanel.inherited(arguments);
			scrollPanel.vTrack = dojo.query(".track", scrollPanel.domNode)[0];
			scrollPanel.vThumb = dojo.query(".track .handle", scrollPanel.domNode)[0];
			scrollPanel.viewport = dojo.query(".viewport", scrollPanel.domNode)[0];

			var domNodePosition = dojo.position(scrollPanel.vTrack, true);

			scrollPanel.connect(scrollPanel.vTrack, "onclick", function (event) {
				dojo.stopEvent(event);
				var pos = event.clientY - domNodePosition.y;
				scrollPanel.updateScrollerFromPos(pos);
				dojo.publish("tui/widget/ScrollPanel/vtrack/onclick", [scrollPanel]);
			});

			scrollPanel.connect(scrollPanel.vThumb, "onclick", function (event) {
				dojo.stopEvent(event);
				dojo.publish("tui/widget/ScrollPanel/vThumb/onclick", [scrollPanel]);
			});

			scrollPanel.connect(scrollPanel.viewport, (!dojo.isMozilla ? "onmousewheel" : "DOMMouseScroll"), function (e) {
                dojo.stopEvent(e);
				// except the direction is REVERSED, and the event isn't normalized! one more line to normalize that:
				var scroll = e[(!dojo.isMozilla ? "wheelDelta" : "detail")] * (!dojo.isMozilla ? 1 : -1);
				scroll = (scroll > 0) ? 3 : -3;
				scrollPanel.viewport.scrollTop -= scroll * scrollPanel.wheel;
				scrollPanel.vUpdateThumbFromContentScroll();
			});

      scrollPanel.connect(scrollPanel.viewport, "onscroll", function(e){
        scrollPanel.vUpdateThumbFromContentScroll();
      })

			scrollPanel.update();
	      
	      // introducing native scrollers for all dropdowns for touch devices 
		  //  and hiding desktop custom scroller
      ipadtouchClass = dojo.hasClass(dojo.query("html")[0],"touch");
      if(ipadtouchClass && (dojo.hasClass(scrollPanel.domNode,"dropdownlist") || dojo.hasClass(scrollPanel.domNode,"autocomplete"))){		    	   	   
		       	domStyle.set(scrollPanel.vTrack,"display","none");
		        domStyle.set(scrollPanel.viewport, {
		     	    "marginRight": "0px",   	    
		     	    "-webkit-overflow-scrolling": "touch",
		     	    "overflowY": "auto"
		     	  });
		       }
	      
			scrollPanel.onAfterScrollPanelInit(scrollPanel);
		},

		updateScrollerPosition: function (dir, amount) {
			var scrollPanel = this;
			amount = amount || scrollPanel.viewport.offsetHeight;
			if (dir === 0) {
				scrollPanel.viewport.scrollTop -= amount;
			} else {
				scrollPanel.viewport.scrollTop += amount;
			}
			scrollPanel.vUpdateThumbFromContentScroll();
		},

		// maybe dep
		updateScrollerFromPos: function (pos, amount) {
			var scrollPanel = this;
			amount = amount || scrollPanel.viewport.offsetHeight;
			var vThumb = (dojo.style(scrollPanel.vThumb, "top"));
			if (pos > vThumb) {
				scrollPanel.viewport.scrollTop += amount;
			} else {
				scrollPanel.viewport.scrollTop -= amount;
			}
			scrollPanel.vUpdateThumbFromContentScroll();
		},

		moveElementInView: function (element) {
			var scrollPanel = this;
			if (!dojo.query(element, scrollPanel.domNode).length > 0) return;
			var pos = dojo.position(element, true);
			var inviewpos = element.offsetTop - scrollPanel.viewport.scrollTop;
			if (inviewpos < 0) {
				scrollPanel.viewport.scrollTop -= 100;
				scrollPanel.vUpdateThumbFromContentScroll();
			} else if ((inviewpos + pos.h) > scrollPanel.domNode.offsetHeight) {
				scrollPanel.viewport.scrollTop += 100;
				scrollPanel.vUpdateThumbFromContentScroll();
			}
		},

		update: function () {
			var scrollPanel = this;
			var display = dojo.style(scrollPanel.domNode, "display");
			dojo.style(scrollPanel.domNode, "display", "block");

			scrollPanel.height = parseInt(dojo.style(scrollPanel.domNode, "height"), 10);
			dojo.style(scrollPanel.viewport, "height", [scrollPanel.height, "px"].join(""));
			if (scrollPanel.topOnUpdate) {
				scrollPanel.viewport.scrollTop = 0;
			}
			scrollPanel.vScroll = (scrollPanel.viewport.scrollHeight > scrollPanel.domNode.offsetHeight);

			// hide vertical track if we dont need, vertical scroll.
			var displaytrack = (scrollPanel.vScroll) ? "block" : "none";
			dojo.style(scrollPanel.vTrack, "display", displaytrack);
			if (!scrollPanel.vScroll) return;

			dojo.style(scrollPanel.vTrack, "height", [scrollPanel.height - scrollPanel.offset, "px"].join(""));
			scrollPanel.vViewportSize = scrollPanel.viewport.offsetHeight;
			scrollPanel.vViewportScrollSize = scrollPanel.viewport.scrollHeight;
			scrollPanel.vViewportRatio = scrollPanel.vViewportSize / scrollPanel.vViewportScrollSize;

			// working out the thumb knob size.
			scrollPanel.vTrackSize = scrollPanel.vTrack.offsetHeight;
			scrollPanel.vThumbSize = Math.min(scrollPanel.vTrackSize, Math.max(scrollPanel.maxThumbSize, scrollPanel.vTrackSize *
			                                                                                             scrollPanel.vViewportRatio));
			dojo.style(scrollPanel.vThumb, 'height', [scrollPanel.vThumbSize, "px"].join(""));

			scrollPanel.vScrollRatio = scrollPanel.vViewportScrollSize / scrollPanel.vTrackSize;
			scrollPanel.vUpdateThumbFromContentScroll();
			scrollPanel.vUpdateContentFromThumbPosition();

			scrollPanel.mover = (scrollPanel.mover) ? scrollPanel.mover :
			                    new moveableLimit(scrollPanel.vThumb, {mover: verticalMover, host: scrollPanel});

			scrollPanel.mover.limit = {y: [0, scrollPanel.vTrackSize - scrollPanel.vThumbSize]};

			dojo.style(scrollPanel.domNode, "display", display);
		},

		vUpdateThumbFromContentScroll: function () {
			var scrollPanel = this;
			scrollPanel.vnow = Math.min(scrollPanel.vTrackSize -
			                            scrollPanel.vThumbSize, Math.max(0, scrollPanel.viewport.scrollTop /
			                                                                scrollPanel.vScrollRatio));
			dojo.style(scrollPanel.vThumb, 'top', [scrollPanel.vnow, "px"].join(""));
		},

		vUpdateContentFromThumbPosition: function () {
			var scrollPanel = this;
			scrollPanel.viewport.scrollTop = scrollPanel.vnow * scrollPanel.vScrollRatio;
		},

		onAfterScrollPanelInit: function (scrollPanel) {},

		onBeforeScrollPanelInit: function (scrollPanel) {},

		onMove: function (moveLimit, leftTop) {
			var scrollPanel = this;
			scrollPanel.vnow = leftTop.t;
			scrollPanel.vUpdateContentFromThumbPosition()
		},

		onMoveStop: function (mover) {

		}
	})

	return tui.widget.ScrollPanel;
})