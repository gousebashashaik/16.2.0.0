define("tui/widget/faqs", [
                            "dojo",
                            "dojo/on",
                            "dojo/dom-class",
                            "tui/widget/_TuiBaseWidget",
                            "dojo/Stateful"
                           ], function(dojo,on, domClass){

   dojo.declare("tui.widget.faqs", [tui.widget._TuiBaseWidget, dojo.Stateful], {

      mainTabId: null,


      subCatItems: null,

       targetURL: null,

       postCreate: function(){

    	   if (document.location.href.indexOf("faqCategories") == -1){
    		   var root = document.getElementsByTagName( 'html' )[0];
    	         //root.className += " modalmode";
    	         domClass.add(root, "modalmode");
    	   }

         //var root1 = document.getElementsByTagName( 'body' )[0];
         //root1.className += "faqFont";

         var faqsModel = this;
         faqsModel.mainTabId = this.id;
         faqsModel.inherited(arguments);
         faqsModel.defaultSelection();

         dojo.query(".contact-us").on("click", function(e){
           domClass.add(root, "modalmode");
          });
        dojo.query(".dijitDialogCloseIcon").on("click", function(e){
           //dojo.removeClass(root, "modalmode");
           domClass.remove(root, "modalmode");

          });
        //domClass.remove(root, "modalmode");
        //domClass.add(root, "modalmodeover");
       },
       validate: function(name, fn){
         return true;
       },
       defaultSelection: function(name, fn){
         defaultSelectionFromGSASearch();
      }

     });

   return tui.widget.faqs;
});

var ObjStr;
/**
 *
 * @param CatId
 * @param fromSearch
 * @return
 * function is called to fill the categories
 */
function getSubcategorySubTabs(CatId, fromSearch){

   document.getElementById("FAQ_Landing_Body").style.display="none";
   document.getElementById("FAQ_Landing_Cats").style.display="block";
   var catid_ = "catname"+CatId;
   mainTabsSelection("categories", "main-cat-selected", catid_, "DIV");

   var results = '';
   var rootPath = dojoConfig.site=="cruise"?'/destinations':dojoConfig.paths.webRoot;
   document.getElementById("sub_before_you_go").innerHTML = '<img class="loaderimg" src=""+rootPath+"/images+"dojoConfig.site"+/loading-3-anim-transparent.gif" />';
   document.getElementById("top_faqs_div").innerHTML = "<p class='subcatDefaultMessage'>Click the relevant topic on the left for help with your query</p>";
   document.getElementById("faq_explain").innerHTML = "<div id='docHeading'></div>";

    //if from search , request comes

   var query = window.location.search.substring(1);
   if(query && fromSearch){
       var vars = query.split('&');
       var vars1 = vars[0];
       var vars2 = vars[1];
       var vars3 = vars[2];

       var inputsCat = vars1.split('=');
       var catId = inputsCat[1];

       var inputsSubCat = vars2.split('=');
       var subCatId = inputsSubCat[1];

       var inputsDocId = vars3.split('=');

       var docId = inputsDocId[1];
             var targetURL = ""+rootPath+"/subCategoriesForCategory?categoryID="+CatId+"&catId="+CatId+"&subcatId="+subCatId+"&docId="+docId;
   }
   else{
   var targetURL = ""+rootPath+"/subCategoriesForCategory?categoryID="+CatId;
   }



   var xmlhttp;
   if (window.XMLHttpRequest) {// code for IE7+, Firefox,
   // Chrome, Opera, Safari
      xmlhttp = new XMLHttpRequest();
   }
   else {// code for IE6, IE5
   xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
   }
   xmlhttp.onreadystatechange = function() {
         if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            results = xmlhttp.responseText;
            var subString1=results.split('test1');

            var subresult = subString1[1];

            var subString=subresult.split('test2');

            var ObjStr = subString[0];
            if(ObjStr != '[]'){

            var divId = "sub_before_you_go";

            document.getElementById(divId).innerHTML = ObjStr;
            dojo.parser.parse(document.getElementById(divId));

            }

         }
    }
  var url = targetURL;
  xmlhttp.open("GET",url,true);
  xmlhttp.send();
  if(ObjStr != '[]'){
      return true;
  }
  return false;
}
/**
 * The function is called to fill the documents list
 * @param subCatId
 * @param fromSearch
 * @return
 */
