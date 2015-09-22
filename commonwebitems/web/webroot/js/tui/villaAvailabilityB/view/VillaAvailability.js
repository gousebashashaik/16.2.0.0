define('tui/villaAvailabilityB/view/VillaAvailability', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/_base/connect',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/_base/xhr',
  'dojo/date',
  'dojo/_base/Deferred',
  'dojo/_base/lang',
  'dojo/store/Observable',
  'dojo/text!tui/villaAvailability/view/templates/villaAvailabilityTmpl.html',
  'dojo/text!tui/villaAvailability/view/templates/villaAvailabilityControlsTmpl.html',
  'tui/villaAvailability/view/DynamicCarousel',
  'tui/widget/form/SelectOption',
  'tui/villaAvailability/store/VillaAvailabilityStore',
  'tui/villaAvailabilityB/view/VillaItem',
  'tui/overlaySearchB/OverlaySearchModal',
  'tui/villaAvailability/nls/I18nable',
  'tui/widget/mixins/Templatable',
  'tui/widget/_TuiBaseWidget'
], function (dojo, query, on, connect,domAttr, domClass, xhr, date, Deferred, lang, Observable, tmpl, controlTmpl, Carousel, SelectOption, VillaStore, VillaItem) {

  dojo.declare('tui.villaAvailabilityB.view.VillaAvailability', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.villaAvailability.nls.I18nable], {

    /**
     * @param tmpl {string} represents django template file
     */
    tmpl: tmpl,

    data: null,

    /**
     * @param dateSelector {object} Reference to date select option widget
     */
    accommodationCode: null,
    /**
     * @param dateSelector {object} Reference to date select option widget
     */
    dateSelector: null,

    /**
     * @param adultSelector {object} Reference to num adults select option widget
     */
    adultSelector: null,

    /**
     * @param childSelector {object} Reference to num children select option widget
     */
    childSelector: null,

    /**
     * @param targetUrl {string} url for Ajax requests
     */
    targetUrl: dojoConfig.paths.webRoot + '/processAccomAvailabilityRequest',

    /**
     * @param searchParams {object} search request parameters for Ajax requests
     */
    searchParams: null,

    carousel: null,

    carouselNode: null,

    carouselData: null,

    carouselGroups: null,

    carouselItems: null,

    observer: null,

    overlaySearch: null,

    subscribableMethods: ['paginate'],

    /**
     * @param carouselOptions {object} options for carousel
     */
    carouselOptions: {
      displayPagination: false,
      pageAmount: 1,
      itemSelector: '.list-item',
      slideSelector: '.list',
      controlsInViewport: false,
      dynamicHeight: false,
      controlTmpl: controlTmpl
    },

    /**
     * Initialise class defaults
     */
    postCreate: function () {
      var villaAvailability = this;
      villaAvailability.inherited(arguments);
      // intialise i18n
      villaAvailability.initLocale();

      // intialise observer and variables
      villaAvailability.carouselData = new Observable(new VillaStore());
      villaAvailability.carouselGroups = [];
      villaAvailability.carouselItems = [];

      // initialise select option widgets
      villaAvailability.dateSelector = new SelectOption(null, query('.villa-availability-when', villaAvailability.domNode)[0]);
      villaAvailability.adultSelector = new SelectOption(null, query('.villa-availability-adult', villaAvailability.domNode)[0]);
      villaAvailability.childSelector = new SelectOption(null, query('.villa-availability-child', villaAvailability.domNode)[0]);

      villaAvailability.addEventListeners();
      // observe store
      villaAvailability.observeStore();
      // intialise carousel from data
      var deferred = new Deferred();
      deferred.then(function (promise) {
        villaAvailability.handleResults(promise);
      });
      deferred.resolve(villaAvailability.data);
    },

    /**
     * Paginate the Villa availability slider.
     */
    paginate: function(element, index) {
      var villaAvailability = this;
      var direction = (index == 0) ? 'prev': 'next';
      if(direction === 'next'){
        //TODO: Season limit check...
        if(villaAvailability.carousel.transition.currentPage === villaAvailability.carousel.transition.page){
            villaAvailability.requestData('updateData', villaAvailability.targetUrl, false);
        }
      }
      else{
        if(!villaAvailability.allowLeftCall()){
          villaAvailability.requestData('updateData', villaAvailability.targetUrl, true);
        }
      }
    },

    addEventListeners: function () {
      var villaAvailability = this;
      dojo.connect(villaAvailability.dateSelector, "onChange", function (event) {
        villaAvailability.requestNewData();
      });

      dojo.connect(villaAvailability.adultSelector, "onChange", function (event) {
        villaAvailability.requestNewData();
      });

      dojo.connect(villaAvailability.childSelector, "onChange", function (event) {
        villaAvailability.requestNewData();
      });
    },

    requestNewData: function (villaAvailability) {
      var villaAvailability = this;
      villaAvailability.requestData('newData', villaAvailability.targetUrl);
    },

    /**
     * Toggles activity indicator
     * @param action {string} 'add'|'remove'
     */
    toggleActivityIndicator: function (action) {
      var villaAvailability = this;
      // clear search activity indicator > move to rendering
      domClass[action](villaAvailability.domNode, "searching");
    },

    // ----------------------------------------------------------------------------- store methods

    /**
     * Observes carousel store, handles adding new items
     */
    observeStore: function () {
      var villaAvailability = this;
      var villaData = villaAvailability.carouselData.query();
      villaAvailability.observer = villaData.observe(function (villaGroup, remove, add) {
        if (add > -1) {
          if (!villaAvailability.carousel) villaAvailability.renderCarouselNode();
          villaAvailability.addGroup(villaGroup);
        }
      })
    },

    /**
     * Adds a group to the carousel
     * Renders group or item if group exists and item does not
     * Initialises carousel if not yet intialised
     * @param groupData {object} contains data for this group
     */
    addGroup: function (groupData) {
      var villaAvailability = this, html, newDays, newDayItems, groupNode, placeWhere, placeNode,
          group = villaAvailability.groupExists(groupData);

      // group is already in carousel
      if (group) {
        // check if any dates are not yet in carousel
        newDays = _.filter(groupData.days, function (day) {
          return !villaAvailability.itemExists(day, group);
        });
        // if so, render them
        if (newDays.length) {
          html = villaAvailability.renderTmpl(tmpl, {
            templateView: "items",
            items: newDays,
            groupId: groupData.id
          });
          dojo.place(html, query('ul', group)[0], "last");
        }
      } else {
        // new group so render
        html = villaAvailability.renderTmpl(tmpl, {
          templateView: "group",
          group: groupData
        });

        var toBeAddedGroupDate = dojo.date.locale.parse(groupData.id, {
          datePattern: "dd-MM-yyyy",
          selector: "date"
        });
        toBeAddedGroupDate.setMonth(toBeAddedGroupDate.getMonth() - 1);
        var toBeAddedGroupDateString = dojo.date.locale.format(toBeAddedGroupDate, {
          selector: "date",
          datePattern: "dd-MM-yyyy"
        });

        var queryTest = query('[data-group-id=' + toBeAddedGroupDateString + ']', villaAvailability.carouselNode);
        placeNode = queryTest.length ? queryTest[0] : query(".list", villaAvailability.carouselNode)[0];
        placeWhere = queryTest.length ? 'after' : 'first';

        groupNode = dojo.place(html, placeNode, placeWhere);
        villaAvailability.carouselGroups.push(groupNode);
      }

      // check if carousel has been initialised
      if (!villaAvailability.carousel) {
        villaAvailability.initCarousel();
      } else {
        villaAvailability.recalcCarousel();
      }


      if(!group) {
        // group doesn't exist so init all items
        villaAvailability.initGroupItems(groupNode, groupData);
      } else {
        // or just initialise new date items
        _.each(newDays, function (day) {
          var dayNode = query('[data-date-id="' + day.when + '"]', groupNode)[0];
          villaAvailability.initGroupItem(dayNode, day);
        });
      }

      villaAvailability.toggleActivityIndicator('remove');
    },

    /**
     * Check if group already exists in carousel
     * @param group {object} group data
     * @returns {object|null} groupNode if exists, else null
     */
    groupExists: function (group) {
      var villaAvailability = this;
      var groupNode = query('[data-group-id="' + group.id + '"]', villaAvailability.carouselNode);
      return groupNode.length > 0 ? groupNode[0] : null;
    },

    /**
     * Check if date item already exists in carousel
     * @param item {object} item data
     * @param groupNode {object} domNode item belong's to
     * @returns {boolean}
     */
    itemExists: function (item, groupNode) {
      return query('[data-date-id="' + item.when + '"]', groupNode).length > 0;
    },

    /**
     * Initialise Villa date group (month)
     * @param groupNode {object} domNode containing items
     */
    initGroupItems: function (groupNode, groupData) {
      var villaAvailability = this;
      _.each(query('.list-item', groupNode), function (item, i) {
        villaAvailability.initGroupItem(item, groupData.days[i]);
      });
    },

    /**
     * Initialise new VillaItem widget for all non-disabled dates
     * @param item {object} domNode
     */
    initGroupItem: function (item, dayData) {
      var villaAvailability = this;
      // Ignore items that are disabled
      console.log(dayData)
      if (!domClass.contains(item, 'disabled')) {
        villaAvailability.carouselItems.push(new VillaItem({
          searchParams: villaAvailability.searchParams,
          data: dayData,
          overlaySearch: villaAvailability.overlaySearch
        }, item));
      }
    },

    // ----------------------------------------------------------------------------- carousel methods

    /**
     * Renders carousel DOM Node
     */
    renderCarouselNode: function () {
      var villaAvailability = this;
      villaAvailability.carouselNode = dojo.place(villaAvailability.renderTmpl(tmpl, {
        templateView: "carousel"
      }), villaAvailability.domNode, "last");
    },

    /**
     * Initialises carousel
     */
    initCarousel: function () {
      var villaAvailability = this;
      villaAvailability.carousel = new Carousel(villaAvailability.carouselOptions, villaAvailability.carouselNode);
    },

    /**
     * Destroy's carousel, used when new search has been fired
     */
    destroyCarousel: function () {
      var villaAvailability = this;
      if (villaAvailability.carousel) {
        _.each(villaAvailability.carouselItems, function (item) {
          item.destroyRecursive();
        });
        villaAvailability.carouselItems.length = 0;
        villaAvailability.carousel.destroyRecursive();
        villaAvailability.carousel = null;
        dojo.destroy(villaAvailability.carouselNode);
      }
    },

    /**
     * Recalculate carousel after new slide items are added
     */
    recalcCarousel: function () {
      var villaAvailability = this,
      slider = villaAvailability.carousel.transition;
      slider.getSlideElements();
      slider.setItemsWidth();
      slider.calcPages();
      slider.calcMoveAmount();
      slider.resizeContainer();
      slider.toggleDisableControls();
      // fixme: determine why this is not updating
      villaAvailability.carousel.updateLabelPos();
    },
    // ----------------------------------------------------------------------------- data methods

    generateRequest: function (reload, left) {
      var villaAvailability = this;
      var date = left ? villaAvailability.getFirstDate() : villaAvailability.getLastDate();
      return {
        code: villaAvailability.accommodationCode,
        noOfChildren: villaAvailability.childSelector.getSelectedData().value,
        noOfAdults: villaAvailability.adultSelector.getSelectedData().value,
        startDate: reload ?  villaAvailability.dateSelector.getSelectedData().value : date,
        direction: left ? 'back' : ''
      };
    },

    /**
     * Request data for component
     * @param reqType {string} request type: 'newData'|'updateData'
     * @param url {string} Ajax url to POST
     */
    requestData: function (reqType, url, left) {
      var villaAvailability = this;
      villaAvailability.toggleActivityIndicator('add');
      reqType = reqType || 'newData'; // options: 'newData' / 'updateData'
      url = url || villaAvailability.targetUrl;
      xhr.post({
        url: url,
        handleAs: "json",
        content: {
          availabilityRequest: dojo.toJson(villaAvailability.generateRequest('newData' === reqType, left))
        },
        error: function (err) {
          _.debug("Ajax Error: " + err, true);
        },
        load: function (promise, options) {
          villaAvailability.handleResults(promise, options);
        },
        reqType: reqType
      });
    },

    handleResults: function (results, options) {
      var villaAvailability = this;
      // assign search params for new request
      villaAvailability.searchParams = results.searchParams;
      options = options || {
        args: {
          reqType: 'newData'
        }
      };

      // handle no results
      if (_.isEmpty(results.villaAvailabilityData)) {
        villaAvailability.handleNoResults();
        return;
      }

      // add id for each group
      results.villaAvailabilityData = _.map(results.villaAvailabilityData, function (group) {
        //group.id = group.monthYear.month.toLowerCase() + '-' + group.monthYear.year;
        group.id = group.monthYear.id;
        return group;
      });

      if (options.args.reqType === 'newData') {
        // if new data, clear the store, destroy carousel
        villaAvailability.carouselData.emptyStore();
        villaAvailability.destroyCarousel();
      }

      // add/append data to the store > triggers observer
      _.each(results.villaAvailabilityData, function (data) {
        villaAvailability.carouselData.putData(data);
      });

    },

    /**
     * Handles no results from server
     */
    handleNoResults: function () {
      var villaAvailability = this;

      // clear search activity indicator
      dojo.removeClass(villaAvailability.domNode, "searching");
    },

    getFirstNode: function () {
      var villaAvailability = this;
      return _.first(query('.list-item', villaAvailability.carouselNode));
    },

    getFirstDate: function() {
      var villaAvailability = this;
      return domAttr.get(villaAvailability.getFirstNode(), 'data-date-id');
    },

    getLastDate: function() {
      var villaAvailability = this;
      return domAttr.get(_.last(query('.list-item', villaAvailability.carouselNode)), 'data-date-id');
    },

    allowLeftCall: function(){
      var villaAvailability = this;
      var leftDateInView = dojo.date.locale.parse(villaAvailability.getFirstDate(), {
        datePattern: "dd-MM-yyyy",
        selector: "date"
      });

      var allowedDateLimitToTheLeft = dojo.date.locale.parse(villaAvailability.data['firstAvailableDate'], {
        datePattern: "dd-MM-yyyy",
        selector: "date"
      });
      return date.compare(leftDateInView,allowedDateLimitToTheLeft) === 0;
    }

  });

  return tui.villaAvailabilityB.view.VillaAvailability;
});
