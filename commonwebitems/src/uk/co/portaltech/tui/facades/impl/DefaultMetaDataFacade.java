/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.metadatarule.lite.MetaDataRuleLite;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.MetaDataGenerateService;
import uk.co.portaltech.tui.components.model.AccommodationPageModel;
import uk.co.portaltech.tui.components.model.LocationPageModel;
import uk.co.portaltech.tui.facades.MetaDataFacade;
import uk.co.portaltech.tui.web.view.data.MetaDataViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author cxw
 *
 */
public class DefaultMetaDataFacade implements MetaDataFacade
{
    @Resource
    private MetaDataGenerateService metaDataGenerateService;
    @Resource
    private FeatureService featureService;
    @Resource
    private BrandUtils brandUtils;

    private static final String NAME = "DEFAULT";

    @Override
    public MetaDataViewData getMetaDataForItem(final ItemModel item, final AbstractPageModel pageModel)
    {

        MetaDataViewData viewData = new MetaDataViewData();

        if ("LocationPage".equalsIgnoreCase(pageModel.getItemtype()))
        {
            viewData = locationMetaDataResolver(item, pageModel);
        }
        else if ("AccommodationPage".equalsIgnoreCase(pageModel.getItemtype()))
        {
            viewData = accommodationMetaDataResolver(item, pageModel);
        }
        else if ("AttractionPage".equalsIgnoreCase(pageModel.getItemtype()))
        {
            viewData = attractionMetaDataResolver(item, pageModel);
        }
        return viewData;
    }

    /**
     * @param item
     * @param pageModel
     * @return metadataviewdata for location based on the priority.
     */
    public MetaDataViewData locationMetaDataResolver(final ItemModel item, final AbstractPageModel pageModel)
    {
        MetaDataViewData viewData = new MetaDataViewData();

        LocationPageModel locationPageModel = (LocationPageModel) pageModel;
        LocationModel location = (LocationModel) item;
        List<MetaDataRuleLite> firstMetaList = new ArrayList<MetaDataRuleLite>();
        List<MetaDataRuleLite> secondMetaList = new ArrayList<MetaDataRuleLite>();
        List<MetaDataRuleLite> thirdMetaList = new ArrayList<MetaDataRuleLite>();
        int flag = 0;
        List<MetaDataRuleLite> metaModel = metaDataGenerateService
                .getMetaDataForItem(item);
         if (metaModel != null) {
            for (MetaDataRuleLite meta : metaModel) {
                String code = meta.getCode();
                getMetaLists(location, firstMetaList, secondMetaList, thirdMetaList, meta, code);

            }
            viewData = getMetaView(firstMetaList, secondMetaList, thirdMetaList, locationPageModel, item, flag);
        }
        return viewData;
    }

    /**
     * @param location
     * @param firstMetaList
     * @param secondMetaList
     * @param thirdMetaList
     * @param meta
     * @param code
     */
    private void getMetaLists(LocationModel location, List<MetaDataRuleLite> firstMetaList,
            List<MetaDataRuleLite> secondMetaList, List<MetaDataRuleLite> thirdMetaList, MetaDataRuleLite meta, String code)
    {
        if (code.contains(location.getCode()))
        {
            firstMetaList.add(meta);
        }
        else if ((code.toLowerCase().contains("overview") || locationTypeCode(code)) && (!(code.contains("-"))))
        {
            secondMetaList.add(meta);
        }
        else
        {
            thirdMetaList.add(meta);
        }
    }

    private boolean locationTypeCode(final String code)
    {
        return code.toLowerCase().contains("placestogo") || code.toLowerCase().contains("thingstodo")
                || code.toLowerCase().contains("essentialinfo");
    }

    /**
     * @param item
     * @param pageModel
     * @return metadataviewData for accommodation based on the priority.
     */
    public MetaDataViewData accommodationMetaDataResolver(final ItemModel item, final AbstractPageModel pageModel)
    {
        MetaDataViewData viewData = new MetaDataViewData();

        final AccommodationPageModel accmPageModel = (AccommodationPageModel) pageModel;
        final ProductModel product = (ProductModel) item;

        List<MetaDataRuleLite> firstMetaList = new ArrayList<MetaDataRuleLite>();
        List<MetaDataRuleLite> secondMetaList = new ArrayList<MetaDataRuleLite>();
        List<MetaDataRuleLite> thirdMetaList = new ArrayList<MetaDataRuleLite>();

        List<MetaDataRuleLite> metaModel = metaDataGenerateService
                .getMetaDataForItem(item);
        if (metaModel != null) {
            for (MetaDataRuleLite meta : metaModel) {
                String code = meta.getCode();
                if (code.contains(product.getCode())) {
                    firstMetaList.add(meta);
                } else if ((code.toLowerCase().contains("overview")
                        || accomCodeType(code)
                        )&& (!(code.contains("-")))
                        && (!(code.toLowerCase().contains(product.getCode())))) {
                    secondMetaList.add(meta);
                } else {
                    thirdMetaList.add(meta);
                }
            }
            final int flag = 0;
            viewData = getMetaView(firstMetaList, secondMetaList, thirdMetaList, accmPageModel, item, flag);
        }
        return viewData;

    }

