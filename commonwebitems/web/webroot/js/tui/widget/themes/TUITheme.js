define(["dojo/_base/kernel","dojo/_base/lang","dojox/charting/Theme", "dojox/charting/themes/common"], 
	function(dojo, lang, Theme, themes){

	// the baseline theme for all PlotKIt themes
	var pk = lang.getObject("tuiTheme", true, themes);

	pk.base = new Theme({
		axis:{
			stroke: {color:"#c4c4c4", width:0.5},
			majorTick: {color: "#c4c4c4", width: 1, length: 6},
			tick: {font: "normal normal normal 10px Calibri,Candara,Segoe,'Segoe UI',Optima,Arial,sans-serif", fontColor: "#999"}
		},
		colors: ["#fcb712", "#599bcf"]
	});

	pk.green = pk.base.clone();

	return pk;
});