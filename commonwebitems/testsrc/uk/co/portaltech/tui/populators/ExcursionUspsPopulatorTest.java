/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class ExcursionUspsPopulatorTest {
	
	@InjectMocks
	private ExcursionUspsPopulator excursionUspsPopulator=new ExcursionUspsPopulator();
	
	

	@Mock
	private FeatureService featureService;
	private  List<Object> listValuesForFeatures;
	private ExcursionModel sourceData;
	private ExcursionViewData targetData;
	
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		sourceData = new ExcursionModel();
		targetData = new ExcursionViewData();
		
		
		
		List<ProductFeatureModel> featureModelList=new ArrayList<ProductFeatureModel>();
		
		ProductFeatureModel featureModel=new ProductFeatureModel();
		
		featureModel.setDescription("DESC");
		featureModel.setQualifier("Qualifier");
		featureModel.setValuePosition(1);
		
		featureModelList.add(featureModel);

		Collection<ProductModel> productModelList = new ArrayList<ProductModel>();

		ProductModel productModel = new ProductModel();

		productModel.setCode("002240");
		productModel.setEan("EAN");
		productModel.setManufacturerAID("Manufacturer AID");
		productModel.setManufacturerName("Manufacturer Name");
		productModel.setOrder(32);
		productModel.setSummary("Good for summer", Locale.UK);

		productModelList.add(productModel);

		List<FeatureValueSetModel> valueSetModelList = new ArrayList<FeatureValueSetModel>();
		FeatureValueSetModel valueSetModel = new FeatureValueSetModel();
		valueSetModel.setCode("002240");
		valueSetModel.setProduct(productModel);
		valueSetModelList.add(valueSetModel);
		
		Set<FeatureDescriptorModel> descriptorModelList=new HashSet<FeatureDescriptorModel>();
		
		FeatureDescriptorModel descriptorModel=new FeatureDescriptorModel();
		
		descriptorModel.setCode("002240");
		descriptorModel.setType(FeatureType.MEDIA);
		
		descriptorModelList.add(descriptorModel);
		
		
		
		sourceData.setCode("002240");
		sourceData.setEan("EAN");
		sourceData.setManufacturerName("MANUFACTURER NAME");
		sourceData.setManufacturerAID("RW12ST");
	
		
		listValuesForFeatures=new ArrayList<Object>();
		
		listValuesForFeatures.add("usps");

		
		
	}
	@Test
	public void testPopulate() {
		
		
		Mockito.when(featureService.getFeatureValues("usps", sourceData, new Date(), null)).thenReturn(listValuesForFeatures);
		assertNotNull(sourceData);
		assertNotNull(targetData);
		
		excursionUspsPopulator.populate(sourceData, targetData);
		
		assertNotNull(targetData);
		assertNotNull(targetData.getUsps());
		
		
		
		
	}

}