    private boolean accomCodeType(final String code)
    {
        return code.toLowerCase().contains("rooms") || code.toLowerCase().contains("facilities")
                || code.toLowerCase().contains("location") || code.toLowerCase().contains("thingstodo");
    }

    /**
     * @param item
     * @param pageModel
     * @return MetaDataViewData for attraction and excursion based on the priority.
     */
    public MetaDataViewData attractionMetaDataResolver(final ItemModel item, final AbstractPageModel pageModel)
    {
        MetaDataViewData viewData = new MetaDataViewData();

        if ("Attraction".equalsIgnoreCase(item.getItemtype()))
        {
            AttractionModel attracModel = (AttractionModel) item;
            List<MetaDataRuleLite> metaModel = metaDataGenerateService.getMetaDataForItem(item);
            String code=attracModel.getCode();
            if (metaModel != null && (!(metaModel.isEmpty())))
            {
                viewData = getExcursionView(metaModel, item, viewData, code, "Attraction");
            }
        }
        else if ("Excursion".equalsIgnoreCase(item.getItemtype()))
        {
            final ProductModel excursionModel = (ProductModel) item;

            List<MetaDataRuleLite> metaModel = metaDataGenerateService.getMetaDataForItem(item);
            String code=excursionModel.getCode();
            if (metaModel != null && (!(metaModel.isEmpty())))
            {
                viewData = getExcursionView(metaModel, item, viewData, code, "Excursion");
            }
        }
        return viewData;
    }

    /**
     * @param firstMetaList
     * @param secondMetaList
     * @param thirdMetaList
     * @param page
     * @param item
     * @param flagValue
     * @return MetaDataViewData
     */
        private MetaDataViewData getMetaView(List<MetaDataRuleLite> firstMetaList , List<MetaDataRuleLite> secondMetaList, List<MetaDataRuleLite> thirdMetaList,
            AbstractPageModel page ,ItemModel item , int flagValue)
    {
        String pageType = " ";
        int flag = flagValue;

        final MetaDataViewData viewData = new MetaDataViewData();
        pageType = getAttractionPageType(page);
        if (!(firstMetaList.isEmpty()))
        {
                for (MetaDataRuleLite metadata : firstMetaList) {
                    if (metadata
                            .getCode()
                            .toUpperCase()
                        .contains(pageType))
                                 {
                        viewData.setMetaTitle(resolvePlaceholders(
                                metadata.getMetaTitleTemplate(), item));
                        viewData.setMetaKeywords(resolvePlaceholders(
                                metadata.getMetaKeywordsTemplate(), item));
                        viewData.setMetaDescription(resolvePlaceholders(
                                metadata.getMetaDescriptionTemplate(), item));
                        flag = 1;
                    }

                }
            }
        if (!(secondMetaList.isEmpty()) && flag == 0)
        {
            for (MetaDataRuleLite metadata : secondMetaList)
            {
                if (metadata.getCode().toUpperCase().contains(pageType))
                {
                    viewData.setMetaTitle(resolvePlaceholders(metadata.getMetaTitleTemplate(), item));
                    viewData.setMetaKeywords(resolvePlaceholders(metadata.getMetaKeywordsTemplate(), item));
                    viewData.setMetaDescription(resolvePlaceholders(metadata.getMetaDescriptionTemplate(), item));
                    flag = 1;
                }
            }
        }
        if (!(thirdMetaList.isEmpty()) && flag == 0)
        {
            for (MetaDataRuleLite metadata : thirdMetaList)
            {
                if (metadata.getCode().toUpperCase().contains(NAME))
                {
                    viewData.setMetaTitle(resolvePlaceholders(metadata.getMetaTitleTemplate(), item));
                    viewData.setMetaKeywords(resolvePlaceholders(metadata.getMetaKeywordsTemplate(), item));
                    viewData.setMetaDescription(resolvePlaceholders(metadata.getMetaDescriptionTemplate(), item));
                    flag = 1;
                }
            }

        }

        return viewData;

    }

