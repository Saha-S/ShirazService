package com.rahbaran.shirazservice;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Nadia on 11/12/2017.
 */

public class Request {
    public String requestId,serviceTitle, areaTitle, insrtTimePersian , stateTitle,priorityTitle ,desc,calculatedPrice,servicePicAddress , catTitle ,timeDesc , subCatTitle , catId , subCatId , areaId ,priority,
            dateFromPersian ,dateToPersian,insrtTimeSimple ;

    public Request(SoapObject soapObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        this.requestId = soapObject.getPropertyAsString("requestId");
        this.serviceTitle = soapObject.getPropertyAsString("serviceTitle");
        this.areaTitle = soapObject.getPropertyAsString("areaTitle");
        this.insrtTimePersian = soapObject.getPropertyAsString("insrtTimePersian");
        this.stateTitle = soapObject.getPropertyAsString("stateTitle");
        this.priorityTitle = soapObject.getPropertyAsString("priorityTitle");
        this.calculatedPrice = soapObject.getPropertyAsString("calculatedPrice");
        this.servicePicAddress = soapObject.getPropertyAsString("servicePicAddress");
        this.catTitle  = soapObject.getPropertyAsString("catTitle");
        this.subCatTitle   = soapObject.getPropertyAsString("subCatTitle");
        this.catId   = soapObject.getPropertyAsString("catId");
        this.subCatId   = soapObject.getPropertyAsString("subCatId");
        this.areaId   = soapObject.getPropertyAsString("areaId");
        this.priority   = soapObject.getPropertyAsString("priority");
        this.timeDesc  = soapObject.getPropertyAsString("timeDesc");
        this.timeDesc  = soapObject.getPropertyAsString("timeDesc");
        this.dateFromPersian   = soapObject.getPropertyAsString("dateFromPersian");
        this.dateToPersian    = soapObject.getPropertyAsString("dateToPersian");
        this.insrtTimeSimple    = soapObject.getPropertyAsString("insrtTimeSimple");
        this.desc = soapObject.getPropertyAsString("desc");

    }


}
