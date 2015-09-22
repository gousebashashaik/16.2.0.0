/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
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
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.tui.components.model.InspirationMapComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.MapFacade;
import uk.co.portaltech.tui.web.view.data.InspirationMapComponentData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.MarkerMapViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;

/**
 * To test Inspiration Map Component for Tabs.
 * 
 * @author niranjani.r
 * 
 */
@UnitTest
public class InspirationMapComponentControllerTest {

	@Mock
	private MapFacade mapFacade;

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

	InspirationMapComponentModel inspirationMapComponentModel = new InspirationMapComponentModel();

	InspirationMapComponentData mapData = new InspirationMapComponentData();

	/*private final Logger LOG = Logger
			.getLogger(InspirationMapComponentControllerTest.class);*/
	private final TUILogUtils LOG = new TUILogUtils("InspirationMapComponentControllerTest");


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		CatalogVersionModel model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalogmodel);
		getInspirationMapComponentModelData();
		getInspirationMapData();
	}

	/**
	 * Methods to set Model.
	 * 
	 * @return InspirationMapComponentModel.
	 */
	private InspirationMapComponentModel getInspirationMapComponentModelData() {
		inspirationMapComponentModel.setUid("InspirationMap");
		inspirationMapComponentModel.setName("Inspiration Map");
		return inspirationMapComponentModel;
	}

	/**
	 * Methods to set Model.
	 * 
	 * @return InspirationMapComponentData.
	 */
	private InspirationMapComponentData getInspirationMapData() {

		List<InspirationMapViewData> mapDataList = new ArrayList<InspirationMapViewData>();
		List<MarkerMapViewData> markupList1 = new ArrayList<MarkerMapViewData>();
		List<MarkerMapViewData> markupList2 = new ArrayList<MarkerMapViewData>();
		MarkerMapViewData markerMapViewData1 = new MarkerMapViewData();
		markerMapViewData1.setCode("code1");
		MarkerMapViewData markerMapViewData2 = new MarkerMapViewData();
		markerMapViewData2.setCode("code2");
		markupList1.add(markerMapViewData1);
		markupList1.add(markerMapViewData2);

		MarkerMapViewData markerMapViewData3 = new MarkerMapViewData();
		markerMapViewData3.setCode("code3");
		MarkerMapViewData markerMapViewData4 = new MarkerMapViewData();
		markerMapViewData4.setCode("code4");
		markupList2.add(markerMapViewData3);
		markupList2.add(markerMapViewData4);

		InspirationMapViewData inspirationMapViewData1 = new InspirationMapViewData();
		inspirationMapViewData1.setTitle("For Beaches");
		inspirationMapViewData1.setMarkerMapViewDataList(markupList1);

		InspirationMapViewData inspirationMapViewData2 = new InspirationMapViewData();
		inspirationMapViewData2.setTitle("For Family");
		inspirationMapViewData2.setMarkerMapViewDataList(markupList2);

		mapDataList.add(inspirationMapViewData1);
		mapDataList.add(inspirationMapViewData2);

		mapData.setVisibleItems(Integer.valueOf(10));
		mapData.setInspirationMapViewDataList(mapDataList);

		return mapData;
	}

	/**
	 * Test method for Map Tabs.
	 */
	@Test
	public void inspirationMapTestForTabData() {
		try {
			when(componentFacade.getComponent("InspirationMap")).thenReturn(
					inspirationMapComponentModel);
			when(mapFacade.getInspirationMap(inspirationMapComponentModel))
					.thenReturn(mapData);
			assertThat(mapData.getInspirationMapViewDataList().get(0)
					.getTitle(), is("For Beaches"));

		} catch (NoSuchComponentException e) {
			LOG.error(" component not found");
		}
	}

	/**
	 * Test method for Map tab locations.
	 */
	@Test
	public void inspirationMapForTabLocationData() {
		try {
			when(componentFacade.getComponent("InspirationMap")).thenReturn(
					inspirationMapComponentModel);
			when(mapFacade.getInspirationMap(inspirationMapComponentModel))
					.thenReturn(mapData);
			assertThat(mapData.getInspirationMapViewDataList().get(0)
					.getMarkerMapViewDataList().get(0).getCode(), is("code1"));

		} catch (NoSuchComponentException e) {
			LOG.error(" component not found");
		}
	}
}