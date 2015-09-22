define('tui/widget/GeoLocationPromo', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/dom-style',
  'dojo/query',
  'dojo/_base/array'
], function(dojo, on, domAttr, domStyle ,query ,array) {
  dojo.declare('tui.widget.GeoLocationPromo', [tui.widget._TuiBaseWidget], {

	  //------------------------------------------------------properties

	  Link: null,

	  promoTitle:null,

	  //-------------------------------------------------------methods

	  postCreate: function() {
		  var geoLocationPromo = this;
		  geoLocationPromo.truncate();
		  geoLocationPromo.addEventListeners();

	  },

	 addEventListeners: function() {
		    var geoLocationPromo = this;
		    var myDiv = dojo.byId("geo");
		    dojo.connect(myDiv, "onclick", function(){
		    	 window.open(geoLocationPromo.Link);
			});
	  	},

	  truncate:function(){
		  var geoLocationPromo = this;
		  var words=geoLocationPromo.promoTitle.split(' ');
		  if(words.length>20)
			  {
			      geoLocationPromo.promoTitle='';
				  array.forEach(words, function(word, i){
					    geoLocationPromo.promoTitle=[geoLocationPromo.promoTitle,word].join(' ');
					    if(i==19)
					    	{
					    	query('.geoDesc span',geoLocationPromo.domNode)[0].innerHTML=geoLocationPromo.promoTitle+'...';
					    	return;
					    	}
					  });
			  }
		  else
			  {
			  query('.geoDesc span',geoLocationPromo.domNode)[0].innerHTML=geoLocationPromo.promoTitle
			  }
	  }

  	});
return tui.widget.GeoLocationPromo;
});

