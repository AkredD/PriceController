package com.price.controller;

import com.price.controller.interfaces.ControllerLocal;
import com.price.controller.models.PriceModel;
import com.price.controller.models.PriceModelPK;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Controller implements ControllerLocal {
    private static volatile Controller instance;

    public static Controller getInstance(){
        Controller local = instance;
        if (local == null) {
            synchronized (Controller.class) {
                if (local == null) {
                    local = instance = new Controller();
                }
            }
        }
        return local;
    }

    private final Function<List<PriceModel>, Map<PriceModelPK, List<PriceModel>>> formCatalogFromList = (prices) -> {
        return (prices == null) ? new HashMap<>() : prices.stream().collect(Collectors.groupingBy((PriceModelPK::new)));
    };

    private final BinaryOperator<List<PriceModel>> merge = (left, right) -> {
        Collections.sort(left, Comparator.comparing(PriceModel::getBegin));
        Collections.sort(right, Comparator.comparing(PriceModel::getBegin));
        List<PriceModel> priceModels = new ArrayList<>();
        Integer leftCursor = 0;
        Integer rightCursor = 0;
        while (rightCursor < right.size() && leftCursor < left.size()) {
            PriceModel leftPrice = left.get(leftCursor);
            PriceModel rightPrice = right.get(rightCursor);
            PriceModel pastPrice = (priceModels.isEmpty())
                    ? new PriceModel(null, null, null, null, new Date(Long.MIN_VALUE), new Date(Long.MIN_VALUE), null)
                    : priceModels.get(priceModels.size() - 1);
            if (leftPrice.getEnd().compareTo(rightPrice.getBegin()) < 0 ||
                    (leftPrice.getEnd().compareTo(rightPrice.getBegin()) == 0) && !leftPrice.getValue().equals(rightPrice.getValue())) {
                if (leftPrice.getBegin().compareTo(pastPrice.getEnd()) > 0) {
                    priceModels.add(leftPrice);
                } else {
                    if (leftPrice.getEnd().compareTo(pastPrice.getEnd()) > 0) {
                        leftPrice.setBegin(pastPrice.getEnd());
                        priceModels.add(leftPrice);
                    }
                }
                leftCursor++;
                continue;
            }

            if (leftPrice.getEnd().compareTo(rightPrice.getBegin()) >= 0
                    && leftPrice.getBegin().compareTo(rightPrice.getEnd()) <= 0
                    && leftPrice.getValue().equals(rightPrice.getValue())) {
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) <= 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) <=0 ) {
                    leftPrice.setEnd(rightPrice.getEnd());
                    if (leftPrice.getBegin().compareTo(pastPrice.getEnd()) < 0) {
                        leftPrice.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(leftPrice);
                    leftCursor++;
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) <= 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) > 0 ) {
                    PriceModel price = new PriceModel(leftPrice);
                    price.setEnd(rightPrice.getEnd());
                    if (price.getBegin().compareTo(pastPrice.getEnd()) < 0) {
                        price.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(price);
                    leftPrice.setBegin(rightPrice.getEnd());
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) > 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) <= 0 ) {
                    priceModels.add(rightPrice);
                    leftCursor++;
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) > 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) > 0 ) {
                    priceModels.add(rightPrice);
                    leftPrice.setBegin(rightPrice.getEnd());
                    rightCursor++;
                    continue;
                }
            }
            if (leftPrice.getEnd().compareTo(rightPrice.getBegin()) >= 0
                    && leftPrice.getBegin().compareTo(rightPrice.getEnd()) <= 0
                    && !leftPrice.getValue().equals(rightPrice.getValue())) {
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) <= 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) <=0 ) {
                    leftPrice.setEnd(rightPrice.getBegin());
                    if (leftPrice.getBegin().compareTo(pastPrice.getEnd()) < 0) {
                        leftPrice.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(leftPrice);
                    priceModels.add(rightPrice);
                    leftCursor++;
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) <= 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) > 0 ) {
                    PriceModel price = new PriceModel(leftPrice);
                    price.setEnd(rightPrice.getBegin());
                    if (price.getBegin().compareTo(pastPrice.getEnd()) < 0) {
                        price.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(price);
                    priceModels.add(rightPrice);
                    leftPrice.setBegin(rightPrice.getEnd());
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) > 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) <= 0 ) {
                    priceModels.add(rightPrice);
                    leftCursor++;
                    rightCursor++;
                    continue;
                }
                if (leftPrice.getBegin().compareTo(rightPrice.getBegin()) > 0
                        && leftPrice.getEnd().compareTo(rightPrice.getEnd()) > 0 ) {
                    priceModels.add(rightPrice);
                    leftPrice.setBegin(rightPrice.getEnd());
                    rightCursor++;
                    continue;
                }
            }


            if (leftPrice.getEnd().compareTo(rightPrice.getBegin()) >= 0
                    && leftPrice.getBegin().compareTo(rightPrice.getEnd()) <= 0
                    && !leftPrice.getValue().equals(rightPrice.getValue())) {
                PriceModel price = new PriceModel(leftPrice);
                price.setEnd(rightPrice.getBegin());
                if (price.getBegin().compareTo(pastPrice.getEnd()) < 0) {
                    price.setBegin(pastPrice.getEnd());
                }
                priceModels.add(price);
                leftCursor++;
                continue;
            }

            if (leftPrice.getBegin().equals(rightPrice.getEnd())) {
                if (leftPrice.getValue().equals(rightPrice.getValue())) {
                    leftPrice.setBegin(rightPrice.getBegin());
                    priceModels.add(leftPrice);
                    leftCursor++;
                    rightCursor++;
                }else {
                    priceModels.add(rightPrice);
                    rightCursor++;
                }
            }
            if (leftPrice.getBegin().compareTo(rightPrice.getEnd()) > 0) {
                priceModels.add(rightPrice);
                rightCursor++;
            }
        }
        if (leftCursor == left.size()) {
            for (int i = rightCursor; i < right.size(); ++i) {
                priceModels.add(right.get(i));
            }
        } else {
            for (int i = leftCursor; i < left.size(); ++ i) {
                priceModels.add(left.get(i));
            }
        }
        List<PriceModel> result = new ArrayList();
        priceModels.forEach(priceModel -> {
            if (!result.isEmpty()
                    &&result.get(result.size() - 1).getEnd().equals(priceModel.getBegin())
                    && result.get(result.size() - 1).getValue().equals(priceModel.getValue())) {
                result.get(result.size() - 1).setEnd(priceModel.getEnd());
                if (result.get(result.size() - 1).getId() == null && priceModel.getId() != null) {
                    result.get(result.size() - 1).setId(priceModel.getId());
                }
                return;
            }
            result.add(priceModel);
        });
        return result;
    };


    @Override
    public List<PriceModel> mergePrices(List<PriceModel> oldPrices, List<PriceModel> updatePrices) {
        final var availableCatalog = formCatalogFromList.apply(oldPrices);
        final var updateCatalog = formCatalogFromList.apply(updatePrices);
        List<PriceModel> resultCatalog = updateCatalog.keySet()
                .stream()
                .map(updatingPriceByPK -> {
                    return (!availableCatalog.containsKey(updatingPriceByPK))
                            ? updateCatalog.get(updatingPriceByPK)
                            : merge.apply(availableCatalog.get(updatingPriceByPK), updateCatalog.get(updatingPriceByPK));
                })
                .collect(Collector.of(ArrayList::new,
                        ArrayList::addAll,
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        }));
        availableCatalog.keySet()
                .forEach(availablePriceModel -> {
                    if (!updateCatalog.containsKey(availablePriceModel)) {
                        resultCatalog.addAll(availableCatalog.get(availablePriceModel));
                    }
                });
        return resultCatalog;
    }
}
