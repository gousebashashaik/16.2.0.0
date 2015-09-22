define("tui/widget/booking/inflightmeals/view/InflightMealView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/inflightmeals/view/templates/InflightMealView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget, dtlTemplate, Templatable, InflightMealView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.inflightmeals.view.InflightMealView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: InflightMealView,
    templateString: "",
    widgetsInTemplate: true,
    childAgeFlag: false,
    childMealAvailable: false,
    adultorseniorFlag: "",


    postMixInProperties: function () {
      var jsonData = this.inflightMealData.packageViewData.passenger;
      for (var index = 0; index < jsonData.length; index++) {
        if (jsonData[index].type == 'ADULT' || jsonData[index].type == 'SENIOR') {
          this.adultorseniorFlag = 'ADULT';
        } else if (jsonData[index].type == 'SENIOR') {
          this.adultorseniorFlag = 'SENIOR';
        }

        if (jsonData[index].type == 'CHILD') {
          if(jsonData[index].selectedMealOption != null){
        	  
        	  jsonData[index].childMealAvailable = true;
        	  
          if (jsonData[index].selectedMealOption.maxChildAge == 99) {
            this.childAgeFlag = true;
            jsonData[index].childAgeFlag = true;
          }else {
            jsonData[index].childAgeFlag = false;
          }
          }else{
        	  jsonData[index].childMealAvailable = false; 
          }
        	
        	
        	
        }
      }

    },

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.selectionAttachPoints = query('select[data-dojo-attach-point$="SelectionBox"]', this.domNode);
      this.attachEvents();
      this.inherited(arguments);
      var mealSelect = dojo.query('.selectMeal', this.domNode);
      var i = 1, j = 1;
      for(var index = 0; index < mealSelect.length; index++){
    	 if(mealSelect[index].id == 'Adult'){
    		 this.tagElement(mealSelect[index],"adult"+i+"Meal");
    		  i++;
    	  }else{
    		  this.tagElement(mealSelect[index],"child"+j+"Meal");
    		  j++;
    	  }
	 }
     this.tagElements(dojo.query('a.button ', this.domNode),"cancelMeal");
     this.tagElements(dojo.query('a.close ', this.domNode),"closeMeal");
     this.tagElements(dojo.query('button.button ', this.domNode),"continueMeal");

    },

    attachEvents: function () {
      on(this.okButton, "click", lang.hitch(this, this.handleOkButton));
    },


    handleOkButton: function () {
      var responseObj = [];
      var ajaxobj = {};
      _.each(this._attachPoints, lang.hitch(this, function (attachPoint) {
        var selectionAttachPoint = this[attachPoint];
        var selectedAttachPoint = attachPoint.split('|');
        if (selectionAttachPoint instanceof tui.widget.form.SelectOption) {
          var key = selectionAttachPoint.getSelectedData().value;


          if(!responseObj[key])
          {
        	  var temparray = [];
        	  temparray.push(selectionAttachPoint.id);
              responseObj[key] = temparray;
          }
          else{
        	  responseObj[key].push(selectionAttachPoint.id);
          }

        }else if(selectedAttachPoint[1] == "childDiv"){
        	var childDivChild = this[attachPoint];
        	var nameVlue = childDivChild.id.split('|');
        	var key = nameVlue[1];
        	 if(!responseObj[key])
             {
           	  var temparray = [];
           	temparray.push(nameVlue[0]);
                 responseObj[key] = temparray;
             }
             else{
           	  responseObj[key].push(nameVlue[0]);
             }
        }

      }));
      for(var key in responseObj){
    	  var tempArray = responseObj[key];
    	  ajaxobj[key] = tempArray.toString();

      }

      var url =BookflowUrl.inflightmealurl ;
      var requestData = {selected: dojo.toJson(ajaxobj)};
      this.controller.generateRequest("meal", url, requestData);

    }


  });
});