define('tui/widget/Editorialresortinfoslider', [
    'dojo',
    'dojo/query',
    'tui/widget/_TuiBaseWidget',
    'tui/widgetFx/Transitiable',
    'dojox/dtl',
    'dojo/cache',
    'dojox/dtl/Context',
    'dojox/dtl/tag/logic',
    'tui/widgetFx/Slide'
], function (dojo) {

    dojo.declare('tui.widget.Editorialresortinfoslider', [tui.widget._TuiBaseWidget, tui.widgetFx.Transitiable], {
        // summary:
        //        A class which defines the carousel behaviours.
        // description:
        //      A class which defines the carousel behaviours. The sliding fx
        //      is encapsulated in the tui/widgetFx/Slide class.

        // ---------------------------------------------------------------- carousel properties.

        tmplPath: '',

        tmpl: null,

        // Default transition type for widget.
        transitionType: 'Slide',

        // Options for slide transition.
        transitionOptions: {
            duration: 1000
        },

        slideSelector: 'ul.plist',

        itemSelector: '> li',

        jsonDataPostLoaded: false,

        displayPagination: false,

        pageAmount: 1,

        controlTmpl: null,

        dynamicHeight: true,

        controlsInViewport: true,

        // ---------------------------------------------------------------- carousel methods.

        postCreate: function () {
            // summary:
            //      Sets up widget data, once created and add to page DOM
            // description:
            //      After the widget is added to DOM. We add the default transition
            //      to the carousel.
            //      This uses information set on the transitionOptions object.
            var carousel = this;
            carousel.inherited(arguments);
            var display = dojo.style(carousel.domNode, 'display');
            dojo.style(carousel.domNode, 'display', 'block');

            carousel.setupTransition();

            carousel.addNewContent();

            dojo.style(carousel.domNode, 'display', display);

            carousel.tagCarousel();
        },

        setupTransition: function () {
            var carousel = this;
            carousel.onBeforeSetupTransition();
            dojo.mixin(carousel.transitionOptions, {
                domNode: carousel.domNode,
                slideSelector: carousel.slideSelector,
                itemSelector: carousel.itemSelector,
                displayPagination: carousel.displayPagination,
                pageAmount: carousel.pageAmount,
                controlTmpl: carousel.controlTmpl,
                dynamicHeight: carousel.dynamicHeight,
                controlsInViewport: carousel.controlsInViewport
            });
            carousel.transitionOptions.jsonData = null;
            if (carousel.jsonData) {
                carousel.transitionOptions.jsonData = carousel.getCarouselData();
            }
            carousel.transition = carousel.addTransition();
            dojo.connect(carousel.transition, 'onPage', function (index, element, slide, direction) {
                carousel.onPage(index, element, slide, direction);
            });
            dojo.connect(carousel.transition, 'onAfterSlide', function (slide, direction) {
                carousel.onAfterSlide(slide, direction);
            });
        },

        tagCarousel: function () {
            var carousel = this;
            // Tag li elements.
            //carousel.tagElements(dojo.query('ul.plist li', carousel.domNode), function (DOMElement) {
            //    return DOMElement.textContent || DOMElement.innerText;
            //});
            
            _.each(dojo.query('ul.plist li', carousel.domNode), function(listNode){
                var linkNode = dojo.query('h4 a', listNode);
                if(!linkNode.length) return;
                carousel.tagElement(listNode, linkNode[0].innerHTML);
            });
            
            
            // Tag next/prev arrows.
            carousel.tagElements(dojo.query('a.prev', carousel.domNode), 'leftNav');
            carousel.tagElements(dojo.query('a.next', carousel.domNode), 'rightNav');
            if (carousel.displayPagination) {
                // Tag paging bullets (must come first).
                var paging = 0;
                carousel.tagElements(dojo.query('ul.paging li', carousel.domNode), function (DOMElement) {
                    return 'paging' + paging++;
                });
            }
        },

        getCarouselData: function () {
            var carousel = this;
            return carousel.jsonData;
        },

        getTransition: function () {
            // summary:
            //      Getter method from transition object
            // description:
            //      Method which returns the current transition object
            //      assigned to the carousel widget.

            var carousel = this;
            return carousel.transition;
        },

        showPagination: function () {
            // summary:
            //      Shows the carousel pagination
            // description:
            //      Shows the carousel pagination created from the transition class.
            //      tui/widgetFx/Slide

            var carousel = this;
            if (carousel.transition.showPagination) {
                carousel.transition.showPagination();
            }
        },

        hidePagination: function () {
            // summary:
            //      Hides the carousel pagination
            // description:
            //      Hides the carousel pagination created from the transition class.
            //      tui/widgetFx/Slide
            var carousel = this;
            if (carousel.transition.hidePagination) {
                carousel.transition.hidePagination();
            }
        },

        createTmpl: function (context) {
            var carousel = this;
            context = new dojox.dtl.Context(context || carousel);
            var template = new dojox.dtl.Template(carousel.tmpl);
            return template.render(context);
        },

        // no-ops

        addNewContent: function () {},

        onPage: function (index, element, slide, direction) {},

        onBeforeSetupTransition: function () {},

        onAfterSlide: function (slide, direction) {}
    });

    return tui.widget.Editorialresortinfoslider;
});
