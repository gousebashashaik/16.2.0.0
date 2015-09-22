define("tui/widget/booking/passengers/PassengerUtils", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/_base/array",
  "dojo/dom-style"
], function (declare, query, domAttr, domConstruct, domClass,  topic, lang, arrayUtils, domStyle) {
  var PassengerUtils =  declare('tui.widget.booking.passengers.PassengerUtils',[], {

    displayMessagesForSelect: function (message, selectBox, selectBlock) {

      if (message) {
        if (selectBox.errorTextBoxNode && selectBox.errorTextBoxNode !== null) {
          domAttr.set(selectBox.id + "_Error", "innerHTML", message);
        } else {
          selectBox.errorTextBoxNode = "<div id= '" + selectBox.id + "_Error' class='error-notation'>" + message + "</div>";
          domConstruct.place(selectBox.errorTextBoxNode, selectBlock, "after");

        }
      } else {
        if (selectBox.errorTextBoxNode && selectBox.errorTextBoxNode !== null) {
          domConstruct.destroy(selectBox.id + "_Error");
          selectBox.errorTextBoxNode = null;
        }
      }
    },



    getDateInCorrectFormat: function (value) {
      var objDate,  // date object initialized from the date string
          mSeconds, // date in milliseconds
          day,      // day
          month,    // month
          year;     // year
      // third and sixth character should be '/'
      if (value.substring(2, 3) !== '/' || value.substring(5, 6) !== '/') {
        return false;
      }
      // extract month, day and year from the date (expected format is dd/mm//yyyy)
      // subtraction will cast variables to integer implicitly (needed
      // for !== comparing)
      day = value.substring(0, 2) - 0;
      month = value.substring(3, 5) - 1; // because months in JS start from 0
      year = value.substring(6, 10) - 0;
      // test year range
      if (year < 1000 || year > 3000) {
        return false;
      }
      // convert ExpiryDate to milliseconds
      mSeconds = (new Date(year, month, day)).getTime();
      // initialize Date() object from calculated milliseconds
      objDate = new Date();
      objDate.setTime(mSeconds);
      // compare input date and parts from Date() object
      // if difference exists then date isn't valid
      if (objDate.getFullYear() !== year ||
          objDate.getMonth() !== month ||
          objDate.getDate() !== day) {
        return null;
      }
      // otherwise return true
      return objDate;
    }
  });
  return new PassengerUtils();
});

