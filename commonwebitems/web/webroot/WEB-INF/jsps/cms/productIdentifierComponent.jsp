<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<c:forEach var="productRange" items="${viewData.productRanges}">
    <c:set var="productCode" value="${fn:toLowerCase(productRange.code)}"/>
    <c:choose>
        <%--FIRSTCHOICE PRODUCTS--%>
        <c:when test="${productCode == 'fsp'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/family-holidays/splashworld/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fhv'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/holiday-villages/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fpr'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/premier-holidays/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fpl'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/premier-holidays/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fpf'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/premier-holidays/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fml'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/club-magic-life/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <c:when test="${productCode == 'fso'}">
            <c:set var="url" value="http://www.firstchoice.co.uk/sun-holidays/suneo-club/"/>
            <c:set var="prodOwner" value="firstchoice"/>
            <c:set var="alignClass" value=" diff-editorial-right"/>
            <c:set var="moreText" value="More about "/>
        </c:when>
        <%--THOMSON PRODUCTS--%>
        <c:when test="${productCode == 'alc'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/a-la-carte/thomson-a-la-carte.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'gld'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/gold/thomson-gold.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'smr'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/couples/thomson-couples.html"/>
            <c:set var="urlCouples" value="http://www.thomson.co.uk/editorial/couples/thomson-couples.html"/>
            <c:set var="urlSensimar" value="/destinations/product/Sensimar-SMR"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'cou'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/couples/thomson-couples.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'sen'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/sensatori/sensatori.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'fam'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/families/thomson-family-resorts.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'plt'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/platinum/thomson-platinum.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'pll'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/platinum/thomson-platinum.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'snf'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/small-and-friendly/small-and-friendly.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'ftw'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/2wentys/2wentys-holidays.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'sky'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/skytours/skytours-holidays.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'cnu'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/chic-and-unique/chic-and-unique.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'hpd'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/gold/handpicked-hotels.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
            <c:set var="accomPageType" value="bookflow"/>
        </c:when>
        <c:when test="${productCode == 'sim'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/world-of-tui/simply-travel.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'tvp'}">
            <c:set var="url" value="http://www.thomson.co.uk/villas.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'sce'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/scene/thomson-scene.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'hub'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/scene/thomson-scene.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'sty'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/scene/thomson-scene.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
          <c:when test="${productCode == 'fav'}">
            <c:set var="url" value="http://www.thomson.co.uk/editorial/3t-favourites/thomson-3t-favourites.html"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:when test="${productCode == 'rob'}">
            <c:set var="url" value="http://www.thomson.co.uk/holidays/robinson-club"/>
            <c:set var="prodOwner" value="thomson"/>
            <c:set var="alignClass" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:when>
        <c:otherwise>
            <c:set var="url" value="javascript:void(0);"/>
            <c:set var="prodOwner" value=""/>
            <c:set var="moreText" value="Find out more about "/>
        </c:otherwise>
    </c:choose>
    <script type="text/javascript">
        dojoConfig.addModuleName("tui/widget/LazyLoadImage");
    </script>
    <div class="span-two-third ${prodOwner}">
        <div class="<c:choose><c:when test="${productCode == 'smr'}">cou</c:when>
	      					  <c:otherwise>${productCode}</c:otherwise>
							  </c:choose> diff-editorial ${alignClass}">
            <c:if test="${prodOwner == 'thomson'}">
			
			<c:set var="editorialTitle" value="${fn:replace(productRange.featureCodesAndValues.name[0], 'Thomson', '')}" />
            <c:set var="className" value="diff-editorial-title" />
			<c:if test="${productCode == 'smr'}">
			<c:set var="editorialTitle" value="<span>Couples</span><span>${fn:replace(productRange.featureCodesAndValues.name[0], 'Thomson', '')}</span>" />
            <c:set var="className" value="dual-brand diff-editorial-title" />
			</c:if>
            <h2 class="${className}">${editorialTitle}</h2>
            </c:if>
            <h3 class="diff-editorial-strapline">${productRange.featureCodesAndValues.strapline[0]}</h3>
            <div class="diff-editorial-copy">
                <ul class="bulleted">
                    <c:forEach var="productUsp" items="${productRange.productUsps}" begin="0" end="5" step="1">
                        <li><i class="icon icon-inline icon-inline-left icon-bullet"></i> ${productUsp.name}</li>
                    </c:forEach>
                </ul>
                <c:if test='${accomPageType != "bookflow"}'>
                <p class="diff-editorial-url">
                <c:choose>
	                <c:when test="${productCode == 'smr'}">
						<c:choose>
							<c:when test="${siteFlag eq true }">
								<h2 class="more-link no-margin"> Find out more about <a href="${urlCouples}">Couples</a> and<a class="view-all ensLinkTrack has-icon-left" href="${urlSensimar}" data-componentId="${componentUid}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> ${moreText} ${productRange.featureCodesAndValues.name[0]}</a>
							</c:when>
						 <c:otherwise>
						 Find out more about <a href="${urlCouples}">Couples</a> and <a href="${urlSensimar}">${productRange.featureCodesAndValues.name[0]}</a>
						 </c:otherwise>
					 </c:choose>
					</c:when>
	                <c:otherwise>					
					<c:choose>
							<c:when test="${siteFlag eq true }">
								<h2 class="more-link no-margin"><a class="view-all ensLinkTrack has-icon-left" href="${url}" data-componentId="${componentUid}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> ${moreText} ${productRange.featureCodesAndValues.name[0]}</a>
							</c:when>
						 <c:otherwise>
						<a href="${url}" class="ensLinkTrack" data-componentId="${componentUid}">${moreText} ${productRange.featureCodesAndValues.name[0]} &raquo;</a>
						 </c:otherwise>
					 </c:choose>
                    </c:otherwise>
                </c:choose>
                </p>
                </c:if>			
            </div>
        </div>
    </div>
</c:forEach>