<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Interactive map accommodation -->
<!--${jsonObj}  -->
<!-- This accommodation code =  ${thisAccommodationCode}  -->

<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?v=3&sensor=false&region=GB&language=en-GB&client=gme-tuitravelplc&channel=thomson.co.uk&signature=6E1I0hBRqfDeaB9BbB1evvHZmlk="></script>

<script type="text/javascript">
	dojoConfig.addModuleName("tui/widget/maps/FilterMap");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise -->
<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
<c:if test = "${sessionScope.siteName == 'cruise'}">
<c:set var="siteFlag" value="true"/> <!--Cruise-->
</c:if>
<!--Condition Ends-->

<c:choose>
    <c:when test="${accomPageType eq 'bookflow'}">
        <c:set var="thisPageType" value="bookflow"/>
        <script type="text/javascript">
            dojoConfig.addModuleName("tui/widget/popup/LightboxPopup");
        </script>
        <div data-dojo-type="tui.widget.popup.LightboxPopup" id="attractionLightboxPopup"></div>
    </c:when>
    <c:when test="${accomPageType eq 'cruise'}">
        <c:set var="thisPageType" value="cruise"/>
        <script type="text/javascript">
            dojoConfig.addModuleName("tui/widget/popup/LightboxPopup");
        </script>
        <div data-dojo-type="tui.widget.popup.LightboxPopup" id="attractionLightboxPopup"></div>
    </c:when>
    <c:otherwise>
        <c:set var="thisPageType" value=""/>
    </c:otherwise>
</c:choose>
<div class="span" id="mapDiv">
	<%--<h2>Things To Do  <c:if test="${not empty viewData.topLocationName }">In ${viewData.topLocationName}</c:if></h2>--%>
	<div class="mapped" data-dojo-type="tui.widget.maps.FilterMap" data-dojo-props='jsonData:${jsonObj}, locationCode: "${thisAccommodationCode}", pageType: "${thisPageType}", pageId: "${pageId}", numberClip: "${siteFlag}"'></div>
</div>