define("tui/widget/booking/infantoptions/view/FinishingTouchesComponents", [
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/query",
    "dojo/dom-construct",
    "tui/widget/booking/constants/BookflowUrl",
    "tui/widget/booking/infantoptions/view/PrebookInfantEquipment"
    ], function(declare,dom,query,domConstruct,BookflowUrl) {

	return declare("tui.widget.booking.infantoptions.view.FinishingTouchesComponents",
			[tui.widget.booking.infantoptions.view.PrebookInfantEquipment], {
		Obj2OLUrl:BookflowUrl.stageschoolurl,
		obj1Url:BookflowUrl.classActurl,
		obj2Url:BookflowUrl.finishingtouchesremoveurl, /*Only for cruise option page finishing touches component remove functionality*/
		contentRefObjPath:"roomOptionsContentViewData.roomContentViewData",

	setContentValues: function(){
		var objetRef = this.bookflowMessaging["InfantAndcelebStye"];



		this.heading= this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment[this.refHeading].heading;
		this.overlayHeading = objetRef.overlayHeading = this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.the_class_of_act_gen_info_displayName;
		this.genericObj={};
		this.genericObj.classNam= this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.classDiff;
		this.genericObj.buttonClasNam= this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.buttonClassName;



		if(!this.obj1.available){
			this.imageSRC1	= objetRef["obj2"].imgSrc = "";
			this.strapLine1 = objetRef["obj2"].strapLine = "";
			this.intro1 = objetRef["obj2"].intro ="";

		}else{

			this.imageSRC1	= objetRef["obj2"].imgSrc = _.isUndefined(this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.the_class_of_act_gen_info_Key_Value_One_small) ? " " : this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.the_class_of_act_gen_info_Key_Value_One_small;
			this.strapLine1 = objetRef["obj2"].strapLine = "";
			this.intro1 = objetRef["obj2"].intro =_.isUndefined(this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.the_class_of_act_gen_info_Key_Value_One_Body) ? " " : this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.the_class_of_act_gen_info_Key_Value_One_Body;
		}


		this.imageSRC2	= objetRef["obj2"].imgSrc = this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_StrapLine_small;
		this.strapLine2 = objetRef["obj2"].strapLine = "";
		this.intro2 = objetRef["obj2"].intro =this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_Key_Value_One_Body;

		this.buttonText = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment[this.refHeading].buttonText;

		this.staticContentPer = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment[this.refHeading].statciContent;

		this.crechOverlayData1 = objetRef["obj2"].crechOverlayData={"imageSrc":this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_StrapLine_small ,
				"usp":this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_usp,
				"descriptionTitle":this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_Key_Value_One_Title,
				"descriptionIntro":this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.stage_academy_intro1
				};



		},

	refresh : function(field,response) {

		if(field == "creche" || field == "stage" || field == "infant" ){
			this.jsonData = response;
			this.model = response.extraFacilityViewDataContainer;
			var obj2 = this.obj2 = this.getContentFunction(this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment[this.refHeading].stageOptionRef,response.extraFacilityViewDataContainer);
			var obj1 = this.obj1= this.getContentFunction(this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment[this.refHeading].workshopOptionsRef,response.extraFacilityViewDataContainer);
			this.destroyRecursive();
			var node = domConstruct.create("div", null, dom.byId("infantOptionsHolder"));
			this.create({
				"jsonData" : response,
				"obj2": obj2,
				"obj1": obj1,
				"transitionType" : 'WipeInOut'
			}, node);
			 if(this.removeInfantLinkFlag == true){
					this.removeInfantLinkFlag = false;
					var prebookMgs = dom.byId("prebook-mgs");
					var getLCRRemoveText = query('.prebook-removed',this.domNode);
					this.removeLCR(this.categoryInfantType+" "+"has been removed", prebookMgs,getLCRRemoveText);
				}
			 if(this.removeCrecheLinkFlag == true){
					this.removeCrecheLinkFlag = false;
					var crecheMgs = dom.byId("creche-mgs");
					 var getLCRRemoveText = query('.creche-removed',this.domNode);
				       this.removeLCR(this.categoryInfantType+" "+"has been removed", crecheMgs, getLCRRemoveText);

				}
		}



		}

	});
});