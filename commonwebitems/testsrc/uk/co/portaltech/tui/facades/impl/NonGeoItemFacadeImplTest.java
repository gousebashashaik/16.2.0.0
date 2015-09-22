/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static junit.framework.Assert.assertEquals;
import static org.easymock.classextension.EasyMock.createMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.services.nongeo.NonGeoItemService;


/**
 * @author niranjani.r
 * 
 */
public class NonGeoItemFacadeImplTest
{
	@Mock
	private NonGeoItemService geoItemService;

	private static final String CODE1 = "L31356_usp1";

	private static final String CODE2 = "L31356_usp2";

	private static final String VALUE1 = "usp1";

	private static final String VALUE2 = "usp2";

	private static final String KEY1 = "SEN_1";

	private static final String KEY2 = "SEN_2";

	private static final String KEY3 = "SEN";

	private static final String KEY4 = "COU";


	private Map<String, ContentValueModel> createDummyContentValueMap()
	{

		final Map<String, ContentValueModel> map = new HashMap<String, ContentValueModel>();

		final ContentValueModel content = new ContentValueModel();
		content.setCode(CODE1);
		content.setValue(new String(VALUE1));

		final ContentValueModel content1 = new ContentValueModel();
		content1.setCode(CODE2);
		content1.setValue(new String(VALUE2));

		map.put(KEY1, content);
		map.put(KEY2, content1);
		return map;
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.facades.impl.NonGeoItemFacadeImpl#getStraplineData(java.util.List)}.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		geoItemService = createMock(NonGeoItemService.class);
	}

	@Test
	public void testforContentValuesForTheGivenEpicCode()
	{
		final List<String> epicCodeList = new ArrayList<String>();
		epicCodeList.add(KEY3);
		epicCodeList.add(KEY4);
		final Map<String, ContentValueModel> content = createDummyContentValueMap();
		assertEquals(CODE1, content.get(KEY1).getCode());

	}

}
