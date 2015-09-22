<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
    <c:set var="webrootPath" value="${pageContext.request.contextPath}" />
    <c:set var="cdnDomain" value="${CDN_URL}" />
<script>
	dojoConfig.addModuleName("tui/widget/carousels/Carousel", "tui/widget/Tabs", "tui/widget/LazyLoadImage");
</script>

<div class="${componentStyle}">
	<h2>Places To Stay <c:if test="${not empty viewData.topLocationName}">In ${viewData.topLocationName} </c:if><a class="view-all ensLinkTrack" href="${viewAllURL}" data-componentId="${componentUid}">&#9658;  View all</a></h2>
	<c:forEach var="locationData" items="${viewData.locations}" varStatus="status">
		<div class="panel labeled image-left">
            <images:first collection="${locationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" width="200"/>
			<!--
				${locationData.priceFrom}
				${locationData.url}
			-->
			<div class="copy">
				<h3>${locationData.name}</h3>
				<p>${locationData.featureCodesAndValues['intro1Body'][0]}</p>

				<ul class="square">
				    <c:if test="${not empty locationData.subLocations}">
					    <c:forEach var="subLocationData" items="${locationData.subLocations}" varStatus="counter">
	                        <li><span class="sprite-img-grp-1"></span><a href="${subLocationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${subLocationData.name}</a></li>
	                    </c:forEach>
	                 </c:if>
	                 <c:if test="${not empty locationData.accommodations}">
                        <c:forEach var="accommodationData" items="${locationData.accommodations}" varStatus="counter">
                            <li><span class="sprite-img-grp-1"></span><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.name}</a></li>
                        </c:forEach>
                     </c:if>
				</ul>
			</div>
		</div>
	</c:forEach>
</div>