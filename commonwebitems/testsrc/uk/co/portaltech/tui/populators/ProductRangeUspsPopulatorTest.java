/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author vinodkumar.g
 *
 */
public class ProductRangeUspsPopulatorTest {

	
	ProductRangeUspsPopulator productRangeUspsPopulator=new ProductRangeUspsPopulator();
	
	List<ProductUspModel> source;
	ProductRangeViewData target;
	
	@Before
	public void setUp() throws Exception {
		
		source=new ArrayList<ProductUspModel>();
		target=new ProductRangeViewData();
		
		ProductUspModel productUspModel=new ProductUspModel();
		
		productUspModel.setCode("code");
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.ProductRangeUspsPopulator#populate(java.util.List, uk.co.portaltech.tui.web.view.data.ProductRangeViewData)}.
	 */
	@Test
	public void testPopulate() {
		
		productRangeUspsPopulator.populate(source, target);
		
		assertNotNull(source);
		assertNotNull(target);
		assertEquals(0, target.getProductUsps().size());
		
	}

}
