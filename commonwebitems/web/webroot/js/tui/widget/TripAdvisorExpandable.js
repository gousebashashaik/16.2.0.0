define('tui/widget/TripAdvisorExpandable', [
  'dojo',
  'tui/widget/expand/LinkExpandable',
  'dojo/html',
  'dojo/NodeList-manipulate'
], function(dojo) {

  /***********************************************************************************/
  /* tui.widget.TripAdvisorExpandable                                                */
  /***********************************************************************************/
  dojo.declare('tui.widget.TripAdvisorExpandable', [tui.widget.expand.LinkExpandable], {

    transitionType: 'WipeInHeight',

    defaultTxt: null,

    openTxt: '',

    itemSelector: '',

    targetSelector: '.link-show-hide',

    itemContentSelector: '.copy',

    charLen: 200,

    copyShowHide: null,

    postCreate: function() {
      var tripAdvisorExpandable = this;

      var targetLink = dojo.query(tripAdvisorExpandable.targetSelector, tripAdvisorExpandable.domNode).remove();
      var contentParagraph = dojo.query(tripAdvisorExpandable.itemContentSelector, tripAdvisorExpandable.domNode);
      var txt = contentParagraph.text();
      dojo.html.set(contentParagraph, '');
      if (txt.length > tripAdvisorExpandable.charLen) {
        txt = [txt.substring(0, tripAdvisorExpandable.charLen), '&hellip;' , txt.substring(tripAdvisorExpandable.charLen, txt.length)].join('');
        txt = txt.split('&hellip;');
        txt.splice(1, 0, ["<span class='dots'>&hellip; </span>", "<span class='copy-show-hide'>"].join(''));
        txt.splice(3, 0, ' </span>');
        dojo.html.set(contentParagraph[0], txt.join(''));
        tripAdvisorExpandable.copyShowHide = dojo.query('.copy-show-hide', contentParagraph[0])[0];
        dojo.style(tripAdvisorExpandable.copyShowHide, 'display', 'none');
        targetLink.insertBefore(tripAdvisorExpandable.copyShowHide);
        var coords = dojo.coords(targetLink[0]);
        var top = [coords.t + coords.h, 'px'].join('');
        dojo.style(contentParagraph[0], 'height', top);
      }
      dojo.mixin(tripAdvisorExpandable.transitionOptions, {
        onClick: function(transition, itemContent, item, wipe, target) {
          var coords = dojo.coords(target);
          if (!tripAdvisorExpandable.start || tripAdvisorExpandable.start === coords.t + coords.h) {
            tripAdvisorExpandable.start = tripAdvisorExpandable.start || coords.t + coords.h;
            dojo.query(target).remove();
            dojo.query('.dots', tripAdvisorExpandable.domNode).style('display', 'none');
            dojo.style(tripAdvisorExpandable.copyShowHide, 'display', 'inline');
            dojo.query(target).insertAfter(tripAdvisorExpandable.copyShowHide);
            coords = dojo.coords(target);
            tripAdvisorExpandable.end = tripAdvisorExpandable.end || coords.t + coords.h;
            arguments[3] = 'wipeIn';
            Array.prototype.push.call(arguments, {end: tripAdvisorExpandable.end, start: tripAdvisorExpandable.start});
          } else {
            arguments[3] = 'wipeOut';
            Array.prototype.push.call(arguments, {end: tripAdvisorExpandable.start, start: tripAdvisorExpandable.end});
          }
          this.inherited(arguments);
        }
      });
      tripAdvisorExpandable.inherited(arguments);
      tripAdvisorExpandable.connect(tripAdvisorExpandable.transition,
        'onWidgetTransitionEnd', function(transition, itemContent, item, wipe, target) {
          if (wipe === 'wipeOut') {
            dojo.query('.dots', tripAdvisorExpandable.domNode).style('display', 'inline');
            dojo.style(tripAdvisorExpandable.copyShowHide, 'display', 'none');
            dojo.query(target).remove().insertBefore(tripAdvisorExpandable.copyShowHide);
          }
        });

      tripAdvisorExpandable.tagElements(dojo.query(tripAdvisorExpandable.targetSelector, tripAdvisorExpandable.domNode), "seeReview");
    }
  });

  return tui.widget.TripAdvisorExpandable;
});