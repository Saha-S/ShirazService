package com.rahbaran.shirazservice;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Nadia on 11/12/2017.
 */

public class MyService {
    public String serviceId,reqId,serviceTitle, areaTitle, insrtTimePersian , stateTitle,priorityTitle ,
            desc,calculatedPrice,servicePicAddress ,subCatTitle, catTitle , rate,state,insrtTimeSimple  ;

    public MyService(SoapObject soapObject) {

        this.serviceId = soapObject.getPropertyAsString("serviceId");
        this.serviceTitle = soapObject.getPropertyAsString("serviceTitle");
        this.areaTitle = soapObject.getPropertyAsString("areaTitle");
        this.insrtTimePersian = soapObject.getPropertyAsString("insrtTimePersian");
        this.stateTitle = soapObject.getPropertyAsString("stateTitle");
        this.priorityTitle = soapObject.getPropertyAsString("priorityTitle");
        this.calculatedPrice = soapObject.getPropertyAsString("calculatedPrice");
        this.servicePicAddress  = soapObject.getPropertyAsString("servicePicAddress");
        this.catTitle  = soapObject.getPropertyAsString("catTitle");
        this.subCatTitle  = soapObject.getPropertyAsString("subCatTitle");
        this.rate  = soapObject.getPropertyAsString("rate");
        this.reqId  = soapObject.getPropertyAsString("reqId");
        this.state  = soapObject.getPropertyAsString("state");
        this.insrtTimeSimple   = soapObject.getPropertyAsString("insrtTimeSimple");
        this.desc = soapObject.getPropertyAsString("desc");

    }


}
