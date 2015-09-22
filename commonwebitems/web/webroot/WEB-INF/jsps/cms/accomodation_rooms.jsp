<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<%-- <script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/LazyLoadImage", "tui/widget/taggable/AccomodationRoomResultPane");
</script>

Accommodation Rooms Result Pane component (WF_COM_046)
<div data-dojo-type="tui.widget.taggable.AccomodationRoomResultPane">
<div class="span">
    <c:choose>
        <c:when test="${accomPageType == 'bookflow'}">
            <h2 class="underline">Room types</h2>
            You can select your room later on
        </c:when>
        <c:otherwise>
            <h2 class="underline">Available room types</h2>
        </c:otherwise>
    </c:choose>
</div>


<c:forEach var="room" items="${viewData.roomsData}" varStatus="counter">
    <div class="span-half">
        <div class="panel labeled-results-pane">
            <div class="image-container">
                <images:first collection="${room.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-rooms.jpg" size="medium" width="488" height="274"  cdnDomain="${cdnDomain}"/>
            </div>

            <div class="copy">
                <h3>${room.roomTitle}</h3>
                <ul>
                    <li>Sleeps: Varies - min ${room.occupancy['minOccupancy']}, max ${room.occupancy['maxOccupancy']}</li>
                    <li>${room.description}</li>
                    <c:if test="${not empty room.sharedUsps}">
                        <li><span>FACILITIES</span>
                            <ul class="square inline">
                                <c:forEach items="${room.sharedUsps}" begin="0" step="1" varStatus="loop">
                                    <li><span class="sprite-img-grp-1"></span>${room.sharedUsps[loop.index] }</li>
                                </c:forEach>
                            </ul>
                        </li>
                     </c:if>
                     <c:if test="${not empty room.varyingUsps}">
                        <li><span>OPTIONAL FACILITIES IN SOME ROOMS</span>
                            <ul class="square inline">
                                <c:forEach items="${room.varyingUsps}" begin="0" step="1" varStatus="loop">
                                    <li><span class="sprite-img-grp-1"></span>${room.varyingUsps[loop.index] }</li>
                                </c:forEach>
                            </ul>
                        </li>
                     </c:if>
                     <c:if test="${not empty room.upgrade}">
                        <li><span>ROOM UPGRADES</span>
                            <ul class="square inline">
                                <c:forEach items="${room.upgrade}" begin="0" step="1" varStatus="loop">
                                    <li><span class="sprite-img-grp-1"></span>${room.upgrade[loop.index] }</li>
                                </c:forEach>
                            </ul>
                        </li>
                     </c:if>
                </ul>
            </div>
        </div>
    </div>
<c:if test="${(counter.count mod 2) == 0}">
    <div class="clear"></div>
</c:if>
</c:forEach>
</div>
 --%>

<c:set var="jsonRoomsData" value="${jsonRoomsData}"/>
 <!-- jsonRoomsData: ${jsonRoomsData} -->
<script type="text/javascript">
		dojoConfig.addModuleName("tui/widget/LazyLoadImage", "tui/widget/taggable/AccomodationRoomResultPane", "tui/widget/expand/Expandable", "tui/widget/media/HeroCarousel", "tui/widget/media/MediaPopup", "tui/widget/RoomPlansOverlay");
</script>

<c:set var="search" value="'" />
<c:set var="replace" value="\\'" />

	<div data-dojo-type="tui.widget.taggable.AccomodationRoomResultPane">
		<div class="span">
			<h2 class="underline no-margin-bottom">Room types</h2>
        </div>

				<div class="component no-margin-top cf bookflow-accordion">
					<div data-dojo-type="tui.widget.expand.Expandable" class="roomTypes">
					<c:forEach var="room" items="${viewData.roomsData}" varStatus="counter">
						<div class="item <c:if test="${counter.index lt 4}">open </c:if> <c:if test="${counter.index eq 0}">first</c:if>">
							<h3 class="item-toggle">
								<span class="roomHeaderSelected capitalize">${room.roomTitle}</span>
								<i></i>
							 </h3>
							<div class="item-content">
								<div class="panel labelled-results-panel">
									<div class="image-container">
										<images:first collection="${room.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-rooms.jpg" size="medium" width="488" height="274"/>
										<!-- <span class="label">Caption text go here</span> -->
										<c:if test="${fn:length(room.galleryImages) gt 4}">
											<div class="gallery" data-dojo-type="tui.widget.media.MediaPopup" data-dojo-props='jsonData:${jsonRoomsData},dataPath:"${counter.index}"'>
												<i class="icon camera"></i>
												<span class="label">Gallery</span>
											</div>
										</c:if>
                                    </div>
									<div class="copy">
										<p class="sub-title">Sleeps: Varies - min ${room.occupancy['minOccupancy']}, max ${room.occupancy['maxOccupancy']}</p>
										<p>${room.description}</p>
										<c:if test="${not empty room.sharedUsps}">
											<p class="sub-title">Facilities:</p>
											<div class="three-cols">
											<ul class="square cf">
												<c:forEach items="${room.sharedUsps}" begin="0" step="1" varStatus="status">
	                                    			<li><span class="sprite-img-grp-1"></span>${room.sharedUsps[status.index]}</li>
		                                    		<c:if test="${(status.count % 3 == 0)}">
		                                    		<c:if test="${not status.last}">
		                                    			<div class="clear"></div>
														</ul>
														<ul class="square cf">
													</c:if>
													</c:if>
													</c:forEach>
											</ul>
											<div class="clear"></div>
											</div>
										</c:if>
										<c:if test="${not empty room.varyingUsps}">
											<p class="sub-title">OPTIONAL FACILITIES IN SOME ROOMS:</p>
											<div class="three-cols">
											<ul class="square cf">
												<c:forEach items="${room.varyingUsps}" begin="0" step="1" varStatus="status">
	                                    			<li><span class="sprite-img-grp-1"></span>${room.varyingUsps[status.index]}</li>
		                                    		<c:if test="${(status.count % 3 == 0)}">
		                                    		<c:if test="${not status.last}">
		                                    			<div class="clear"></div>
														</ul>
														<ul class="square cf">
													</c:if>
													</c:if>
												</c:forEach>
											</ul>
											<div class="clear"></div>
											</div>
										</c:if>
										<c:if test="${not empty room.upgrade}">
											<p class="sub-title">ROOM UPGRADES:</p>
											<div class="three-cols">
											<ul class="square cf">
												<c:forEach items="${room.upgrade}" begin="0" step="1" varStatus="status">
	                                    			<li><span class="sprite-img-grp-1"></span>${room.upgrade[status.index]}</li>
		                                    		<c:if test="${(status.count % 3 == 0)}">
		                                    		<c:if test="${not status.last}">
		                                    			<div class="clear"></div>
														</ul>
														<ul class="square cf">
													</c:if>
													</c:if>
													</c:forEach>
											</ul>
											<div class="clear"></div>
											</div>
										</c:if>
											<c:if test="${not empty room.roomPlanImage && room.roomPlanImage ne ''}">
												<a class="uppercase" href="#" data-dojo-type="tui.widget.RoomPlansOverlay" data-dojo-props="title:'${fn:replace(room.roomTitle, search, replace)}', imageSrc:'${room.roomPlanImage}', idx:'${counter.index}'">Room plans <i class="arrow-right"></i></a>
											</c:if>
									</div>
								</div>
							</div>
						</div>
						</c:forEach>
					</div>
				</div>
				<c:if test="${siteFlag eq true }"> <br/> </c:if>
			</div>
