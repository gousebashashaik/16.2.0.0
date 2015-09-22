<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?v=3&sensor=false&region=GB&language=en-GB&client=gme-tuitravelplc&channel=thomson.co.uk&signature=6E1I0hBRqfDeaB9BbB1evvHZmlk="></script>

<script type="text/javascript">
	dojoConfig.addModuleName("tui/widget/maps/LocationMap");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
<c:if test = "${sessionScope.siteName == 'cruise'}">
<c:set var="siteFlag" value="true"/> <!--Cruise-->
</c:if>
<!--Condition Ends-->
<div class="span" id="mapDiv">
	<!-- ${jsonObj}-->
	<%--<h2>Things To Do <c:if test="${not empty viewData.topLocationName}">In ${viewData.topLocationName}</c:if></h2>--%>
	<div class="mapped" data-dojo-type="tui.widget.maps.LocationMap" data-dojo-props='jsonData:${jsonObj}, numberClip: "${siteFlag}"'></div>
    <!-- ${viewData.locations[0].superCategoryNames} -->
   	<!--<p>${viewData.superCategoryNames}</p> -->
</div>