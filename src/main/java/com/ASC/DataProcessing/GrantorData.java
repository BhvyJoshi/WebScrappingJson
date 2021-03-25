package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.List;

public class GrantorData {

    public GrantorData(){}

    public final String[] headerSubTable = {"Name", "Type_c"};

    public static final String labelPath = "//*[@id=\"DocDetails1_Label_GrantorGrantee\"]";

    public JSONArray getGrantorData(WebDriver driver, int rowID) {

        //System.out.println("\n--------------+++++++++++++++++--------------------\n");
       // System.out.println("main table row value is "+rowID);
        String grantorLabel = null;
        int dataRecords;

        try {
            Thread.sleep(3500);
            //System.out.println("xpath for btn "+getButtonXpath(rowID));
            WebElement linkedText = driver.findElement(By.xpath(getButtonXpath(rowID)));
            linkedText.click();
            Thread.sleep(1500);
            WebElement grantorLabelPath = driver.findElement(By.xpath(labelPath));
            grantorLabel = grantorLabelPath.getText();
            Thread.sleep(1000);

        }catch (Exception e){e.printStackTrace();}


        //System.out.println("value of grantor label"+grantorLabel);
        dataRecords = Integer.parseInt(grantorLabel.substring(16));
        //System.out.println("no of data records "+dataRecords);

        if (dataRecords<=10){
            return getActualGrantorData(listOfRows(driver,dataRecords));

        }else{
         return getMultipleGrantorData(driver,dataRecords);
        }
    }

    private List<WebElement> listOfRows(WebDriver driver,int length)
    {
        List<WebElement> rowOfSubTable = new ArrayList<>();
        while(rowOfSubTable.size()!=length){
            try{
                Thread.sleep(3000);
                for (int itr=2;itr<=length+1;itr++){
                WebElement row = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr["+itr+"]"));
                rowOfSubTable.add(row);
                }
            }catch(Exception e1){
                e1.printStackTrace();}
        }
        //System.out.println("size of listOfRows "+rowOfSubTable.size());
        return rowOfSubTable;
    }

    private JSONArray getMultipleGrantorData(WebDriver driver,int dataRecords){
        int noOfPages = dataRecords/10;
        int dataInLastPage = dataRecords % 10;

        //System.out.println("inside getMultipleGrantorData and value of dataRecords is "+dataRecords);

        JSONArray subTableContent = getActualGrantorData(listOfRows(driver,10));
        int pageCount = 1;

        if(pageCount<noOfPages){
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(listOfRows(driver,10)));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }else {
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(listOfRows(driver,dataInLastPage)));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        //System.out.println("after clicking 2nd page");
        return subTableContent;
    }

    private void clickOnNextPage(WebDriver driver, int pageCount) throws InterruptedException {
        pageCount++;
        Thread.sleep(2000);
        boolean check = true;
        WebElement nextBtn = null;
        while(check){
            nextBtn = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td["+pageCount+"]/a"));
            check = !nextBtn.isDisplayed();
        }
        nextBtn.click();
        Thread.sleep(3000);
    }

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }

    private JSONArray getActualGrantorData(List<WebElement>  rowElement){

        JSONArray objForSubTable = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        //System.out.println("inside getActualGrantorData and size of rows is "+rowElement.size());

        for (WebElement r:rowElement) {
            try{
                Thread.sleep(1000);
                List<WebElement> cols = r.findElements(By.tagName("td"));
                for (int i = 0; i < cols.size(); i++) {
                    objForSubRow.put(headerSubTable[i],cols.get(i).getText());
                }
            }catch (Exception e){e.printStackTrace();}
            objForSubTable.put(objForSubRow);
            objForSubRow = new JSONObject();
        }
       return objForSubTable;
    }

    private String getButtonXpath(int rowValue){

        if(rowValue<9){
            return "//*[@id=\"DocList1_GridView_Document_ctl0"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }else{
            return "//*[@id=\"DocList1_GridView_Document_ctl"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }
    }
}
