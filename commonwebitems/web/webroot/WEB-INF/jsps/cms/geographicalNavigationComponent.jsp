<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tui" uri="/WEB-INF/TreeStructIterator.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/modules/PopupModule", "tui/widget/carousels/Carousel", "tui/widget/taggable/GeographicalNavigation", "tui/widget/StatefulPills");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->

<c:set var="tabProps" value="" />
<c:set var="hotelTabId" value="" />
<c:set var="villaTabId" value="" />
<c:choose>
    <c:when test="${not empty viewData.categories[0].categories}">
        <c:if test="${not empty viewData.categoriesByAccomType['HOTEL'] && not empty viewData.categoriesByAccomType['VILLA']}">
            <c:set var="tabProps" value="class='tabs-container' data-dojo-type='tui.widget.StatefulPills'" />
            <c:set var="hotelTabId" value="tabHotels" />
            <c:set var="villaTabId" value="tabVillas" />
        </c:if>
    </c:when>
    <c:otherwise>
        <c:set var="accommodations" value="${viewData.accommodations}" />
        <c:if test="${not empty accommodations['HOTEL'] && not empty accommodations['VILLA']}">
            <c:set var="tabProps" value="class='tabs-container' data-dojo-type='tui.widget.StatefulPills'" />
            <c:set var="hotelTabId" value="tabHotels" />
            <c:set var="villaTabId" value="tabVillas" />
        </c:if>
    </c:otherwise>
