define('tui/widget/carousels/CSSCarousel', [
  'dojo/_base/declare',
  'dojo/_base/event',
  'dojo/query',
  'dojo/on',
  'dojo/aspect',
  'dojo/topic',
  'dojo/dom-construct',
  'dojo/dom-class',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/dom-geometry',
  'tui/utils/DetectFeature',
  'dojo/text!tui/widgetFx/paging/templates/carouselControlTmpl.html',
  'dojo/text!tui/widgetFx/paging/templates/carouselPagingTmpl.html',
  'tui/widget/mixins/Templatable',
  'tui/widget/_TuiBaseWidget'
], function (declare, event, query, on, aspect, topic, domConstruct, domClass, domAttr, domStyle, domGeom, detectFeature, controlTmpl, pagingTmpl) {

  function getMaxHeight(items, vp) {
    var heights = [],
        defDisplay = domStyle.get(vp, 'display');

    domStyle.set(vp, {
      display: 'block',
      visibility: 'hidden'
    });

    _.each(items, function(item){
      heights.push(domStyle.get(item, "height"));
    });

    domStyle.set(vp, {
      display: defDisplay,
      visibility: ''
    });
    return _.max(heights);
  }

  return declare('tui.widget.carousels.CSSCarousel', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

    tmpl: null,

    controlTmpl: controlTmpl,

    controls: null,

    controlSelector: '.js-carousel-control',

    pagingTmpl: pagingTmpl,

    pagination: null,

    paginationSelector: '.js-carousel-paginator',

    currentPage: 1,

    pages: 0,

    pageWidth: 0,

    maxSlidesPerPage: 0,

    viewport: null,

    viewportSelector: '.viewport',

    slides: null,

    slideSelector: '.plist > li',

    slideHeight: 0,

    slideWidth:0,

    slidePadding: 0,

    animateNodeSelector: '.plist',

    animateNode: null,

    placeControls: '.viewport',

    placePagination: null,

    evtListeners: null,

    postCreate: function () {
      var carousel = this;
      carousel.inherited(arguments);
      carousel.onBeforeInit();
      carousel.initCarousel();
      carousel.onAfterInit();
    },

    initCarousel: function () {
      var carousel = this;
      carousel.evtListeners = {};

      carousel.viewport = query(carousel.viewportSelector, carousel.domNode)[0];
      carousel.animateNode = query(carousel.animateNodeSelector, carousel.domNode)[0];

      // return early if no slides present
      if(!carousel.initSlides()) {
        _.debug('Error initialising carousel: '+ carousel.declaredClass + '. No carousel elements!', true);
        return;
      }

      carousel.calcPages();
      carousel.initViewport();

      // don't draw pagination and controls if only one page
      if(carousel.pages > 1) {
        carousel.initControls();
        carousel.initPagination();
      }
    },

    initSlides: function () {
      var carousel = this;
      carousel.slides = query(carousel.slideSelector, carousel.domNode);
      carousel.slideWidth = carousel.getWidth(carousel.slides[0]);

      return carousel.slides.length > 0;
    },

    initViewport: function () {
      var carousel = this, height, setWidth,
          vp = carousel.viewport,
          itemWidth = carousel.slideWidth;

      height = (carousel.slideHeight > 0) ? carousel.slideHeight : getMaxHeight(carousel.slides, vp);
      setWidth = itemWidth * carousel.maxSlidesPerPage;

      domStyle.set(vp, {
        height: _.pixels(height + carousel.slidePadding),
        width: _.pixels(setWidth)
      });

      domStyle.set(carousel.animateNode, 'width', _.pixels(itemWidth * carousel.slides.length));
      carousel.onAfterInitViewport();
    },

    initControls: function () {
      var carousel = this;
      carousel.renderControls();
      carousel.controls = query(carousel.controlSelector, carousel.domNode);
      carousel.attachControlListeners();
    },

    renderControls: function () {
      var carousel = this, lc, rc,
          placeNode = carousel.placeControls ? query(carousel.placeControls, carousel.domNode)[0] : carousel.domNode;
      var leftControl = carousel.renderTmpl(carousel.controlTmpl, {
        controlClass: 'prev',
        controlText: 'previous'
      });
      var rightControl = carousel.renderTmpl(carousel.controlTmpl, {
        controlClass: 'next',
        controlText: 'next'
      });
      domConstruct.place(leftControl, placeNode, 'last');
      domConstruct.place(rightControl, placeNode, 'last');
    },

    attachControlListeners: function () {
      var carousel = this;
      carousel.evtListeners['controls'] = [];
      _.each(carousel.controls, function(control, i){
        carousel.evtListeners['controls'].push(on(control, 'click', function(){
          if(!carousel.isLimit(i)){
            carousel.onControl(control, i);
          }
        }));
        carousel.tagElement(control, (i === 0) ? 'leftNav' : 'rightNav');
      });
    },

    initPagination: function () {
      var carousel = this;
      carousel.renderPagination();
      carousel.pagination = query(carousel.paginationSelector, carousel.domNode);
      carousel.attachPaginationListeners();
    },

    renderPagination: function () {
      var carousel = this,
          placeNode = carousel.placePagination ? query(carousel.placePagination, carousel.domNode)[0] : carousel.domNode,
          html = carousel.renderTmpl(carousel.pagingTmpl, {
            id: carousel.id + '_paging',
            pages: _.range(carousel.pages)
          });
      domConstruct.place(html, placeNode, 'last');
    },

    attachPaginationListeners: function () {
      var carousel = this;
      carousel.evtListeners['pagination'] = [];
      _.each(carousel.pagination, function(paginator, i){
        carousel.evtListeners['pagination'].push(on(paginator, 'click', function () {
          carousel.onPaginate(paginator, i + 1);
        }));
        carousel.tagElement(paginator, 'paging' + i);
      });
    },

    onPage: function (elem, pageNum) {
      var carousel = this,
          move = (carousel.pageWidth * (pageNum - 1) * -1);
      //domClass.add(carousel.domNode, 'avoid-clicks');
      carousel.onBeforePage();
      carousel.currentPage = pageNum;
      domStyle.set(carousel.animateNode, 'marginLeft', _.pixels(move));
      carousel.onAfterPage();
    },

    onPaginate: function (elem, pageNum) {
      var carousel = this;
      carousel.onPage(elem, pageNum);
      carousel.toggleControls();
      carousel.toggleActivePaginator();
    },

    onControl: function (elem, dir) {
      var carousel = this, action;
      var moveToPage = (dir === 0) ? carousel.currentPage - 1 : carousel.currentPage + 1;
      carousel.onPage(elem, moveToPage);
      carousel.toggleControls();
      carousel.toggleActivePaginator();
    },

    toggleActivePaginator: function () {
      var carousel = this;
      _.each(carousel.pagination, function(paginator, i){
        domClass.remove(paginator, 'active');
        if(i === carousel.currentPage - 1) {
          domClass.add(paginator, 'active');
        }
      });
    },

    toggleControls: function () {
      var carousel = this;
      _.each(carousel.controls, function(elem, i){
        carousel.enableControl(elem);
        if (i === 0 && carousel.currentPage === 1) {
            carousel.disableControl(elem);
        } else if (i === 1 && carousel.currentPage === carousel.pages) {
          carousel.disableControl(elem);
        }
      });
    },

    disableControl: function (elem) {
      domClass.add(elem, 'disable');
    },

    enableControl: function (elem) {
      domClass.remove(elem, 'disable');
    },

    isLimit: function (dir) {
      var carousel = this;
      return (dir === 1) ? (carousel.currentPage === carousel.pages) : (carousel.currentPage === 1);
    },

    calcPages: function () {
      var carousel = this,
          width = carousel.getWidth(carousel.slides[0]),
          maxWidth = carousel.getWidth(carousel.domNode);

      carousel.maxSlidesPerPage = (carousel.maxSlidesPerPage > 0) ? carousel.maxSlidesPerPage : Math.floor(maxWidth / width);
      carousel.pages = Math.ceil(carousel.slides.length / carousel.maxSlidesPerPage);
      carousel.pageWidth = (carousel.slideWidth * carousel.maxSlidesPerPage);
    },

    getWidth: function (elem) {
      return domGeom.getContentBox(elem).w;
    },

    /**
     * no-op
     * fired after viewport is initialised
     */
    onAfterInitViewport: function () {},

    /**
     * no-op
     * fired before carousel is initialised
     */
    onBeforeInit: function () {},

    /**
     * no-op
     * fired after carousel is initialised
     */
    onAfterInit: function () {},

    /**
     * no-op
     * fired before page is transitioned
     */
    onBeforePage: function () {},

    /**
     * no-op
     * fired after page is transitioned
     */
    onAfterPage: function () {}

  });
});