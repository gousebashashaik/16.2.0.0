/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import de.hybris.platform.commerceservices.converter.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

/**
 * @author sureshbabu.rn
 *
 */
public class CMSLinkComponentPopulatorTest {
	
	@InjectMocks
	CMSLinkComponentPopulator cmsLinkComponentPopulator=new CMSLinkComponentPopulator();
	
	@Mock
	Converters converters;
	
	@Mock
	Converter<CategoryModel, CategoryData>          categoryConverter;
	
	@Mock
	Converter<ProductModel, ProductData>            productConverter;
	
	@Mock
	ConfigurablePopulator<ProductModel, ProductData, ProductOption>     productConfiguredPopulator;


	CMSLinkComponentModel source;
	CMSLinkComponentViewData target;
	CategoryModel categoryModel;
	CategoryData categoryData;
	ProductModel productModel;
	ProductData productData;
	de.hybris.platform.cms2.enums.LinkTargets linkTargets;
	Map<String, CategoryData> categoryDataList;
	List<CategoryModel> categoryModelList;
	String languageCode="en";
	Locale locale=new Locale(languageCode);
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		source=new CMSLinkComponentModel();
		target=new CMSLinkComponentViewData();
		categoryModel=new CategoryModel();
		categoryData=new CategoryData();
		productModel=new ProductModel();
		productData=new ProductData();
		
		categoryDataList=new HashMap<String, CategoryData>();
		categoryModelList=new ArrayList<CategoryModel>();
		
		
		productData.setCode("productdata_code");
		productData.setDescription("productdata_description");
		
		productModel.setCode("productmodel_code");
		productModel.setChangeDescription("productmodel_description");
		
		categoryData.setCode("category_data");
		categoryData.setDescription("category_data_description");
		categoryData.setUrl("www.tui.co.uk");
		
		categoryDataList.put("first", categoryData);
		
		categoryModel.setCode("category_model");
		categoryModel.setCreationtime(new Date());
	
		
		source.setCategory(categoryModel);
		source.setLinkName("www.tui.co.uk",locale);
		source.setProduct(productModel);
		source.setUrl("www.tui.co.uk");
		source.setTarget(linkTargets);
		
	
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.CMSLinkComponentPopulator#populate(de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel, uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData)}.
	 */
	@SuppressWarnings({ "static-access", "boxing" })
	@Test
	public void testPopulate() {

	    Mockito.when(productConverter.convert(productModel)).thenReturn(productData);
	    Mockito.doNothing().when(productConfiguredPopulator).populate(productModel, productData, Arrays.asList(ProductOption.BASIC));
	    
	    cmsLinkComponentPopulator.populate(source, target);
	    
	    assertNotNull(source);
	    assertNotNull(target);
	    
	
	    assertEquals("www.tui.co.uk", target.getLinkName());
	    assertEquals(productData, target.getProduct());
	    assertEquals("www.tui.co.uk", target.getUrl());
	    assertEquals(linkTargets, target.getTarget());
	}

}
