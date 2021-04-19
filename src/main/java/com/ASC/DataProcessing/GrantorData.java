package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrantorData extends CommonMethods{

    public GrantorData(){}

    public final String[] headerSubTable = {"Name", "Type__c"};

    public static final String labelPath = "//*[@id=\"DocDetails1_Label_GrantorGrantee\"]";

    public JSONObject getGrantorData(WebDriver driver, int rowID) {

        String grantorLabel = null;
        int dataRecords;

        try {
            Thread.sleep(3500);
            WebElement linkedText = driver.findElement(By.xpath(getButtonXpath(rowID)));
            linkedText.click();
            Thread.sleep(2000);
            WebElement grantorLabelPath = driver.findElement(By.xpath(labelPath));
            grantorLabel = grantorLabelPath.getText();
            Thread.sleep(1000);

        }catch (Exception e){e.printStackTrace();}

        dataRecords = Integer.parseInt(grantorLabel.substring(16));
        //System.out.println("No of dataRecords:"+dataRecords);

        if (dataRecords<=10){
            JSONArray records = getActualGrantorData(listOfRows(driver,dataRecords),rowID);
            return new JSONObject().put("records",records);

        }else{
         JSONArray rec = getMultipleGrantorData(driver,dataRecords,rowID);
            return new JSONObject().put("records",rec);
        }
    }

    private List<WebElement> listOfRows(WebDriver driver,int length)
    {
        List<WebElement> rowOfSubTable = new ArrayList<>();
        while(rowOfSubTable.size()!=length){
            try{
                Thread.sleep(3000);
               // for (int itr=2;itr<=length+1;itr++
                    for (int itr=2;itr<=length;itr++){ // --> fot plymouth county
                WebElement row = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr["+itr+"]"));
                rowOfSubTable.add(row);
                }
            }catch(Exception e1){
                e1.printStackTrace();}
        }
        return rowOfSubTable;
    }

    private JSONArray getMultipleGrantorData(WebDriver driver,int dataRecords,int mainRowId){
        int noOfPages = dataRecords/10;
        int dataInLastPage = dataRecords % 10;

        JSONArray subTableContent = getActualGrantorData(listOfRows(driver,10),mainRowId);
        int pageCount = 1;

        if(pageCount<noOfPages){
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(listOfRows(driver,10),mainRowId));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }else {
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(listOfRows(driver,dataInLastPage),mainRowId));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        return subTableContent;
    }

    private void clickOnNextPage(WebDriver driver, int pageCount) throws InterruptedException {
        pageCount++;
        Thread.sleep(2500);
        boolean check = true;
        WebElement nextBtn = null;
        while(check){
            nextBtn = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td["+pageCount+"]/a"));
            check = !(nextBtn!=null);
        }
        nextBtn.click();
        Thread.sleep(3000);
    }



    private JSONArray getActualGrantorData(List<WebElement>  rowElement,int mainRowId){

        JSONArray objForSubTable = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject objAttributes = new JSONObject();

        for (WebElement r:rowElement) {
            try{
                Thread.sleep(1500);
                List<WebElement> cols = r.findElements(By.tagName("td"));
                for (int i = 0; i < cols.size(); i++) {
                    objForSubRow.put(headerSubTable[i],cols.get(i).getText());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            objAttributes.put("type","Grantor__c");
            objAttributes.put("referenceId","ref"+new Random().nextInt(10000)+"_"+mainRowId);
            objForSubRow.put("attributes",objAttributes);
            objForSubTable.put(objForSubRow);
            objForSubRow = new JSONObject();
            objAttributes = new JSONObject();
        }
       return objForSubTable;
    }

  /*  private String getButtonXpath(int rowValue){

        if(rowValue<9){
            return "//*[@id=\"DocList1_GridView_Document_ctl0"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }else{
            return "//*[@id=\"DocList1_GridView_Document_ctl"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }
    }*/

    private String getButtonXpath(int rowValue){ //for plymouth county only

        if(rowValue<9){
            return "//*[@id=\"DocList1_GridView_Document_ctl0"+(rowValue+1)+"_ButtonRow_Name_"+(rowValue-1)+"\"]";
        }else{
            return "//*[@id=\"DocList1_GridView_Document_ctl"+(rowValue+1)+"_ButtonRow_Name_"+(rowValue-1)+"\"]";
        }
    }
}
