define("tui/widget/ScrollHorizontal", ["dojo",
    "dojo/_base/window",
    "dojo/dom-style",
	"dojo/text!tui/widget/Templates/ScrollHorizontalTmpl.html",
	"tui/widgetFx/MoveableLimit",
	"tui/widgetFx/HorizontalMover",
	"tui/widget/_TuiBaseWidget",
	"tui/widget/mixins/ContentLoader"], function (dojo, win, domStyle, scrollPanelTmpl, moveableLimit, horizontalMover) {

	dojo.declare("tui.widget.ScrollHorizontal", [tui.widget._TuiBaseWidget, tui.widget.mixins.ContentLoader], {

		height: 0,

		wheel: 8,

		hhandle: null,

		hScroll: true,

		hTrack: null,

		maxThumbSize: 15,

		offset: 3,

		mover: null,

		hideThumb: false,

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
			scrollPanel.hTrack = dojo.query(".hz-track", scrollPanel.domNode)[0];
			scrollPanel.hThumb = dojo.query(".hz-track .hz-handle", scrollPanel.domNode)[0];
			scrollPanel.viewport = dojo.query(".viewport", scrollPanel.domNode)[0];

			var domNodePosition = dojo.position(scrollPanel.hTrack, true);

			scrollPanel.connect(scrollPanel.hTrack, "onclick", function (event) {
				dojo.stopEvent(event);
				var pos = event.clientX - domNodePosition.x;
				scrollPanel.updateScrollerFromPos(pos);
				dojo.publish("tui/widget/ScrollPanel/vtrack/onclick", [scrollPanel]);
			});

			scrollPanel.connect(scrollPanel.hThumb, "onclick", function (event) {
				dojo.stopEvent(event);
				dojo.publish("tui/widget/ScrollPanel/hThumb/onclick", [scrollPanel]);
			});

			scrollPanel.connect(scrollPanel.viewport, (!dojo.isMozilla ? "onmousewheel" : "DOMMouseScroll"), function (e) {
                dojo.stopEvent(e);
				// except the direction is REVERSED, and the event isn't normalized! one more line to normalize that:
				var scroll = e[(!dojo.isMozilla ? "wheelDelta" : "detail")] * (!dojo.isMozilla ? 1 : -1);
				scroll = (scroll > 0) ? 3 : -3;
				scrollPanel.viewport.scrollLeft -= scroll * scrollPanel.wheel;
				scrollPanel.hUpdateThumbFromContentScroll();
			});

      scrollPanel.connect(scrollPanel.viewport, "onscroll", function(e){
        scrollPanel.hUpdateThumbFromContentScroll();
        //dojo.place( "#", dojo.query(".page-title")[0], "last");
      });

			scrollPanel.update();

	      // introducing native scrollers for all dropdowns for touch devices
		  //  and hiding desktop custom scroller
	      if(dojo.hasClass(win.body(),"touch") && (dojo.hasClass(scrollPanel.domNode,"dropdownlist") || dojo.hasClass(scrollPanel.domNode,"autocomplete"))){
		       	domStyle.set(scrollPanel.hTrack,"display","none");
		        domStyle.set(scrollPanel.viewport, {
		     	    "marginRight": "0px",
		     	    "-webkit-overflow-scrolling": "touch",
		     	    "overflowX": "auto"
		     	  });
		       }

			scrollPanel.onAfterScrollPanelInit(scrollPanel);
		},

		updateScrollerPosition: function (dir, amount) {
			var scrollPanel = this;
			amount = amount || scrollPanel.viewport.offsetWidth;
			if (dir === 0) {
				scrollPanel.viewport.scrollLeft -= amount;
			} else {
				scrollPanel.viewport.scrollLeft += amount;
			}
			scrollPanel.hUpdateThumbFromContentScroll();
		},

		// maybe dep
		updateScrollerFromPos: function (pos, amount) {
			var scrollPanel = this;
			amount = amount || scrollPanel.viewport.offsetWidth;
			var hThumb = (dojo.style(scrollPanel.hThumb, "left"));
			if (pos > hThumb) {
				scrollPanel.viewport.scrollLeft += amount;
			} else {
				scrollPanel.viewport.scrollLeft -= amount;
			}
			scrollPanel.hUpdateThumbFromContentScroll();
		},

		moveElementInView: function (element) {
			var scrollPanel = this;
			if (!dojo.query(element, scrollPanel.domNode).length > 0) return;
			var pos = dojo.position(element, true);
			var inviewpos = element.offsetLeft - scrollPanel.viewport.scrollLeft;
			if (inviewpos < 0) {
				scrollPanel.viewport.scrollLeft -= 100;
				scrollPanel.hUpdateThumbFromContentScroll();
			} else if ((inviewpos + pos.w) > scrollPanel.domNode.offsetWidth) {
				scrollPanel.viewport.scrollLeft += 100;
				scrollPanel.hUpdateThumbFromContentScroll();
			}
		},

		update: function () {
			var scrollPanel = this;
			var display = dojo.style(scrollPanel.domNode, "display");
			dojo.style(scrollPanel.domNode, "display", "block");

			scrollPanel.width = parseInt(dojo.style(scrollPanel.domNode, "width"), 0);
			dojo.style(scrollPanel.viewport, "width", [scrollPanel.width, "px"].join(""));
			if (scrollPanel.topOnUpdate) {
				scrollPanel.viewport.scrollLeft = 0;
			}
			scrollPanel.hScroll = (scrollPanel.viewport.scrollLeft < scrollPanel.domNode.offsetWidth);

			// hide vertical track if we dont need, vertical scroll.
			var displaytrack = (scrollPanel.hScroll) ? "block" : "none";
			dojo.style(scrollPanel.hTrack, "display", displaytrack);
			if (!scrollPanel.hScroll) return;

			dojo.style(scrollPanel.hTrack, "width", [scrollPanel.width - (scrollPanel.offset), "px"].join(""));
			scrollPanel.hViewportSize = scrollPanel.viewport.offsetWidth;
			scrollPanel.hViewportScrollSize = scrollPanel.viewport.scrollWidth;
			scrollPanel.hViewportRatio = scrollPanel.hViewportSize / scrollPanel.hViewportScrollSize;

			// working out the thumb knob size.
			scrollPanel.hTrackSize = scrollPanel.hTrack.offsetWidth;
			scrollPanel.hThumbSize = Math.min(scrollPanel.hTrackSize, Math.max(scrollPanel.maxThumbSize, scrollPanel.hTrackSize *
			                                                                                             scrollPanel.hViewportRatio));
			dojo.style(scrollPanel.hThumb, 'width', [scrollPanel.hThumbSize, "px"].join(""));

			scrollPanel.hScrollRatio = scrollPanel.hViewportScrollSize / scrollPanel.hTrackSize;
			scrollPanel.hUpdateThumbFromContentScroll();
			scrollPanel.hUpdateContentFromThumbPosition();

			scrollPanel.mover = (scrollPanel.mover) ? scrollPanel.mover :
			                    new moveableLimit(scrollPanel.hThumb, {mover: horizontalMover, host: scrollPanel});

			scrollPanel.mover.limit = {x: [0, scrollPanel.hTrackSize - scrollPanel.hThumbSize]};

			dojo.style(scrollPanel.domNode, "display", display);
		},

		hUpdateThumbFromContentScroll: function () {
			var scrollPanel = this;
			if(scrollPanel.viewport.scrollLeft != 0){
				dojo.publish("tui/widget/ScrollPanel/deckPlan", scrollPanel);
			}
			scrollPanel.hnow = Math.min(scrollPanel.hTrackSize -
			                            scrollPanel.hThumbSize, Math.max(0, scrollPanel.viewport.scrollLeft /
			                                                                scrollPanel.hScrollRatio));
			//var str = "<span>" + parseInt(scrollPanel.hTrackSize) + " " + parseInt(scrollPanel.hThumbSize)
			//+ " " + parseInt(scrollPanel.viewport.scrollLeft) + " " + parseInt(scrollPanel.hScrollRatio) + "</span><span>... </span>"
			//dojo.place( str, dojo.query(".page-title")[0], "last");
			dojo.style(scrollPanel.hThumb, 'left', [scrollPanel.hnow, "px"].join(""));
		},

		hUpdateContentFromThumbPosition: function () {
			var scrollPanel = this;
			scrollPanel.viewport.scrollLeft = scrollPanel.hnow * scrollPanel.hScrollRatio;
		},

		onAfterScrollPanelInit: function (scrollPanel) {},

		onBeforeScrollPanelInit: function (scrollPanel) {},

		onMove: function (moveLimit, leftTop) {
			var scrollPanel = this;
			scrollPanel.hnow = leftTop.l;
			scrollPanel.hUpdateContentFromThumbPosition()
		},

		onMoveStop: function (mover) {

		}
	})

	return tui.widget.ScrollHorizontal;
})