function getSubcategories(subCatId, fromSearch){
   var results = '';
   var rootPath = dojoConfig.site=="cruise"?'/destinations':dojoConfig.paths.webRoot;


   //if from search , request comes

   var query = window.location.search.substring(1);
   if(query && fromSearch){
       var vars = query.split('&');
       var vars1 = vars[0];
       var vars2 = vars[1];
       var vars3 = vars[2];

       var inputsCat = vars1.split('=');
       var catId = inputsCat[1];

       var inputsSubCat = vars2.split('=');
       var subCatId = inputsSubCat[1];

       var inputsDocId = vars3.split('=');

       var docId = inputsDocId[1];
       var targetURL = ""+rootPath+"/faqsForSubCategory?subCategoryID="+subCatId+"&catId="+catId+"&subcatId="+subCatId+"&docId="+docId;
   }
   else{
      var targetURL = ""+rootPath+"/faqsForSubCategory?subCategoryID="+subCatId;
   }
   var targetURL = ""+rootPath+"/faqsForSubCategory?subCategoryID="+subCatId+"&docId="+docId;
   document.getElementById("top_faqs_div").innerHTML = '<img class="loaderimg" src=""+rootPath+"images"+dojoConfig.site+"/loading-3-anim-transparent.gif" />';

   document.getElementById("faq_explain").innerHTML = "<div id='docHeading'></div>";
   var subcat= "subcat"+subCatId;

   var subcat = "subcat"+subCatId;
   tabsSelection("sub_before_you_go", "subcat-tab-selected", subcat, "DIV");
//
   var xmlhttp;
   if (window.XMLHttpRequest) {// code for IE7+, Firefox,
   // Chrome, Opera, Safari
      xmlhttp = new XMLHttpRequest();
   }
   else {// code for IE6, IE5
   xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
   }
   xmlhttp.onreadystatechange = function() {
         if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            results = xmlhttp.responseText;
            var subString1=results.split('test1');

            var subresult = subString1[1];

            var subString=subresult.split('test2');

            var ObjStr = subString[0];
            var divId = "top_faqs_div";
            if(ObjStr != '[]' || ObjStr != ''){

            document.getElementById(divId).innerHTML = ObjStr;
            }
            else{
               document.getElementById(divId).innerHTML = "No documents available";
            }

if (docId)
{
  getSubcategoryDocumnetExaplined(docId,this,1)
}

         }
    }
  var url = targetURL;
  xmlhttp.open("GET",url,true);
  xmlhttp.send();
  if(ObjStr != '[]' || ObjStr != ''){
     return true;
  }
  return false;
}




/**
 * The function is called to fill the document explained
 * @param docId
 * @param obj
 * @param fromSearch
 * @return
 */
