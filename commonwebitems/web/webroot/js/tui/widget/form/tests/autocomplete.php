<?php


$keyword =  $_GET['title'];
//echo $keyword;
$encoded = "";
// encode array $json to JSON string
if ( preg_match("/^a/i", $keyword) ) {
$encoded = <<<JSON
	[
		{
			id: 1,
			value: 1,
			title: "Hotel in Australia",
			type: "Hotel"
		},
		{
			id: 2,
			value: 2,
			title: "Australia Holiday",
			type:  "Holiday"
		},
		{
			id: 3,
			value: 3,
			title: "Austria Holiday",
			type:  "Holiday"
		},
		{
			id: 4,
			value: 4,
			title: "Hotel in Austria",
			type:  "Hotel"
		}
	]
JSON;
}elseif( preg_match("/^b/i", $keyword) ){
$encoded = <<<JSON
	[
		{
			id: 5,
			value: 5,
			title: "Hotel in Barbados",
			type: "Hotel"
		},
		{
			id: 6,
			value: 6,
			title: "Barbados Holiday",
			type:  "Holiday"
		},
		{
			id: 7,
			value: 7,
			title: "Brazil Holiday",
			type:  "Holiday"
		},
		{
			id: 8,
			vaule: 8,
			title: "Hotel in Brazil",
			type:  "Hotel"
		}
	]
JSON;
}elseif( preg_match("/^c/i", $keyword) ){
$encoded = <<<JSON
	[
		{
			id: 9,
			value: 9,
			title: "Hotel in China",
			type: "Hotel"
		},
		{
			id: 10,
			value: 10,
			title: "China Holiday",
			type:  "Holiday"
		},
		{
			id: 11,
			value: 11,
			title: "Congo Holiday",
			type:  "Holiday"
		},
		{
			id: 12,
			value: 12,
			title: "Hotel in Congo",
			type:  "Hotel"
		}
	]
JSON;
}elseif( preg_match("/^d/i", $keyword) ){
$encoded = <<<JSON
	[
		{
			id: 13,
			value: 13,
			title: "Hotel in Denmark",
			type: "Hotel"
		},
		{
			id: 14,
			value: 14,
			title: "Holiday Denmark",
			type:  "Holiday"
		},
		{
			id: 15,
			value: 15,
			title: "Dominica Holiday",
			type:  "Holiday"
		},
		{
			id: 16,
			value: 16,
			title: "Hotel in Dominica",
			type:  "Hotel"
		}
	]
JSON;
}else {
	$encoded = "";
}
// send response back to index.html
// and end script execution
die($encoded);
 
?>