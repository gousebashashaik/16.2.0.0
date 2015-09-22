define('tui/widget/media/Touristboard', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/text!tui/widget/media/templates/TouristBoardBannerTmpl.html',
  'tui/widget/maps/Mappers',
  'tui/utils/HasCssProperty',
  'dojo/query',
  'dojo/html'
], function(dojo, on, domAttr, domStyle, touristBoardBannerTmpl, mappers, hasCssProperty) {

  dojo.declare('tui.widget.media.Touristboard', [tui.widget._TuiBaseWidget], {

    current: 0,

    tmpl: touristBoardBannerTmpl,

    hasCSSTransition: false,

    imageContainer: null,


    postCreate: function() {
      var touristBoard = this;

      touristBoard.inherited(arguments);

      var imagesize = (dojo.query(touristBoard.domNode).parents('.span-two-third').length > 0) ? 'large' : 'xlarge';
      var data = touristBoard.jsonData.galleryImages;
      touristBoard.jsonData.heroImages = _.filter(data, function(item) {
        return (item.size === imagesize);
      });
      var li = dojo.query('li', touristBoard.domNode)[0];
      touristBoard.liHeight = dojo.style(li, 'height');

      var image = dojo.query('img', li)[0];

      touristBoard.showWidget(image);
      //touristBoard.positionImg(image);
      touristBoard.addEventListeners();

      touristBoard.jsonData = mappers.thumbnailSet(touristBoard.jsonData, 5);
      var template = new dojox.dtl.Template(touristBoard.tmpl);
      var context = new dojox.dtl.Context(dojo.mixin(touristBoard, {
    	cdnDomain:dojoConfig.paths.cdnDomain,
        siteName:dojoConfig.site
      }));
      var html = template.render(context);

      dojo.place(html, touristBoard.domNode, 'only');
      dojo.parser.parse(touristBoard.domNode);

      // Cache image container element.
      touristBoard.imageContainer = dojo.query('ul.tour li', touristBoard.domNode)[0];

      // Get images contained
      var thumb = dojo.query('ul.thumimg img');
      thumb.forEach(function(item, i) {
        domAttr.set(item, 'idx', i);
      });

      var images = dojo.query('ul.tour li');
      images.forEach(function(item, i) {
        if (i !== 0) {
          dojo.setStyle(item, 'opacity', '0');
          dojo.setStyle(item, 'zIndex', '1');
        }
      });

      // test for css transitions support, allows to connect to transitionend with fallback to setTimeout
      touristBoard.hasCSSTransition = !!hasCssProperty.test('transitionProperty');

      touristBoard.tagElement(dojo.byId('thingstodo'));
      
      touristBoard.attachTag();
      touristBoard.tagElement(dojo.query(".tourist-readmore-anchor")[0], "readmore");
      touristBoard.tagElement(dojo.query(".view-all")[0], "ttdviewall");
      
      touristBoard.tagElements(dojo.query(".plist .plist-li"), function(DOMElement){
    	  var index=_.indexOf(dojo.query(".plist .plist-li"), DOMElement);
    	  return "ttd"+(index+1);
      });
      
      touristBoard.tagElements(dojo.query(".thumimg .thuming-list"), function(DOMElement){
    	  var index1=_.indexOf(dojo.query(".thumimg .thuming-list"), DOMElement);
    	  return "thumbnail"+(index1+1);
      });
    },

    addEventListeners: function() {
      var touristBoard = this;

      on(touristBoard.domNode, 'ul.thumimg img:mouseover', function(event) {
        var oldImages = dojo.query('ul.tour li img', touristBoard.domNode);
        var oldImg = oldImages[oldImages.length - 1];
        var newImgSrc = dojo.getAttr(event.target, 'data-xlarge-src');

        // If the hover image is already display, do nothing.
        if (dojo.attr(oldImg, 'src') === newImgSrc) {
          return;
        }

        touristBoard.fadeToNewImage(newImgSrc);

        var captionContent = domAttr.get(event.target, 'data-location-name');
        var captionEl = dojo.byId('touristBoardBannerCaption');
        if (captionContent) {
          captionEl.innerHTML = captionContent;
          dojo.setStyle(captionEl, 'display', 'block');
        } else {
          dojo.setStyle(captionEl, 'display', 'hidden');
        }
      });
    },

    fadeToNewImage: function(newImgSrc) {
      var touristBoard = this;

      var oldImages = dojo.query('ul.tour li img', touristBoard.domNode);
      var oldImg = oldImages[oldImages.length - 1];
      var newImg = dojo.create('img', {src: newImgSrc}, touristBoard.imageContainer, 'last');

      on(newImg, 'load', function() {
        setTimeout(function() {
          dojo.setStyle(newImg, 'opacity', 1);
        }, 1);
      });

      if (touristBoard.hasCSSTransition) {
        on.once(newImg, 'webkitTransitionEnd,transitionend,oTransitionEnd', function() {
          dojo.destroy(oldImg);
        });
      } else {
        setTimeout(function() {
          dojo.destroy(oldImg);
        }, 350);
      }
    },

    positionImg: function(image) {
      var touristBoard = this;
      var imageHeight = image.height;
      var height = dojo.style(touristBoard.getTransition().viewport, 'height');
      var top = (imageHeight > height) ? (imageHeight - height) / 2 : 0;
      dojo.style(image, 'top', -top + 'px');
    }
  });

  return tui.widget.media.Touristboard;
});