</c:choose>
<div class="span <c:if test='${siteFlag eq true }'> ${componentStyle}</c:if>">
    <div ${tabProps}>
        <c:choose>
            <c:when test="${viewData.locationType == 'CONTINENT'}">
                <h2 class="underline">A-Z of Destinations</h2>
                <c:set var="categories" value="${viewData.categories}" />
                <c:set var="accomType" value="" />
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${empty viewData.categories[0].categories}">
                        <c:if test="${not empty accommodations}">
                            <h2 class="underline">Where we go in ${viewData.locationName}
                                <c:if test="${(fn:length(accommodations['HOTEL']) > 0 && fn:length(accommodations['VILLA']) > 0) || (fn:length(viewData.categoriesByAccomType['HOTEL']) > 0 && fn:length(viewData.categoriesByAccomType['VILLA']) > 0)}">
                                    <ul class="pills pills-inline <c:if test='${ siteFlag eq true }'> marginLeft no-margin</c:if>" >
                                        <li class="pill" data-dojo-value="hotel"><a class="ensLinkTrack" href="#${hotelTabId}">Hotels / Apartments</a></li> <!-- data-componentid="WF_COM_001-1" -->
                                        <li class="pill" data-dojo-value="villa"><a class="ensLinkTrack" href="#${villaTabId}">Villas</a></li> <!-- data-componentid="WF_COM_001-1" -->
                                    </ul>
                                </c:if>
                            </h2>
                        </c:if>
                        <c:set var="accomType" value="" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="categories" value="${viewData.categories[0].categories}" />
                        <c:if test="${not empty categories}">
                            <h2 class="underline"><span>Where we go in ${viewData.categories[0].name}</span>
                                <c:if test="${pageType == 'overview'}">
								<c:choose>
								<c:when test="${siteFlag eq true }">
								<a class="view-all ensLinkTrack has-icon-left" href="${viewData.placesToGoUrl}" ><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> Show on a map</a>
								</c:when>
								<c:otherwise>
								<a href="${viewData.placesToGoUrl}" data-componentId="${componentUid}" class="show-map ensLinkTrack ">
																Show on a map &raquo;
															</a>
								</c:otherwise>
								</c:choose>
                                    
                                </c:if>
                                <c:if test="${(fn:length(accommodations['HOTEL']) > 0 && fn:length(accommodations['VILLA']) > 0) || (fn:length(viewData.categoriesByAccomType['HOTEL']) > 0 && fn:length(viewData.categoriesByAccomType['VILLA']) > 0)}">
                                    <c:choose> 
									<c:when test="${pageType == 'overview'}">  
										<ul class="pills pills-inline <c:if test='${ siteFlag eq true }'> marginLeft </c:if>">
									</c:when>
									<c:otherwise>
										<ul class="pills pills-inline <c:if test='${ siteFlag eq true }'> marginLeft no-margin</c:if>">
									</c:otherwise>
									</c:choose>									
                                        <li class="pill" data-dojo-value="hotel"><a class="ensLinkTrack" href="#${hotelTabId}">Hotels / Apartments</a></li> <!-- data-componentid="WF_COM_001-1" -->
                                        <li class="pill" data-dojo-value="villa"><a class="ensLinkTrack" href="#${villaTabId}">Villas</a></li> <!-- data-componentid="WF_COM_001-1" -->
                                    </ul>
                                </c:if>
                            </h2>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${viewData.locationType =='CONTINENT'}">
                <ul id="geo-nav">
                <c:forEach var="category" items="${categories}" varStatus="status">
                    <li>
                        <h3 <c:if test="${empty category.categories}">class="no-category"</c:if>>${category.name}</h3>
                        <tui:navIteration category="${category}" depth="${viewData.navigationLevelDepth}" accomType="" componentID="${componentId}" />
                    </li>
                </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <c:if test="${not empty viewData.categoriesByAccomType['HOTEL'] || not empty accommodations['HOTEL']}">
                    <div id="${hotelTabId}">
                        <ul class="geo-nav <c:if test='${siteFlag eq true }'> square-with-arrow</c:if>">
                            <c:set var="categories" value="${viewData.categoriesByAccomType['HOTEL']}" />
                            <c:if test="${not empty categories}">
                                <c:forEach var="category" items="${categories}" varStatus="status">
                                    <li>
                                        <h3 <c:if test="${empty category.categories}">class="no-category"</c:if>>
                                            <a class="ensLinkTrack" data-componentId="${componentUid}" href="${category.url}">${category.name}</a>
                                        </h3>
                                        <tui:navIteration category="${category}" depth="${viewData.navigationLevelDepth}" accomType="HOTEL" componentID="${componentId}"/>
                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not empty accommodations['HOTEL']}">
                                <c:forEach var="accom" items="${accommodations['HOTEL']}">
                                    <li class="wide">
                                        <h3>
                                            <a href="${accom.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accom.name}</a>
                                            <c:if test="${accom.tRating != ''}">
                                                <span class="rating rating-inline rating-sm t${fn:replace(accom.featureCodesAndValues['tRating'][0],' ','')}">T Rating: ${accom.featureCodesAndValues['tRating'][0]}</span>
                                            </c:if>
                                             <c:if test="${not empty accom.productRanges}">
											    <c:set var="className" value="diff-label-sm ${fn:toLowerCase(accom.productRanges[0].code)}" />
												<c:if test="${fn:toLowerCase(accom.productRanges[0].code) eq 'smr'}">
												   <c:set var="className" value="diff-label-sm cou-sensimar" />
												</c:if>
												 <c:if test="${fn:toLowerCase(accom.productRanges[0].code) eq 'fam'}">
												  	<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
												   		<c:set var="className" value="diff-label-sm fam-life" />
												   	</c:if>
												</c:if>

                                                <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accom.productRanges[0].featureCodesAndValues.strapline[0]}'" class="${className}"></span>
                                            </c:if>
                                        </h3>
                                    </li>
                                </c:forEach>
                            </c:if>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${not empty viewData.categoriesByAccomType['VILLA'] || not empty accommodations['VILLA']}">
                    <div id="${villaTabId}">
                        <ul class="geo-nav <c:if test='${siteFlag eq true }'> square-with-arrow</c:if>">
                            <c:set var="categories" value="${viewData.categoriesByAccomType['VILLA']}" />
                            <c:if test="${not empty categories}">
                                <c:forEach var="category" items="${categories}" varStatus="status">
                                    <li>
                                        <h3 <c:if test="${empty category.categories}">class="no-category"</c:if>>
                                            <a class="ensLinkTrack" data-componentId="${componentUid}" href="${category.url}">${category.name}</a>
                                        </h3>
                                        <tui:navIteration category="${category}" depth="${viewData.navigationLevelDepth}" accomType="VILLA" componentID="${componentId}"/>
                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not empty accommodations['VILLA']}">
                                <c:forEach var="accom" items="${accommodations['VILLA']}">
                                    <li class="wide">
                                        <h3>
                                            <a href="${accom.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accom.name}</a>
                                            <c:if test="${accom.tRating != ''}">
                                                <span class="rating rating-inline rating-sm t${fn:replace(accom.featureCodesAndValues['tRating'][0],' ','')}">T Rating: ${accom.featureCodesAndValues['tRating'][0]}</span>
                                            </c:if>
                                            <c:if test="${not empty accom.productRanges}">
											
												<c:set var="className" value="diff-label-sm ${fn:toLowerCase(accom.productRanges[0].code)}" />
												<c:if test="${fn:toLowerCase(accom.productRanges[0].code) eq 'smr'}">
												   <c:set var="className" value="diff-label-sm ${fn:toLowerCase(accom.productRanges[0].code)}-sensimar" />
												</c:if>
												<c:if test="${fn:toLowerCase(accom.productRanges[0].code) eq 'fam'}">
												 	<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
												   		<c:set var="className" value="diff-label-sm fam-life" />
												   	</c:if>
												</c:if>
                                                <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accom.productRanges[0].featureCodesAndValues.strapline[0]}'" class="${className}"></span>
                                            </c:if>
                                        </h3>
                                    </li>
                                </c:forEach>
                            </c:if>
                        </ul>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>