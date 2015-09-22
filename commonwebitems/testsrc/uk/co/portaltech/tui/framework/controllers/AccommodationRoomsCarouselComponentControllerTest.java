/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AccommodationRoomsCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationRoomViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.DisplayMode;

import com.enterprisedt.util.debug.Logger;


/**
 * @author niranjani.r
 * 
 */
public class AccommodationRoomsCarouselComponentControllerTest
{

	@Mock
	private AccommodationFacade accomodationFacade;

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private CategoryService categoryService;

	@Mock
	private CMSSiteService cmsSiteService;

	@Mock
	private CatalogVersionModel model;
	@Mock
	private CatalogModel catalogmodel;

	
	private final TUILogUtils LOG = new TUILogUtils("AccommodationRoomsCarouselComponentControllerTest");

	AccommodationViewData accomm = new AccommodationViewData();
	AccommodationRoomsCarouselComponentModel room = new AccommodationRoomsCarouselComponentModel();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		final CatalogVersionModel model = new CatalogVersionModel();
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalogmodel);
		getDummyAccommodationRooms();
		getDummyAccommodationViewData();
	}

	private AccommodationViewData getDummyAccommodationViewData()
	{
		final AccommodationRoomViewData room1 = new AccommodationRoomViewData();
		room1.setCode("room1");
		room1.setDescription("description1");

		final AccommodationRoomViewData room2 = new AccommodationRoomViewData();
		room1.setCode("room2");
		room1.setDescription("description2");
		final List<AccommodationRoomViewData> roomList = new ArrayList<AccommodationRoomViewData>();
		roomList.add(room1);
		roomList.add(room2);

		accomm.setCode("code1");
		accomm.setRoomsData(roomList);
		return accomm;
	}

	@SuppressWarnings("boxing")
	private AccommodationRoomsCarouselComponentModel getDummyAccommodationRooms()
	{
		room.setUid("roomsHighlights");
		room.setCatalogVersion(model);
		room.setDisplayMode(DisplayMode.HIGHLIGHT);
		room.setVisibleItems(Integer.valueOf(2));
		return room;
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.framework.controllers.AccommodationRoomsCarouselComponentController#viewRoomsForAccomodation()}
	 * .
	 */
	@Test
	public void testViewRoomsForAccomodation()
	{

		try
		{
			when(componentFacade.getComponent(room.getUid())).thenReturn(room);
			when(accomodationFacade.getRoomsForAccommodation("code1", room.getVisibleItems())).thenReturn(accomm);
			assertThat(accomm.getRoomsData().get(0).getCode(), is("room2"));

		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("Component not found");
		}


	}

}
