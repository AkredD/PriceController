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
            List<PriceModel> exprectedRes = Arrays.asList(r1, r2, r3, r4);
            var resust = Controller.getInstance().mergePrices(catalog, update);
            assertTrue("[contains expected value r1]", resust.contains(r1));
            assertTrue("[contains expected value r2]", resust.contains(r2));
            assertTrue("[contains expected value r3]", resust.contains(r3));
            assertTrue("[contains expected value r4]", resust.contains(r4));


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
            var resustL = Controller.getInstance().mergePrices(catalogL, updateL);
            assertTrue("[contains expected value r1]", resustL.contains(rr1));
            assertTrue("[contains expected value r2]", resustL.contains(rr2));
            assertTrue("[contains expected value r3]", resustL.contains(rr3));
            assertTrue("[contains expected value r4]", resustL.contains(rr4));
            assertTrue("[contains expected value r5]", resustL.contains(rr5));
            assertTrue("[contains expected value r6]", resustL.contains(rr6));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}