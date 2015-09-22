/**
 *
 */
package uk.co.portaltech.tui.attributehandlers;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.model.CategoryItemsListModel;
import uk.co.portaltech.tui.model.CollectionToBestForModel;
import uk.co.tui.web.common.enums.ItemListType;

/**
 * @author Kandipr
 *
 */
public class CategoriesHandler implements
   DynamicAttributeHandler<Collection<? extends ItemModel>, CategoryItemsListModel>
{

   @Resource(name = "categoryService")
   private CategoryService categoryService;

   @Resource(name = "productService")
   private ProductService productService;

   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   private static final Logger LOG = Logger.getLogger(CategoriesHandler.class);

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform
    * .servicelayer.model .AbstractItemModel)
    */
   @Override
   public Collection<? extends ItemModel> get(final CategoryItemsListModel model)
   {

      final List<String> itemCodes = model.getItemCodes();

      if (itemCodes != null && !itemCodes.isEmpty())
      {
         if (model.getItemListType().equals(ItemListType.LOCATIONS))
         {
            final Collection<LocationModel> locations = new ArrayList<LocationModel>();
            for (final String code : itemCodes)
            {
               final CategoryModel cat = categoryService.getCategoryForCode(code);
               if (cat instanceof LocationModel)
               {
                  locations.add((LocationModel) cat);
               }
            }
            return locations;
         }
         else if (model.getItemListType().equals(ItemListType.PRODUCTRANGES))
         {
            final Collection<ProductRangeModel> productRanges = new ArrayList<ProductRangeModel>();
            for (final String code : itemCodes)
            {
               final CategoryModel cat = categoryService.getCategoryForCode(code);
               if (cat instanceof ProductRangeModel)
               {
                  productRanges.add((ProductRangeModel) cat);
               }
            }
            return productRanges;
         }
         else if (model.getItemListType().equals(ItemListType.ACCOMMODATIONS))
         {
            final Collection<AccommodationModel> accommodations =
               new ArrayList<AccommodationModel>();
            for (final String code : itemCodes)
            {
               final ProductModel product = productService.getProductForCode(code);
               if (product instanceof AccommodationModel)
               {
                  accommodations.add((AccommodationModel) product);
               }
            }
            return accommodations;
         }
         else if (model.getItemListType().equals(ItemListType.COLLECTIONTOBESTFOR))
         {
            final Collection<CollectionToBestForModel> collectionToBestForModels =
               new ArrayList<CollectionToBestForModel>();
            for (final String code : itemCodes)
            {
               CollectionToBestForModel collectionToBestForModel = null;

               try
               {
                  collectionToBestForModel =
                     mstravelLocationService.getCollectionToBestForByCode(code);
               }
               catch (final AmbiguousIdentifierException e)
               {
                  LOG.error(e);
               }

               if (collectionToBestForModel != null)
               {
                  collectionToBestForModels.add(collectionToBestForModel);
               }
            }
            return collectionToBestForModels;
         }

      }

      return Collections.emptyList();
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#set(de.hybris.platform
    * .servicelayer.model .AbstractItemModel, java.lang.Object)
    */
   @Override
   public void set(final CategoryItemsListModel model, final Collection<? extends ItemModel> value)
   {
      // Do nothing because an overridden method.
   }

}
