define('tui/widget/media/TouristBoardBanner', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/text!tui/widget/media/templates/TouristBoardBannerTmpl.html',
  'tui/widget/maps/Mappers'
], function(dojo, on, domAttr, domStyle, touristBoardBannerTmpl, mappers) {

  dojo.declare('tui.widget.media.TouristBoardBanner', [tui.widget._TuiBaseWidget], {

    tmpl: touristBoardBannerTmpl,

    postCreate: function() {
      // summary:
      var touristBoardBanner = this;

      touristBoardBanner.inherited(arguments);

      touristBoardBanner.jsonData = mappers.thumbnailSet(touristBoardBanner.jsonData);

      var template = new dojox.dtl.Template(touristBoardBanner.tmpl);
      var context  = new dojox.dtl.Context(touristBoardBanner);
      var html = template.render(context);

      dojo.place(html, touristBoardBanner.domNode, 'only');

      on(touristBoardBanner.domNode, 'ul.thumimg img:mouseover', function(event) {
        var xlargeSrc = domAttr.get(event.target, 'data-xlarge-src');
        var captionContent = domAttr.get(event.target, 'data-location-name');
        var bigImage = dojo.byId('touristBoardBannerImg');
        var captionEl = dojo.byId('touristBoardBannerCaption');

        bigImage.src = xlargeSrc;
        if (captionContent) {
          captionEl.innerHTML = captionContent;
          dojo.setStyle(captionEl, 'display', 'block');
        } else {
          dojo.setStyle(captionEl, 'display', 'hidden');
        }
      });

      console.log(touristBoardBanner);
      console.log(html);
    }
  });

  return tui.widget.media.TouristBoardBanner;
});
