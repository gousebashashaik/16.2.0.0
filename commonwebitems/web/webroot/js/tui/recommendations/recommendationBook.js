define("tui/recommendations/recommendationBook", [
	"dojo", 
	"dojo/dom-attr",
	"dojo/parser",
	"dojo/number",
	"dojo/cookie",
	"dojo/date/locale",
	"dojo/text!tui/recommendations/view/templates/recommendationsBookTmpl.html",
	"tui/recommendations/recommendation",
	"tui/mvc/Controller",
	"tui/widget/mixins/Templatable"
	], function (dojo, domAttr, parser, number, cookie, dtLocale, tmpl) {

	dojo.declare("tui.recommendations.recommendationBook", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.recommendations.recommendation], {
		
		bayNoteBnTrailurlParameter: '',
		browseTmpl: tmpl,
		itmCntLimit: 999,
		identifierFetchAjaxUrl: '',
		productCode: '',
		bayNoteQuery : {
						cn:'tui', 
						cc:dojoConfig.site, 
						elementIds: 'BN_PersonalizedPackages,BN_PackageDetailsRec',
						deduplicate: true,
						outputFormat:'XML', 
						attrList:"BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,TRATING,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE"
						},
		postCreate: function () {
			var rec = this;
			rec.productCode = rec.bayNoteReqData.productCode;
			rec.bayNoteBnTrailurlParameter = "http://www."+dojoConfig.site+".co.uk"+rec.webrootPath+"/bookaccommodation?productCode="+ rec.productCode;
			rec.identifierFetchAjaxUrl= '/ws/baynoteBook?cspSorting='+ rec.bayNoteReqData.cspSorting +'&productCodes[]=';
			
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
			if( rec.bayNoteReqData.departureDate ){
				var dt = rec.bayNoteReqData.departureDate.split("-");
				//in JS month is 1 index based
				dt = new Date(dt[2], Number(dt[1])-1, dt[0]); //year: 2, month: 1, date: 0
				query.depDate = dtLocale.format( dt, {selector:"date", datePattern:"yyyy-MM-d" } ); ;
			}
			if( rec.bayNoteReqData.depAirport ) { query.depAirport = rec.bayNoteReqData.depAirport; }
			if( rec.bayNoteReqData.rating ) { query.rating = rec.bayNoteReqData.rating; }
			if( rec.bayNoteReqData.bestFor ) { query.BEST_FOR = rec.bayNoteReqData.bestFor; }
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
				if( itm.boardBasis ){
					var bb = rec.accomAry[itm.code]['boardBasis'] = itm.boardBasis;
					bb[0].price = number.format(bb[0].price, { 	places: 2  }).replace(/.00/g, "");
				}
				rec.accomAry[itm.code]['book_link'] = itm.recBookUrl;
				rec.accomAry[itm.code]['duration'] = itm.duration;
				rec.accomAry[itm.code]['departurePoint'] = itm.departurePoint;
				rec.accomAry[itm.code]['recBoardBasis'] = itm.recBoardBasis;
				rec.accomAry[itm.code]['departureDate'] = "";
				var dtAry  = itm.departureDate ? itm.departureDate.split("-") : "";
				if(dtAry && dtAry.length >= 3){
					//in JS month is 1 index based
					var dt = new Date( dtAry[2], Number(dtAry[1])-1, dtAry[0]); //year:2, month: 1, date: 0
					rec.accomAry[itm.code]['departureDate'] = dtLocale.format( dt, {selector:"date", datePattern:"EEE d MMM yyyy" } ); 
				}
				rec.accomAry[itm.code]['endecaResultFlag'] = true; //Updating flag as result is available
			});
		}
		
	});

	return tui.recommendations.recommendationBook;
});
