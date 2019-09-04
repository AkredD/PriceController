package com.price.controller.models;

import java.io.Serializable;
import java.util.Date;

public class PriceModel implements Serializable {
    private Long id;
    private Long productCode;
    private Integer number;
    private Integer depart;
    private Date begin;
    private Date end;
    private Long value;

    public PriceModel(Long id, Long productCode, Integer number, Integer depart, Date begin, Date end, Long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public PriceModel(PriceModel original) {
        this.productCode = original.productCode;
        this.number = original.number;
        this.depart = original.depart;
        this.begin = original.begin;
        this.end = original.end;
        this.value = original.value;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDepart() {
        return depart;
    }

    public void setDepart(Integer depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "id: " + id + " productCode: " + productCode + " number: " + number + " depart: " + depart + " begin: " + begin.toString() + " end: " + end.toString() + " value: " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != PriceModel.class) {
            return false;
        }
        PriceModel o = (PriceModel) obj;
        return (productCode.equals(o.productCode) && value.equals(o.value) && begin.equals(o.begin)
                && end.equals(o.end) && number.equals(o.number) && depart.equals(o.depart));
    }
}
