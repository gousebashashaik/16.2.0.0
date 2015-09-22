define('tui/villaAvailability/view/DynamicCarousel', [
  'dojo',
  'dojo/query',
  'dojo/dom-geometry',
  'dojo/dom-style',
  'dojo/dom-attr',
  'tui/widget/_TuiBaseWidget',
  'tui/widgetFx/Transitiable',
  'tui/villaAvailability/view/VillaAvailabilitySlide'
], function (dojo, query, domGeom, domStyle, domAttr) {

  dojo.declare('tui.villaAvailability.view.DynamicCarousel', [tui.widget._TuiBaseWidget, tui.widgetFx.Transitiable], {

    //transition: null,

    // Default transition type for widget
    transitionType: 'Slide',

    // Options for slide transition.
    transitionOptions: {
      duration: 300
    },

    slideSelector: 'ul.plist',

    itemSelector: '> li',

    jsonDataPostLoaded: false,

    displayPagination: true,

    pageAmount: null,

    controlTmpl: null,

    dynamicHeight: true,

    controlsInViewport: true,

    slideGroups: null,

    slideGroupLabels: null,

    postCreate: function () {
      var dynamicCarousel = this;
      dynamicCarousel.inherited(arguments);

      dynamicCarousel.setupTransition();

      dynamicCarousel.tagCarousel();
    },

    setupTransition: function () {
      var dynamicCarousel = this;
      dynamicCarousel.onBeforeSetupTransition();
      dojo.mixin(dynamicCarousel.transitionOptions, {
        domNode: dynamicCarousel.domNode,
        slideSelector: dynamicCarousel.slideSelector,
        itemSelector: dynamicCarousel.itemSelector,
        displayPagination: dynamicCarousel.displayPagination,
        pageAmount: dynamicCarousel.pageAmount,
        controlTmpl: dynamicCarousel.controlTmpl,
        dynamicHeight: dynamicCarousel.dynamicHeight,
        controlsInViewport: dynamicCarousel.controlsInViewport
      });
      dynamicCarousel.transitionOptions.jsonData = null;
      if (dynamicCarousel.jsonData) {
        dynamicCarousel.transitionOptions.jsonData = dynamicCarousel.getCarouselData();
      }
      dynamicCarousel.transition = dynamicCarousel.addTransition();
      dynamicCarousel.onAfterSetupTransition();
      dojo.connect(dynamicCarousel.transition, 'onPage', function (index, element, slide, direction) {
        dynamicCarousel.onPage(index, element, slide, direction);
      });
      dojo.connect(dynamicCarousel.transition, 'onAfterSlide', function (slide, direction) {
        dynamicCarousel.onAfterSlide(slide, direction);
      });
    },

    tagCarousel: function () {
      var dynamicCarousel = this;
      // Tag li elements.
      dynamicCarousel.tagElements(dojo.query('.list-item', dynamicCarousel.domNode), function (DOMElement) {
        return DOMElement.textContent || DOMElement.innerText;
      });
      // Tag next/prev arrows.
      dynamicCarousel.tagElements(dojo.query('a.prev', dynamicCarousel.domNode), 'prev');
      dynamicCarousel.tagElements(dojo.query('a.next', dynamicCarousel.domNode), 'next');
      if (dynamicCarousel.displayPagination) {
        // Tag paging bullets (must come first).
        var paging = 0;
        dynamicCarousel.tagElements(dojo.query('ul.paging li', dynamicCarousel.domNode), function (DOMElement) {
          return 'paging' + paging++;
        });
      }
    },

    getCarouselData: function () {
      var dynamicCarousel = this;
      return dynamicCarousel.jsonData;
    },

    getTransition: function () {
      // summary:
      //      Getter method from transition object
      // description:
      //      Method which returns the current transition object
      //      assigned to the dynamicCarousel widget.

      var dynamicCarousel = this;
      return dynamicCarousel.transition;
    },

    showPagination: function () {
      // summary:
      //      Shows the dynamicCarousel pagination
      // description:
      //      Shows the dynamicCarousel pagination created from the transition class.
      //      tui/widgetFx/Slide

      var dynamicCarousel = this;
      if (dynamicCarousel.transition.showPagination) {
        dynamicCarousel.transition.showPagination();
      }
    },

    hidePagination: function () {
      // summary:
      //      Hides the dynamicCarousel pagination
      // description:
      //      Hides the dynamicCarousel pagination created from the transition class.
      //      tui/widgetFx/Slide
      var dynamicCarousel = this;
      if (dynamicCarousel.transition.hidePagination) {
        dynamicCarousel.transition.hidePagination();
      }
    },

    onPage: function (index, element, slide) {
    },

    onAfterSlide: function () {
      var dynamicCarousel = this;
      dynamicCarousel.updateLabelPos();
    },

    updateLabelPos: function () {
      var dynamicCarousel = this,
          viewPortBox = domGeom.position(dynamicCarousel.transition.viewport),
          leftEdge = viewPortBox.x + 1, // box is off by 1 pixel
          rightEdge = viewPortBox.x + viewPortBox.w;
      _.each(dynamicCarousel.slideGroups, function (group) {
        var groupBox = domGeom.position(group),
            itemBox = domGeom.position(query('.list-item', group)[0]),
            label = query('.list-group-label p', group)[0];

        // clear styles
        domAttr.remove(label, 'style');

        // only one item in group, leave positioning alone
        if (groupBox.w === itemBox.w) return;

        if (isVisible(groupBox, leftEdge, rightEdge)) {
          if (group.offsetLeft + groupBox.w > viewPortBox.w) {
            // overlaps on right
            domStyle.set(label, {
              'position': 'absolute',
              'left': 0,
              'width': (viewPortBox.x + viewPortBox.w) - groupBox.x + "px"
            });
          } else if (group.offsetParent.offsetLeft + group.offsetLeft <= 1) {
            // overlaps on left
            domStyle.set(label, {
              'position': 'absolute',
              'right': 0,
              'width': (groupBox.x + groupBox.w) - viewPortBox.x + "px"
            })
          }
        }
      });
    },

    onBeforeSetupTransition: function () {
    },

    onAfterSetupTransition: function () {
      var dynamicCarousel = this;
      dynamicCarousel.slideGroups = query('.list-group', dynamicCarousel.transition.slideContainer);
      dynamicCarousel.slideGroupLabels = query('.list-group-label', dynamicCarousel.transition.slideContainer);
      dynamicCarousel.updateLabelPos();
    },

    addTransition: function(){
      var transitiable = this;
        return new tui.villaAvailability.view.VillaAvailabilitySlide(transitiable.transitionOptions);
    }
  });

  function isVisible(groupBox, leftEdge, rightEdge) {
    var gLeftX = groupBox.x;
    var gRightX = groupBox.x + groupBox.w;
    return (gLeftX < rightEdge) && (gRightX > leftEdge);
  }

  return tui.villaAvailability.view.DynamicCarousel;
});
