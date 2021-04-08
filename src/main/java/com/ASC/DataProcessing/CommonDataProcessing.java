package com.ASC.DataProcessing;

import java.util.Arrays;

public class CommonDataProcessing {

    public String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Type Desc.","Type_Desc");
        header = header.replace("Type","Type__c").replace("Name/ Corporation","Name_Corporation__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c").replace("Type__c_Desc","Type_Desc__c");
        header = header.replace("Rec. Date","Rec_Date__c").replace("Street #","Street__c");
        header = header.replace("Property Descr","Property_Descr__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        hdr = header.split(",");
        return hdr;
    }
}
