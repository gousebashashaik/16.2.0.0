<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
    <c:set var="webrootPath" value="${pageContext.request.contextPath}" />
    <c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/LazyLoadImage", "tui/widget/taggable/AccomodationRoomHighlights");
</script>
<c:set var="roomtitle" value="Rooms" />
<c:set var="checkLink" value="true" />
<c:if test="${viewData.accommodationType eq 'SHIP'}">
     <c:set var="roomtitle" value="Cabins" />
     <c:set var="checkLink" value="false" />
</c:if>
<%-- Accommodation Rooms Highlight component (WF_COM_039.1) --%>
<c:if test="${not empty viewData.roomsData}">
	<div class="${componentStyle} span-two-third" data-dojo-type="tui.widget.taggable.AccomodationRoomHighlights">
		<h2 class="underline">${roomtitle}<c:if test="${checkLink}"><a class="view-all ensLinkTrack" href="${viewData.roomsData[0].accommodationRoomsUrl}" data-componentId="${componentUid}">&#9658; View all</a></c:if></h2>
		<c:forEach var="room" items="${viewData.roomsData}" varStatus="counter">
            <div class="panel labeled image-left <c:if test="${counter.count == 1}">first</c:if>">
                 <c:choose>
                	<c:when test="${checkLink}">
                <a href="${room.accommodationRoomsUrl}" class="ensLinkTrack" data-componentId="${componentUid}">
                    <images:first collection="${room.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-rooms-small.jpg" size="medium" width="360" height="201"/>
                </a>
                <div class="copy">
					<a href="${room.accommodationRoomsUrl}" class="ensLinkTrack" data-componentId="${componentUid}"><h3>${room.roomTitle} <c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3></a>
                    <p>${room.description}</p>
                </div>
                	</c:when>
                	<c:otherwise>
                    	<images:first collection="${room.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-rooms-small.jpg" size="medium" width="360" height="201"/>
                		<div class="copy">
                			<h3 class="product-highlight-title-nolink">${room.roomTitle} <c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3>
                			<p>${room.description}</p>
                		</div>
                	</c:otherwise>
                </c:choose>
            </div>
		</c:forEach>
	</div>
</c:if>