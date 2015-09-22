define("tui/widget/expand/FilterExpandable", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/_base/Deferred",
  "dojo/dom-class",
  "dojo/dom-construct",
  "dojo/dom-geometry",
  "dojo/dom-style",
  "dojo/dom-attr",
  "tui/utils/DetectFeature",
  "tui/widget/_TuiBaseWidget"
], function (dojo, on, query, Deferred, domClass, domConstruct, domGeom, domStyle, domAttr, detectFeature) {

  dojo.declare("tui.widget.expand.FilterExpandable", [tui.widget._TuiBaseWidget], {

    /**
     *  Summary : Base class for self expanding widgets similar to an accordion. The widgets do not close other widgets.
     *  The widgets have to be wrapped inside a div with data-dojo-type attribute = "tui.widget.expand.FilterExpandable"
     *  and must use class "expandable".
     *
     *
     * @example
     *
     *  ```<div data-dojo-type="tui.widget.expand.FilterExpandable" class="expandable">
     *     <div class ="item">
     *       <p class="item-toggle"> <a href="javascript:void(0);">Item one</a></p>
     *         <div class="item-content">
     *           <div class="item-content-wrap">
     *            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt nisl et lacus tristique fringilla condimentum et magna. Duis gravida dolor sapien, non tincidunt augue interdum ac. Cras id turpis et orci commodo mattis. Etiam volutpat libero id magna fringilla dapibus at a dolor. In hac habitasse platea dictumst. Ut congue ligula sed egestas commodo. Proin vel tellus vel leo egestas pharetra malesuada viverra elit. Nam porttitor est sodales, ultrices nisi non, rhoncus justo. Maecenas placerat purus metus, at convallis ante elementum vel. In eu nisi eu mauris feugiat viverra. Aliquam pretium mauris turpis, a pulvinar magna tincidunt non. Maecenas eu magna in libero rhoncus venenatis blandit eget orci. Aliquam vel lacinia justo. Curabitur lorem eros, consequat lobortis sagittis ac, elementum ac sapien. Quisque vel velit non leo eleifend sagittis vel fermentum dui.</p>
     *           </div>
     *         </div>
     *     </div>
     *     <div class ="item">
     *       <p class="item-toggle"> <a href="javascript:void(0);">Item two</a></p>
     *         <div class="item-content">
     *           <div class="item-content-wrap">
     *            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tincidunt nisl et lacus tristique fringilla condimentum et magna. Duis gravida dolor sapien, non tincidunt augue interdum ac. Cras id turpis et orci commodo mattis. Etiam volutpat libero id magna fringilla dapibus at a dolor. In hac habitasse platea dictumst. Ut congue ligula sed egestas commodo. Proin vel tellus vel leo egestas pharetra malesuada viverra elit. Nam porttitor est sodales, ultrices nisi non, rhoncus justo. Maecenas placerat purus metus, at convallis ante elementum vel. In eu nisi eu mauris feugiat viverra. Aliquam pretium mauris turpis, a pulvinar magna tincidunt non. Maecenas eu magna in libero rhoncus venenatis blandit eget orci. Aliquam vel lacinia justo. Curabitur lorem eros, consequat lobortis sagittis ac, elementum ac sapien. Quisque vel velit non leo eleifend sagittis vel fermentum dui.</p>
     *           </div>
     *         </div>
     *     </div>
     *  </div>```
     *
     */

    itemSelector: ".item",

    targetSelector: ".item-toggle",

    itemContentSelector: ".item-content",

    wrapSelector: ".item-content-wrap",

    padding: 24,

    expandableItems: null,

    defaultOpen: [],

    hasTransition: null,

    hasTransform: null,

    has3D: null,

    //subscribableMethods: ["resize"],

    postCreate: function () {
      var filterExpandable = this;
      filterExpandable.inherited(arguments);
      filterExpandable.expandableItems = query(filterExpandable.itemSelector, filterExpandable.domNode);

      filterExpandable.hasTransition = detectFeature.isTransitionSupported();
      filterExpandable.hasTransform = detectFeature.isTransformSupported();
      filterExpandable.has3D = detectFeature.is3dSupported();

      filterExpandable.attachEvents();
      dojo.subscribe('tui:channel=lazyload', function () {
        filterExpandable.openDefault();
      });
    },

    resize: function () {
      var filterExpandable = this, content, setHeight, setSpeed;
      _.each(filterExpandable.expandableItems, function (item){
        content = query(filterExpandable.itemContentSelector, item)[0];
        if(domAttr.get(content, 'data-toggle-state') === 'open') {
          setHeight = filterExpandable.calcHeight(content);
          domStyle.set(content, "maxHeight", _.pixels(setHeight));
        }
      });
    },

    openDefault: function () {
      var filterExpandable = this, content;
      if (!filterExpandable.defaultOpen.length) return;

      _.each(filterExpandable.expandableItems, function (item, i) {
        if(_.indexOf(filterExpandable.defaultOpen, i) > -1){
          content = query(filterExpandable.itemContentSelector, item)[0];
          filterExpandable.toggleOpen(content, item, 'open');
        }
      });
    },

    attachEvents: function () {
      var filterExpandable = this;
      on(filterExpandable.domNode, on.selector(filterExpandable.targetSelector, "click"), function (e) {
          filterExpandable.toggleView(e);
      });
    },

    toggleView: function(e){
       var filterExpandable = this, parent, content;
       parent = query(e.target).parents(filterExpandable.itemSelector)[0];
       content = query(filterExpandable.itemContentSelector, parent)[0];
       if (domAttr.get(content, 'data-toggle-state') !== 'open') {
          // open it
          filterExpandable.toggleOpen(content, parent, 'open');
        } else {
           // close it
           filterExpandable.toggleOpen(content, parent, '');
        }
    },

    toggleOpen: function (domNode, parent, state) {
      var filterExpandable = this,
          action = 'remove', setHeight = 0, setSpeed = false;

      if(state === 'open')  {
        action = 'add';
        setHeight = filterExpandable.calcHeight(domNode);
        setSpeed = filterExpandable.calcSpeed(setHeight);
      }

      filterExpandable.onBeforeToggle(domNode, state);

      // set transition speed and height of node
      setSpeed ? domStyle.set(domNode, detectFeature.firstSupportedPropName('TransitionDuration'), setSpeed + 's') : null;
      domStyle.set(domNode, "maxHeight", _.pixels(setHeight));

      // add/remove class
      domClass[action](domNode, 'open');
      domClass[action](parent, 'open');

      // save state
      domAttr.set(domNode, 'data-toggle-state', state);

      filterExpandable.onAfterToggle(domNode, state);
    },

    calcHeight: function (domNode) {
      var filterExpandable = this;
      return dojo.position(query(filterExpandable.wrapSelector, domNode)[0]).h + filterExpandable.padding;
    },

    calcSpeed: function(height) {
      return height > 300 ? (height / 2000) : 0.4;
    },

    onBeforeToggle: function (domNode, state) {},

    onAfterToggle: function (domNode, state) {}

  });

  return tui.widget.expand.FilterExpandable;
});