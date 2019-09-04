package com.price.controller.models;

public class PriceModelPK {
    private final Integer priceNumber;
    private final Integer priceDepart;
    private final Long priceId;


    public PriceModelPK(PriceModel priceModel) {
        this.priceNumber = priceModel.getNumber();
        this.priceDepart = priceModel.getDepart();
        this.priceId = priceModel.getProductCode();
    }

    public Integer getPriceNumber() {
        return priceNumber;
    }

    public Integer getPriceDepart() {
        return priceDepart;
    }

    public Long getPriceId() {
        return priceId;
    }


    @Override
    public int hashCode() {
        return (priceNumber * 13 + priceDepart) * 13 + priceId.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != PriceModelPK.class) {
            return false;
        }
        PriceModelPK o = (PriceModelPK) obj;
        return (priceId.equals(o.getPriceId()) && priceNumber.equals(o.getPriceNumber()) && priceDepart.equals(o.getPriceDepart()));
    }
}
