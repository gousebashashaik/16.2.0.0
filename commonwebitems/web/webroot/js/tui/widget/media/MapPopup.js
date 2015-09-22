define("tui/widget/media/MapPopup", [
  "dojo",
  "tui/widget/maps/Mappers",
  "dojo/text!tui/widget/media/templates/MapPopupTmpl.html",
  "tui/widget/popup/Popup"], function (dojo, Mappers, MapPopupTmpl, popup) {

  dojo.declare("tui.widget.media.MapPopup", [tui.widget.popup.Popup], {

    videoId: null,

    modal: true,

    tmpl: MapPopupTmpl,

    name: null,

    jsonData: null,

    capitalCity: null,

    postCreate: function () {
      var mediaPopup = this;
      mediaPopup.inherited(arguments);
      //mediaPopup.jsonData = Mappers.thumbnailSet(mediaPopup.jsonData);
      //console.log(mediaPopup.name);
    },

    onAfterTmplRender: function () {
      var mapPopup = this;
      mapPopup.inherited(arguments);
      var domNode = mapPopup.domNode;
    }

  });

  return tui.widget.media.MapPopup;
});