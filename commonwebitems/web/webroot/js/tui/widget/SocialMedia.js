define('tui/widget/SocialMedia', [
  'dojo',
  'dojo/query'
], function(dojo, query) {

  dojo.declare('tui.widget.SocialMedia', [tui.widget._TuiBaseWidget], {

	  Text: '',
  	  MediaColor: '',
  	  OnHoverText: '',
  	  OnHoverColor: '',
  	  IconURL: '',

	  postCreate: function() {
		  var socialMedia = this;
		  socialMedia.inherited(arguments);
		  socialMedia.addStyle();
          socialMedia.onHover();

	  },

	  addStyle: function(){
		  var socialMedia = this;
		  dojo.style(socialMedia.domNode, {
		        "backgroundColor": socialMedia.MediaColor
		  });
		  /*dojo.query('a',socialMedia.domNode).forEach(function(node){
	        	  dojo.style(node, {
	  		        "background": 'url('+ socialMedia.IconURL+') no-repeat scroll 4px 3px transparent'
	  		  });
	      });*/

	  },

	 onHover: function() {
		    var socialMedia = this;
			dojo.connect(socialMedia.domNode, "mouseenter", function(){
				dojo.style(socialMedia.domNode, {
			        "backgroundColor": socialMedia.OnHoverColor
			    });
				dojo.query('a',socialMedia.domNode).forEach(function(node){
		        	 node.innerHTML = socialMedia.OnHoverText;
		        });
			});
			dojo.connect(socialMedia.domNode, "mouseleave", function(){
				dojo.style(socialMedia.domNode, {
			        "background": socialMedia.MediaColor
			    });
				dojo.query('a',socialMedia.domNode).forEach(function(node){
		        	 node.innerHTML = socialMedia.Text;
		        });
			});
	 	}
  });


return tui.widget.SocialMedia;

});

