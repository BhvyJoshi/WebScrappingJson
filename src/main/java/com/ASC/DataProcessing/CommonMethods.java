package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class CommonMethods {

    public JSONArray appendToList(JSONArray original, JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }

    public void generateFile(String fileName, JSONArray tableDataContent)
    {
        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(tableDataContent.toString());
                myWriter.close();
            }
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            writeLog("An error occured","");
            e.printStackTrace();
        }
    }

    public void getObjectForRow(String requestID, JSONObject objForRow, int rowCount) {
        JSONObject attributes = new JSONObject();
        attributes.put("type", "Lead_Search_Result__c");
        attributes.put("referenceId","ref"+ rowCount +"_"+new Random().nextInt(100000));
        objForRow.put("attributes", attributes);
        objForRow.put("Lead_Search__c", requestID);
    }

    public void getObjectForRow(WebDriver driver, String requestID, JSONObject objForRow, int rowCount, String logFileName, GrantorData childrecord) {

        objForRow.put("attributes",putAttributes(rowCount));
        objForRow.put("Grantors__r",childrecord.getGrantorData(driver,rowCount,logFileName));
        objForRow.put("Lead_Search__c",requestID);
    }

    public String generateDate(String date){
        Date dob = null;
        try {
            dob = new SimpleDateFormat("MM/dd/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dob);
    }

    public String generateDateFormat(String date){
        Date dob = null;
        try {
            dob = new SimpleDateFormat("MM-dd-yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dob);
    }

    public JSONObject putAttributes(int rowCount){
        JSONObject attributes = new JSONObject();
        attributes.put("type", "Lead_Search_Result__c");
        attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
        return attributes;
    }

    public JSONObject putSubAttributes(int rowCount){
        JSONObject attributes = new JSONObject();
        attributes.put("type", "Grantor__c");
        attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
        return attributes;
    }

    public void writeLog(String line, String County){
        File myObj = new File("C:\\JsonResponse\\Logs\\"+County+".txt");
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(FileWriter fw = new FileWriter("C:\\JsonResponse\\Logs\\"+County+".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(LocalDateTime.now() + " " + line);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

}
