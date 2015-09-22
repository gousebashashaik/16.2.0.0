<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/LazyLoadImage");
    dojoConfig.addModuleName("tui/widget/taggable/ThumbnailMap");
</script>


<c:choose>
    <c:when test="${viewData.locationType.code == 'COUNTRY'}">
        <c:set value="4" var="zoom"/>
    </c:when>
    <c:when test="${viewData.locationType.code == 'REGION'}">
        <c:set value="7" var="zoom"/>
    </c:when>
    <c:when test="${viewData.locationType.code == 'DESTINATION'}">
        <c:set value="9" var="zoom"/>
    </c:when>
    <c:when test="${viewData.locationType.code == 'RESORT'}">
        <c:set value="11" var="zoom"/>
    </c:when>
    <c:otherwise>
        <c:set value="4" var="zoom"/>
    </c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${productPage == 'true'}">
      <c:set var="isSubType" value="false" />
    </c:when>
    <c:otherwise>
        <c:set var="isSubType" value="true" />
    </c:otherwise>
</c:choose>

<div class="span-third" data-dojo-type="tui.widget.taggable.ThumbnailMap">
    <div class="thumbnail-map">
        <img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'http://maps.googleapis.com/maps/api/staticmap?center=${viewData.featureCodesAndValues['latitude'][0]},${viewData.featureCodesAndValues['longitude'][0]}&zoom=${zoom}&size=317x213&markers=color:orange|${viewData.featureCodesAndValues['latitude'][0]},${viewData.featureCodesAndValues['longitude'][0]}&Client=gme-tuitravelplc&sensor=false'" alt="Map of ${viewData.featureCodesAndValues['name'][0]}" width="317" height="213"/>
<c:choose>
<c:when test="${siteFlag eq true }">
<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${viewData.thingstodoMapUrl}" data-componentId=""><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> See places to go</a>
</h2>
</c:when>
<c:otherwise>
 <c:if test="${isSubType}">
	<a href="${viewData.thingstodoMapUrl}" class="ensLinkTrack" data-componentId="${componentUid}">See places to go&nbsp;&raquo;</a>
</c:if>
</c:otherwise>
</c:choose>
    </div>
</div>