    /**
     * @param meta
     * @param item
     * @param view
     * @param code
     * @return MetaDataViewData
     */
    private MetaDataViewData getExcursionView (List<MetaDataRuleLite> meta , ItemModel item,MetaDataViewData view , String code , String type)
    {

        final MetaDataViewData viewData = view;
        final List<MetaDataRuleLite> metaModel = meta;
        final String attractionCode = code;
        final String attractionType = type;
        String value = "";
                                for (MetaDataRuleLite metadata : metaModel) {
                        if (metadata.getCode().toUpperCase()
                    .contains(attractionCode)) {
                            viewData.setMetaTitle(resolvePlaceholders(
                                    metadata.getMetaTitleTemplate(), item));
                            viewData.setMetaKeywords(resolvePlaceholders(
                                    metadata.getMetaKeywordsTemplate(), item));
                            viewData.setMetaDescription(resolvePlaceholders(
                                    metadata.getMetaDescriptionTemplate(), item));
                break;
            }
            if (metadata.getCode().equalsIgnoreCase(attractionType)) {
                value = "set";
                            viewData.setMetaTitle(resolvePlaceholders(
                                    metadata.getMetaTitleTemplate(), item));
                            viewData.setMetaKeywords(resolvePlaceholders(
                                    metadata.getMetaKeywordsTemplate(), item));
                            viewData.setMetaDescription(resolvePlaceholders(
                                    metadata.getMetaDescriptionTemplate(), item));
                continue;
            }
                        if (metadata.getCode().toUpperCase()
                                .contains(NAME)
                                && (!("set".equalsIgnoreCase(value)))) {
                            viewData.setMetaTitle(resolvePlaceholders(
                                    metadata.getMetaTitleTemplate(), item));
                            viewData.setMetaKeywords(resolvePlaceholders(
                                    metadata.getMetaKeywordsTemplate(), item));
                            viewData.setMetaDescription(resolvePlaceholders(
                                    metadata.getMetaDescriptionTemplate(), item));
                continue;
            }
        }
        return viewData;

    }

    protected String resolvePlaceholders(final String input, final ItemModel item)
    {
        if (StringUtils.isNotBlank(input) && item != null)
        {
            String brand = null;
            String parentName = "";

            if (item instanceof LocationModel)
            {
                final List<CategoryModel> parents = ((LocationModel) item).getSupercategories();
                parentName = resolveParentName(parents);
                brand = brandUtils.getFeatureServiceBrand(((LocationModel) item).getBrands());
            }
            else if (item instanceof ProductModel)
            {
                final Collection<CategoryModel> parents = ((ProductModel) item).getSupercategories();
                parentName = resolveParentName(parents);
            }
            else if (item instanceof AttractionModel)
            {
                final Collection<CategoryModel> parents = ((AttractionModel) item).getSupercategories();
                parentName = resolveParentName(parents);
            }
            if (item instanceof AccommodationModel)
            {
                brand = brandUtils.getFeatureServiceBrand(((AccommodationModel) item).getBrands());
            }

            String itemName = featureService.getFirstFeatureValueAsString("name", item, new Date(), brand);
            if (itemName == null)
            {
                itemName = "";
            }

            return input.replaceAll("\\[item\\]", itemName).replaceAll("\\[parent\\]", parentName);
        }
        else
        {
            return "";
        }
    }

    private String resolveParentName(final Collection<CategoryModel> parents)
    {
        String parentName = "";
        for (final CategoryModel parent : parents)
        {
            if (parent instanceof LocationModel)
            {
                parentName = featureService.getFirstFeatureValueAsString("name", parent, new Date(),
                        brandUtils.getFeatureServiceBrand(parent.getBrands()));
                if (parentName == null)
                {
                    parentName = "";
                }
                break;
            }
        }
        return parentName;
    }

    /**
     * @param attractionPageModel
     * @return pageType This method returns the page type .
     */
    private String getAttractionPageType(final AbstractPageModel attractionPageModel)
    {
        String pageType = "";
        final AbstractPageModel page = attractionPageModel;
        if ("LocationPage".equalsIgnoreCase(page.getItemtype()))
        {
            final LocationPageModel pageModel = (LocationPageModel) page;
            pageType = pageModel.getPageType().toString().toUpperCase();
        }
        else
        {
            final AccommodationPageModel pageModel = (AccommodationPageModel) page;
            pageType = pageModel.getPageType().toString().toUpperCase();
        }
        return pageType;
    }
}
