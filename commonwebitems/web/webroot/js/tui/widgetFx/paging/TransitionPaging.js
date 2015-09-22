define("tui/widgetFx/paging/TransitionPaging", [
    "dojo",
    "dojo/text!tui/widgetFx/paging/templates/carouselControlTmpl.html",
    "tui/widget/mixins/Templatable",
    "tui/widgetFx/Transition",
    "tui/widgetFx/paging/Paging"
], function (dojo, controlTmpl) {

    function getMaxHeight(items) {
      var heights = [];
      _.each(items, function(item){
        heights.push(dojo.style(item, "height"));
      });
      return _.max(heights);
    }

    dojo.declare("tui.widgetFx.paging.TransitionPaging", [tui.widgetFx.Transition, tui.widgetFx.paging.Paging, tui.widget.mixins.Templatable], {

        viewportSelector: ".viewport",

        viewport: null,

        slideSelector: null,

        slideContainer: null,

        itemSelector: "> li",

        slideItemElements: null,

        viewportWidth: 0,

        itemWidth: 0,

        itemShow: 0,

        jsonData: null,

        actualItemWidth: null,

        controlTmpl: null,

        dynamicHeight: true,

        controlsInViewport: true,

        beforeInit: function () {
            var transitionPaging = this, height = null;

            transitionPaging.controls = [];

            transitionPaging.slideContainer = dojo.query(transitionPaging.slideSelector, transitionPaging.domNode)[0];
            transitionPaging.viewport = dojo.query(transitionPaging.viewportSelector, transitionPaging.domNode)[0] || transitionPaging.domNode;
            transitionPaging.viewportWidth = dojo.getContentBox(transitionPaging.viewport).w;

            dojo.style(transitionPaging.slideContainer, "position", "absolute");

            transitionPaging.getSlideElements();
            transitionPaging.setItemsWidth();
            transitionPaging.setViewportHeight(getMaxHeight(transitionPaging.slideItemElements));
            transitionPaging.renderControls();

            transitionPaging.calcPages();

            // inherited from tui.widgetFx.paging.Paging
            transitionPaging.renderPagination();
        },

        getSlideElements: function () {
          var transitionPaging = this, control;
          transitionPaging.slideItemElements = dojo.query(transitionPaging.itemSelector, transitionPaging.slideContainer);
          control = transitionPaging.slideItemElements[0];

          transitionPaging.calcSlideItems(control);
        },

        setItemsWidth: function () {
          var transitionPaging = this;
          _.each(transitionPaging.slideItemElements, function (item) {
            dojo.style(item, "width", transitionPaging.actualItemWidth + "px");
          });
        },

        calcPages: function () {
            // calculates number of pages in carousel
            var transitionPaging = this,
                len = (transitionPaging.jsonData) ? transitionPaging.jsonData.length : transitionPaging.slideItemElements.length;

            transitionPaging.page = transitionPaging.pageAmount
                    ? Math.ceil((len - transitionPaging.itemShow) / transitionPaging.pageAmount) + 1
                    : Math.ceil(len / transitionPaging.itemShow);
        },

        calcSlideItems: function (controlItem) {
            // calculates number of items shown on one carousel 'page'
            // and item width based on number of items that fit in viewport
            var transitionPaging = this;
            transitionPaging.itemWidth = dojo.getContentBox(controlItem).w;
            transitionPaging.itemShow = Math.floor(transitionPaging.viewportWidth / transitionPaging.itemWidth);
            transitionPaging.actualItemWidth = Math.ceil(transitionPaging.viewportWidth / transitionPaging.itemShow);
        },

        setViewportHeight: function (height) {
            // sets height of viewport if dynamically calculated
            // set dynamicHeight to false if pre-calculated in css
            var transitionPaging = this;
            transitionPaging.dynamicHeight ? dojo.style(transitionPaging.viewport, "height", height + "px") : null;
        },

        renderControls: function () {
            // summary:
            //		Methods which renders slide controls.
            // description:
            //		Creates and renders the next and previous controls which handles the sliding.
            var transitionPaging = this,
                tmpl = transitionPaging.controlTmpl ? transitionPaging.controlTmpl : controlTmpl,
                placeNode = transitionPaging.controlsInViewport ? transitionPaging.viewport : transitionPaging.domNode;

            var prevControl = dojo.place(transitionPaging.renderTmpl(tmpl, {
                controlClass: 'prev',
                controlText: 'previous'
            }), placeNode, "first");

            var nextControl = dojo.place(transitionPaging.renderTmpl(tmpl, {
                controlClass: 'next',
                controlText: 'next'
            }), placeNode, "last");

            transitionPaging.controls.push(prevControl);
            transitionPaging.controls.push(nextControl);
            /*transitionPaging.controls.push(
                    dojo.create("a", {
                        href: "#",
                        innerHTML: "<span class='text'>previous</span><span class='arrow'></span>",
                        className: "prev"
                    }, transitionPaging.viewport, "first"))
            transitionPaging.controls.push(
                    dojo.create("a", {
                        href: "#",
                        innerHTML: "<span class='text'>next</span><span class='arrow'></span>",
                        className: "next"
                    }, transitionPaging.controls[0], "after"))*/
        },

        attachTransitionEventListeners: function () {
            var transitionPaging = this;
            transitionPaging.inherited(arguments);
            _.forEach(transitionPaging.controls, function (element, index) {
                dojo.connect(element, "onclick", function (event) {
                    dojo.stopEvent(event);
                        if(dojo.hasClass(element, 'disable')) return;
                    transitionPaging.widgetTransition(transitionPaging, element);
                })
            })
        }
    });

    return tui.widgetFx.paging.TransitionPaging;
});