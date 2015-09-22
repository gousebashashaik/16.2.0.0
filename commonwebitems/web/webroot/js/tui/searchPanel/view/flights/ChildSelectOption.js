define("tui/searchPanel/view/flights/ChildSelectOption", [
  "dojo/_base/declare",
  "dojo/text!tui/searchPanel/view/flights/templates/ChildAgeSelectOption.html", 
  "tui/searchPanel/view/ChildSelectOption"], function (declare, childAgeSelectorTmpl) {

  dojo.declare("tui.searchPanel.view.flights.ChildSelectOption", [tui.searchPanel.view.ChildSelectOption], {


    postCreate: function () {
      var childSelectOption = this;    
      childSelectOption.renderTmpl(childAgeSelectorTmpl, childSelectOption);
      childSelectOption.inherited(arguments);
     
      
    }
  });
  
  return tui.searchPanel.view.flights.ChildSelectOption;
});
