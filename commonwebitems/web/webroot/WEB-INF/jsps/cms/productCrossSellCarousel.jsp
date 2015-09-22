<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<script>
    dojoConfig.addModuleName("tui/widget/carousels/Carousel", "tui/widget/popup/Tooltips", "tui/widget/LazyLoadImage", "tui/widget/taggable/ProductCrossSellTaggable");
</script>

<%--  Similar Holidays Carousel --%>
<c:if test="${not empty viewData.accommodationCarousels}">
    <div class="span" data-dojo-type="tui.widget.taggable.ProductCrossSellTaggable">
        <c:forEach var="crossSellcarousel" items="${viewData.accommodationCarousels}" varStatus="status">
            <c:choose>
            <c:when test="${pageType == 'OVERVIEW_LAPLAND'}">
					<h2>Similar ${crossSellcarousel.name}</h2>
			</c:when>
			 <c:when test="${pageType == 'LOCATION_LAPLAND'}">
					<h2>Similar ${crossSellcarousel.name}</h2>
			</c:when>
			 <c:when test="${pageType == 'THINGSTODO_LAPLAND'}">
					<h2>Similar ${crossSellcarousel.name}</h2>
			</c:when>
			<c:otherwise>
					<h2>Similar ${crossSellcarousel.name} Holidays</h2>
			</c:otherwise>
            </c:choose>
            <c:if test="${not empty crossSellcarousel.accomodationDatas}">
                <div id="${crossSellcarousel.code}${status.count}" class="product-list carousel" data-dojo-type="tui.widget.carousels.Carousel">
                    <div class="viewport <c:if test="${not empty crossSellcarousel.accomodationDatas[0].multiCentreDatas and crossSellcarousel.accomodationDatas[0].multiCentre eq true}">cross-sell-list</c:if>" >
			<ul class="plist">
                            <c:forEach var="accommodationData" items="${crossSellcarousel.accomodationDatas}" varStatus="counter">
				<li>
                                	<c:choose>
                                	<c:when test="${not empty accommodationData.multiCentreDatas and accommodationData.multiCentre eq true}">
                                		<c:set var="isSpecial" value="outline"/>
                                	</c:when>
                                	<c:otherwise>
                                    <c:set var="isSpecial" value="special"/>
                                	</c:otherwise>

                                	</c:choose>
                                    <div class="${fn:toLowerCase(accommodationData.productRanges[0].code)} product ${isSpecial} ">
                                        <div class="image-container <c:if test='${not empty accommodationData.multiCentreDatas}'> image-container-split</c:if>">
                                            <c:if test="${not empty accommodationData.productRanges[0].featureCodesAndValues.strapline[0]}">
											<c:set var="className" value="diff-label ${fn:toLowerCase(accommodationData.productRanges[0].code)}" />
											<c:if test="${fn:toLowerCase(accommodationData.productRanges[0].code) eq 'smr'}">
											   <c:set var="className" value="diff-label cou-sensimar" />
											</c:if>
											<c:if test="${fn:toLowerCase(accommodationData.productRanges[0].code) eq 'fam'}">
											    	<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
												   		<c:set var="className" value="diff-label fam-life" />
												   </c:if>
											</c:if>

                                            <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accommodationData.productRanges[0].featureCodesAndValues.strapline[0]}'" class="${className}"></span>
                                            </c:if>
                                            <a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                            <c:choose>
                                                <c:when test="${not empty accommodationData.multiCentreDatas and accommodationData.multiCentre eq true}">
                                                    <c:forEach var="accomImg" items="${accommodationData.multiCentreDatas}" varStatus="imgCount" end="1">
                                                    <c:choose>
                                                        <c:when test="${imgCount.first}"><c:set var="imgClass" value="img-split img-split-left"/></c:when>
                                                        <c:otherwise><c:set var="imgClass" value="img-split img-split-right"/></c:otherwise>
                                                    </c:choose>
                                                        <div class="${imgClass}">
                                                            <img src="${cdnDomain}/images/b.gif" alt="${accommodationData.name}" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${accomImg.imageUrl}'" />
                                                        </div>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <images:first collection="${accommodationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" />
                                                </c:otherwise>
                                            </c:choose>
                                            </a>
                                        </div>
                                        <c:choose>
                                        <c:when test="${not empty accommodationData.multiCentreDatas and accommodationData.multiCentre eq true}">
                                        <div class="floater title-split">
                                        	<div class="left det-split">
                                        		<h4><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.multiCentreDatas[0].name}</a></h4>

                                        		<c:if test="${fn:trim(accommodationData.multiCentreDatas[0].tRating) ne ''}">
                                        			<p class="rating rating-md t${fn:replace(accommodationData.multiCentreDatas[0].tRating,' ','')}">Rating: ${accommodationData.multiCentreDatas[0].tRating}</p>
	    										</c:if>

		                                        <c:if test="${not empty accommodationData.multiCentreDatas[0].locations}">
		                                        	<p>
		                                        	<c:forEach var="locations" items="${accommodationData.multiCentreDatas[0].locations}" varStatus="stat">
													   <c:if test="${not stat.first}">, </c:if>${locations.value}
													</c:forEach>
													</p>
		                                         </c:if>
                                        	</div>

                                        	<div class="right det-split">
                                        		<h4><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.multiCentreDatas[1].name}</a></h4>

	                                        	<c:if test="${fn:trim(accommodationData.multiCentreDatas[1].tRating) ne ''}">
	                                        		<p class="rating rating-md t${fn:replace(accommodationData.multiCentreDatas[1].tRating,' ','')}">Rating: ${accommodationData.multiCentreDatas[1].tRating}</p>
	                                        	</c:if>

		                                        <c:if test="${not empty accommodationData.multiCentreDatas[1].locations }">
		                                        	<p>
		                                        	<c:forEach var="locations" items="${accommodationData.multiCentreDatas[1].locations}" varStatus="stat">
													   <c:if test="${not stat.first}">, </c:if>${locations.value}
													</c:forEach>
													</p>
		                                         </c:if>
                                        	</div>
						</div>

                                        </c:when>
                                        <c:otherwise>

	                                        <h4><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.name}</a></h4>
	                                        <c:if test="${fn:trim(accommodationData.featureCodesAndValues['tRating'][0]) ne ''}">
	                                        	<p class="rating rating-md t${fn:replace(accommodationData.featureCodesAndValues['tRating'][0],' ','')}">Rating: ${accommodationData.featureCodesAndValues['tRating'][0]}</p>
	                                        </c:if>

	                                         <c:if test="${not empty accommodationData.locations}">
	                                         	<p>
	                                         	<c:forEach var="locations" items="${accommodationData.locations}" varStatus="stat">
													   <c:if test="${not stat.first}">, </c:if>${locations.value}
												</c:forEach>
	                                         	</p>
	                                         </c:if>

                                        </c:otherwise>

                                        </c:choose>

						<div class="boxout floater">
                                            <c:if test="${accommodationData.priceFrom!=null}">
                                                <p class="price left">
													<c:choose>
														<c:when test="${pageType == 'CRUISE_AND_STAY_OVERVIEW' || pageType == 'CRUISE_AND_STAY_HOTEL_OVERVIEW'}">
												  			CRUISE & STAY FROM
												  		</c:when>
												  		<c:otherwise>
												  		From
												  		</c:otherwise>
													</c:choose>
                                                    <strong>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" groupingUsed="false"  value="${accommodationData.priceFrom}" /></strong>
                                                    per person
                                                </p>
                                            </c:if>
                                            <a class="button right ensLinkTrack" href="${accommodationData.url}" data-componentId="${componentUid}">View details</a>
						</div>
					</div>
				</li>
                                <%--<c:if test='${((counter.index + 1) mod 3) == 0}'><li class="clear-it"></li></c:if>  --%>
				</c:forEach>
			</ul>
		</div>
		<p class="load">Loading</p>
	</div>
            </c:if>
        </c:forEach>
</div>
</c:if>