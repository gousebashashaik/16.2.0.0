define('tui/villaAvailability/view/VillaItem', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-class',
  'dojo/dom-construct',
  'dojo/_base/lang',
  'dojo/aspect',
  'dojo/mouse',
  'dojo/_base/connect',
  'dojo/text!tui/villaAvailability/view/templates/popupTmpl.html',
  'tui/villaAvailability/view/VillaAvailabilityPopup',
  'tui/searchPanel/model/AirportModel',
  'tui/villaAvailability/nls/I18nable',
  'tui/widget/mixins/Templatable',
  'tui/widget/_TuiBaseWidget'
], function (dojo, query, on, domAttr, domClass, domConstruct, lang, aspect, mouse, connect, popupTmpl, Popup, AirportModel) {

  dojo.declare('tui.villaAvailability.view.VillaItem', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.villaAvailability.nls.I18nable], {

    tmpl: popupTmpl,

    searchParams: null,

    popup: null,

    popupTimer: null,

    overlaySearch: null,

    data: null,

    postCreate: function () {
      var villaItem = this;
      villaItem.inherited(arguments);
      villaItem.initLocale();

      villaItem.delegateEvents();
    },

    delegateEvents: function () {
      var villaItem = this;
      // create and show popup on hover

      on(villaItem.domNode, mouse.enter, function (e) {
        dojo.stopEvent(e);
        if (villaItem.popup && villaItem.popup.isShowing(villaItem.popup.popupDomNode)) {
          villaItem.clearPopupTimer();
          return;
        }
        villaItem.createPopup(e);
      });

      // destroys popup on mouseout
      on(villaItem.domNode, mouse.leave, function (e) {
        dojo.stopEvent(e);
        villaItem.popupTimer = setTimeout(function () {
          villaItem.closePopup();
        }, 60);
      });

      // keep popup if hovered over
      on(dojo.body(), on.selector('#'+villaItem.id + '_Popup', 'mouseover'), function (e) {
        villaItem.clearPopupTimer();
      });

      // destroys popup on mouseout (cancelled if returning to trigger node)
      on(dojo.body(), on.selector('#'+villaItem.id + '_Popup', 'mouseout'), function (e) {
        villaItem.popupTimer = setTimeout(function () {
          villaItem.closePopup();
        }, 60);
      });
    },

    createPopup: function(e) {
      var villaItem = this;

      if (villaItem.popup) {
        villaItem.popup.open();
        return;
      }

      var text = lang.replace(villaItem.localeStrings.priceFor, {
        NUM_NIGHT: villaItem.data.duration,
        NIGHT: villaItem.searchParams.duration > 0 ? 'nights' : 'night',
        AIRPORT: villaItem.data.airportName,
        NUM_PEOPLE: villaItem.searchParams.noOfAdults + villaItem.searchParams.noOfChildren
      });

      var popupDom = domConstruct.place(villaItem.renderTmpl(popupTmpl, {
        text: text,
        id: villaItem.id + '_Popup',
        promoText: domClass.contains(villaItem.domNode, 'promo') ? villaItem.localeStrings.freeChild : '',
        btnText: villaItem.localeStrings.checkPrice
      }), dojo.body(), "last");

      villaItem.popup = new Popup({
        widgetId: villaItem.id + '_Popup',
        floatWhere: 'position-top-center',
        elementRelativeTo: villaItem.domNode,
        posOffset: {top: -8, left: 0}
      });
      villaItem.popup.open();

      on(dojo.byId(villaItem.id + '_Popup'+ '_button'), "click", function(e){
        villaItem.closePopup();
        villaItem.prePopulateSearchCriteria();
      });
    },

    prePopulateSearchCriteria: function(){
      var villaItem = this,
          from = [],
          when = villaItem.data.when;

      from.push({
        id: villaItem.data.airportCode,
        name: villaItem.data.airportName
      });

      villaItem.overlaySearch.open({
        from:  from,
        to: villaItem.searchParams.units,
        when: when,
        noOfAdults: villaItem.searchParams.noOfAdults,
        noOfChildren: villaItem.searchParams.noOfChildren
      });
    },

    clearPopupTimer: function () {
      var villaItem = this;
      if (villaItem.popupTimer) clearTimeout(villaItem.popupTimer);
    },

    closePopup: function () {
      var villaItem = this;
      if (villaItem.popup) villaItem.popup.close();
    },

    destroyPopup: function () {
      var villaItem = this;
      if (villaItem.popup) {
        villaItem.popup.close();
        villaItem.popup.destroyRecursive();
        villaItem.popup = null;
        villaItem.clearPopupTimer();
      }
    },

    destroyRecursive: function () {
      var villaItem = this;
      villaItem.destroyPopup();
      villaItem.inherited(arguments);
    }

  });

  return tui.villaAvailability.view.VillaItem;
});