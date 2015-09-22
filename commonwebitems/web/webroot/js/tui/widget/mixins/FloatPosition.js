// # FloatPosition
// ## Mixin module
//
// Positions a DOM Node absolutely relative to specified node
// Can specify if element should be positioned above/below/left/right etc...
// This is a mixin class, it must be extended.
//
// @author: Maurice Morgan.

define("tui/widget/mixins/FloatPosition", ["dojo", "dojo/has", "dojo/_base/window", "dojo/window"], function (dojo, has, baseWin) {

  dojo.declare("tui.widget.mixins.FloatPosition", null, {

    // ----------------------------------------------------------------------------- properties

    /**
     * ### posOffset
     * Offset applied to target positioning. Position is calculated relative to specified node,
     * this allows us to add "padding" around specified node.
     * @type {Object}
     */
    posOffset: {top: 0, left: 0},

    /**
     * ### elementRelativeTo
     * DOM node target will be positioned relative to
     * @type {Node}
     */
    elementRelativeTo: null,

    /**
     * ### floatWhere
     * Specify if target will be positioned above/below/left/right of specified node
     * @type {String}
     */
    floatWhere: "position-bottom-left",

    /**
     * ### includeScroll
     * Include document's scroll position in calculations. For example, modals will be positioned centre to the viewport by default
     * If user has scrolled down the page, the modal would be outside the visible area if `includeScroll` is false
     * @type {Boolean}
     */
    includeScroll: false,

    // ----------------------------------------------------------------------------- methods

    /**
     * ### posElement()
     * Calculate positioning and apply top/left style attributes to target node
     * @param {Node} elementRelativeTo  Positioning will be calulated relative to this DOM Node. If `elementToFloat` is not specified
     * will fallback to caller's domNode or caller's `elementRelativeTo` attribute.
     * @param {Node?} elementToFloat  DOM Node positioning will be applied to. If not specified will use first argument
     * @param {String} where  Specify where element will be positioned, available options are:
     * `position-top-center`, `position-top-left`, `position-top-right`, `position-bottom-center`, `position-bottom-left`
     * , `position-bottom-right`, `position-center`, `position-center-top`, `position-over`, `position-right-center`
     */
    posElement: function (elementRelativeTo, elementToFloat, where) {
      var floatPosition = this;
      // Return early if floatWhere attribute has not been specified
      if (!floatPosition.floatWhere) return;
      switch (arguments.length) {
        case 1:
          // if 1 param specified, assume target DOM Node `elementToFloat`
          elementToFloat = elementRelativeTo;
          // define `elementRelativeTo` as caller's `domNode` or `elementRelativeTo` attribute
          elementRelativeTo = floatPosition.elementRelativeTo || floatPosition.domNode;
          break;
        case 2:
          // only 2 params specified
          if (typeof arguments[1] === "string") {
            // assume 2nd param is `where`
            where = elementToFloat;
            // first is target DOM Node `elementToFloat`
            elementToFloat = elementRelativeTo;
            // define `elementRelativeTo` as caller's `domNode` or `elementRelativeTo` attribute
            elementRelativeTo = floatPosition.elementRelativeTo || floatPosition.domNode;
          }
          break;
      }
      // Where to position the target relative to the source
      floatPosition.floatWhere = where || floatPosition.floatWhere;
      // Cache current display style
      var display = dojo.style(elementToFloat, "display");
      // Set to display block for positioning calculations
      dojo.style(elementToFloat, "display", "block");

      var position ;
      if (elementRelativeTo != undefined) {
			try {
				position = dojo.position(elementRelativeTo, true);
			} catch (err) {
				position = {"h" : "0","w" : "0","x" : "0","y" : "0"};
			}
      } else {
    	  position = null;
	  }

      var subjectPosition = dojo.position(elementToFloat, true);
      // revert original display style
      dojo.style(elementToFloat, "display", display);

      // `dojo.window.getBox()` is innacurate on iOS so fallback to innerWidth/innerHeight
      var windowBox = (has("agent-ios")) ? {w: window.innerWidth, h: window.innerHeight} : dojo.window.getBox();

      floatPosition.onBeforePosition(subjectPosition, position, windowBox, elementToFloat, elementRelativeTo, floatPosition);

      var scrollTop = 0, scrollLeft = 0;

      // include scroll Offset in positioing calculations
      if (floatPosition.includeScroll) {
        scrollLeft = (window.pageXOffset !== undefined) ? window.pageXOffset :
            (document.documentElement || document.body.parentNode || document.body).scrollLeft;
        scrollTop = (window.pageYOffset !== undefined) ? window.pageYOffset :
            (document.documentElement || document.body.parentNode || document.body).scrollTop;
      }

      // calculate and set position as specified
      switch (floatPosition.floatWhere) {

        case "position-top-center":
          dojo.setStyle(elementToFloat, {
            "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
            "left": (position.x + position.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-top-left":
          dojo.setStyle(elementToFloat, {
            "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
            "left": position.x + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-top-right":
          dojo.setStyle(elementToFloat, {
            "top": (position.y - subjectPosition.h) + floatPosition.posOffset.top + "px",
            "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-bottom-left":
          dojo.setStyle(elementToFloat, {
            "top": position.y + position.h + floatPosition.posOffset.top + "px",
            "left": position.x + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-bottom-right":
          dojo.setStyle(elementToFloat, {
            "top": position.y + position.h + floatPosition.posOffset.top + "px",
            "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-bottom-center":
          dojo.setStyle(elementToFloat, {
            "top": (position.y + position.h) + floatPosition.posOffset.top + "px",
            "left": (position.x + position.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-center":
          dojo.setStyle(elementToFloat, {
            "top": ((windowBox.h / 2) - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
            "left": ((windowBox.w / 2) - (subjectPosition.w / 2) + floatPosition.posOffset.left) + scrollLeft + "px"
          });
          break;
        case "position-center-top":
          dojo.setStyle(elementToFloat, {
            "top": ((windowBox.h / 2) - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px"
          });
          break;
        case "position-over":
          dojo.setStyle(elementToFloat, {
            "top": position.y + "px",
            "left": position.x + "px"
          });
          break;
        case "position-right-center":
          dojo.setStyle(elementToFloat, {
            "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
            "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
          });
          break;
        case "position-left-center":
            dojo.setStyle(elementToFloat, {
              "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
              "left": (position.x - subjectPosition.w ) + floatPosition.posOffset.left + "px"
            });
            break;
        case "position-right-right":
            dojo.setStyle(elementToFloat, {
              "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
              "left": (position.x + position.w) + floatPosition.posOffset.left + "px"
            });
            break;
	case "position-left-center-arrow-right":
            dojo.setStyle(elementToFloat, {
              "top": (position.y - (subjectPosition.h / 2) + floatPosition.posOffset.top) + scrollTop + "px",
              "left": (position.x - subjectPosition.w ) + floatPosition.posOffset.left + "px"
            });
            break;
      }

      floatPosition.onAfterPosition(subjectPosition, position, windowBox, floatPosition);
    },

    /**
     * ### onBeforePosition()
     * Event method fired prior to positioning being applied
     * @param {Object} subjectPosition Position object of target DOM Node
     * @param {Object} position Position object of relative DOM Node
     * @param {Object} windowBox Position object of window
     * @param {Node} elementToFloat Target DOM Node
     * @param {Node} elementRelativeTo Relative DOM Node
     * @param {Object} floatPosition Reference to extending class
     */
    onBeforePosition: function (subjectPosition, position, windowBox, elementToFloat, elementRelativeTo, floatPosition) {
    },

    /**
     * ### onAfterPosition()
     * Event method fired after positioning is applied
     * @param {Object} subjectPosition Position object of target DOM Node
     * @param {Object} position Position object of relative DOM Node
     * @param {Object} windowBox Position object of window
     * @param {Object} floatPosition Reference to extending class
     */
    onAfterPosition: function (subjectPosition, position, windowBox, floatPosition) {
    }
  });

  tui.widget.mixins.FloatPosition.TOP_CENTER = "position-top-center";
  tui.widget.mixins.FloatPosition.TOP_LEFT = "position-top-left";
  tui.widget.mixins.FloatPosition.TOP_RIGHT = "position-top-right";

  tui.widget.mixins.FloatPosition.BOTTOM_CENTER = "position-bottom-center";
  tui.widget.mixins.FloatPosition.BOTTOM_LEFT = "position-bottom-left";
  tui.widget.mixins.FloatPosition.BOTTOM_RIGHT = "position-bottom-right";

  tui.widget.mixins.FloatPosition.CENTER = "position-center";
  tui.widget.mixins.FloatPosition.OVER = "position-over";

  return tui.widget.mixins.FloatPosition;
});