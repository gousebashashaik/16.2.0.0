define('tui/widgetFx/Slide', [
  'dojo',
  'dojo/_base/fx',
  'tui/widgetFx/paging/TransitionPaging'
], function(dojo, baseFx) {

  dojo.declare('tui.widgetFx.Slide', [tui.widgetFx.paging.TransitionPaging], {

    parentNode: null,

    // amount to move in pixels.
    moveAmount: 0,

    pageAmount: null,

    dynamicHeight: true,

    controlsInViewport: true,

    // ---------------------------------------------------------------- transition methods

    beforeInit: function() {
      // summary:
      //		Override the beforeInit method defined in the transition class.
      // description:
      //		This method is called before the slide class is init.
      // 		Which works out the height of viewport, container width, and the amount to slide.
      // 		It also renders the paging and next and previous controls.
      var slide = this;
      slide.inherited(arguments);
      slide.calcMoveAmount();
      slide.resizeContainer();
      slide.toggleDisableControls();
    },

    calcMoveAmount: function () {
        var slide = this;
        slide.moveAmount = slide.pageAmount ? (slide.itemWidth * slide.pageAmount) : (slide.itemWidth * slide.itemShow);
    },

    resizeContainer: function () {
        var slide = this;
        var slideContainerWidth = slide.slideItemElements.length * slide.actualItemWidth;
        dojo.style(slide.slideContainer, 'width', slideContainerWidth + 'px');
    },

    // ---------------------------------------------------------------- slide methods

    widgetTransition: function(transition, element) {
      var slide = this;
      if (slide.animating) return;
      slide.inherited(arguments);
      var index = _.indexOf(slide.controls, element);
      if (index !== -1) {
        var direction = (index === 0) ? +slide.moveAmount : -slide.moveAmount;
        var dir = (index === 0) ? 1 : -1;
        var containerLeft = dojo.style(slide.slideContainer, 'left');
        var move = containerLeft + direction;
        if (!slide.isLimit(index)) {
          slide.onPage(Math.abs(move / slide.moveAmount), null, slide, dir);
        }
      }
    },

    onPage: function(index, element, slide, direction) {
      var slide = this;
      slide.inherited(arguments);
      var move = index * slide.moveAmount;
      move = (move > 0) ? -move : move;
      slide.widgetSlide(move, direction);
    },

    isLimit: function(direction) {
      var slide = this;
      return (direction === 1) ? (slide.currentPage === slide.page) : (slide.currentPage === 1);
    },

    toggleDisableControls: function() {
      var slide = this;
      _.forEach(slide.controls, function(element, index) {
        var prop = (slide.isLimit(index)) ? 'addClass' : 'removeClass';
        dojo[prop](element, 'disable');
      });
    },

    widgetSlide: function(move, direction) {
      var slide = this;
      var config = {
        node: slide.slideContainer,
        left: move,
        unit: 'px',
        duration: slide.duration || 300,
        onBegin: function() {
          slide.animating = true;

          // HACK: To get fix safari/mac bug where carousel paging animation is sticky/distorted by google maps instance
          if (dojo.isSafari) {
            dojo.query('.image-container', slide.slideContainer).style('position', 'static');
            dojo.query('.diff', slide.slideContainer).style('display', 'none');
          }
        },
        onEnd: function() {
          slide.animating = false;
          slide.toggleDisableControls();
          slide.onWidgetTransitionEnd(slide, direction);

          if (dojo.isSafari) {
            dojo.query('.image-container', slide.slideContainer).style('position', 'relative');
            dojo.query('.diff', slide.slideContainer).style('display', 'block');
          }
        }
      };
      dojo.fx.slideTo(config).play();
    },

    onWidgetTransitionEnd: function(slide, direction) {
      var slide = this;
      slide.onAfterSlide(slide, direction);
    },

    onAfterSlide: function(slide) {}
  });

  return tui.widgetFx.Slide;
});
