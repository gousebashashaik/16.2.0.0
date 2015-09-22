/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;

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

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author sunil.bd
 * 
 */
public class AttractionEditorialContentPopulatorTest {

	@InjectMocks
	private AttractionEditorialContentPopulator populator = new AttractionEditorialContentPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private TUIUrlResolver<AttractionModel> attractionUrlResolver;

	private ItemModel sourceModel;
	private AttractionViewData targetData;
	private Map<String, List<Object>> mapValuesForFeatures;

	private CommentModel commentModel;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		targetData = new AttractionViewData();

		sourceModel = new ItemModel();

		commentModel = new CommentModel();
		commentModel.setCode("code");
		commentModel.setText("sampleText");
		commentModel.setPriority(1);

		List<CommentModel> commentModelList = new ArrayList<CommentModel>();

		commentModelList.add(commentModel);

		sourceModel.setComments(commentModelList);
		sourceModel.setCreationtime(new Date());

		List<Object> value = new ArrayList<Object>();
		value.add(sourceModel);

		mapValuesForFeatures = new HashMap<String, List<Object>>();
		mapValuesForFeatures.put("002240", value);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.AttractionEditorialContentPopulator#populate(de.hybris.platform.core.model.ItemModel, uk.co.portaltech.tui.web.view.data.AttractionViewData)}
	 * .
	 */
	@Test
	public void testPopulate() {
		List<String> featureDescriptorList = new ArrayList(
				Arrays.asList(new String[] { "name", "latitude", "longitude" }));

		Mockito.when(
				featureService.getFirstFeatureValueAsString("editorialContent",
						sourceModel, new Date(), null)).thenReturn("AnyString");

		targetData.setEditorialContent("AnyString");
		Mockito.when(
				featureService.getValuesForFeatures(featureDescriptorList,
						sourceModel, new Date(), null)).thenReturn(
				mapValuesForFeatures);

		assertNotNull(sourceModel);
		assertNotNull(targetData);

		populator.populate(sourceModel, targetData);

		assertNotNull(targetData);

		Map<String, List<Object>> expectedResult = targetData
				.getFeatureCodesAndValues();

		Set set = expectedResult.entrySet();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator
					.next();

			List<Object> featureList = keyVal.getValue();

			Iterator featureListIterator = featureList.iterator();

			while (featureListIterator.hasNext()) {

				Object object = featureListIterator.next();
				ItemModel model = (ItemModel) object;

				assertNotNull(model.getCreationtime());
				assertThat(model.getComments().get(0), is(commentModel));

			}
		}
	}
}
