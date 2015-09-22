// #Taggable
// ##Mixin module
//
// Widget and domNode UX Analytics tagging methods
// This mixin is provided to all widgets extending `_TuiBaseWidget`
//
// Attaches up to 3 attributes to widget domNodes: `analytics-id`,
// `analytics-instance` and `analytics-text`
//
// @author: Guillaume Marty.

define('tui/widget/mixins/Taggable', [
  'dojo',
  'dojo/query',
  'dojo/has',
  'tui/widget/TagMappingTable',
  'dojo/_base/sniff'
], function(dojo, query, has, tagMappingTable) {

  dojo.declare('tui.widget.mixins.Taggable', null, {

    // ----------------------------------------------------------------------------- properties

    /**
     * ###tag
     * The tag to attach, this is a string representation of a WF-COM UX number
     * eg '300'
     * @type {String}
     */
    tag: null,

    /**
     * ###number
     * Number representing the instance of the Widget's Class
     * @type {Number}
     */
    number: null,

    /**
     * ###isTaggable
     * Boolean determining if this widget is taggable or not
     * Prevents tagging if widget is not configured in TagMappingTable or a
     * tag and number have not been otherwise specified
     * @type {Boolean}
     */
    isTaggable: false,

    // ----------------------------------------------------------------------------- methods

    /**
     * ###attachTag()
     * Attach the main tag to the widget's domNode element
     * Called by `_TuiBaseWidget.postCreate()`
     */
    attachTag: function () {
      var taggable = this;

      // Disable for now.
      //return;

      taggable.defineTag();

      // and the number
      taggable.defineNumber();

      // exit early if widget is not taggable and a number/tag have not been otherwise specified
      if (!taggable.isTaggable && !taggable.number && !taggable.tag) {
        return;
      }

      // Add the tag attribute to the DOM element.
      taggable.tagElement(taggable.domNode);

      //console.log(taggable.domNode, taggable.declaredClass);
    },

    tagElements: function(DOMElements, /* Optional */ value) {
      // summary:
      //    Tag DOM elements with the widget details and an optional value.
      //    If value is a function, it takes each DOM element as a parameter and the returned value
      //    is used to populate the text attribute.
      var taggable = this;

      // Disable for now.
      //return;

      if (typeof value !== 'function') {
        _.forEach(DOMElements, function (DOMElement) {
          taggable.tagElement(DOMElement, value);
        });
      } else {
        // value is a function, execute the callback on each DOM element
        _.forEach(DOMElements, function (DOMElement) {
          taggable.tagElement(DOMElement, value(DOMElement));
        });
      }
    },

    /**
     * ###tagElement()
     * Tag a DOM element with the widget details and an optional value.
     * @param {Node} DOMElement DOM element to tag
     * @param {String|Function?} value Either a string to attach as a tag or a function to determine the string to attach
     */
    tagElement: function (DOMElement, /* Optional */ value) {
      // summary:
      //    Tag a DOM element with the widget details and an optional value.
      var taggable = this;

      // Disable for now.
      //return;

      if (!taggable.tag) {
          //_.debug(taggable.declaredClass + " Error! No tag defined");
        return;
      }

      // exit early if DOMElement argument is invalid
      if (!DOMElement) {
        _.debug(taggable.declaredClass + " Error! DOM Element is null or undefined");
        return;
      }

      if(!dojo.hasAttr(DOMElement, 'analytics-id')) {
        dojo.setAttr(DOMElement, 'analytics-id', taggable.tag);
      }
      if(!dojo.hasAttr(DOMElement, 'analytics-instance')) {
        dojo.setAttr(DOMElement, 'analytics-instance', taggable.number);
      }

      if (value && !dojo.hasAttr(DOMElement, 'analytics-text')) {
        value = ('' + value).replace(/\s+/g, ' '); // Remove extra whitespaces.
        value = dojo.trim(value);
        dojo.setAttr(DOMElement, 'analytics-text', value);
      }
    },

    /**
     * ###defineTag()
     * Define the value of tag property with a lookup table of widget
     * class names to tracking IDs.
     */
    defineTag: function () {
      // summary:
      //    Define the value of tag property with a lookup table of widget classnames to
      //    tracking ID.
      var taggable = this;

      // exit early if tag already set
      if (taggable.tag !== null) {
        return;
      }

      //console.log(taggable.declaredClass)

      var tag = tagMappingTable.table[taggable.declaredClass];

      if (tag !== undefined) {
        taggable.isTaggable = true;
        taggable.tag = tag;
      }
    },

    /**
     * ###defineNumber()
     * Compute the position of this widget inside several of the same type
     * Defines the instance number of this widget
     */
    defineNumber: function () {
      // summary:
      //    Compute the position of this widget inside several of the same type.
      var taggable = this;

      if (taggable.number !== null) {
        return;
      }

      // 1. Taking the number from the id property.
      var number = taggable.id.substring(taggable.id.lastIndexOf('_') + 1);

      // 2. Otherwise, compute it using components of the same type.
      if (!isNaN(parseInt(number, 10))) {
        number++; // Unique ID are 0-based index.
      } else {
        // @TODO: IE7 breaks on attribute selector query, need to determine a solution
        if (has('ie') > 7 || !has('ie')) {
          number = 1;

            var sameWidgets = dojo.query('*[data-dojo-type="' + taggable.declaredClass + '"]');

            if (sameWidgets.length > 0) {
            _.some(sameWidgets, function (widget) {
              number++;
              return widget.id === taggable.id;
            });
          }
        } else {
          // set number to 1 arbitrarily for IE7
          number = 1;
        }
      }

      taggable.number = number;
    }
  });

  return tui.widget.mixins.Taggable;
});