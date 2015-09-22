/**
 *
 */
package uk.co.tui.fo.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.fo.book.view.data.TermsAndConditionsViewData;

/**
 * @author thyagaraju.e
 *
 */
public class SpecialNeedsContentViewDataPopulator implements
   Populator<Object, TermsAndConditionsViewData>
{

   @Resource
   private GenericContentService genericContentService;

   /**
    * Population of terms and conditions
    */
   @Override
   public void populate(final Object source, final TermsAndConditionsViewData target)
      throws ConversionException
   {
      final List<String> specialNeedsList = new ArrayList<String>();
      specialNeedsList.add("B506489373028F83");
      specialNeedsList.add("E16D5514BCE88133");
      specialNeedsList.add("42BC191450877404");
      specialNeedsList.add("9C01FF5D54BE8D05");
      specialNeedsList.add("C5ECE573AEADE224");
      specialNeedsList.add("091C4E0865003889");
      specialNeedsList.add("BD1725B5DFFEE597");
      specialNeedsList.add("04A47F737298AB45");
      specialNeedsList.add("59447FE99C069691");
      specialNeedsList.add("64A56F69E72ECCE3");

      for (final String code : specialNeedsList)
      {
         final List<DynamicContentConfigModel> dynamicContents =
            genericContentService.getDynamicContentConfig(code, StringUtils.EMPTY,
               StringUtils.EMPTY);
         for (final DynamicContentConfigModel dynamicContent : dynamicContents)
         {
            target.getSpecialNeedsMap().putAll(
               genericContentService.getGenericDynamicContentValue(dynamicContent));
         }
      }
   }
}
