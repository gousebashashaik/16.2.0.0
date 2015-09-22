define("tui/flightdeals/GoToDealsExpandable",[
       "tui/widget/form/flights/DealsExpandable",
       "dojo",
       "dojo/on",
       "dojo/text!tui/flightdeals/templates/GoTo.html",
       "dojo/NodeList-traverse"
       ],function(DealsExpandable,dojo,on,goToTmpl){
		dojo.declare("tui.flightdeals.GoToDealsExpandable",[DealsExpandable],{
			//.............................................Properties
			airportjson : {"KEN":[{"group":[],"children":[],"countryName":"Kenya","countryCode":"KEN","synonym":"","available":false,"name":"Mombasa","id":"MBA"}],"JAM":[{"group":[],"children":[],"countryName":"Jamaica","countryCode":"JAM","synonym":"","available":true,"name":"Montego Bay","id":"MBJ"}],"IND":[{"group":[],"children":[],"countryName":"India","countryCode":"IND","synonym":"","available":true,"name":"Goa","id":"GOI"}],"PRT":[{"group":[],"children":[],"countryName":"Portugal","countryCode":"PRT","synonym":"","available":true,"name":"Faro","id":"FAO"},{"group":[],"children":[],"countryName":"Portugal","countryCode":"PRT","synonym":"","available":true,"name":"Madeira","id":"FNC"},{"group":[],"children":[],"countryName":"Portugal","countryCode":"PRT","synonym":"","available":true,"name":"Porto Santo","id":"PXO"}],"ABW":[{"group":[],"children":[],"countryName":"Aruba","countryCode":"ABW","synonym":"","available":true,"name":"Aruba International","id":"AUA"}],"HRV":[{"group":[],"children":[],"countryName":"Croatia","countryCode":"HRV","synonym":"","available":true,"name":"Dubrovnik","id":"DBV"},{"group":[],"children":[],"countryName":"Croatia","countryCode":"HRV","synonym":"","available":true,"name":"Pula","id":"PUY"},{"group":[],"children":[],"countryName":"Croatia","countryCode":"HRV","synonym":"","available":true,"name":"Split","id":"SPU"}],"GRC":[{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Chania","id":"CHQ"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Corfu","id":"CFU"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Heraklion","id":"HER"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Kavala","id":"KVA"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Kefalonia","id":"EFL"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Kos","id":"KGS"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Mykonos","id":"JMK"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Preveza Lefkas","id":"PVK"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Rhodes","id":"RHO"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Samos","id":"SMI"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Santorini","id":"JTR"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Skiathos","id":"JSI"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Thessaloniki","id":"SKG"},{"group":[],"children":[],"countryName":"Greece","countryCode":"GRC","synonym":"","available":true,"name":"Zante","id":"ZTH"}],"CPV":[{"group":[],"children":[],"countryName":"Cape Verde","countryCode":"CPV","synonym":"","available":true,"name":"Boa Vista","id":"BVC"},{"group":[],"children":[],"countryName":"Cape Verde","countryCode":"CPV","synonym":"","available":true,"name":"Sal","id":"SID"}],"ISL":[{"group":[],"children":[],"countryName":"Iceland","countryCode":"ISL","synonym":"","available":true,"name":"Keflavik International","id":"KEF"}],"MAR":[{"group":[],"children":[],"countryName":"Morocco","countryCode":"MAR","synonym":"","available":true,"name":"Agadir","id":"AGA"},{"group":[],"children":[],"countryName":"Morocco","countryCode":"MAR","synonym":"","available":true,"name":"Marrakech","id":"RAK"}],"TUN":[{"group":[],"children":[],"countryName":"Tunisia","countryCode":"TUN","synonym":"","available":true,"name":"Djerba Island","id":"DJE"},{"group":[],"children":[],"countryName":"Tunisia","countryCode":"TUN","synonym":"","available":true,"name":"Enfidha Hammamet","id":"NBE"}],"EGY":[{"group":[],"children":[],"countryName":"Egypt","countryCode":"EGY","synonym":"","available":true,"name":"Hurghada","id":"HRG"},{"group":[],"children":[],"countryName":"Egypt","countryCode":"EGY","synonym":"","available":true,"name":"Luxor","id":"LXR"},{"group":[],"children":[],"countryName":"Egypt","countryCode":"EGY","synonym":"","available":true,"name":"Marsa Alam","id":"RMF"},{"group":[],"children":[],"countryName":"Egypt","countryCode":"EGY","synonym":"","available":true,"name":"Sharm El Sheikh","id":"SSH"}],"CYP":[{"group":[],"children":[],"countryName":"Cyprus","countryCode":"CYP","synonym":"","available":true,"name":"Larnaca","id":"LCA"},{"group":[],"children":[],"countryName":"Cyprus","countryCode":"CYP","synonym":"","available":true,"name":"Paphos","id":"PFO"}],"GBR":[{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Aberdeen","id":"ABZ"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Belfast City","id":"BHD"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Belfast International","id":"BFS"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Birmingham","id":"BHX"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Bournemouth","id":"BOH"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Bristol","id":"BRS"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Cardiff","id":"CWL"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":false,"name":"City of Derry","id":"LDY"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Doncaster Sheffield","id":"DSA"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"East Midlands","id":"EMA"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Edinburgh","id":"EDI"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Exeter","id":"EXT"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Glasgow","id":"GLA"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Humberside","id":"HUY"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Leeds Bradford","id":"LBA"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Liverpool John Lennon","id":"LPL"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"London Gatwick","id":"LGW"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"London Luton","id":"LTN"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"London Southend","id":"SEN"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"London Stansted","id":"STN"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Manchester","id":"MAN"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Newcastle","id":"NCL"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Norwich","id":"NWI"},{"group":[],"children":[],"countryName":"","countryCode":"GBR","synonym":"","available":true,"name":"Southampton","id":"SOU"}],"TUR":[{"group":[],"children":[],"countryName":"Turkey","countryCode":"TUR","synonym":"","available":true,"name":"Adnan Menderes","id":"ADB"},{"group":[],"children":[],"countryName":"Turkey","countryCode":"TUR","synonym":"","available":true,"name":"Antalya","id":"AYT"},{"group":[],"children":[],"countryName":"Turkey","countryCode":"TUR","synonym":"","available":true,"name":"Dalaman","id":"DLM"},{"group":[],"children":[],"countryName":"Turkey","countryCode":"TUR","synonym":"","available":true,"name":"Milas Bodrum","id":"BJV"}],"USA":[{"group":[],"children":[],"countryName":"United States Of America","countryCode":"USA","synonym":"","available":true,"name":"Orlando Sanford","id":"SFB"}],"DOM":[{"group":[],"children":[],"countryName":"Dominican Republic","countryCode":"DOM","synonym":"","available":true,"name":"Puerto Plata","id":"POP"},{"group":[],"children":[],"countryName":"Dominican Republic","countryCode":"DOM","synonym":"","available":true,"name":"Punta Cana","id":"PUJ"}],"THA":[{"group":[],"children":[],"countryName":"Thailand","countryCode":"THA","synonym":"","available":true,"name":"Phuket","id":"HKT"}],"MUS":[{"group":[],"children":[],"countryName":"Mauritius","countryCode":"MUS","synonym":"","available":true,"name":"Mauritius","id":"MRU"}],"BGR":[{"group":[],"children":[],"countryName":"Bulgaria","countryCode":"BGR","synonym":"","available":true,"name":"Bourgas","id":"BOJ"}],"ITA":[{"group":[],"children":[],"countryName":"Italy","countryCode":"ITA","synonym":"","available":true,"name":"Alghero","id":"AHO"},{"group":[],"children":[],"countryName":"Italy","countryCode":"ITA","synonym":"","available":true,"name":"Cappodichino","id":"NAP"},{"group":[],"children":[],"countryName":"Italy","countryCode":"ITA","synonym":"","available":true,"name":"Fontanarossa","id":"CTA"},{"group":[],"children":[],"countryName":"Italy","countryCode":"ITA","synonym":"","available":true,"name":"Venice","id":"VCE"}],"MLT":[{"group":[],"children":[],"countryName":"Malta","countryCode":"MLT","synonym":"","available":true,"name":"Malta","id":"MLA"}],"CRI":[{"group":[],"children":[],"countryName":"Costa Rica","countryCode":"CRI","synonym":"","available":true,"name":"Liberia","id":"LIR"}],"FIN":[{"group":[],"children":[],"countryName":"","countryCode":"FIN","synonym":"","available":true,"name":"Ivalo","id":"IVL"},{"group":[],"children":[],"countryName":"","countryCode":"FIN","synonym":"","available":true,"name":"Kittila","id":"KTT"},{"group":[],"children":[],"countryName":"","countryCode":"FIN","synonym":"","available":true,"name":"Rovaniemi","id":"RVN"}],"MEX":[{"group":[],"children":[],"countryName":"Mexico","countryCode":"MEX","synonym":"","available":true,"name":"Cancun","id":"CUN"},{"group":[],"children":[],"countryName":"Mexico","countryCode":"MEX","synonym":"","available":true,"name":"Puerto Vallarta","id":"PVR"}],"ESP":[{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Alicante","id":"ALC"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Almeria","id":"LEI"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Fuerteventura","id":"FUE"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Girona Costa Brava","id":"GRO"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Gran Canaria","id":"LPA"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Ibiza","id":"IBZ"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Jerez","id":"XRY"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"La Palma","id":"SPC"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Lanzarote","id":"ACE"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Malaga Costa del Sol","id":"AGP"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Menorca","id":"MAH"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Palma de Mallorca","id":"PMI"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Reus","id":"REU"},{"group":[],"children":[],"countryName":"Spain","countryCode":"ESP","synonym":"","available":true,"name":"Tenerife South","id":"TFS"}],"BRB":[{"group":[],"children":[],"countryName":"Barbados","countryCode":"BRB","synonym":"","available":true,"name":"Barbados","id":"BGI"}]},
			overseasAirports:[],
			//.............................................methods
			onAfterTmplRender:function () {
				var goToDealsExpandable = this;
				var tmplt = goToDealsExpandable.renderOverseasAirports();
    	    	target = dojo.query(".deals-wrapper",goToDealsExpandable.expandableDom)[0];
	    	    dojo.html.set(target,tmplt,{
	    	    	parseContent: true
	    	    });
	    	    goToDealsExpandable.attachEvents();

	    	    goToDealsExpandable.attachBodyEvent();
			},
			renderOverseasAirports:function(){
				var goToDealsExpandable = this, data,html;
				var airportsListKeys = Object.keys(goToDealsExpandable.airportjson);
                var  airportTempList= [];
                var	 airportSwapList = [];
                _.each(airportsListKeys, function(countryKey){
                	airportTempList = goToDealsExpandable.airportjson[countryKey];
                	var checkCName = "";
                	_.each(airportTempList, function(airport){

                		if(!airport.countryName){
                			airport.countryName ="Finland";
                		}

                		if(airport.countryName == checkCName){
                			airport.cFlag = false;
                		}else{
                			airport.cFlag = true;

                		}
                		checkCName = airport.countryName;
                		airportSwapList.push(airport);

                	});

                 });
                goToDealsExpandable.airportList = airportSwapList;
                _.each(goToDealsExpandable.airportList, function(airport){
                	if(airport.countryCode !== "GBR"){
                		airport.nextCol = false;
                		if(airport.id =="RVN" || airport.id =="MRU"){
            				airport.nextCol = true;
            			}
                		goToDealsExpandable.overseasAirports.push(airport);
                	}
                });
                goToDealsExpandable.overseasAirports = goToDealsExpandable.sortByName(goToDealsExpandable.overseasAirports);
                data={
                		overseasAirportslist: goToDealsExpandable.overseasAirports

                }

             html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, goToTmpl));
   	    	 return html;

			},
			attachEvents:function(){
				var flyFromDealsExpandable = this;
				//Connecting event for group checkbox
				dojo.query('.grpHeading .dijitCheckBox').forEach(function(checkBox){
					on(dijit.byNode(checkBox),"click",function(evt){
						dojo.query("." +this.class/*CountryName*/).forEach(function(c){
							if((dijit.byNode(checkBox).checked) && (dijit.byNode(c).disabled===false)){
								dijit.byNode(c).set("checked",true);
							}
							else{
								dijit.byNode(c).set("checked",false);
							}
						});
					});
				});
				//connecting event for child checkbox
				dojo.query('.childCountry .dijitCheckBox').forEach(function(childCheckBox){

					on(dijit.byNode(childCheckBox),"click",function(evt){
						var grpCheckbox = dojo.query(".grpHeading ." +dijit.byNode(childCheckBox).focusNode.dataset.airportmodelCountrycode)[0],
						childCheckbox=dojo.query(".childCountry ." +dijit.byNode(childCheckBox).focusNode.dataset.airportmodelCountrycode),
						checkedChildCheckbox=dojo.query(".childCountry .dijitChecked." +dijit.byNode(childCheckBox).focusNode.dataset.airportmodelCountrycode);
						if(!dijit.byNode(childCheckBox).checked) {
							dijit.byNode(grpCheckbox).set("checked",false);
						}else{
							if(childCheckbox.length===checkedChildCheckbox.length) dijit.byNode(grpCheckbox).set("checked",true);
						}
					});
				});
			},
			sortByName :  function(array){
                return _.sortBy(array, "countryName");
            }
		});

	return tui.flightdeals.GoToDealsExpandable;
});