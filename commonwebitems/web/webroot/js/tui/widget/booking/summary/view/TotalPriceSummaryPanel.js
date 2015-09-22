define("tui/widget/booking/summary/view/TotalPriceSummaryPanel", [
                                                                  "dojo",
                                                                  "dojo/_base/declare",
                                                                  "dojo/query",
                                                                  "dojo/dom-class",
                                                                  "dojo/on",
                                                                  'dojo/dom',
                                                                  "dojo/_base/lang",
                                                                  "dojo/text!tui/widget/booking/summary/view/templates/TotalPriceSummaryPanel.html",
                                                                  "dojo/dom-construct",
                                                                  "dojo/_base/xhr",
                                                                  "dojo/dom-attr",
                                                                  "dojo/parser",
                                                                  "tui/widget/booking/constants/BookflowUrl",
                                                                  "dojo/dom-style",
                                                                  "tui/widget/booking/constants/TotalPrice"

                                                                  ], function (dojo,declare, query, domClass,on,dom,lang,TotalPriceSummaryPanel, domConstruct,xhr,domAttr, parser,BookflowUrl,domStyle,TotalPrice) {

	return declare("tui.widget.booking.summary.view.TotalPriceSummaryPanel", [tui.widget._TuiBaseWidget], {

		tmpl: TotalPriceSummaryPanel,
		controller: null,
		removeFlag: false,


		postCreate: function () {
			this.controller = dijit.registry.byId("controllerWidget");
			this.controller.registerView(this);
			this.renderWidget();
			this.attachEventsForSaveLink();
			this.inherited(arguments);
			if (this.jsonData.summaryViewData.currentPage !== "confirmation"){
				domAttr.set(dom.byId('newprice'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,'poundSize','number','decimal'));
			}
			this.tagElements(query('.save'),"savePackage");
		},

		attachEventsForSaveLink : function(){
			this.saveLink = query(".savecall", this.domNode);
			_.each(this.saveLink, lang.hitch(this, function(saveLink) {
				on(saveLink,'click', lang.hitch(this, this.handleSaveButton, saveLink));
			}));
			this.removeLink = query(".rremove", this.domNode)[0];
			if(this.removeLink != null){
				on(this.removeLink,'click', lang.hitch(this, this.handleRemoveButton));
			}


		},

		refresh: function (field, response) {
			this.jsonData = response;
			this.renderWidget();
			this.attachEventsForSaveLink();
			if (this.jsonData.summaryViewData.currentPage !== "confirmation"){
				domAttr.set(dom.byId('newprice'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,'poundSize','number','decimal'));
			}
		},

		handleSaveButton : function () {
			var requestData={};
			var url =BookflowUrl.saveholidayurl;
			this.generateRequest("saveholiday",url);
		},
		handleRemoveButton : function () {
			var requestData={};
			var url =BookflowUrl.removeHolidayUrl;
			this.removeFlag=true;
			this.generateRequest("removeholiday",url);
		},
		closeFadeout: function(){
			domStyle.set("saveSummary",{
				"display":"none"
			});
		},

		renderWidget: function () {
			var template = new dojox.dtl.Template(this.tmpl),
			context = new dojox.dtl.Context(this),
			html;
			html = template.render(context);
			domConstruct.place(html, this.domNode, "only");
			parser.parse(this.domNode);
		},

		generateRequest: function(field,value,contentValue)
		{
			var widget =this;
			var results = xhr.post({
				url: value,
				content: contentValue,
				handleAs: "json",
				headers: {Accept: "application/javascript, application/json"},
				error: function (err) {
					if (dojoConfig.devDebug) {
					}
					widget.afterFailure(err.responseText);
				}
			});


			dojo.when(results,function(response){
				if(response.resFlag)
				{
					if(response.resFlag == "0"){

						widget.saveLink = query(".save", widget.domNode);
						widget.savedLink = query(".saved", widget.domNode);
						domStyle.set(widget.saveLink[0], "display", "none");
						domStyle.set(widget.savedLink[0], "display", "block");

						if(widget.removeFlag){
							widget.removeLink = query(".rremove", widget.domNode)[0];
							domStyle.set(widget.removeLink, "display", "none");
							domStyle.set(widget.saveLink[0], "display", "block");
							domStyle.set(widget.savedLink[0], "display", "none");
						}
						var fadeoutHtml =
							"<div style='positon:relative'> <div style='positon:absolute;top:3px;right:3px;float:right;'><a id='prebook-closeqty'  href='javascript:void(0)'>X</a></div><div style='border:1px'>"+ response.message+ "</div></div>";
						domConstruct.place(domConstruct.toDom(fadeoutHtml), "saveSummary", "only");
						on(dom.byId("prebook-closeqty"), "click", lang.hitch(widget, widget.closeFadeout));
						domStyle.set("saveSummary",{
							"display":"block",
							"opacity":"1",
							"backgroundColor": "#FFFF99",
							"min-height":"40px",
							"float": "left",
							"margin-top": "5px",
						    "padding": "5px"

						});
						var fadeArgs = {
								node: "saveSummary",
								duration: 5000,
								onEnd: function () {
									domStyle.set("saveSummary",{
										"display":"none"
									});
								}
						};
						dojo.fadeOut(fadeArgs).play();
						widget.getShorListedHolidaysCount();
					}

					else if(response.resFlag=="1"){
						console.log("res equal 1");
						widget.saveLink = query(".save", widget.domNode)[0];
						domStyle.set(widget.saveLink, "display", "none");
						widget.removeLink = query(".rremove", widget.domNode)[0];
						domStyle.set(widget.removeLink, "display", "block");
						var fadeoutHtml =
							"<div style='positon:relative'> <div style='positon:absolute;top:3px;right:3px;float:right;'><a id='prebook-closeqty'  href='javascript:void(0)'>X</a></div><div style='border:1px'>"+ response.message+ "</div></div>";
						domConstruct.place(domConstruct.toDom(fadeoutHtml), "saveSummary", "only");
						on(dom.byId("prebook-closeqty"), "click", lang.hitch(widget, widget.closeFadeout));
						domStyle.set("saveSummary",{
							"display":"block",
							"opacity":"1",
							"backgroundColor": "#FFFF99",
							"min-height":"40px",
							"float": "left",
							"margin-top": "5px",
						    "padding": "5px"

						});
						var fadeArgs = {
								node: "saveSummary",
								duration: 5000,
								onEnd: function () {
									domStyle.set("saveSummary",{
										"display":"none"
									});
								}
						};
						dojo.fadeOut(fadeArgs).play();
					}

				}

			});
		}

	});
});