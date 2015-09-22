define('tui/widget/mobile/DomRearranger', [
  'dojo',
  'dojo/query',
  'dojo/dom-attr',
  'dojo/dom-construct',
  'tui/common/ListMonad',
  'tui/widget/mobile/Widget'], function (dojo, query, domAttr, domConstruct, ListMonad) {

  function getIndex(node) {
    var parent = node.parentElement || node.parentNode, i = -1, child;
    while (parent && (child = parent.childNodes[++i])) if (child == node) return i;
    return -1;
  }


  function reposition(x) {
    domConstruct.place(x['element'], x['parent'], parseInt(x['index']));
    return x;
  }

  function sort(elements) {
    return elements.slice(0).sort(function (lhs, rhs) {
      return lhs['index'] - rhs['index'];
    });
  }


  dojo.declare('tui.widget.mobile.DomRearranger', [tui.widget.mobile.Widget], {

    positionBackup: null,

    mobileTargetPositions: null,

    onDesktop: function () {
      var widget = this;
      if (widget.positionBackup) {
        ListMonad.pipe(widget.positionBackup, [reposition]);
      }
    },

    onMobile: function () {
      var widget = this;
      if (widget.mobileTargetPositions) {
        ListMonad.pipe(widget.mobileTargetPositions, [reposition]);
      }
    },

    onMiniTablet: function () {
      var widget = this;
      if (widget.miniTabTargetPositions) {
        ListMonad.pipe(widget.miniTabTargetPositions, [reposition]);
      } else if (widget.positionBackup) {
        ListMonad.pipe(widget.positionBackup, [reposition]);
      }
    },

    onTablet: function () {
      var widget = this;
      if (widget.tabTargetPositions) {
        ListMonad.pipe(widget.tabTargetPositions, [reposition]);
      } else if (widget.positionBackup) {
        ListMonad.pipe(widget.positionBackup, [reposition]);
      }
    },


    postCreate: function () {
      var widget = this;

      function actualLocation(element) {
        return {
          'element': element,
          'parent': element.parentElement || element.parentNode,
          'index': getIndex(element)
        };
      }

      function targetLocation(positionAttribute) {
        return function (element) {
          return {
            'element': element,
            'parent': widget.domNode,
            'index': domAttr.get(element, positionAttribute)
          };
        }
      }

      var mobElements = query('*[data-mob-position]', widget.domNode);
      var miniTabElements = query('*[data-minitab-position]', widget.domNode);
      var tabElements = query('*[data-tab-position]', widget.domNode);

      if (mobElements && mobElements.length > 0) {
        widget.positionBackup = sort(ListMonad.pipe(mobElements, [actualLocation]));
      }
      if (mobElements && mobElements.length > 0) {
        widget.mobileTargetPositions = sort(ListMonad.pipe(mobElements, [targetLocation('data-mob-position')]));
      }
      if (miniTabElements && miniTabElements.length > 0) {
        widget.miniTabTargetPositions = sort(ListMonad.pipe(miniTabElements, [targetLocation('data-minitab-position')]));
      }
      if (tabElements && tabElements.length > 0) {
        widget.tabTargetPositions = sort(ListMonad.pipe(tabElements, [targetLocation('data-tab-position')]));
      }

      widget.inherited(arguments);
    }

  });
});


