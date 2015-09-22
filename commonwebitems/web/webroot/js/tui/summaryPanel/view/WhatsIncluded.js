define("tui/summaryPanel/view/WhatsIncluded", [
    "dojo",
    "dojo/on",
	"dojo/parser",
    "dojo/text!tui/summaryPanel/view/templates/whatsIncluded.html",
    "tui/widget/mixins/Templatable",
    "tui/searchResults/service/RoomGroupingService",
    "tui/search/nls/Searchi18nable"
], function (dojo, on, parser, whatsIncludedTmpl, Templatable, roomGroupService) {

    return dojo.declare("tui.summaryPanel.view.WhatsIncluded", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

        tmpl: whatsIncludedTmpl,

        data: null,

        postCreate: function () {
            var whatsIncluded = this;
            whatsIncluded.initSearchMessaging();
            console.log('Whats Included panel View');
            dojo.removeClass(whatsIncluded.domNode, "loading");
            whatsIncluded.renderContent();
            dojo.subscribe("tui/summaryPanel/controller/updateSummaryPanel", function (data) {
               whatsIncluded.clear();
               whatsIncluded.data = data;
               whatsIncluded.renderContent();
            });
        },

        clear: function(){
           var whatsIncluded = this;
           dojo.disconnect(whatsIncluded.removeStayOverlayTrigger);
           dojo.disconnect(whatsIncluded.removeStayClose);
           dojo.disconnect(whatsIncluded.removeHotelHandle);
           whatsIncluded.domNode.innerHTML = '';
        },

        fetchData: function(data){
            var whatsIncluded = this;
            var packageInfo = data.packageInfo;
			var selectedResult = data.selectedResult;
            var tracs = (selectedResult.variantNInv.indexOf('TRACS') > -1);
            var accomApplicable = ((selectedResult.variantNInv === 'CRUISE_STAY_TRACS_HOTEL') || (selectedResult.variantNInv === 'CRUISE_STAY_ATCOM_HOTEL') || (selectedResult.variantNInv === 'STAY_CRUISE_TRACS_HOTEL') || (selectedResult.variantNInv === 'STAY_CRUISE_ATCOM_HOTEL'));
            var rooms = roomGroupService.groupRooms(selectedResult.accommodation.rooms, "roomType");
            var cabins = roomGroupService.groupCabins(selectedResult.sailings[0].cabins, "roomType");
            var differentiatedProduct  = data.selectedResult.sailings[0].differentiatedProduct && data.selectedResult.sailings[0].differentiatedProduct.differentiatedProduct ? data.selectedResult.sailings[0].differentiatedProduct : null;

            return {
            	tracs: tracs,
                accomDetails: whatsIncluded.accomDetails,
                showItinerary: whatsIncluded.showItinerary,
                itinerary: {
                    name:selectedResult.itinenaries[0].name,
                    ship:data.selectedResult.sailings[0].shipName.name,
                    duration: packageInfo.cruiseDuration,
                    diffProd: differentiatedProduct,
                    productIdentifierCode: _.isNull(differentiatedProduct) ? '' : differentiatedProduct.code,
                    productIdentifierTooltip: _.isNull(differentiatedProduct) ? '' : differentiatedProduct.featureCodesAndValues.strapline[0]
                },
                accommodation: {
                   roomsExist: ( _.size(rooms) > 0 ) ,
                   rooms: rooms,
                   boardbasis: {
                       available: (( _.size(rooms) > 0 ) && accomApplicable) ,
                       name: ( _.size(rooms) > 0 ) ? _.first(data.selectedResult.accommodation.rooms).boardType : ''
                   },
                   roomTooltip: tracs ? whatsIncluded.searchMessaging.summaryPanel.tracsLimitedAvailabilityRoom : whatsIncluded.searchMessaging.summaryPanel.atComLimitedAvailabilityRoom,
                   otherRoomTypes: whatsIncluded.searchMessaging.summaryPanel.otherRoomTypes
                },
                flights: packageInfo.flight, //boolean
                cruiseDuration: packageInfo.cruiseDuration,
                stayDuration: packageInfo.stayDuration,
                stayRemoveAvailable: data.stayTeaser.teaserAvaliable && !tracs,
                price:{
                    withStay:Math.round(_.first(data.selectedResult.sailings).price.perPerson),
                    withoutStay: (Math.round(_.first(data.selectedResult.sailings).price.perPerson)- (Math.round(data.stayTeaser.altPrice.perPerson)))
                },
                cabin: {
                	cabinAry : cabins,
                	limitedAvailabilityCabin: whatsIncluded.searchMessaging.summaryPanel.limitedAvailabilityCabin,
                	otherCabinTypes: whatsIncluded.searchMessaging.summaryPanel.otherCabinTypes,
                	tooltip: _.isNull(packageInfo.cabin.tooltipInfo.tooltip) ? '' : packageInfo.cabin.tooltipInfo.tooltip
                },
                boardbasis: packageInfo.boardBasis.name,
                boardbasisTooltip: whatsIncluded.searchMessaging.summaryPanel.boardBasis[packageInfo.boardBasis.code],
                accomBoardbasisTooltip: whatsIncluded.searchMessaging.summaryPanel.accomBoardBasis[ _.isEmpty(data.selectedResult.accommodation.rooms) || _.isNull(data.selectedResult.accommodation.rooms[0].boardBasisCode) ? '' : data.selectedResult.accommodation.rooms[0].boardBasisCode],
                transfers: packageInfo.transfers, //boolean
                allTipsAndCharges: packageInfo.tipsAndCharges, //boolean
                baggage: false,
                atol: packageInfo.atolProtection, //boolean
                offerMessage: false,
                worldCare: packageInfo.worldCareFund,
                worldCareTooltip:  whatsIncluded.searchMessaging.summaryPanel.worldCare
            };

        },

        renderContent: function(){
            var whatsIncluded = this;
            var tracs = (whatsIncluded.data.selectedResult.variantNInv.indexOf('TRACS') > -1);
            var durationInfo = whatsIncluded.data.selectedResult.sailings[0].outbound;
            var html = whatsIncluded.renderTmpl(whatsIncluded.tmpl, whatsIncluded.fetchData(whatsIncluded.data));
            var dom = dojo.place(html, whatsIncluded.domNode, "last");

            if( !whatsIncluded.accomDetails ){
            	//Adding clear LIs to avoid alignment issues in Itinerary page alone.
            	//Will be executed while loading the page and adding, removing stays
	            _.each(dojo.query("li.clear-it", whatsIncluded.domNode), function(liEle, inx){ dojo.destroy(liEle); });
	            var includedLis = dojo.query("li", whatsIncluded.domNode);
	            _.each(includedLis, function(liEle, inx){
	            	if( (inx + 1) % 3  === 0 || includedLis.length-1 == inx ){
	            		dojo.place("<li class='clear-it'></li>", liEle, "after");
	            	}
	            });
            }else{
            	//DE39756 - alignment issue fix
            	var liBorderElems = dojo.query(".included li.border", whatsIncluded.domNode);
            	var liIncludedElems = dojo.query(".included li", whatsIncluded.domNode);
            	var pos = liIncludedElems.indexOf(liBorderElems[1]) -  liIncludedElems.indexOf(liBorderElems[0]) - 1;
            	if( pos > 3 ){
            		dojo.place("<li class='clear-it'></li>", dojo.query(liBorderElems[0]).next().next().next()[0], "after");
            	}
            }
            var departDate = dojo.date.locale.parse(durationInfo.schedule.departureDate, {datePattern: "EEE dd MMM yyyy", selector: 'date'})
            var changePaxdate = dojo.date.locale.format(departDate,{selector: "date", datePattern: "dd MMMM yyyy" });
            dojo.byId("changePaxDeptDate") && ( dojo.byId("changePaxDeptDate").innerHTML =  changePaxdate );

			parser.parse(whatsIncluded.domNode);
            if(true){
                whatsIncluded.removeStayOverlayTrigger =  on(whatsIncluded.domNode, on.selector(".remove-stay", "click"), function (event) {
                    whatsIncluded.showOverlay();
                    //dojo.publish("tui/summaryPanel/controller/updateSummaryPanel",addAStay.data.requestData);
                });
                whatsIncluded.removeStayClose = on(whatsIncluded.domNode, on.selector(".remove-stay-overlay-close", "click"), function (event) {
                    whatsIncluded.hideOverlay();
                    //dojo.publish("tui/summaryPanel/controller/updateSummaryPanel",addAStay.data.requestData);
                });
                whatsIncluded.removeHotelHandle = on(whatsIncluded.domNode, on.selector(".remove-hotel-from-itinerary", "click"), function (event) {
                    whatsIncluded.data.requestData.searchRequest.addAStay = whatsIncluded.data.stayTeaser.duration + ':'+whatsIncluded.data.requestData.searchRequest.addAStay;
                    dojo.publish("tui/summaryPanel/controller/removeStay", tracs ? whatsIncluded.data.requestData : whatsIncluded.data.stayTeaser.pkgRequestupdated);
                    whatsIncluded.hideOverlay();
                    //whatsIncluded.showOverlay();
                    //dojo.publish("tui/summaryPanel/controller/updateSummaryPanel",addAStay.data.requestData);
                });
            }
        },

        showOverlay: function(){
            var whatsIncluded = this;
            whatsIncluded.showWidget(dojo.query(".remove-stay-overlay", whatsIncluded.domNode)[0]);
        },

        hideOverlay: function(){
            var whatsIncluded = this;
            whatsIncluded.hideWidget(dojo.query(".remove-stay-overlay", whatsIncluded.domNode)[0]);
        }
    });
});