function getSubcategoryDocumnetExaplined(docId, obj, fromSearch){

	var rootPath = dojoConfig.site=="cruise"?'/destinations':dojoConfig.paths.webRoot;
   if(docId != ''){
   document.getElementById("faq_explain").innerHTML = '<img class="loaderimg" src=""+rootPath+"/images"+dojoConfig.site+"/loading-3-anim-transparent.gif" />';
    var docid = "doc_"+docId;
   tabsSelection("documentsListing", "documents-selected", docid, "LI");

   var results = '';
    var cookieName = "faq_document_"+docId;
   var cookievalue = faqGetCookie(cookieName);
   var d = new Date();
   var targetURL = ""+rootPath+"/contentForDocument?documentID="+docId + "&time=" + d.getTime();
   var targetURLAJX = ""+rootPath+"/getcontent?documentID="+docId + "&time=" + d.getTime();

   var xmlhttp;
   if (window.XMLHttpRequest) {// code for IE7+, Firefox,
   // Chrome, Opera, Safari
      xmlhttp = new XMLHttpRequest();
     // xmlhttp1 = new XMLHttpRequest();
   }
   else {// code for IE6, IE5
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
     // xmlhttp1 = new ActiveXObject("Microsoft.XMLHTTP");
   }

   xmlhttp.open("GET",targetURLAJX,true);
   xmlhttp.setRequestHeader("Accept","application/json");
   xmlhttp.send();

   xmlhttp.onreadystatechange = function(){
      if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
         var results1 = xmlhttp.responseText;
         var parsedData = JSON.parse(results1);

var str = "";
str = str + '<div id="documentID" style="display:none;">'+ docId +'</div>';
if (document.getElementById("anchordoc_"+docId))
{
   str = str + '<div id="docHeading"><h2 style="text-transform:none;">'+ document.getElementById("anchordoc_"+docId).innerHTML +'</h2></div>';
}
str = str + '<p id="faq_explained_p">'+parsedData+'</p>';
str = str + '<div id="feedbackResult">';
if (cookievalue == "true"){
  str = str + '<div id="feedbackyes"  class=\'feedbackResult\'>Thanks for your feedback. We\'ll use it to improve our answers.</div>';
}
if (cookievalue == "false"){
  str = str + '<div id="feedbackno"  class=\'feedbackResult\'>Sorry we couldn\'t help. Please <a href=\'http://www.thomson.co.uk/editorial/legal/contact-us-package-holidays.html\' target=\'_parent\'  class=\'ensLinkTrack\' data-componentId=\'WF_COM_262\'>contact us</a> for more information</div>';
}
if (cookievalue == null){
str = str + '<div id="feedbackno" style="display:none;" class="feedbackResult">Sorry we couldn\'t help. Please <a href="http://www.thomson.co.uk/editorial/legal/contact-us-package-holidays.html" target="_parent" class="ensLinkTrack" data-componentId="WF_COM_262">contact us</a> for more information</div>';
str = str + '<div id="feedbackyes" style="display:none;" class="feedbackResult">Thanks for your feedback. We\'ll use it to improve our answers.</div>';
str = str + '<div id="feedbacksubmit">';
str = str + '<h2 style="text-transform:none;">Does this answer your question ?</h2>';
str = str + '<div id="buttons">';
str = str + '<form action="#" method="post">';
str = str + '<input src="'+rootPath+'/images/faq-images/Feedback_Yes.png" value="YES" id="yes_button" onclick="return faqs_customer_feedback(\'true\');" type="image">';
str = str + '<input src="'+rootPath+'/images/faq-images/Feedback_No.png" value="NO" id="no_button" onclick="return faqs_customer_feedback(\'false\');" type="image">';
str = str + '</form>';
str = str + '</div>';
}
str = str + '</div>';
str = str + '</div>';

         //var subString1=results.split('test1');

         //var subresult = subString1[1];

         //var subString=subresult.split('test2');

         //var ObjStr = subString[0];
         var divId = "faq_explain";
         if(parsedData != '[]' || parsedData != ''){
            //document.getElementById(divId).style.display="none";
            //document.getElementById(divId).innerHTML = '<div id="documentID" style="display:none;">'+docId+'</div>'+ObjStr;
          //if(fromSearch){
                  //sleep(2000);
                //}

            //var dd = "anchordoc_"+docId;
            //if(document.getElementById(dd) != undefined){
               //var objContent = document.getElementById(dd).innerHTML;
               //document.getElementById("docHeading").innerHTML = '<h2 style="text-transform:none;">'+objContent+"</h2>";
            //}
         document.getElementById("faq_explain").style.display="block";
            document.getElementById("faq_explain").innerHTML = str;
         }
         else{
            document.getElementById(divId).innerHTML = "No documents available";
         }






      }
   }

   /*xmlhttp1.onreadystatechange = function() {
         if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            results = xmlhttp.responseText;
            var subString1=results.split('test1');

            var subresult = subString1[1];

            var subString=subresult.split('test2');

            var ObjStr = subString[0];
            var divId = "faq_explain";
            if(ObjStr != '[]' || ObjStr != ''){
               document.getElementById(divId).style.display="none";
               document.getElementById(divId).innerHTML = '<div id="documentID" style="display:none;">'+docId+'</div>'+ObjStr;
               xmlhttp1.open("GET",targetURLAJX,true);
               xmlhttp1.setRequestHeader("Accept","application/json");
               xmlhttp1.send();

               if(fromSearch){
                     //sleep(2000);
                   }

               var dd = "anchordoc_"+docId;
               if(document.getElementById(dd) != undefined){
               var objContent = document.getElementById(dd).innerHTML;

                  document.getElementById("docHeading").innerHTML = '<h2 style="text-transform:none;">'+objContent+"</h2>";
               }

            }
            else{
               document.getElementById(divId).innerHTML = "No documents available";
            }


         }
    }*/
  /*var url = targetURL;
  xmlhttp.open("GET",url,true);
  xmlhttp.send();*/
  if(ObjStr != '[]' || ObjStr != ''){
     return true;
  }
  return false;
}
}

