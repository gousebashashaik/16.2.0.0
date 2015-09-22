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

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureGroupModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;


/**
 * @author sunil.bd
 * 
 */
public class AccommodationTravelLifePopulatorTest
{

	/**
	 * @throws java.lang.Exception
	 * 
	 * 
	 */
	@InjectMocks
	private final AccommodationTravelLifePopulator accommodation = new AccommodationTravelLifePopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private TypeService typeService;

	private AccommodationModel sourceModel;
	private AccommodationViewData targetData;
	private Map<String, List<Object>> map;

	/*
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		targetData = new AccommodationViewData();
		final Set<FeatureDescriptorModel> featureDescriptorModelSet = new HashSet<FeatureDescriptorModel>();

		final FeatureGroupModel groupModel = new FeatureGroupModel();
		groupModel.setCode("002240");

		final List<ProductFeatureModel> featureModels = new ArrayList<ProductFeatureModel>();

		final ProductFeatureModel featureModel = new ProductFeatureModel();
		featureModel.setValueDetails("Value Details");
		featureModels.add(featureModel);
		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();
		descriptorModel.setCode("002240");
		descriptorModel.setFeatureGroup(groupModel);
		descriptorModel.setCode("002240");

		featureDescriptorModelSet.add(descriptorModel);

		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");
		sourceModel.setFeatureDescriptors(featureDescriptorModelSet);
		sourceModel.setFeatures(featureModels);
		sourceModel.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		map = new HashMap<String, List<Object>>();
		map.put("002240", value);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{
		try
		{

			Mockito.when(featureService.getValuesForFeatures(Arrays.asList(new String[]
			{ "travelLifeBronze", "travelLifeGold", "travelLifeSilver" }), sourceModel, new Date(), "")).thenReturn(map);

			accommodation.populate(sourceModel, targetData);

			assertNotNull(targetData);

			final Map<String, List<Object>> expectedResult = targetData.getFeatureCodesAndValues();

			final Set set = expectedResult.entrySet();

			final Iterator iterator = set.iterator();

			while (iterator.hasNext())
			{
				final Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator.next();

				final List<Object> featureList = keyVal.getValue();

				final Iterator featureListIterator = featureList.iterator();

				while (featureListIterator.hasNext())
				{
					final Object object = featureListIterator.next();
					final AccommodationModel model = (AccommodationModel) object;

					assertThat(model.getCode(), is("002240"));

					assertThat(model.getFeatureDescriptors().size(), is(Integer.valueOf(1)));
					assertThat(model.getFeatures().size(), is(Integer.valueOf(1)));
				}
			}

		}
		catch (final UnknownIdentifierException uie)
		{
			uie.printStackTrace();
		}
	}

}
