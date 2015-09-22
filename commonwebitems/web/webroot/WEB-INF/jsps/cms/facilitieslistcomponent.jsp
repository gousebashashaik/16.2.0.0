<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<script type="text/javascript">
	dojoConfig.addModuleName("tui/widget/Pills", "tui/widget/carousels/Carousel", "tui/widget/LazyLoadImage", "tui/widget/IntroExpandable");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->

<c:set var="imageExist" value="false"/>
<c:forEach var="facility" items="${accommodationData.facilities}">
    <c:forEach var="image" items="${facility.galleryImages}">
        <c:if test="${image.size == 'medium'}">
            <c:set var="imageExist" value="true"/>
        </c:if>
    </c:forEach>
</c:forEach>

<div class="facilities-hacked">
	<div class="span quarter-bottom-margin">
		<h2 class="underline">Facilities</h2>
	</div>
	<div class="carousel-list-row spacing">
		<div class="span-half">
        <c:if test="${imageExist}">
            <div class="product-list carousel"
				data-dojo-type="tui.widget.carousels.Carousel">
				<div class="viewport">
					<ul class="plist">
                        <c:forEach var="facility" items="${accommodationData.facilities}">
							<c:forEach var="image" items="${facility.galleryImages}">
								<c:if test="${image.size == 'medium'}">
                                    <li><img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${image.mainSrc}'" alt="${image.altText}" width="488" height="274" /></li>
								</c:if>
							</c:forEach>
						</c:forEach>
					</ul>
				</div>
			</div>
        </c:if>
		</div>

		<c:set var="otherCount" value="1" />
		<c:forEach var="facility" items="${accommodationData.facilities}">
			<c:forEach var="image" items="${facility.facilityType}"
				varStatus="status">
				<c:if test="${status.count>0}">

					<c:if test="${facility.parentFacilityType=='FT000S'}">
						<!-- <c:out value="${facility.facilityType}" /> -->
						<c:set var="sportCount" value="${status.count}" scope="page" />
					</c:if>

					<c:if test="${facility.parentFacilityType=='FT000P'}">
						<!-- <c:out value="${facility.facilityType}" /> -->
						<c:set var="poolCount" value="${status.count}" scope="page" />
					</c:if>

					<c:if test="${facility.parentFacilityType=='FT000F'}">
						<!-- <c:out value="${facility.facilityType}" /> -->
						<c:set var="foodCount" value="${status.count}" scope="page" />
					</c:if>

					<c:if test="${facility.parentFacilityType=='FT000H'}">

						<c:set var="healthCount" value="${status.count}" scope="page" />
					</c:if>

					<c:if test="${facility.parentFacilityType=='FT000E'}">

						<c:set var="entCount" value="${status.count}" scope="page" />
					</c:if>

					<c:if test="${facility.parentFacilityType=='FT000O'}">

						<!-- <c:set var="otherCount" value="${status.count}" scope="page" /> -->
						<c:set var="otherCount" value="${otherCount+1}" />
					</c:if>

				</c:if>

			</c:forEach>
			<!--   a<c:out value="${sportCount}" />b
			 c<c:out value="${foodCount}" />d
			 e<c:out value="${healthCount}" />f
			 g<c:out value="${entCount}" />h  -->

		</c:forEach>
		<!--   i<c:out value="${otherCount}" />l -->

		<c:if test="${sportCount > 0}">
			<div class="span-quarter">
				<h3 class="sports">Sports &amp; Activities</h3>
				<c:if test='${siteFlag eq false }'>
				<div class="intro" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
				    <p id="intro-main-copy" class="copy">
					    ${accommodationData.featureCodesAndValues['sport_activities_1_new'][0]}
					    <c:if test="${not empty accommodationData.featureCodesAndValues['sport_activities_2_new'][0]}">
					   	 	<br/><br/>${accommodationData.featureCodesAndValues['sport_activities_2_new'][0]}
					   	</c:if>
					    <c:if test="${not empty accommodationData.featureCodesAndValues['sport_activities_3_new'][0]}">
						    <span class="copy-show-hide"><br/><br/>${accommodationData.featureCodesAndValues['sport_activities_3_new'][0]}</span>
						    <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
					    </c:if>
				    </p>
			    </div>
				 </c:if>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000S'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<c:if test="${foodCount > 0}">
			<div class="span-quarter">
				<h3 class="food">Food &amp; Drink</h3>
				<c:if test='${siteFlag eq false }'>
				<div class="intro" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
				    <p id="intro-main-copy" class="copy">
					    ${accommodationData.featureCodesAndValues['food_drink_1_new'][0]}
					    <c:if test="${not empty accommodationData.featureCodesAndValues['food_drink_2_new'][0]}">
					    	<br/><br/>${accommodationData.featureCodesAndValues['food_drink_2_new'][0]}
					    </c:if>
					    <c:if test="${not empty accommodationData.featureCodesAndValues['food_drink_3_new'][0]}">
						    <span class="copy-show-hide"><br/><br/>${accommodationData.featureCodesAndValues['food_drink_3_new'][0]}</span>
						    <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
					    </c:if>
				    </p>
			    </div>
				 </c:if>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000F'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

	</div>

	<div class="normal-row">

		<c:if test="${healthCount > 0}">
			<div class="span-quarter">
				<h3 class="health">Health &amp; Beauty</h3>
				<c:if test='${siteFlag eq false }'>
				<div class="intro" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
				    <p id="intro-main-copy" class="copy">
					    ${accommodationData.featureCodesAndValues['health_beauty_1_new'][0]}
					    <c:if test="${not empty accommodationData.featureCodesAndValues['health_beauty_2_new'][0]}">
					    	<br/><br/>${accommodationData.featureCodesAndValues['health_beauty_2_new'][0]}
					    </c:if>
					    <c:if test="${not empty accommodationData.featureCodesAndValues['health_beauty_3_new'][0]}">
						    <span class="copy-show-hide"><br/><br/>${accommodationData.featureCodesAndValues['health_beauty_3_new'][0]}</span>
						    <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
					    </c:if>
				    </p>
			    </div>
				 </c:if>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000H'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<c:if test="${entCount > 0}">
			<div class="span-quarter">
				<h3 class="entertain">Entertainment</h3>
				<c:if test='${siteFlag eq false }'>
				<div class="intro" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
				    <p id="intro-main-copy" class="copy">
					    ${accommodationData.featureCodesAndValues['entertainment_1_new'][0]}
					    <c:if test="${not empty accommodationData.featureCodesAndValues['entertainment_2_new'][0]}">
					   		<br/><br/>${accommodationData.featureCodesAndValues['entertainment_2_new'][0]}
					    </c:if>
					    <c:if test="${not empty accommodationData.featureCodesAndValues['entertainment_3_new'][0]}">
						    <span class="copy-show-hide"><br/><br/>${accommodationData.featureCodesAndValues['entertainment_3_new'][0]}</span>
						    <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
					    </c:if>
				    </p>
			    </div>
				 </c:if>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000E'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<c:if test="${poolCount > 0}">
			<div class="span-quarter">
				<h3 class="pool">Pool</h3>
				<c:if test='${siteFlag eq false }'>
				<div class="intro" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
				    <p id="intro-main-copy" class="copy">
					    ${accommodationData.featureCodesAndValues['pool_scene_1_new'][0]}
					    <c:if test="${not empty accommodationData.featureCodesAndValues['pool_scene_2_new'][0]}">
					    	<br/><br/>${accommodationData.featureCodesAndValues['pool_scene_2_new'][0]}
					    </c:if>
					    <c:if test="${not empty accommodationData.featureCodesAndValues['pool_scene_3_new'][0]}">
						    <span class="copy-show-hide"><br/><br/>${accommodationData.featureCodesAndValues['pool_scene_3_new'][0]}</span>
						    <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
					    </c:if>
				    </p>
			    </div>
				 </c:if>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000P'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<c:if test="${otherCount > 0}">
			<div class="span-quarter">
				<h3 class="other">Other facilities</h3>
				<ul>
					<c:forEach var="facility" items="${accommodationData.facilities}">
						<c:if test="${facility.parentFacilityType == 'FT000O'}">
							<li>${facility.name} <c:if
									test="${not empty facility.featureCodesAndValues['openingTimes']}">
									<span>Opening Times:
										${facility.featureCodesAndValues['openingTimes']}</span>
								</c:if> <span>${facility.description}</span>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>

	</div>
</div>