/**
 * The function is called to select the tab
 * @param mainDiv
 * @param className
 * @param id
 * @param currentElementType
 * @return
 */
function tabsSelection(mainDiv, className, id, currentElementType){
    //alert("mainDiv:"+mainDiv+", className:"+className+", id:"+id+", currentElementType:"+currentElementType);
   var matches = [];
   if(document.getElementById(mainDiv) != undefined){
      var searchEles = document.getElementById(mainDiv).children;

      for(var i = 0; i < searchEles.length; i++) {
          if(searchEles[i].tagName == currentElementType) {

              searchEles[i].className="";
          }
      }
       if(searchEles.length > 0 && document.getElementById(id) != undefined){
      document.getElementById(id).className += className;
       }
   }
    return true;
}
/**
 * The function is called to select category tab
 * @param mainDiv
 * @param className
 * @param id
 * @param currentElementType
 * @return
 */
function mainTabsSelection(mainDiv, className, id, currentElementType){
    //alert("mainDiv:"+mainDiv+", className:"+className+", id:"+id+", currentElementType:"+currentElementType);
   var matches = [];
   if(document.getElementById(mainDiv) != undefined){
      var searchEles = document.getElementById(mainDiv).children;

      for(var i = 0; i < searchEles.length; i++) {
          if(searchEles[i].tagName == currentElementType) {

              searchEles[i].className="mainCatNames";
          }
      }
       if(document.getElementById(id) != undefined){
         document.getElementById(id).className += " "+className;
       }
   }
}


