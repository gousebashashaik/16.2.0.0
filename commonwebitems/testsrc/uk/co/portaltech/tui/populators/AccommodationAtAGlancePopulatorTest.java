/**
 * 
 */
package uk.co.portaltech.tui.populators;

import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class AccommodationAtAGlancePopulatorTest
{

	/**
	 * @throws java.lang.Exception
	 */
	
	private final TUILogUtils LOGGER = new TUILogUtils("AccommodationAtAGlancePopulatorTest");

	private AccommodationModel sourceModel;

	private Map mapAccommodation;

	@Mock
	private AccommodationViewData targetData;

	private Map mapAccommodationViewData;

	private static final String date = "23/06/2011";

	@InjectMocks
	private FeatureService featureService;




	@Before
	public void setUp() throws Exception
	{
		sourceModel = new AccommodationModel();
		targetData = new AccommodationViewData();
	}

	public AccommodationModel dummydataforAccommodationModel()
	{

		sourceModel.setMetaDescription("metaDescription");
		sourceModel.setMetaKeywords("metaKeyword");
		sourceModel.setMetaTitle("metaTitle");
		
		return sourceModel;
	}

	public Collection<AccommodationModel> dummyDataforModel()
	{
		return (Collection<AccommodationModel>) sourceModel;
	}

	public AccommodationViewData dummyDataForAccommodationViewData()
	{
		targetData.setAccommodationType("accommodation1");
		targetData.settRating("5");
		targetData.setCount("3");
		
		return targetData;
	}

	@Test
	public void testPopulate()
	{
		final AccommodationModel expected = dummydataforAccommodationModel();
		final AccommodationViewData expected1 = dummyDataForAccommodationViewData();
		//targetData.putFeatureCodesAndValues(featureService.getValuesForFeatures(Arrays.asList(new String[]
		
		Assert.assertEquals(expected, sourceModel);
		Assert.assertEquals(expected1, targetData);


	}
}
