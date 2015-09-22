define("tui/bookFlow/LoginAction", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widgetFx/Transitiable",
  "tui/widgetFx/WipeTransitions",
  "dojo/NodeList-traverse",
  "dojo/dom-class"], function(dojo) {

  dojo.declare("tui.bookFlow.LoginAction", [tui.widget._TuiBaseWidget, tui.widgetFx.Transitiable], {

    /*openLogin: function() {
      var loginSwitch = this.domNode,
        children = loginSwitch.childNodes,
        loginForm = dojo.query(".loginForm")[0];

      for (var i = children.lenght in children) {
        if (children[i].checked == true) {
          var selectedRadio = children[i].value;
        }
      };

      console.log(selectedRadio);
      var selectedRadio = this.value;

      if (selectedRadio == "signup") {
        dojo.addClass(loginForm, "open");
      } else {
        dojo.removeClass(loginForm, "open");
      }

    },*/


    postCreate: function() {
      
      //var selectedRadio = this.domNode,
      //    selectedValue = selectedRadio.value;
      //
      //dojo.connect(selectedRadio, "onClick", function(){
      //  if(selectedValue == "signin"){
      //    
      //  }
      //})

    }
  });

  return tui.bookFlow.LoginAction;
});