define("tui/widget/media/BrightCovePlayer", ["dojo"], function (dojo) {

    dojo.declare("tui.widget.media.BrightCovePlayer", null, {

		bcExp: null,
		
		videoPlayer: null,
		
		prefix: "bcp_",
		
		autoplay: true,
		
		createBrightCovePlayer: function(){
			var brightCovePlayer = this;
			brightcove.createExperiences();
			brightCovePlayer.subscribe("tui/widget/media/BrightCovePlayer/onTemplateLoaded",function(bcExp, id){
				if ([brightCovePlayer.prefix, brightCovePlayer.domNode.id].join("") === id){
					brightCovePlayer.setVideo(bcExp);
				}
			})
		
		},
		
		setVideo: function(bcExp){
			var brightCovePlayer = this;
			brightCovePlayer.bcExp = bcExp;
			brightCovePlayer.video = bcExp.getModule(APIModules.VIDEO_PLAYER);
			if (brightCovePlayer.autoplay){
				brightCovePlayer.playVideo();
			}
		},
		
		playVideo: function(){
			var brightCovePlayer = this;
			if (!brightCovePlayer.video || brightCovePlayer.video.isPlaying()) return;
			brightCovePlayer.video.play();
			brightCovePlayer.onPlayVideo(brightCovePlayer.bcExp, brightCovePlayer.video);
		},
		
		stopVideo: function(){
			var brightCovePlayer = this;
			if (brightCovePlayer.video){
				brightCovePlayer.video.stop();
			}
			brightCovePlayer.subscribe("tui/widget/media/BrightCovePlayer/onTemplateLoaded",function(bcExp, id){
				if ([brightCovePlayer.prefix, brightCovePlayer.domNode.id].join("") === id){
					if (!brightCovePlayer.video || brightCovePlayer.video.isPlaying()) {
						brightCovePlayer.video.stop();
						brightCovePlayer.onStopVideo(brightCovePlayer.bcExp, brightCovePlayer.video);
						
					}
					
				}
			})
			
		},
		
		onPlayVideo: function(bcExp, videoPlayer){},
		
		onStopVideo: function(bcExp, videoPlayer){}
    });

	
    return tui.widget.media.BrightCovePlayer;
})
