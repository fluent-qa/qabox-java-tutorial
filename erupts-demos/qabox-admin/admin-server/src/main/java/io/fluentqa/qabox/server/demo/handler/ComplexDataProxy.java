package io.fluentqa.qabox.server.demo.handler;

import io.fluentqa.qabox.server.demo.model.complex.Complex;
import io.fluentqa.qabox.server.demo.model.complex.Component;
import org.apache.poi.ss.usermodel.Workbook;
import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.annotation.query.Condition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Component
public class ComplexDataProxy implements DataProxy<Complex> {

    @Override
    public void addBehavior(Complex o) {
    }


    @Override
    public void beforeAdd(Complex o) {
        System.err.println("beforeAdd");
    }

    @Override
    public void afterAdd(Complex o) {
        System.err.println("afterAdd");
    }

    @Override
    public void beforeUpdate(Complex o) {
        System.err.println("beforeUpdate");
    }

    @Override
    public void afterUpdate(Complex o) {
        System.err.println("afterUpdate");
    }

    @Override
    public void beforeDelete(Complex o) {
        System.err.println("beforeDelete");
    }

    @Override
    public void afterDelete(Complex o) {
        System.err.println("afterDelete");
    }

    @Override
    public String beforeFetch(List<Condition> conditions) {
        System.err.println("beforeFetch");
        return null;
    }

    @Override
    public void afterFetch(Collection<Map<String, Object>> list) {
        System.err.println("afterFetch");
    }

    @Override
    public void editBehavior(Complex o) {
        System.err.println("editBehavior");
    }

    @Override
    public void excelExport(Object obj) {
        Workbook wb = (Workbook) obj;
        System.err.println("excelExport");
    }

    @Override
    public void excelImport(Object o) {
        System.err.println("excelImport");
    }

}
