define("tui/recommendations/recommendation", [
	"dojo", 
	"dojo/cookie",
	"dojo/io/script",
	"dojo/_base/lang",
	"dojo/parser",
	"dojo/text!tui/recommendations/view/templates/recommendationsHomeTmpl.html",
	"tui/widget/mixins/Templatable"
	], function (dojo, cookie, script, lang, parser,tmpl) {

	dojo.declare("tui.recommendations.recommendation", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
		accomAry : {},
		accomIds: '',
		browseTmpl: tmpl,
		itmCntLimit: 3,
		tmplPlaceHolder: '',
		identifierFetchAjaxUrl: '/ws/baynote?productCodes[]=',
		bayNoteQuery : {
						cn:'tui', 
						cc:dojoConfig.site, 
						guide:'HomeRec',
						attrList:"BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,TRATING,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE",
						outputFormat: 'XML'
						},
		
		bayNoteURL: "https://tui-"+dojoConfig.site+".baynote.net",
//http://localhost:9001/destinations/ws/baynote?productCodes[]=000288
		postMixInProperties: function(){
			var rec = this;
			rec.inherited(arguments);
			lang.mixin(dojo.io.script, {
			
				urlEndFlag: "v=1",
				
				_makeScriptDeferred: function(args){
						
						var mxnScrObj = this;
						var dfd = dojo._ioSetArgs(args, mxnScrObj._deferredCancel, mxnScrObj._deferredOk, mxnScrObj._deferredError);

						var ioArgs = dfd.ioArgs;
						ioArgs.id = dojo._scopeName + "IoScript" + (mxnScrObj._counter++);
						ioArgs.canDelete = false;

						ioArgs.jsonp = args.callbackParamName || args.jsonp;
						if(ioArgs.jsonp){
							//Add the jsonp parameter.
							ioArgs.query = ioArgs.query || "";
							if(ioArgs.query.length > 0){
								ioArgs.query += "&";
							}
							ioArgs.query += ioArgs.jsonp
								+ "="
								+ (args.frameDoc ? "parent." : "")
								+ dojo._scopeName + ".io.script.jsonp_" + ioArgs.id + "._jsonpCallback";
								
							ioArgs.frameDoc = args.frameDoc;
							
							mxnScrObj.setUrlEndFlag(ioArgs);

							//Setup the Deferred to have the jsonp callback.
							ioArgs.canDelete = true;
							dfd._jsonpCallback = mxnScrObj._jsonpCallback;
							mxnScrObj["jsonp_" + ioArgs.id] = dfd;
						}
						return dfd; // dojo.Deferred
					},
					
					setUrlEndFlag: function(currArgs){
						var mxnScrObj = this;
						currArgs.query += ( "&" + mxnScrObj.urlEndFlag );
					}
			});
		},
				
		postCreate: function () {
			var rec = this;
			rec.inherited(arguments);
			rec.tmplPlaceHolder = rec.domNode;
			rec.detectBayNoteTag();
			//console.log(JSON.stringify(rec.buildQuery()));
		},

		detectBayNoteTag: function(){
			//Starting a listner for BaynoteAPI as we can't add the BaynoteAPI as a dependency
			var rec = this;
			var clear = setInterval(function(){
				if(typeof BaynoteAPI != "undefined" && typeof BaynoteAPI.getAjaxTag != "undefined" && typeof myPostHandler != "undefined"){
					clearInterval(clear);
					rec.bayNoteRequest();
				}
			}, 300);
			setTimeout(function(){ 
				// if not loaded with in 3 minutes; clear the interval
				rec.displayErrorMessage("BaynoteAPI not loaded");
				clearInterval(clear); 
			}, 1000*60*30);
		},
		bayNoteRequest: function(){
			 var rec = this;
			 if( document.domain.indexOf("localhost") === -1 ){
			 
				 BaynoteAPI.getAjaxTag().show({	server: rec.bayNoteURL, 
												customerId: rec.bayNoteQuery.cn, 
												code: rec.bayNoteQuery.cc
												});
				 BaynoteAPI.getAjaxTag().send({
					bnURL: "/baynote/guiderest",
					onSuccess:	ajaxSuccessHandlr, 
					onFailure: 
					function(error){
						rec.displayErrorMessage("BaynoteAPI Response Failed");
					},
					params:	rec.buildQuery()
				});
			}else{
				ajaxSuccessHandlr(xmlData);
			}
			
			function ajaxSuccessHandlr(xmlStr){
					if (!xmlStr) {
						rec.displayErrorMessage("BaynoteAPI Response Failed");
						return;
					}
					var xml;
					if (window.DOMParser)
					{
					  var parser=new DOMParser();
					  var xml=parser.parseFromString(xmlStr,"text/xml");
					  console.log(xml);
					}else // Internet Explorer
					{
					  var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					  xmlDoc.async=false;
					  xmlDoc.loadXML(xmlStr);
                      xml = xmlDoc;
					}
					rec.data = rec.processBayNoteData(rec.xmlToJson(xml));
					rec.identifierRequest();
					dojo.removeClass(rec.domNode, 'spinner');
				}
			  dojo.addClass(rec.domNode, 'loading');
			  
		},
		
		identifierRequest: function(){
			var rec = this;
			var xhrArgs = {
				url: rec.webrootPath + rec.identifierFetchAjaxUrl + rec.accomIds,
				handleAs: 'json',
				timeout: 0
			};
			var deferred = dojo.xhrGet(xhrArgs);
			
			deferred.then(
			  function(data){
				if (!data) {
					rec.displayErrorMessage("Endeca Response Failed");
					return;
				}
				if(data.accomodationDatas.length < 3) {
					dojo.setStyle(rec.domNode, {"display": "none"});
					dojo.setStyle(dojo.query(".recsDefaultHomePage"), {"display": "none"});
				}
				else {
					dojo.removeClass(rec.domNode, 'loading');
					rec.data = rec.processIdentifierData((data.accomodationDatas)? data.accomodationDatas : data);
					var html = rec.renderTmpl(rec.browseTmpl);
					dojo.place(html, rec.tmplPlaceHolder, 'only');
					rec.tmplPostRender();
				}
				
			  }, 
			  
			  function(error){
				//console.log("An unexpected error occurred", error);
				rec.displayErrorMessage("Endeca Response Failed");
			  } 
			);
		},
		
		processIdentifierData: function(data){
			var rec = this;
			_.each(data, function(itm){
				rec.accomAry[itm.code]['featureCodesAndValues'] = itm.featureCodesAndValues;
				rec.accomAry[itm.code]['productRanges'] = itm.productRanges;
				rec.accomAry[itm.code]['endecaResultFlag'] = true; //Updating flag as result is available
			});
		},
		
		processBayNoteData: function(data){
			var rec = this, 
				cs = "overview//";
				var reqGuideType = rec.bayNoteQuery.guide == "HomeRec" ? data.guides.attributes.gr : data.guideset.guides[0].attributes.gr;
				var welcomeMsg   = rec.bayNoteQuery.guide == "HomeRec" ? data.guides.attributes.w : "";
				data.list = rec.bayNoteQuery.guide == "HomeRec" ? data.guides.r.slice(0,rec.itmCntLimit) : data.guideset.guides[0].r.slice(0,rec.itmCntLimit);
				dojo.byId("rec_welcm_txt") && (dojo.byId("rec_welcm_txt").innerHTML = welcomeMsg);
			_.each(data.list, function(itm){
				var attrObj = {
					baynote_bnrank: itm.attributes.rk, /*Baynote Rank*/
					baynote_guide: itm.attributes.g, /* Algorithm used to compute this recommendation*/
					baynote_req: reqGuideType,
					baynote_pid: ''
				};
				_.each(itm.a, function(attr){
					var key = attr.attributes.n, val = attr.attributes.v;
					if(key == "HOTEL_ID"){
						var idAry = val.split("H");
						val = (idAry.length == 2 && idAry[0] == "" ) ? idAry[1] : val;
					}
					attrObj[key] = val;
				});
				attrObj.baynote_pid = ( attrObj.HOTEL_ID ) ? attrObj.HOTEL_ID : '';
				var resortName = attrObj.RESORT, 
					accurl = attrObj.DEEPLINK_URL;
					
				if ( accurl.indexOf(rec.webrootPath) === -1 ){
					accurl = rec.webrootPath + accurl;
				}
				if ( resortName && accurl.indexOf(cs) != -1 ) {
					var csDestination = "overview/" + resortName.replace(' ', '-') + "/";
					accurl = accurl.replace(cs, csDestination);
				}
				attrObj.DEEPLINK_URL = accurl;
				rec.accomIds += (!rec.accomIds ? '' : ',') + attrObj.HOTEL_ID;
				rec.accomAry[attrObj.HOTEL_ID] = attrObj;
				//If endeca result available for a particular product ID, the below flag need to change to true
				rec.accomAry[attrObj.HOTEL_ID]['endecaResultFlag'] = false;
			});
		},
		
		displayErrorMessage: function(errMsg){
				var rec = this;
				dojo.query(".recs-carousel").style("display", "none");
				dojo.removeClass(rec.domNode, 'loading');
				dojo.addClass(rec.domNode, 'error');
				console.log(errMsg);
		},
		
		buildQuery: function(){
			var rec = this, query = rec.bayNoteQuery, cookVal = "";
			if( cookVal = cookie("bn_u") ) { query.userId = cookVal; }
			if( cookVal = cookie("bnTrail") ) { query.bnTrail = cookVal; }
			if( cookVal = cookie("BN_Segment") ) { query.segment = cookVal; }
			if( cookVal = cookie("BN_conditioncode") ) { query.condition = cookVal; }
			return query;
		},
		
		xmlToJson: function(xml) {
			var rec = this;
			// Create the return object
			var obj = {};
			if (xml.nodeType == 1) { // element
				// do attributes
				if (xml.attributes.length > 0) {
				obj["attributes"] = {};
					for (var j = 0; j < xml.attributes.length; j++) {
						var attribute = xml.attributes.item(j);
						obj["attributes"][attribute.nodeName] = attribute.nodeValue;
					}
				}
			} else if (xml.nodeType == 3) { // text
				obj = xml.nodeValue;
			}

			// do children
			if (xml.hasChildNodes()) {
				for(var i = 0; i < xml.childNodes.length; i++) {
					var item = xml.childNodes.item(i);
					var nodeName = item.nodeName;
					if (typeof(obj[nodeName]) == "undefined") {
						obj[nodeName] = rec.xmlToJson(item);
					} else {
						if (typeof(obj[nodeName].push) == "undefined") {
							var old = obj[nodeName];
							obj[nodeName] = [];
							obj[nodeName].push(old);
						}
						obj[nodeName].push(rec.xmlToJson(item));
					}
				}
			}
			return obj;
		},
		
		tmplPostRender: function(){
			var rec = this;
			parser.parse({ rootNode: rec.domNode });
		}
		
	});

	return tui.recommendations.recommendation;
});
