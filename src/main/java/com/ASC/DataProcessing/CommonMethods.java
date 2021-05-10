package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class CommonMethods {

    public JSONArray appendToList(String fileContent, JSONArray toBeAppend) //METHOD USED WHILE WRITING WHOLE PAGE DATA TO FILE
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(fileContent);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }
    public JSONArray appendToListJSON(JSONArray original, JSONArray toBeAppend)// used for subtable/child data
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }

    public void generateFile(String fileName, JSONArray tableDataContent) // used when file is generated for first time
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

    public void appendJSONinFile(String fileName,JSONArray arrayToBeAppend){ // used when need to add json array data in existing file
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get("C:\\JsonResponse\\"+fileName+".txt")));
            FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
            myWriter.write(appendToList(fileContent,arrayToBeAppend).toString());
            myWriter.close();
        } catch (IOException e) {
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

    public void writeLog(String line, String logFileName){
        File myObj = new File("C:\\JsonResponse\\Logs\\"+logFileName+".txt");
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(FileWriter fw = new FileWriter("C:\\JsonResponse\\Logs\\"+logFileName+".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(LocalDateTime.now() + " " + line);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

}
