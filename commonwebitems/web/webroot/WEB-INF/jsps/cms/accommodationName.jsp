<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
    dojoConfig.addModuleName("tui/widget/popup/Tooltips");
</script>
<c:choose>
    <c:when test="${accomPageType == 'bookflow' && sessionScope.siteName == 'thomson' &&
                   (viewData.productRanges[0].code == 'FHV' ||
                    viewData.productRanges[0].code == 'FSP' ||
                    viewData.productRanges[0].code == 'FSO' ||
                    viewData.productRanges[0].code == 'FPF' ||
                    viewData.productRanges[0].code == 'FPL' ||
                    viewData.productRanges[0].code == 'FPR' )}">
        <c:set var="showTooltip" value="false"/>
    </c:when>
    <%--<c:when test="${accomPageType == 'bookflow' && sessionScope.siteName == 'thomson' && (viewData.productRanges[0].code == 'SKY')}">--%>
        <%--<c:set var="crossBrand" value="true"/>--%>
        <%--<c:set var="crossBrandText" value="SKYTOURS"/>--%>
    <%--</c:when>--%>
    <%--<c:when test="${accomPageType == 'bookflow' && sessionScope.siteName == 'thomson' && (viewData.productRanges[0].code == 'SIM')}">--%>
        <%--<c:set var="crossBrand" value="true"/>--%>
        <%--<c:set var="crossBrandText" value="Simply Travel"/>--%>
    <%--</c:when>--%>
    <c:otherwise>
        <c:set var="showTooltip" value="true"/>
    </c:otherwise>
</c:choose>

<div class="${componentStyle}">
	<h1 class="page-title">
		<span class="inline-title">
            <c:choose>
                <c:when test="${viewData.featureCodesAndValues['name'][0]} ne ''">${viewData.featureCodesAndValues['name'][0]}</c:when>
                <c:otherwise>${viewData.name}</c:otherwise>
            </c:choose>
		</span>
        <c:if test="${not empty viewData.featureCodesAndValues['tRating'][0]
                        && !fn:contains(viewData.featureCodesAndValues['tRating'][0], '/')
                        && !fn:contains(viewData.featureCodesAndValues['tRating'][0], 'apartments')}">
		    <span>
		    <span class="rating rating-inline t${fn:replace(viewData.featureCodesAndValues['tRating'][0],' ','')}">&nbsp;</span>
		    </span>
        </c:if>
        <c:if test="${not empty viewData.productRanges and not empty viewData.productRanges[0].code}">
			 <c:choose>
                <c:when test="${sessionScope.siteName == 'cruise'}">				
				<c:set var="className" value="diff-label ${fn:toLowerCase(viewData.productRanges[0].code)}" />
				<c:if test="${fn:toLowerCase(viewData.productRanges[0].code) eq 'smr'}">
				   <c:set var="className" value="diff-label cou-sensimar" />
				</c:if>
				<c:if test="${fn:toLowerCase(viewData.productRanges[0].code) eq 'fam'}">
				<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
				   <c:set var="className" value="diff-label fam-res-life" />
				</c:if>
				</c:if>
				<span class="diff-label-block ${className}" <c:if test="${showTooltip}">data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${viewData.productRanges[0].featureCodesAndValues.strapline[0]}'"</c:if>></span>
			
				</c:when>
                <c:otherwise>
					<span class="diff-label diff-label-block ${fn:toLowerCase(viewData.productRanges[0].code)}" <c:if test="${showTooltip}">data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${viewData.productRanges[0].featureCodesAndValues.strapline[0]}'"</c:if>></span>
				</c:otherwise>
            </c:choose>
        </c:if>
	</h1>
</div>