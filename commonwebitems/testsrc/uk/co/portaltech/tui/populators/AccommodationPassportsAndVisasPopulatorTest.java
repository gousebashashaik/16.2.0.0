/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

import com.thoughtworks.xstream.converters.ConversionException;


/**
 * @author sunil.bd
 * 
 */
public class AccommodationPassportsAndVisasPopulatorTest
{

	@InjectMocks
	private final AccommodationPassportsAndVisasPopulator accommodationPassportsAndVisasPopulator = new AccommodationPassportsAndVisasPopulator();

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
	public void setUp() throws Exception
	{


		MockitoAnnotations.initMocks(this);

		targetData = new AccommodationViewData();

		sourceModel = new AccommodationModel();
		sourceModel.setCode("002240");
		sourceModel.setBrands(Arrays.asList(BrandType.FC));

		final List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	private AccommodationModel getAccommodationDataNull()
	{
		final AccommodationModel accommodationModel = new AccommodationModel();

		accommodationModel.setCode(null);
		return accommodationModel;
	}

	private AccommodationModel getAccommodationDataEmpty()
	{
		final AccommodationModel accommodationModel = new AccommodationModel();

		accommodationModel.setCode("");
		return accommodationModel;
	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{
		try
		{
			Mockito.when(featureService.getValuesForFeatures(Arrays.asList(new String[]
			{ "passportAndVisaInfo" }), sourceModel, new Date(), "")).thenReturn(mapValuesForFeatures);
			accommodationPassportsAndVisasPopulator.populate(sourceModel, targetData);

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

				}
			}

		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}

	@Test
	public void testPopulateNull()
	{
		try
		{

			final AccommodationModel sourceDataNull = getAccommodationDataNull();

			Mockito.when(featureService.getValuesForFeatures(Arrays.asList(new String[]
			{ "passportAndVisaInfo" }), sourceModel, new Date(), "")).thenReturn(mapValuesForFeatures);
			accommodationPassportsAndVisasPopulator.populate(sourceDataNull, targetData);

			assertNotNull(targetData);

		}
		catch (final NullPointerException nullPointerException)
		{
			nullPointerException.printStackTrace();
		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}

	@Test
	public void testPopulateEmpty()
	{
		try
		{
			final AccommodationModel sourceDataEmpty = getAccommodationDataEmpty();

			Mockito.when(featureService.getValuesForFeatures(Arrays.asList(new String[]
			{ "passportAndVisaInfo" }), sourceModel, new Date(), "")).thenReturn(mapValuesForFeatures);
			accommodationPassportsAndVisasPopulator.populate(sourceDataEmpty, targetData);

			assertNotNull(targetData);

		}
		catch (final NullPointerException nullPointerException)
		{
			nullPointerException.printStackTrace();
		}
		catch (final ConversionException conversionException)
		{
			conversionException.printStackTrace();
		}
	}
}
