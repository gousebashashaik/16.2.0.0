<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<%--
/*
 * Copyright 2011 Portaltech.
 *
 * Originating Unit: Portal Technology Systems Ltd.
 * http://www.portaltech.co.uk
 *
 * Please contact authors regarding licensing and redistribution.
 */
--%>

<div class="${componentStyle}">

	<p>${editorialIntroductionData.featureCodesAndValues['strapline'][0]}</p>
	
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro1Title'][0]}"> 
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['intro1Title'][0]}</h2>
	</c:if>
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro1Body'][0]}"> 
		<p>${editorialIntroductionData.featureCodesAndValues['intro1Body'][0]}</p>
	</c:if>	

	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro2Title'][0]}"> 
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['intro2Title'][0]}</h2>
	</c:if>
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro2Body'][0]}"> 
		<p>${editorialIntroductionData.featureCodesAndValues['intro2Body'][0]}</p>
	</c:if>	

	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro3Title'][0]}"> 
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['intro3Title'][0]}</h2>
	</c:if>
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro3Body'][0]}"> 
		<p>${editorialIntroductionData.featureCodesAndValues['intro3Body'][0]}</p>
	</c:if>	

	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro4Title'][0]}"> 
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['intro4Title'][0]}</h2>
	</c:if>
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro4Body'][0]}"> 
		<p>${editorialIntroductionData.featureCodesAndValues['intro4Body'][0]}</p>
	</c:if>	

	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro5Title'][0]}"> 
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['intro5Title'][0]}</h2>
	</c:if>
	<c:if test="${not empty editorialIntroductionData.featureCodesAndValues['intro5Body'][0]}"> 
		<p>${editorialIntroductionData.featureCodesAndValues['intro5Body'][0]}</p>
	</c:if>	
	
</div>