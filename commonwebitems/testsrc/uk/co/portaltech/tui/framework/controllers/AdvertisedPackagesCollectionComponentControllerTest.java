/**
 *
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.tui.components.model.AdvertisedPackagesCollectionComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.AdvertisedPackagesCollectionData;
import com.enterprisedt.util.debug.Logger;

/**
 * @author niranjani.r
 *
 */
public class AdvertisedPackagesCollectionComponentControllerTest {
	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private AccommodationFacade accommodationFacade;

	@Mock
	private SimpleCMSComponentModel component;

	private static final String COMPONENT_UID = "Advertised package collection";

	private final Logger logger = Logger
			.getLogger(AdvertisedPackagesCollectionComponentControllerTest.class);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		createDummyModel();
		createDummyData();
	}

	private AdvertisedPackagesCollectionComponentModel createDummyModel() {
		AdvertisedPackagesCollectionComponentModel model = new AdvertisedPackagesCollectionComponentModel();
		model.setUid(COMPONENT_UID);
		AccommodationModel model1 = new AccommodationModel();
		model1.setCode("accomm1");
		model.setAccommodation1("accomm1");
		return model;
	}

	private List<AdvertisedPackagesCollectionData> createDummyData() {

		List<AdvertisedPackagesCollectionData> dataList = new ArrayList<AdvertisedPackagesCollectionData>();
		AdvertisedPackagesCollectionData data = new AdvertisedPackagesCollectionData();
		data.setAccommodationName("hotel orion");
		data.setPrice("567");
		dataList.add(data);
		return dataList;
	}

	/**
	 * Test method for AdvertisedPackagesCollection Component.
	 *
	 *
	 */
	@Test
	public void testViewAdvertisedPackagesCollectionComponent()
	{
		try
		{
			Mockito.when(componentFacade.getComponent(COMPONENT_UID)).thenReturn(createDummyModel());
			//			Mockito.when(
			//					accommodationFacade
			//							.getAdvertisedPackagesCollectionData(createDummyModel()))
			

		} catch (NoSuchComponentException e) {
			logger.error("No component with uid [" + COMPONENT_UID + "]");
		}
		assertThat(createDummyData().get(0).getAccommodationName(),
				is("hotel orion"));
	}

}
