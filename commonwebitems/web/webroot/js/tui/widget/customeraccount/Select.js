define('tui/widget/customeraccount/Select', [
  'dojo',
  'dojo/dom-class',
  'dojo/query',
  'dojo/on',
  "dojo/NodeList-traverse",
  'dijit/_Widget'], function(dojo, domClass, query,on) {
   /* Select option */
	query('.select').forEach(function(selectDiv) {
		var target = query('span', selectDiv)[0];
		var select = query('select', selectDiv)[0];
		if (target && select) {
			dojo.connect(select, 'onchange', function(e) {
				target.innerHTML = e.target.options[e.target.selectedIndex].text;
			});
		}
	});
	
	/* Prefered airports */
	dojo.query('.airports').on('click', function(){ 
	    var obj = dojo.query(this).parent(".airportsLi");
		var thisObjChk = dojo.query(obj).children(".airportschk");
		var thisObjRadio = dojo.query(obj).children(".radio");
		var checkboxStatus = thisObjChk.attr('checked');
		
		if(checkboxStatus == "false"){		
		thisObjChk.attr("checked",true);
		var allClasses = "airports radio active";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		else{
		thisObjChk.attr("checked",false);
		var allClasses = "airports radio";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		
	});
	
    /* checkbox options */
	dojo.query('.boards').on('click', function(){ 
	    var obj = dojo.query(this).parent(".boardsLi");
		var thisObjChk = dojo.query(obj).children(".boardschk");
		var thisObjRadio = dojo.query(obj).children(".radio");
		var checkboxStatus = thisObjChk.attr('checked');
		
		if(checkboxStatus == "false"){		
		thisObjChk.attr("checked",true);
		var allClasses = "boards radio active";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		else{
		thisObjChk.attr("checked",false);
		var allClasses = "boards radio";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		
	});
    
	/* t-rating options */	
	dojo.query('.dojoRating').on('click', function(){ 
		var extClass = dojo.query(this).attr("class");
		var vals = dojo.query(this).attr("name");
		
		var selectedRatingClass=  ".class"+vals;
		var SelectedthisObj = dojo.query(selectedRatingClass); 
		var curSelectedClass = dojo.query(SelectedthisObj).attr("class");
		
		var selClass = "dojoRating radio class"+vals;
		var SelectedallClasses = "dojoRating caret t-rating blue class"+vals;
		
		for(var i=1; i<= 5; i++){
			var clasname = ".class"+i;
			var clas = "class"+i;
			var allClasses = "dojoRating radio "+clas;
			var thisObj = dojo.query(clasname); 
			dojo.query(thisObj).attr("class",allClasses);
		}
		for(var i=1; i<= vals; i++){
			var clasname = ".class"+i;
			var clas = "class"+i;
			var allClasses = "dojoRating caret t-rating blue "+clas;
			var thisObj = dojo.query(clasname); 
			dojo.query(thisObj).attr("class",allClasses);
		}
		
		if(curSelectedClass == selClass){ 
			dojo.query(SelectedthisObj).attr("class",SelectedallClasses);
		}
		else{ 
			dojo.query(SelectedthisObj).attr("class",selClass);
		}
		dojo.query(".caRating").attr("value",vals);
		
	});
	
	/* Holiday collections */
	
    dojo.query('.collections').on('click', function(){ 
	    var obj = dojo.query(this).parent(".collectionsLi");
		var thisObjChk = dojo.query(obj).children(".collectionschk");
		var thisObjRadio = dojo.query(obj).children(".radio");
		var checkboxStatus = thisObjChk.attr('checked');
		
		if(checkboxStatus == "false"){		
		thisObjChk.attr("checked",true);
		var allClasses = "collections radio active";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		else{
		thisObjChk.attr("checked",false);
		var allClasses = "collections radio";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		
	});
	
	/* Things to do section */
	
	dojo.query('.things').on('click', function(){ 
	    var obj = dojo.query(this).parent(".thingsLi");
		var thisObjChk = dojo.query(obj).children(".thingschk");
		var thisObjRadio = dojo.query(obj).children(".radio");
		var checkboxStatus = thisObjChk.attr('checked');
		
		if(checkboxStatus == "false"){		
		thisObjChk.attr("checked",true);
		var allClasses = "things radio active";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		else{
		thisObjChk.attr("checked",false);
		var allClasses = "things radio";
		dojo.query(thisObjRadio).attr("class",allClasses);
		}
		
	});



});


