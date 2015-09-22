/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FacilityTypeModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.facility.FacilityTypeService;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author sunil.bd
 * 
 */
public class FacilityBasicPopulatorTest
{

	@InjectMocks
	private final FacilityBasicPopulator facilityBasicPopulator = new FacilityBasicPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private BrandUtils brandUtils;

	@Mock
	private FacilityTypeService facilityType;

	@Mock
	private CMSSiteService cmsSiteService;

	@Mock
	private MediaDataPopulator mediaDataPopulator;

	private FacilityModel sourceModel;
	private FacilityViewData targetData;
	private FacilityTypeModel facilityTypeModel;
	private Map<String, List<Object>> mapValuesForFeatures;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		targetData = new FacilityViewData();

		sourceModel = new FacilityModel();

		final List<FeatureValueSetModel> featureValueSetModelList = new ArrayList<FeatureValueSetModel>();

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();

		featureValueSetModel.setCode("002240");

		featureValueSetModelList.add(featureValueSetModel);

		final Set<FeatureDescriptorModel> descriptorModelSet = new HashSet<FeatureDescriptorModel>();

		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();

		descriptorModel.setCode("002240");
		descriptorModel.setName("Flamingo Benidorm", Locale.UK);
		descriptorModel.setType(FeatureType.MEDIA);

		descriptorModelSet.add(descriptorModel);

		facilityTypeModel = new FacilityTypeModel();

		facilityTypeModel.setCode("002240");
		facilityTypeModel.setName("Flamingo Benidorm", Locale.UK);
		facilityTypeModel.setDescription("Good For Lunch", Locale.UK);
		facilityTypeModel.setOrder(32);

		sourceModel.setCode("002240");
		sourceModel.setName("Flamingo Benidorm", Locale.UK);
		sourceModel.setType(facilityTypeModel);
		sourceModel.setFeatureDescriptors(descriptorModelSet);
		sourceModel.setFeatureValueSets(featureValueSetModelList);

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);

	}

	private CatalogVersionModel getCatalogVersion()
	{

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;

	}

	@Test
	public void testPopulate()
	{

		final CatalogVersionModel catalog = getCatalogVersion();

		try
		{

			Mockito.when(brandUtils.getFeatureServiceBrand(sourceModel.getBrands())).thenReturn("TH");

			Mockito.when(
					featureService.getFirstFeatureValueAsString("description", sourceModel, new Date(),
							brandUtils.getFeatureServiceBrand(sourceModel.getBrands()))).thenReturn("Good For Lunch");

			Mockito.when(
					featureService.getFirstFeatureValueAsString("name", sourceModel, new Date(),
							brandUtils.getFeatureServiceBrand(sourceModel.getBrands()))).thenReturn(sourceModel.getName(Locale.UK));

			Mockito.when(
					featureService.getValuesForFeatures(Arrays.asList(new String[]
					{ "name", "description", "priority", "openingTimes" }), sourceModel, new Date(),
							brandUtils.getFeatureServiceBrand(sourceModel.getBrands()))).thenReturn(mapValuesForFeatures);

			Mockito.when(facilityType.getFacilityTypeParent(catalog, sourceModel.getType().getCode())).thenReturn(facilityTypeModel);

			assertNotNull(sourceModel);

			facilityBasicPopulator.populate(sourceModel, targetData);

			assertNotNull(targetData);

			final Map<String, List<Object>> map = targetData.getFeatureCodesAndValues();

			final Set aSet = map.entrySet();

			final Iterator iterator = aSet.iterator();
			while (iterator.hasNext())
			{
				final Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator.next();
				final List<Object> featureList = keyVal.getValue();

				final Iterator featureListIterator = featureList.iterator();

				while (featureListIterator.hasNext())
				{

					final FacilityModel model = (FacilityModel) featureListIterator.next();

					assertThat(model.getCode(), is("002240"));
					assertThat(model.getType(), is(facilityTypeModel));
				}
			}

		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}
}
