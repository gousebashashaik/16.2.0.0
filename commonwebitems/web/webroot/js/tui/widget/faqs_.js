define ("tui/widget/faqs_", ["dojo", "tui/widget/_TuiBaseWidget","dojo/Stateful"], function(dojo){

	dojo.declare("tui.widget.faqs_", [tui.widget._TuiBaseWidget, dojo.Stateful], {

		mainTabId: null,


		subCatItems: null,

	    targetURL: null,

	    postCreate: function(){
	      var faqsModel = this;
	      faqsModel.mainTabId = this.id;
	      faqsModel.inherited(arguments);

	    },
	    validate: function(name, fn){
	      return true;
	    }

	  });

	return tui.widget.faqs_;
})

var ObjStr;
/**
 *
 * @param CatId
 * @param fromSearch
 * @return
 * function is called to fill the categories
 */
function getSubcategorySubTabs_(CatId, fromSearch){

	document.getElementById("FAQ_Landing_Body_").style.display="none";
	document.getElementById("FAQ_Landing_Cats_").style.display="block";
	var catid_ = "catname_"+CatId;
	mainTabsSelection("categories_", "main-cat-selected", catid_, "DIV");

	var results = '';
	document.getElementById("sub_before_you_go_").innerHTML = '<img class="loaderimg" src="images/firstchoice/loading-3-anim-transparent.gif" />';
	document.getElementById("top_faqs_div_").innerHTML = "";
	document.getElementById("faq_explain_").innerHTML = "<div id='docHeading'></div>";

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
	    var targetURL = "./subCategoriesForCategory?categoryID="+CatId+"&catId="+CatId+"&subcatId="+subCatId+"&docId="+docId;
	}
	else{
	var targetURL = "subCategoriesForCategory?categoryID="+CatId;
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

				var divId = "sub_before_you_go_";

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
function getSubcategories_(subCatId, fromSearch){
	var results = '';


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
	    var targetURL = "faqsForSubCategory?subCategoryID="+subCatId+"&catId="+catId+"&subcatId="+subCatId+"&docId="+docId;
	}
	else{
		var targetURL = "faqsForSubCategory?subCategoryID="+subCatId;
	}
	var targetURL = "faqsForSubCategory?subCategoryID="+subCatId+"&docId="+docId;
	document.getElementById("top_faqs_div_").innerHTML = '<img class="loaderimg" src="images/firstchoice/loading-3-anim-transparent.gif" />';

	document.getElementById("faq_explain_").innerHTML = "<div id='docHeading'></div>";
	var subcat= "subcat_"+subCatId;


	tabsSelection("sub_before_you_go_", "subcat-tab-selected", subcat, "DIV");
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
				var divId = "top_faqs_div_";
				if(ObjStr != '[]' || ObjStr != ''){

				document.getElementById(divId).innerHTML = ObjStr;
				}
				else{
					document.getElementById(divId).innerHTML = "No documents available";
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
function getSubcategoryDocumnetExaplined_(docId, obj, fromSearch){

	document.getElementById("faq_explain_").innerHTML = '<img class="loaderimg" src="images/firstchoice/loading-3-anim-transparent.gif" />';
    var docid = "doc_"+docId;
	tabsSelection("documentsListing_", "documents-selected", docid, "LI");

	var results = '';
    var cookieName = "faq_document_"+docId;
	var cookievalue = faqGetCookie(cookieName);

	var targetURL = "contentForDocument?documentID="+docId;

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
				var divId = "faq_explain_";
				if(ObjStr != '[]' || ObjStr != ''){
					document.getElementById(divId).innerHTML = '<div id="documentID" style="display:none;">'+docId+'</div>'+ObjStr;
	                if(fromSearch){
	                	//sleep(2000);
	                }

			    	var dd = "anchordoc_"+docId;
			    	if(document.getElementById(dd) != undefined){
			    	var objContent = document.getElementById(dd).innerHTML;

		   		 	document.getElementById("docHeading_").innerHTML = "<h2>"+objContent+"</h2>";
			    	}

				}
				else{
					document.getElementById(divId).innerHTML = "No documents available";
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
		document.getElementById("feedbackno_").style.display = 'none';
		document.getElementById("feedbackyes_").style.display = 'block';
		document.getElementById("feedbacksubmit_").style.display = 'none';
	}
	else{
		document.getElementById("feedbackno_").style.display = 'block';
		document.getElementById("feedbackyes_").style.display = 'none';
		document.getElementById("feedbacksubmit_").style.display = 'none';
	}
	var docId = document.getElementById("documentID_").innerHTML;
	//document.getElementById("feedbackResult").innerHTML = str;

	var exdays = null;
	var c_name ="faq_document_"+docId;
	var value = feedback;
	faqSetCookie(c_name,value,exdays);
	var targetURL = "submitFeedBack?documentID="+docId+"&customerSatisfied="+feedback;

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
	var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
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
}








