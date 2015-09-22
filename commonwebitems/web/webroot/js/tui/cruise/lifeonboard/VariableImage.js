define('tui/cruise/lifeonboard/VariableImage', [
    'dojo',
    'dojo/on',
    'dojo/query',
    'dojo/dom-style',
    'dojo/text!tui/cruise/lifeonboard/templates/VariableImage.html',
    'dojo/text!tui/cruise/lifeonboard/templates/OverlayMask.html',
    'tui/widget/media/HeroCarousel',
    'tui/widget/media/MediaPopup',
    'tui/widget/media/VideoPopup',
    'tui/widget/mixins/Templatable'
], function(dojo, on, query, domStyle, tmpl, overlayMask , HeroCarousel, MediaPopup, VideoPopup) {
    dojo.declare('tui.cruise.lifeonboard.VariableImage', [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        jsonData: null,

        index:0,

        tmpl: tmpl,

        heroWidget: null,
        
        width:"318",
        
        height:"260",
        
        imagedefaultSize: "medium",

        link: null,

        postCreate: function() {
            var variableImage = this;
            variableImage.inherited(arguments);

            var galleryImagesOrg = variableImage.jsonData.galleryImages || variableImage.jsonData.genericNonGeoListComponentViewData[variableImage.index].galleryImages;
            var video = _.first(variableImage.jsonData.galleryVideos ||  variableImage.jsonData.genericNonGeoListComponentViewData[variableImage.index].galleryVideos);

            var galleryImages = _.uniq(dojo.filter(galleryImagesOrg, function(item) {
                return (item.size === variableImage.imagedefaultSize);
            }), 'code');

            var data ={
                carousel: _.size(galleryImages)>1,
                galleryImages: galleryImages,
                imageUrl: galleryImages[0].mainSrc,
                width: variableImage.width,  
                height:	variableImage.height
            };

            var html = variableImage.renderTmpl(null, data);
            dojo.place(html, variableImage.domNode , "last");

            if( _.size(galleryImages)>1)
            {
                var heroCarouselDomNode = query(".slideshow", variableImage.domNode)[0];
                variableImage.heroWidget = new HeroCarousel({
                    jsonData: {
                        'galleryImages': galleryImages
                    },
                    imagedefaultSize: variableImage.imagedefaultSize
                }, heroCarouselDomNode);
                var viewport = query(".viewport", variableImage.domNode)[0];
                domStyle.set(viewport, "width",  variableImage.width+"px" );
                domStyle.set(viewport, "height", variableImage.height+"px");
            }
            if(variableImage.link)
                variableImage.setupLink(galleryImages, galleryImagesOrg, video);


        },

        setupLink : function(galleryImages, galleryImagesOrg, video){
            var variableImage = this;
            if(_.size(galleryImages)>0)
            {
                var html = variableImage.renderTmpl(overlayMask, '');
                dojo.place(html, variableImage.domNode , "last");

                var mediaPopupDomNode = query(".mediaPopup", variableImage.domNode)[0];
                var mediaPopupNode = '';
                if(_.isUndefined(video))
                {
                    var xLargeGalleryImages = _.uniq(dojo.filter(galleryImagesOrg, function(item) {
                        return (item.size === 'xlarge');
                    }), 'code');
                    mediaPopupNode = new MediaPopup({
                        jsonData: {
                            'galleryImages': xLargeGalleryImages
                        }
                    },mediaPopupDomNode);
                    console.log(query(".viewlarge", variableImage.domNode)[0]);
                    query(".viewlarge", variableImage.domNode)[0].innerHtml = 'View Large';
                }
                else
                {
                    mediaPopupNode = new VideoPopup({
                        'videoId': video.code
                    },mediaPopupDomNode);
                    console.log('test thr---'+query(".viewlarge", variableImage.domNode)[0].innerHTML);
                    query(".viewlarge", variableImage.domNode)[0].innerHTML = 'Play Video';
                }
            }
        }

    });

    return tui.cruise.lifeonboard.VariableImage;
});
