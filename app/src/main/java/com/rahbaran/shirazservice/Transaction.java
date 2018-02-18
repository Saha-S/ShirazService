package com.rahbaran.shirazservice;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Nadia on 11/12/2017.
 */

public class Transaction {
    public String id,actionTitle, typeTitle, requestId , price,desc ,insrtTimePersian,advanceDesc,remainCredit;
    public Transaction(SoapObject soapObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        this.id = soapObject.getPropertyAsString("id");
        this.actionTitle = soapObject.getPropertyAsString("actionTitle");
        this.typeTitle = soapObject.getPropertyAsString("typeTitle");
        this.price = soapObject.getPropertyAsString("price");
        this.desc = soapObject.getPropertyAsString("desc");
        this.insrtTimePersian = soapObject.getPropertyAsString("insrtTimePersian");
        this.requestId = soapObject.getPropertyAsString("requestId");
        this.advanceDesc = soapObject.getPropertyAsString("advanceDesc");
        this.remainCredit = soapObject.getPropertyAsString("remainCredit");

    }


}
