package com.price.controller;

import com.price.controller.models.PriceModel;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void mergePrices() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            PriceModel p1 = new PriceModel(1L, 122856L, 1, 1,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T23:59:59"), 11000L);
            PriceModel p2 = new PriceModel(2L, 122856L, 2, 1,
                    formatter.parse("2013-01-10T00:00:00"), formatter.parse("2013-01-20T23:59:59"), 99000L);
            PriceModel p3 = new PriceModel(3L, 6654L, 1, 2,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T00:00:00"), 5000L);
            List<PriceModel> catalog = Arrays.asList(p1, p2, p3);
            PriceModel n1 = new PriceModel(null, 122856L, 1, 1,
                    formatter.parse("2013-01-20T00:00:00"), formatter.parse("2013-02-20T23:59:59"), 11000L);
            PriceModel n2 = new PriceModel(null, 122856L, 2, 1,
                    formatter.parse("2013-01-15T00:00:00"), formatter.parse("2013-01-25T23:59:59"), 92000L);
            PriceModel n3 = new PriceModel(null, 6654L, 1, 2,
                    formatter.parse("2013-01-12T00:00:00"), formatter.parse("2013-01-13T00:00:00"), 5000L);
            List<PriceModel> update = Arrays.asList(n1, n2, n3);
            PriceModel r1 = new PriceModel(3L, 6654L, 1, 2,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T00:00:00"), 5000L);
            PriceModel r2 = new PriceModel(2L, 122856L, 2, 1,
                    formatter.parse("2013-01-10T00:00:00"), formatter.parse("2013-01-15T00:00:00"), 99000L);
            PriceModel r3 = new PriceModel(null, 122856L, 2, 1,
                    formatter.parse("2013-01-15T00:00:00"), formatter.parse("2013-01-25T23:59:59"), 92000L);
            PriceModel r4 = new PriceModel(1L, 122856L, 1, 1,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-02-20T23:59:59"), 11000L);
            var result = Controller.getInstance().mergePrices(catalog, update);
            assertTrue("[contains expected value r1]", result.contains(r1));
            assertTrue("[contains expected value r2]", result.contains(r2));
            assertTrue("[contains expected value r3]", result.contains(r3));
            assertTrue("[contains expected value r4]", result.contains(r4));
            assertTrue("[contains too many values]", result.size() == 4);
        } catch (ParseException e) {
            fail("this statement cannot be reached");
        }
    }

    @Test
    public void mergePricesSecond() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            PriceModel pp1 = new PriceModel(1L, 122856L, 1, 1,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T23:59:59"), 11000L);
            PriceModel pp2 = new PriceModel(2L, 122856L, 2, 1,
                    formatter.parse("2013-01-10T00:00:00"), formatter.parse("2013-01-20T23:59:59"), 99000L);
            PriceModel pp3 = new PriceModel(3L, 6654L, 1, 2,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T00:00:00"), 5000L);
            List<PriceModel> catalogL = Arrays.asList(pp1, pp2, pp3);
            PriceModel nn1 = new PriceModel(null, 122856L, 1, 1,
                    formatter.parse("2013-01-20T00:00:00"), formatter.parse("2013-02-20T23:59:59"), 11000L);
            PriceModel nn2 = new PriceModel(null, 122856L, 2, 1,
                    formatter.parse("2013-01-15T00:00:00"), formatter.parse("2013-01-25T23:59:59"), 92000L);
            PriceModel nn3 = new PriceModel(null, 6654L, 1, 2,
                    formatter.parse("2013-01-12T00:00:00"), formatter.parse("2013-01-13T00:00:00"), 4000L);
            List<PriceModel> updateL = Arrays.asList(nn1, nn2, nn3);
            PriceModel rr1 = new PriceModel(3L, 6654L, 1, 2,
                    formatter.parse("2013-01-13T00:00:00"), formatter.parse("2013-01-31T00:00:00"), 5000L);
            PriceModel rr2 = new PriceModel(2L, 122856L, 2, 1,
                    formatter.parse("2013-01-10T00:00:00"), formatter.parse("2013-01-15T00:00:00"), 99000L);
            PriceModel rr3 = new PriceModel(null, 122856L, 2, 1,
                    formatter.parse("2013-01-15T00:00:00"), formatter.parse("2013-01-25T23:59:59"), 92000L);
            PriceModel rr4 = new PriceModel(1L, 122856L, 1, 1,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-02-20T23:59:59"), 11000L);
            PriceModel rr5 = new PriceModel(null, 6654L, 1, 2,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-12T00:00:00"), 5000L);
            PriceModel rr6 = new PriceModel(null, 6654L, 1, 2,
                    formatter.parse("2013-01-12T00:00:00"), formatter.parse("2013-01-13T00:00:00"), 4000L);
            var result = Controller.getInstance().mergePrices(catalogL, updateL);
            assertTrue("[contains expected value r1]", result.contains(rr1));
            assertTrue("[contains expected value r2]", result.contains(rr2));
            assertTrue("[contains expected value r3]", result.contains(rr3));
            assertTrue("[contains expected value r4]", result.contains(rr4));
            assertTrue("[contains expected value r5]", result.contains(rr5));
            assertTrue("[contains expected value r6]", result.contains(rr6));
            assertTrue("[contains too many values]", result.size() == 6);
        }catch (ParseException ex) {
            fail("this statement cannot be reached");
        }
    }

    @Test
    public void mergePricesThird() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            PriceModel p1 = new PriceModel(1L, 122856L, 2, 1,
                    formatter.parse("2013-01-01T00:00:00"), formatter.parse("2013-01-31T23:59:59"), 11000L);
            List<PriceModel> catalog = Arrays.asList(p1);
            PriceModel n1 = new PriceModel(null, 122856L, 1, 1,
                    formatter.parse("2013-01-20T00:00:00"), formatter.parse("2013-02-20T23:59:59"), 11000L);
            List<PriceModel> update = Arrays.asList(n1);
            var result = Controller.getInstance().mergePrices(catalog, update);
            assertTrue("[contains expected value p1]", result.contains(p1));
            assertTrue("[contains expected value n1]", result.contains(n1));
            assertTrue("[contains too many values]", result.size() == 2);
        }catch (ParseException ex) {
            fail("this statement cannot be reached");
        }
    }


}