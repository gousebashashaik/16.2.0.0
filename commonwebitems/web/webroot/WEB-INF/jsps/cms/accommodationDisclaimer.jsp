<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/taggable/AccommodationDisclaimer");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<%-- Disclaimer component (WF_COM_094) --%>
<div class="${componentStyle} disclaimer <c:if test='${siteFlag eq true }'> padBottomZero</c:if>" data-dojo-type="tui.widget.taggable.AccommodationDisclaimer">
    <c:if test="${disclaimerType == 'ACCOMMODATION'}">
        <p>Items marked with * incur extra charges which are payable locally.</p>
        <p <c:if test='${siteFlag eq true }'>class="no-margin"</c:if>>${viewAccommodation.featureCodesAndValues['pleaseNote'][0]}</p>
    </c:if>

    <c:if test="${disclaimerType == 'EXCURSION_ATTRACTION'}">
        <p>${viewRestriction.featureCodesAndValues['restrictionInfo'][0]}</p>
    </c:if>
</div>
