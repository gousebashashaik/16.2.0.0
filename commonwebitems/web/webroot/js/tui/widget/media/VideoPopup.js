define("tui/widget/media/VideoPopup", ['dojo',
  'dojo/text!tui/widget/media/templates/VideoTmpl.html',
  'tui/widget/popup/Popup',
  'tui/widget/media/BrightCovePlayer'], function (dojo, videoTmpl, popup) {

  dojo.declare("tui.widget.media.VideoPopup", [tui.widget.popup.Popup, tui.widget.media.BrightCovePlayer], {

    videoId: null,

	videoPlayerId: null,
	
	videoPlayerKey: null,

    modal: true,

    tmpl: videoTmpl,

    videoPlayerPopup: null,

    onAfterTmplRender: function () {
      var videoPopup = this;
      videoPopup.inherited(arguments);
	        if (!videoPopup.videoId || videoPopup.videoId === '') {
			  return;
		    }
     /* var videoLink = dojo.query('.play-video', videoPopup.domNode)[0];
        var videoConnect = videoPopup.connect(videoLink, 'onclick', function (event) {
          dojo.stopEvent(event);
          if (!videoPopup.videoPlayerPopup) {
            videoPopup.open();
          }
          dojo.disconnect(videoConnect);
        });*/
      videoPopup.createBrightCovePlayer();
    },

    onOpen: function () {
      var videoPopup = this;
      window.scrollTo(0, 0);
      videoPopup.playVideo();
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [true]);
    },

        onClose: function(popupDomNode, popupBase) {
      var videoPopup = this;
        	var object = dojo.query(".BrightcoveExperience", popupDomNode)[0];
      	  	if(object){
      			var cloned = dojo.clone(object);
      			dojo.destroy(object);
      			dojo.place(cloned, popupDomNode, 1);
      	  	}
      try {
    	  videoPopup.stopVideo();
      } catch(err){
    	  console.log("error occured while closing brighCove player");
      }
      dojo.publish("tui/widget/media/HeroCarousel/onTemplateLoaded", [false]);
    }
  })

  return tui.widget.media.VideoPopup;
})




