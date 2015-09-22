<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<c:set var="type" value="${viewData.title}" scope="page"/>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<div class="span-third">

<div class="<c:if test='${siteFlag eq true }'> boxout pad24 floatLeft </c:if>">

    <h2 class="<c:if test='${siteFlag eq false }'>underline </c:if>">${component.name}</h2>
    <div class="travel-life">
        <h4 class="travel">${viewData.title}</h4>
        <div class="desc">
            <p>${viewData.description}</p>
        </div>
        <c:if test='${ type eq "TRAVELIFE GOLD"}'>
        <div class="image-container">
           <img alt="" src="${cdnDomain}/images/travellife-gold.gif" placeholder="${cdnDomain}/images/loader-slideshow.gif">
        </div>
        </c:if>
        <c:if test='${ type eq "TRAVELIFE SILVER"}'>
        <div class="image-container">
           <img alt="" src="${cdnDomain}/images/travellife-silver.gif" placeholder="${cdnDomain}/images/loader-slideshow.gif">
        </div>
        </c:if>
        <c:if test='${ type eq "TRAVELIFE BRONZE"}'>
        <div class="image-container">
           <img alt="" src="${cdnDomain}/images/travellife-bronze.gif" placeholder="${webrootPath}/images/loader-slideshow.gif">
        </div>
        </c:if>
    </div>
</div>
</div>