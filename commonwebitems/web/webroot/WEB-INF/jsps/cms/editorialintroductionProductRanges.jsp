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
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<!--- Editorial Introduction Component (NARROW) --->

<div class="${componentStyle}">
	<div class="content-block">
		<h2 class="lowercase <c:if test='${siteFlag eq true }'>title-color-size</c:if>">${editorialIntroductionData.featureCodesAndValues['overviewDescription'][0]}</h2>
	</div>
</div>