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
</script>

<c:choose>
    <c:when test="${pageType == 'GULET_CRUISE'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${pageType == 'CRUISE_ONLY'}">
      <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${pageType == 'CRUISE_AND_STAY_OVERVIEW'}">
      <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'cruise_and_stay_overview'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'safari_and_stay_overview'}">
      <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'cruise_only'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'gulet_cruise'}">
      <c:set var="isSubType" value="false" />
    </c:when>
    <c:otherwise>
        <c:set var="isSubType" value="true" />
    </c:otherwise>
</c:choose>


<%-- Thumbnail Map component (WF_COM_016) --%>
<c:if test="${not empty viewData.featureCodesAndValues['latitude'][0] and not empty viewData.featureCodesAndValues['longitude'][0]}">
    <div class="span-third" analytics-id="016" analytics-instance="1">
			<div class="thumbnail-map">
            <img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'http://maps.googleapis.com/maps/api/staticmap?center=${viewData.featureCodesAndValues['latitude'][0]},${viewData.featureCodesAndValues['longitude'][0]}&zoom=13&size=317x213&markers=color:orange|${viewData.featureCodesAndValues['latitude'][0]},${viewData.featureCodesAndValues['longitude'][0]}&Client=gme-tuitravelplc&sensor=false'" alt="Map of ${viewData.featureCodesAndValues['name'][0]}" width="317" height="213" />

<c:choose>
<c:when test="${siteFlag eq true }">
	<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${viewData.locationMapUrl}" data-componentId=""><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> See places to go</a>
	</h2>
</c:when>
<c:otherwise>
	 <c:if test="${isSubType}">
		<a href="${viewData.locationMapUrl}" class="ensLinkTrack" data-componentId="${componentUid}">See places to go&nbsp;&raquo;</a>
	</c:if>
</c:otherwise>
</c:choose>
			</div>
		</div>
</c:if>

