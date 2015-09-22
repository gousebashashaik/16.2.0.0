define('tui/widget/AllInOneExpandable', [
  'dojo',
  'tui/widget/expand/LinkExpandable',
  'dojo/html',
  'dojo/NodeList-manipulate'
], function(dojo) {

  /***********************************************************************************/
  /* tui.widget.AllInOneExpandable                                                */
  /***********************************************************************************/
  dojo.declare('tui.widget.AllInOneExpandable', [tui.widget.expand.LinkExpandable], {

    transitionType: 'WipeInHeight',

    defaultTxt: null,

    openTxt: '',

    itemSelector: '',

    targetSelector: '.link-show-hide',

    itemContentSelector: '.copy',

    charLen: 200,

    copyShowHide: null,

    postCreate: function() {
      var AllInOneExpandable = this;

      var targetLink = dojo.query(AllInOneExpandable.targetSelector, AllInOneExpandable.domNode).remove();
      var contentParagraph = dojo.query(AllInOneExpandable.itemContentSelector, AllInOneExpandable.domNode);
      var txt = contentParagraph.text();
      dojo.html.set(contentParagraph, '');
      if (txt.length > AllInOneExpandable.charLen) {
        txt = [txt.substring(0, AllInOneExpandable.charLen), '&hellip;' , txt.substring(AllInOneExpandable.charLen, txt.length)].join('');
        txt = txt.split('&hellip;');
        txt.splice(1, 0, ["<span class='dots'>&hellip; </span>", "<span class='copy-show-hide'>"].join(''));
        txt.splice(3, 0, ' </span>');
        dojo.html.set(contentParagraph[0], txt.join(''));
        AllInOneExpandable.copyShowHide = dojo.query('.copy-show-hide', contentParagraph[0])[0];
        dojo.style(AllInOneExpandable.copyShowHide, 'display', 'none');
        targetLink.insertBefore(AllInOneExpandable.copyShowHide);
        var coords = dojo.coords(targetLink[0]);
        var top = [coords.t + coords.h, 'px'].join('');
        dojo.style(contentParagraph[0], 'height', top);
      }
      dojo.mixin(AllInOneExpandable.transitionOptions, {
        onClick: function(transition, itemContent, item, wipe, target) {
          var coords = dojo.coords(target);
          if (!AllInOneExpandable.start || AllInOneExpandable.start === coords.t + coords.h) {
            AllInOneExpandable.start = AllInOneExpandable.start || coords.t + coords.h;
            dojo.query(target).remove();
            dojo.query('.dots', AllInOneExpandable.domNode).style('display', 'none');
            dojo.style(AllInOneExpandable.copyShowHide, 'display', 'inline');
            dojo.query(target).insertAfter(AllInOneExpandable.copyShowHide);
            coords = dojo.coords(target);
            AllInOneExpandable.end = AllInOneExpandable.end || coords.t + coords.h;
            arguments[3] = 'wipeIn';
            Array.prototype.push.call(arguments, {end: AllInOneExpandable.end, start: AllInOneExpandable.start});
          } else {
            arguments[3] = 'wipeOut';
            Array.prototype.push.call(arguments, {end: AllInOneExpandable.start, start: AllInOneExpandable.end});
          }
          this.inherited(arguments);
        }
      });
      AllInOneExpandable.inherited(arguments);
      AllInOneExpandable.connect(AllInOneExpandable.transition,
        'onWidgetTransitionEnd', function(transition, itemContent, item, wipe, target) {
          if (wipe === 'wipeOut') {
            dojo.query('.dots', AllInOneExpandable.domNode).style('display', 'inline');
            dojo.style(AllInOneExpandable.copyShowHide, 'display', 'none');
            dojo.query(target).remove().insertBefore(AllInOneExpandable.copyShowHide);
          }
        });

      AllInOneExpandable.tagElements(dojo.query(AllInOneExpandable.targetSelector, AllInOneExpandable.domNode), "seeReview");
    }
  });

  return tui.widget.AllInOneExpandable;
});