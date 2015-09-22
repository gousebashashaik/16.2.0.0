define("tui/recommendations/recommendationBrowse", [
	"dojo", 
	"dojo/dom-attr",
	"dojo/parser",
	"dojo/number",
	"dojo/cookie",
	"dojo/text!tui/recommendations/view/templates/recommendationsBrowseTmpl.html",
	"tui/recommendations/recommendation",
	"tui/widget/mixins/Templatable"
	], function (dojo, domAttr, parser, number, cookie, tmpl) {

	dojo.declare("tui.recommendations.recommendationBrowse", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.recommendations.recommendation], {
		
		bayNoteBnTrailurlParameter: '',
		browseTmpl: tmpl,
		itmCntLimit: 999,
		identifierFetchAjaxUrl: '',
		productCode: '',
		bayNoteQuery : {
						cn:'tui', 
						cc:dojoConfig.site, 
						elementIds: 'BN_PersonalizedAccomodations,BN_AccomodationRec',
						deduplicate: true,
						outputFormat:'XML', 
						attrList:"BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,TRATING,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE"
						},
		postCreate: function () {
			var rec = this;
			rec.productCode = rec.bayNoteReqData.productCode;
			rec.bayNoteBnTrailurlParameter = "http://www."+dojoConfig.site+".co.uk"+rec.webrootPath+"/bookaccommodation?productCode="+ rec.productCode;
			rec.identifierFetchAjaxUrl= '/ws/baynoteBrowse?cspSorting='+ rec.bayNoteReqData.cspSorting +'&productCodes[]=';
			rec.inherited(arguments);
			rec.tmplPlaceHolder = dojo.query("UL.plist", rec.domNode)[0];
		},
		
		buildQuery: function(){
			var rec = this, query = rec.bayNoteQuery, cookVal = "";
			if( cookVal = cookie("bn_u") ) { query.userId = cookVal; }

			if( cookVal = cookie("bnTrail") ) {
				var bnTrailDecodedValue = decodeURIComponent(cookVal); 
				var bnTrailDecodedValuesDelimeter = bnTrailDecodedValue.substring(bnTrailDecodedValue.indexOf("[")+1 , bnTrailDecodedValue.lastIndexOf("]"));
				var bnTrailDecodedValueList = bnTrailDecodedValuesDelimeter.split(",");
				//var  bnTrailCookieList = Arrays.asList(bnTrailDecodedValueList);
				if ( rec.bayNoteBnTrailurlParameter ){
					rec.bayNoteBnTrailurlParameter = "\"" + rec.bayNoteBnTrailurlParameter + "\"";
				}
				_.each(bnTrailDecodedValueList, function(url){
					rec.bayNoteBnTrailurlParameter += (!rec.bayNoteBnTrailurlParameter ? "" : ",") + url;
				});
				
				query.bnTrail = "["+rec.bayNoteBnTrailurlParameter+"]";
			}else if( rec.bayNoteBnTrailurlParameter ){
				query.url= rec.bayNoteBnTrailurlParameter;
			}
			
			if( cookVal = cookie("BN_conditioncode") ) { query.condition = decodeURIComponent(cookVal); }
			return query;
		},
		
		tmplPostRender: function(){
			var rec = this, carouselNode = dojo.query(".carousel",rec.domNode)[0];
			domAttr.set(carouselNode, "data-dojo-type", "tui.widget.carousels.Carousel");
			parser.parse({ rootNode: rec.domNode });
		},
		
		processIdentifierData: function(data){
			var rec = this; 
			_.each(data, function(itm){
				rec.accomAry[itm.code]['featureCodesAndValues'] = itm.featureCodesAndValues;
				rec.accomAry[itm.code]['productRanges'] = itm.productRanges;
				var bb = rec.accomAry[itm.code]['boardBasis'] = itm.boardBasis;
				bb[0].price = number.format(bb[0].price, { 	places: 2  }).replace(/.00/g, "");
				rec.accomAry[itm.code]['endecaResultFlag'] = true; //Updating flag as result is available
			});
		}
		
	});

	return tui.recommendations.recommendationBrowse;
});