function faqs_customer_feedback(feedback){

   if(feedback == "true"){
      document.getElementById("feedbackno").style.display = 'none';
      document.getElementById("feedbackyes").style.display = 'block';
      document.getElementById("feedbacksubmit").style.display = 'none';
   }
   else{
      document.getElementById("feedbackno").style.display = 'block';
      document.getElementById("feedbackyes").style.display = 'none';
      document.getElementById("feedbacksubmit").style.display = 'none';
   }
   var docId = document.getElementById("documentID").innerHTML;
   //document.getElementById("feedbackResult").innerHTML = str;
   var rootPath = dojoConfig.site=="cruise"?'/destinations':dojoConfig.paths.webRoot;

   var exdays = null;
   var c_name ="faq_document_"+docId;

   var value = feedback;
   faqSetCookie(c_name,value,exdays);
   var targetURL = ""+rootPath+"/submitFeedBack?documentID="+docId+"&customerSatisfied="+feedback;

      var xmlhttp;
      if (window.XMLHttpRequest) {// code for IE7+, Firefox,
      // Chrome, Opera, Safari
         xmlhttp = new XMLHttpRequest();
      }
      else {// code for IE6, IE5
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.onreadystatechange = function() {

       }
     var url = targetURL;
     xmlhttp.open("GET",url,true);
     xmlhttp.send();
   return false;
}

/**
 * The function is called to store the cookie
 * @param c_name
 * @param value
 * @param exdays
 * @return
 */
function faqSetCookie(c_name,value,exdays)
{
   var exdate=new Date();
   exdate.setDate(exdate.getDate() + exdays);

   var c_value=escape(value) + ( (exdays==null) ? "" : "; expires="+exdate.toUTCString());
   document.cookie=c_name + "=" + c_value;
}
/**
 * The function is called to read the cookie
 * @param c_name
 * @return
 */
function faqGetCookie(c_name)
{
   var c_value = document.cookie;
   var c_start = c_value.indexOf(" " + c_name + "=");
   if (c_start == -1)
     {
     c_start = c_value.indexOf(c_name + "=");
     }
   if (c_start == -1)
     {
     c_value = null;
     }
   else
     {
     c_start = c_value.indexOf("=", c_start) + 1;
     var c_end = c_value.indexOf(";", c_start);
     if (c_end == -1)
     {
   c_end = c_value.length;
   }
   c_value = unescape(c_value.substring(c_start,c_end));
   }
   return c_value;
   //response.ClearHeaders();
   //response.addHeader("Cache-Control", "private");

}

function searchRemove(obj)
{
   alert(obj);

}

function defaultSelectionFromGSASearch(){
   var query = window.location.search.substring(1);
   if(query){

       var vars = query.split('&');
       var vars1 = vars[0];
       var vars2 = vars[1];
       var vars3 = vars[2];

       var inputsCat = vars1.split('=');
       var catId = inputsCat[1];

       var inputsSubCat = vars2.split('=');
       var subCatId = inputsSubCat[1];
       var inputsDocId = vars3.split('=');
       var docId = inputsDocId[1];

       var catIdName = '';
       var subCatIdName = '';
       var docIdName = '';

       catIdName = inputsCat[0];
       subCatIdName = inputsSubCat[0];
       docIdName = inputsDocId[0];
        if(catIdName == "catId" && subCatIdName == "subcatId" && docIdName == "docId"){
          var return1 = 0;
          var return2 = 0;
          var return3 = 0;

          var return1 = getSubcategorySubTabs(catId, 1);
          //alert("return1:"+return1);
          if(return1){
            //sleep(50000);
            return2 = getSubcategories(subCatId, 1);
            //alert("return2:"+return2);
            if(return2){
               //sleep(150000);
               sleep(1200000);
               //return3 = getSubcategoryDocumnetExaplined(docId,this,1);
               //alert("return3:"+return3);
             }
          }
        }


   }
}


function questionSelection(catId,subCatId, docId){

     var return1 = 0;
     var return2 = 0;
     var return3 = 0;

     if(catId != ''&& subCatId != '' && docId != ''){
          var return1 = getSubcategorySubTabs(catId, 1);
          //alert("return1:"+return1);
          if(return1){
            //sleep(50000);
            return2 = getSubcategories(subCatId, 1);
            //alert("return2:"+return2);
            //sleep(80000);
            if(return2){
               sleep(100000);
               //return3 = getSubcategoryDocumnetExaplined(docId,this,1);
               //alert("return3:"+return3);
             }
          }
     }

}

function sleep(milliseconds) {
     var start = new Date().getTime();
     for (var i = 0; i < milliseconds; i++) { //10000000
       if ((new Date().getTime() - start) > milliseconds){
         //break;
       }
     }
}

function faqspopup(){
alert('ok');
}


