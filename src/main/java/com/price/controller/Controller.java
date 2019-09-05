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
    private final Function<List<PriceModel>, Map<PriceModelPK, List<PriceModel>>> formCatalogFromList = (prices) -> {
        return (prices == null) ? new HashMap<>() : prices.stream().collect(Collectors.groupingBy((PriceModelPK::new)));
    };
    //merging simple catalog
    private final BinaryOperator<List<PriceModel>> merge = (previousCatalog, newCatalog) -> {
        Collections.sort(previousCatalog, Comparator.comparing(PriceModel::getBegin));
        Collections.sort(newCatalog, Comparator.comparing(PriceModel::getBegin));
        List<PriceModel> priceModels = new ArrayList<>();
        Integer previousCursor = 0;
        Integer newCursor = 0;
        while (newCursor < newCatalog.size() && previousCursor < previousCatalog.size()) {
            PriceModel previousPrice = previousCatalog.get(previousCursor);
            PriceModel newPrice = newCatalog.get(newCursor);
            PriceModel pastPrice = (priceModels.isEmpty())
                    ? new PriceModel(null, null, null, null, new Date(Long.MIN_VALUE), new Date(Long.MIN_VALUE), null)
                    : priceModels.get(priceModels.size() - 1);
            //bool variables for check previous and new prices positions relative to each other
            Boolean previousBeginMoreOrEqPastEnd = previousPrice.getBegin().compareTo(pastPrice.getEnd()) >= 0;
            Boolean previousEndLessNewBegin = previousPrice.getEnd().compareTo(newPrice.getBegin()) < 0;
            Boolean previousEndLessOrEqNewBegin = previousPrice.getEnd().compareTo(newPrice.getBegin()) <= 0;
            Boolean previousEndLessOrEqNewEnd = previousPrice.getEnd().compareTo(newPrice.getEnd()) <= 0;
            Boolean previousEndLessNewEnd = previousPrice.getEnd().compareTo(newPrice.getEnd()) < 0;
            Boolean previousBeginLessOrEqNewEnd = previousPrice.getBegin().compareTo(newPrice.getEnd()) <= 0;
            Boolean previousBeginLessNewBegin = previousPrice.getBegin().compareTo(newPrice.getBegin()) < 0;
            Boolean previousBeginLessNewEnd = previousPrice.getBegin().compareTo(newPrice.getEnd()) < 0;
            if (previousPrice.getValue().equals(newPrice.getValue())) {
                if (previousEndLessNewBegin) {
                    if (!previousBeginMoreOrEqPastEnd) {
                        previousPrice.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(previousPrice);
                    previousCursor++;
                    continue;
                }
                if (previousEndLessOrEqNewEnd) {
                    previousPrice.setBegin(getMinDate(previousPrice.getBegin(), newPrice.getBegin()));
                    previousPrice.setEnd(newPrice.getEnd());
                    priceModels.add(previousPrice);
                    previousCursor++;
                    newCursor++;
                    continue;
                }
                if (previousBeginLessOrEqNewEnd) {
                    newPrice.setBegin(getMinDate(previousPrice.getBegin(), newPrice.getBegin()));
                    previousPrice.setBegin(newPrice.getEnd());
                }
                priceModels.add(newPrice);
                newCursor++;
            } else {
                if (previousEndLessOrEqNewBegin) {
                    if (!previousBeginMoreOrEqPastEnd) {
                        previousPrice.setBegin(pastPrice.getEnd());
                    }
                    priceModels.add(previousPrice);
                    previousCursor++;
                    continue;
                }
                if (previousEndLessNewEnd) {
                    if (previousBeginLessNewBegin) {
                        previousPrice.setEnd(newPrice.getBegin());
                        priceModels.add(previousPrice);
                    }
                    priceModels.add(newPrice);
                    previousCursor++;
                    newCursor++;
                    continue;
                }
                if (previousBeginLessNewBegin) {
                    PriceModel price = new PriceModel(previousPrice);
                    price.setEnd(newPrice.getBegin());
                    previousPrice.setBegin(newPrice.getEnd());
                    priceModels.add(price);
                    priceModels.add(newPrice);
                    newCursor++;
                    continue;
                }
                if (previousBeginLessNewEnd) {
                    previousPrice.setBegin(newPrice.getEnd());
                }
                priceModels.add(newPrice);
                newCursor++;
            }
        }
        //add unmerged catalogs
        if (previousCursor == previousCatalog.size()) {
            for (int i = newCursor; i < newCatalog.size(); ++i) {
                priceModels.add(newCatalog.get(i));
            }
        } else {
            for (int i = previousCursor; i < previousCatalog.size(); ++i) {
                priceModels.add(previousCatalog.get(i));
            }
        }
        List<PriceModel> result = new ArrayList();
        // combination of identical prices for which the beginning and the end coincide
        priceModels.forEach(currentPrice -> {
            if (!result.isEmpty()) {
                PriceModel previousPrice = result.get(result.size() - 1);
                if (previousPrice.getEnd().equals(currentPrice.getBegin())
                        && previousPrice.getValue().equals(currentPrice.getValue())) {
                    previousPrice.setEnd(currentPrice.getEnd());
                    if (previousPrice.getId() == null && currentPrice.getId() != null) {
                        previousPrice.setId(currentPrice.getId());
                    }
                    return;
                }
            }
            result.add(currentPrice);
        });
        return result;
    };

    public static Controller getInstance() {
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

    private Date getMinDate(Date left, Date right) {
        return (left.compareTo(right) < 0) ? left : right;
    }

    @Override
    public List<PriceModel> mergePrices(List<PriceModel> oldPrices, List<PriceModel> updatePrices) {
        final var availableCatalog = formCatalogFromList.apply(oldPrices);
        final var updateCatalog = formCatalogFromList.apply(updatePrices);
        List<PriceModel> resultCatalog = updateCatalog.keySet()
                .parallelStream()
                .map(updatingPriceByPK -> {
                    return (!availableCatalog.containsKey(updatingPriceByPK))
                            ? updateCatalog.get(updatingPriceByPK)
                            : merge.apply(availableCatalog.get(updatingPriceByPK), updateCatalog.get(updatingPriceByPK));
                })
                .collect(Collector.of(ArrayList::new,
                        ArrayList::addAll,
                        (previousCatalog, newCatalog) -> {
                            previousCatalog.addAll(newCatalog);
                            return previousCatalog;
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
