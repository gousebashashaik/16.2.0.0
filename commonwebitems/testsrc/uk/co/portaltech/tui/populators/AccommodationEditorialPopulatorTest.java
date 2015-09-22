/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureGroupModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author sunil.bd
 * 
 */
public class AccommodationEditorialPopulatorTest {

	@InjectMocks
	private AccommodationEditorialPopulator editorialPopulator = new AccommodationEditorialPopulator();
	
	@Mock
	private FeatureService featureService;
	
	@Mock
	private TypeService typeService;

	private AccommodationModel sourceModel;

	private AccommodationViewData targetData;
	
	private Map<String, List<Object>> mapValuesForFeatures;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		targetData = new AccommodationViewData();

		List<RoomModel> roomModelList = new ArrayList<RoomModel>();
		List<FacilityModel> facilityModelList = new ArrayList<FacilityModel>();

		FacilityModel facilityModel = new FacilityModel();
		facilityModel.setCode("002240");
		facilityModelList.add(facilityModel);

		RoomModel roomModel1,roomModel2;

		roomModel1 = new RoomModel();
		roomModel2 = new RoomModel();

		roomModel1.setCode("002240");
		roomModel1.setRoomTypeCode("R1268");

		roomModel2.setCode("55555");
		roomModel2.setRoomTypeCode("R5667");

		roomModelList.add(roomModel1);
		roomModelList.add(roomModel2);

		Set<FeatureDescriptorModel> featureDescriptorModelset = new HashSet<FeatureDescriptorModel>();
		
		FeatureGroupModel groupModel = new FeatureGroupModel();
		groupModel.setCode("002240");
		
		ProductFeatureModel featureModel = new ProductFeatureModel();
		featureModel.setValueDetails("Value Details");
		
		List<ProductFeatureModel> featureModels = new ArrayList<ProductFeatureModel>();
		featureModels.add(featureModel);
		
		FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();
		descriptorModel.setCode("002240");
		descriptorModel.setFeatureGroup(groupModel);
		descriptorModel.setCode("002240");

		featureDescriptorModelset.add(descriptorModel);

		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");

		sourceModel.setRooms(roomModelList);
		sourceModel.setFacilities(facilityModelList);

		List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		try {
			Mockito.when(
					featureService.getValuesForFeatures(
							Mockito.anyCollection(), Mockito.any(AccommodationModel.class),
							Mockito.any(Date.class), Mockito.anyString())).thenReturn(mapValuesForFeatures);
			editorialPopulator.populate(sourceModel, targetData);

			assertNotNull(targetData);

			Map<String, List<Object>> expectedResult = targetData
					.getFeatureCodesAndValues();

			Set set = expectedResult.entrySet();

			Iterator iterator = set.iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator
						.next();

				List<Object> featureList = keyVal.getValue();

				Iterator iterator2 = featureList.iterator();

				while (iterator2.hasNext()) {

					Object object = iterator2.next();
					AccommodationModel model = (AccommodationModel) object;
					
					assertThat(model.getCode(), is("002240"));
					assertThat(model.getRooms().size(), is(2));
					assertThat(model.getFacilities().size(), is(1));
				}

			}

		} catch (UnknownIdentifierException uie) {
			uie.printStackTrace();
		}
	}
}
