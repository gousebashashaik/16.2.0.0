
define("tui/cruise/masonryComponent/view/CruiseMasonryComponent", [
    'dojo',
    'dojo/parser',
    "dojo/_base/declare",
    'dojo/text!tui/cruise/masonryComponent/view/templates/CruiseMasonryFamilyComponentTmpl.html',
    'dojo/text!tui/cruise/masonryComponent/view/templates/CruiseMasonryFacilityComponentTmpl.html',
    'dojo/text!tui/cruise/masonryComponent/view/templates/CruiseMasonryUNEComponentTmpl.html',
    'dojo/on',
    "dojo/dom-construct",
    'dojo/query',
    "dojo/dom-style",
    "tui/widget/media/cruise/CruiseMediaPopup",
    "tui/widget/media/VideoPopup",
    'tui/widget/mixins/Templatable',
    'tui/widget/_TuiBaseWidget'], function (dojo, parser, declare, FamiliesTmpl, FacilityTmpl, UNEtmpl, on, domConstruct, query, domStyle, MediaPopup, VideoPopup) {


	return declare('tui.cruise.masonryComponent.view.CruiseMasonryComponent', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        jsonData: null,

	  	data: null,

	  	tmplOne: FamiliesTmpl,

	  	tmplTwo: FacilityTmpl,

	    tmplThree: UNEtmpl,

	    itemWidth: null, //

	    colCount :4,  //default value

	    calcHeightList : [],

	    colPositions: [],

	    parentWidth : null,

	    top: null,

	    left: null,

	    mediaNode: [],

	    videoNode:[],

	    componentName: "",

        postCreate: function () {

           var crMasonryComp = this;
           crMasonryComp.inherited(arguments);
           crMasonryComp.configureMasonry();
           crMasonryComp.loadMasonry();
        },

        configureMasonry: function () {
        	 var crMasonryComp = this, lVal = 0, parentNodeWidth = 0;
        	 parentNodeWidth = crMasonryComp.parentWidth ? crMasonryComp.parentWidth : crMasonryComp.domNode.offsetWidth;
        	 crMasonryComp.itemWidth = parentNodeWidth/crMasonryComp.colCount;
        	/* _.each(crMasonryComp.colCount, function(colNumber){
        		   crMasonryComp.calcHeightList.push(0);
            	   lVal = colNumber*crMasonryComp.itemWidth;
            	   crMasonryComp.colPositions.push(lVal);
        	 });*/

        	 for(i =0; i <crMasonryComp.colCount; i++ ) {
        	   crMasonryComp.calcHeightList.push(0);
          	   lVal = i*(crMasonryComp.itemWidth + 6);
          	   crMasonryComp.colPositions.push(lVal);
        	 }
        },

        loadMasonry: function(){
       	  var crMasonryComp = this, i = -1 , html = null, item = null, index = null, flag = true;

       	  if( crMasonryComp.mediaNode.length > 0){
       		_.each(crMasonryComp.mediaNode, function(media){
       			media.destroyRecursive();
       		})
       		_.each(crMasonryComp.videoNode, function(video){
       			video.destroyRecursive();
       		})
       	  }
       	  dojo.destroy(query(".facility-loader")[0]);
       	 _.each(crMasonryComp.jsonData, function(itemData, indx) {
       		 i++;

       		 item = {"item": itemData};
       		 index = crMasonryComp.getSmallestComponentIndex();
       		 crMasonryComp.setComponentPosition(index);
       		 crMasonryComp.renderComponent(item, i);

       		//gets latest created node from the domNode
             var len =  query("div.cr-masonry-section", crMasonryComp.domNode).length;
             var newNode = query("div.cr-masonry-section", crMasonryComp.domNode)[len-1];
             domStyle.set(newNode, {
            	"position" :"absolute",
            	"left": crMasonryComp.left+"px",
            	"top": crMasonryComp.top+"px"
             });
             //adding height of the new node
             crMasonryComp.calcHeightList[index] += ( newNode.offsetHeight);
             crMasonryComp.updateDomNodeHeight();
             parser.parse( dojo.query(".cr-masonry-section")[indx] );			
       	 });

       },

       setComponentPosition: function(index){
      	 var crMasonryComp = this;
      	//sets top and left
      	 crMasonryComp.top = crMasonryComp.calcHeightList[index];
      	 crMasonryComp.left = crMasonryComp.colPositions[index];
      },

      updateDomNodeHeight: function(){
       	 var crMasonryComp = this;
         var domHeight = _.max(crMasonryComp.calcHeightList);
         domStyle.set(crMasonryComp.domNode, {
    		"height" :domHeight+"px"
    	 });
       },

      renderComponent: function(item, i){
      	var crMasonryComp = this;
      	//filtering small images for mansorny components
      	var smallImages = _.filter(item.item.galleryImages , function(item) {
  								return (item.size === "small");
  						  });
  		item.item["smallImages"] = smallImages;
      	//renders the html
      	if(crMasonryComp.componentName === "facility"){
      		crMasonryComp.renderTemplate(crMasonryComp.tmplTwo, item, i);
      	}else if(crMasonryComp.componentName === "UNE"){
      		item.componentName = "UNE";
      		if(  !item.item.menuList[0] || !item.item.menuList[0].galleryImages  ||
      		  (item.item.menuList[0].galleryImages && item.item.menuList[0].galleryImages.length != 4)
      		  ){
      			item.item.menuList[0] = "";
      		}else{
      			item.item.menuListJson = dojo.toJson(item.item.menuList[0]);
      		}

      		crMasonryComp.renderTemplate(crMasonryComp.tmplThree, item, i);
      	}else {
      		crMasonryComp.renderTemplate(crMasonryComp.tmplOne, item, i);
      	}
     },

    renderTemplate : function(tmpl, data, index){ 
        var crMasonryComp = this; 
        data.divCount = index; 
        html = crMasonryComp.renderTmpl(tmpl, data); 
        domConstruct.place(html, crMasonryComp.domNode, "last"); 
        crMasonryComp.initializeMediaPopup(data, index); 
     },


     //creating Media pop instance
     initializeMediaPopup: function(item, i){

    	 var crMasonryComp = this;
    	 var len =  query("div.cr-masonry-section", crMasonryComp.domNode).length;
         var newNode = query("div.cr-masonry-section", crMasonryComp.domNode)[len-1];
         // Media pop up instance
         var videoPopupNode = query("a.view-images", newNode)[0];
         if(videoPopupNode != undefined && item.item.videoCode != null ) {
		     crMasonryComp.videoNode.push(new VideoPopup({
		    	 videoId: item.item.videoCode //5f545e72ca984f4ef741cbc463f6c3d23f395f27
		     },videoPopupNode));
    	 }
    	 //Video pop up instance
    	 var mediaPopupNode = query("a.play-media", newNode)[0];
    	 if(mediaPopupNode != undefined ) {
    		 crMasonryComp.mediaNode.push(new MediaPopup({
		         jsonData: item.item
		     },mediaPopupNode));
    	 }
     },

     //This will return the index of the smallest component in terms of height.
     getSmallestComponentIndex: function(){
     		var crMasonryComp = this;
     		return _.indexOf(crMasonryComp.calcHeightList, _.min(crMasonryComp.calcHeightList));
     }

    });
});


