define("tui/widget/media/cruise/MultiVideoPopup", [
  'dojo',
  'dojo/_base/declare',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/_base/fx',
  'dojo/_base/array',
  'dojo/text!tui/widget/media/cruise/templates/multiVideoTmpl.html',
  'tui/widget/popup/Popup',
  'dojo/query',
  'dojo/window',
  'dojo/NodeList-traverse',
  'tui/widget/media/BrightCovePlayer' ], function (dojo, declare, on, domAttr, baseFx, array, multiVideoTmpl, popup, query, win) {

   return declare("tui.widget.media.cruise.MultiVideoPopup", [tui.widget.popup.Popup,  tui.widget.media.BrightCovePlayer], {

	    //---------------------------------------------- properties

	    jsonData: null,

	    cdnDomain: dojoConfig.paths.cdnDomain,

	    modal: true,

	    autoplaymode: true,

	    autoplaygap: 8000,

	    pause: false,

	    videoId: null,

	    tmpl: multiVideoTmpl,

	    thumbImages: null,

	    multiVideoPopup: null,

	    current: 0,

	    thumbViewport: null,

	    thumbViewportWidth: null,

	    itemContainer: null,

	    itemContainerWidth: null,

	    itemElementWidth: 210,

	    slidesNo: null,

	    currentSlide: 0,

	    slideAmount: 990,

	    start: 0,

	    end: 0,

	    includeScroll: true,

	    videoNodes: null,

	    galleryVideosCount: 0,

	    loadDelay: 5000,

	    index:0,

	    //--------------------------------------------------------------methods

	    postCreate: function () {
	      var multiVideoPopup = this;
	      multiVideoPopup.inherited(arguments);
	      multiVideoPopup.jsonData = multiVideoPopup.jsonData;
	      _.each(multiVideoPopup.jsonData.galleryVideos, function(item,i){
	    	  item["index"] = i;
	      });
	      multiVideoPopup.jsonData.videoId = multiVideoPopup.jsonData.galleryVideos[0].code;
	      multiVideoPopup.jsonData.galleryVideosCount = _.size(multiVideoPopup.jsonData.galleryVideos);
	    },

	    onAfterTmplRender: function () {

	      var multiVideoPopup = this;
	      multiVideoPopup.inherited(arguments);
	      multiVideoPopup.videoNodes = query('.product-list li', multiVideoPopup.popupDomNode);
	      multiVideoPopup.thumbImages = query('ul.plist img', multiVideoPopup.popupDomNode);
	      multiVideoPopup.addEventListener();
	      var timer = setTimeout(function () {
	    	  multiVideoPopup.hideLoading();
	      }, multiVideoPopup.loadDelay);
	      multiVideoPopup.getcaption( multiVideoPopup.videoNodes[0]);
	      multiVideoPopup.showVideoNode(0);

	      if(multiVideoPopup.jsonData.galleryVideosCount > 1) {
	        var thumb = query('ul.plist img', multiVideoPopup.popupDomNode).forEach(function (item, i) {
	          domAttr.set(item, "idx", i);
	        });
	        // calculating the max number of slides for the thumb Carousel

	        multiVideoPopup.thumbViewport = query('.thumb-carousel', multiVideoPopup.popupDomNode)[0];
	        multiVideoPopup.thumbViewportWidth = dojo.style(multiVideoPopup.thumbViewport, 'width');

	        multiVideoPopup.itemContainer = query('ul.plist', multiVideoPopup.popupDomNode)[0];
	        multiVideoPopup.itemContainerWidth = multiVideoPopup.itemElementWidth * thumb.length;

	        multiVideoPopup.slidesNo = Math.ceil(multiVideoPopup.itemContainerWidth / multiVideoPopup.thumbViewportWidth);
	      }

	      multiVideoPopup.createBrightCovePlayer();
	    },

    	addEventListener: function () {
    		var multiVideoPopup = this;
	        _.each(multiVideoPopup.thumbImages, function(thumbImg){
	        	on(thumbImg, 'click', function (event) {
	      		  var index = domAttr.get(thumbImg, "idx");
	      		  //multiVideoPopup.stopVideo();
	      		  multiVideoPopup.video.stop();
	      		  multiVideoPopup.getcaption( multiVideoPopup.videoNodes[index]);
	      		  multiVideoPopup.showVideoNode(index);
	      		  multiVideoPopup.setSelctedVideo(index);
	      		  //dojo.publish("tui/widget/media/BrightCovePlayer/onTemplateLoaded",[brightcove, "bcp_"+multiVideoPopup.domNode.id]);
	        	});
	        	on(thumbImg, 'mouseover', function (event) {
		          var anims = [];
		          multiVideoPopup.thumbImages.forEach(function (img, i) {
		            if (img !== event.target) {
		              anims.push(multiVideoPopup.animProp(img, 300, {opacity: 0.2}));
		            }
		          });
		          dojo.fx.combine(anims).play();
		        });

		        on(thumbImg, 'mouseout', function (event) {
		          var anims = [];
		          multiVideoPopup.thumbImages.forEach(function (img, i) {
		            if (img !== event.target) {
		              anims.push(multiVideoPopup.animProp(img, 300, {opacity: 0.5}));
		            }
		          });
		          dojo.fx.combine(anims).play();
		        });
	        });

			on(multiVideoPopup.popupDomNode, '.next:click', function (event) {
				multiVideoPopup.createNextSlide();
			});
			on(multiVideoPopup.popupDomNode, '.prev:click', function (event) {
				multiVideoPopup.createPrevSlide();
			});
        },

        setSelctedVideo: function (index) {
        	var brightCovePlayer = this;
			var experinceID = "bcp_tui_widget_media_cruise_MultiVideoPopup_0#"+index;
			var bcExp = brightcove.getExperience(experinceID);
	        var modExp = bcExp.getModule(APIModules.EXPERIENCE);
	        brightCovePlayer.setVideo(bcExp);
        },

	    showVideoNode: function (index) {
	        var multiVideoPopup = this;
	        _.each(multiVideoPopup.videoNodes, function (item) {
	            dojo.setStyle(item, 'opacity', '0');
	            dojo.setStyle(item, 'zIndex', '0');
	            if(item.className != ""){
	            	dojo.removeClass(item, 'active');
	            }
	        });
		    dojo.addClass(multiVideoPopup.videoNodes[index], 'active');
		    dojo.setStyle(multiVideoPopup.videoNodes[index], 'zIndex', '1');
		    dojo.setStyle(multiVideoPopup.videoNodes[index], 'opacity', '1');
	    },

	    getcaption: function (node) {
	        var multiVideoPopup = this;
	        var mainVideo = query('ul.product-list li', multiVideoPopup.popupDomNode);
	        var index = dojo.indexOf(mainVideo, node);
	        var captionContent = domAttr.get(node, 'data-location-name');;
	        var captionEl = query('p.video', multiVideoPopup.popupDomNode)[0];
	        captionEl.innerHTML = captionContent;
	        dojo.setStyle(captionEl, 'display', 'block');
	    },


	    hideLoading: function () {
	      var multiVideoPopup = this;
	      dojo.addClass(multiVideoPopup.popupDomNode, 'loaded');
	      dojo.removeClass(query('.container', multiVideoPopup.popupDomNode)[0], 'loading');
	    },

	    createNextSlide: function () {
	       var multiVideoPopup = this;
	       if (multiVideoPopup.currentSlide === multiVideoPopup.slidesNo - 1) {
	         return;
	       }
	       else {
	         multiVideoPopup.start = multiVideoPopup.end;
	         multiVideoPopup.end = multiVideoPopup.slideAmount * (multiVideoPopup.currentSlide + 1);
	         multiVideoPopup.animProp(multiVideoPopup.itemContainer, 1000, {right: {start: multiVideoPopup.start, end: multiVideoPopup.end, units: 'px'}}).play();
	         multiVideoPopup.currentSlide++;
	       }
	    },

	    createPrevSlide: function () {
		    var multiVideoPopup = this;
		    if (multiVideoPopup.currentSlide === 0) {
		      return;
		    }
		    else {
		      multiVideoPopup.start = multiVideoPopup.end;
		      multiVideoPopup.end = multiVideoPopup.slideAmount * (multiVideoPopup.currentSlide - 1);
		      multiVideoPopup.animProp(multiVideoPopup.itemContainer, 1000, {right: {start: multiVideoPopup.start, end: multiVideoPopup.end, units: 'px'}}).play();
		      multiVideoPopup.currentSlide--;
		    }
	    },

	    animProp: function (node, duration, properties, delay) {
	      var anim = dojo.animateProperty({
	        node: node,
	        duration: duration,
	        properties: properties,
	        delay: delay || 0
	      });
	      return anim;
	    },

	    onClose: function () {
	    	var multiVideoPopup = this;
	      // ----- setting first element "active" in-order to show on next VideoPopup invoke ----
	    	multiVideoPopup.getcaption( multiVideoPopup.videoNodes[0]);
	        multiVideoPopup.showVideoNode(0);
		 // ---------------------------------
		    multiVideoPopup.stopVideo();
	    }
  });
});