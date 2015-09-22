/*
 * Copyright (C)2006 TUI UK Ltd
 *
 * TUI UK Ltd, Columbus House, Westwood Way, Westwood Business Park, Coventry, United Kingdom CV4
 * 8TT
 *
 * Telephone - (024)76282828
 *
 * All rights reserved - The copyright notice above does not evidence any actual or intended
 * publication of this source code.
 *
 * $RCSfile: DreamLinerToolTipDataPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Jan 14, 2015$
 *
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.tui.flights.web.view.data.DreamLinerTooltipViewData;


/**
 * @author phanisekhar.n
 *
 */
public class DreamLinerToolTipDataPopulator implements Populator<List<ContentValueModel>, DreamLinerTooltipViewData>
{
   @Override
   public void populate(final List<ContentValueModel> source, final DreamLinerTooltipViewData target) throws ConversionException
   {
      final Map<String, String> values = new HashMap<String, String>();

      for (final ContentValueModel contentValueModel : source)
      {
         values.put(contentValueModel.getName(), contentValueModel.getValue().toString());
      }
      target.setContentValue(values);
   }

}
