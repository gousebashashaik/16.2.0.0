<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->

<div class="span-two-third <c:if test='${siteFlag eq true }'>${componentStyle} </c:if> clearfix board-basis">
    <h2 class="underline">Food &amp; Drink</h2>
    <c:forEach var="boardBasis" varStatus="status" items="${normalBoardBasisDatas}">
        <h3>${boardBasis.name}</h3>
        <c:if test="${not empty boardBasis.featureCodesAndValues['meals_description'][0]
                        || not empty boardBasis.featureCodesAndValues['drinks_description'][0]
                        || not empty boardBasis.featureCodesAndValues['snacks_description'][0]}">
        <p>${boardBasis.featureCodesAndValues['meals_description'][0]}
            ${boardBasis.featureCodesAndValues['drinks_description'][0]}
            ${boardBasis.featureCodesAndValues['snacks_description'][0]}</p>
        </c:if>
    </c:forEach>

    <c:if test="${not empty complexBoardBasisDatas}">
        <table class="board-basis-data table-ticks">
            <thead>
                <tr class="col-headings underline-dashed">
                    <th class="row-heading uc">Dining options</th>
                    <th class="col-heading uc">Breakfast</th>
                    <th class="col-heading uc">Lunch</th>
                    <th class="col-heading uc">Evening meal</th>
                    <th class="col-heading uc">Drinks</th>
                    <th class="col-heading uc">Snacks, Icecream</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="boardBasis" varStatus="status" items="${complexBoardBasisDatas}">
                <tr <c:if test='${siteFlag eq true }'>class="border-tb" </c:if> >
                    <th class="row-sub-heading">${boardBasis.name}</th>
                    <td 
                    	<c:choose>
                    	<c:when test="${boardBasis.featureCodesAndValues['breakfast_included'][0] eq 'Y'}">
                    		class="tick"
                    	</c:when>	
                    	<c:otherwise>
                    		class="tick <c:if test='${siteFlag eq true }'> uncheck </c:if>"
                    	</c:otherwise>
                    	</c:choose>
                    ></td>
                    <td  
                    	<c:choose>
                    	<c:when test="${boardBasis.featureCodesAndValues['lunch_included'][0] eq 'Y' || boardBasis.featureCodesAndValues['meals_description'][0] eq 'Y'}">
                    		class="tick"
                    	</c:when>	
                    	<c:otherwise>
                    		class="tick <c:if test='${siteFlag eq true }'> uncheck </c:if>"
                    	</c:otherwise>
                    	</c:choose>
                    ></td>
                    <td  
                    <c:choose>
                    	<c:when test="${boardBasis.featureCodesAndValues['dinner_included'][0] eq 'Y'}">
                    		class="tick"
                    	</c:when>	
                    	<c:otherwise>
                    		class="tick <c:if test='${siteFlag eq true }'> uncheck </c:if>"
                    	</c:otherwise>
                    	</c:choose>
                    ></td>
                    <td
                    
                    <c:choose>
                    	<c:when test="${boardBasis.featureCodesAndValues['drinks_included'][0] eq 'Y' || boardBasis.featureCodesAndValues['drinks_description'][0] eq 'Y'}">
                    		class="tick" 
                    	</c:when>	
                    	<c:otherwise>
                    		class="tick <c:if test='${siteFlag eq true }'> uncheck </c:if>"
                    	</c:otherwise>
                    	</c:choose>
                    ></td>
                    <td
                    <c:choose>
                    	<c:when test="${boardBasis.featureCodesAndValues['drinks_included'][0] eq 'Y' || boardBasis.featureCodesAndValues['drinks_description'][0] eq 'Y'}">
                    		class="tick"
                    	</c:when>	
                    	<c:otherwise>
                    		class="tick <c:if test='${siteFlag eq true }'> uncheck </c:if>"
                    	</c:otherwise>
                    	</c:choose>
                    ></td>
                </tr>
                <tr <c:if test='${siteFlag eq false }'> <c:if test="${!status.last}">class="underline-dashed"</c:if></c:if>>
                    <td colspan="6" class="row-description">
                        <p>${boardBasis.featureCodesAndValues['meals_description'][0]}&nbsp;
                            ${boardBasis.featureCodesAndValues['drinks_description'][0]}&nbsp;
                            ${boardBasis.featureCodesAndValues['snacks_description'][0]}
                        </p>
                    </td>
                </tr>
                
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>
