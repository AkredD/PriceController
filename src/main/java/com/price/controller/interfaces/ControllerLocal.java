package com.price.controller.interfaces;

import com.price.controller.models.PriceModel;

import java.util.List;

public interface ControllerLocal {
    List<PriceModel> mergePrices(List<PriceModel> oldPrices, List<PriceModel> updatePrices);
}
