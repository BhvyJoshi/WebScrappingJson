package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.List;

public class GrantorData {

    public final String[] headerSubTable = {"Name", "Type_c"};

    public static final String labelPath = "//*[@id=\"DocDetails1_Label_GrantorGrantee\"]";

    public JSONArray getGrantorData(WebDriver driver, int rowID) {

        WebElement linkedText = driver.findElement(By.xpath(getButtonXpath(rowID)));
        linkedText.click();
        String grantorLabel = driver.findElement(By.xpath(labelPath)).getText();
        String[] number = grantorLabel.split("Grantor/Grantee-");

        int dataRecords = Integer.parseInt(number[1]);

        if (dataRecords<=10){

            return getActualGrantorData(listOfRows(driver,dataRecords));

        }else{
         return getMultipleGrantorData(driver,dataRecords);
        }
    }

    private List<WebElement> listOfRows(WebDriver driver,int length)
    {
        List<WebElement> rowOfSubTable = new ArrayList<>();
        for (int itr=2;itr<=length+1;itr++){
            rowOfSubTable.add(driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr["+itr+"]")));
        }
        return rowOfSubTable;
    }

    private JSONArray getMultipleGrantorData(WebDriver driver,int dataRecords){
        int noOfPages = dataRecords/10;
        int dataInLastPage = dataRecords % 10;

        JSONArray subTableContent = getActualGrantorData(listOfRows(driver,10));
        int pageCount = 1;

        if (pageCount<noOfPages){
            try{
                WebElement nextBtn = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td["+(pageCount+1)+"]/a"));
                nextBtn.click();
                Thread.sleep(5000);
                subTableContent = appendToList(subTableContent,getActualGrantorData(listOfRows(driver,10)));
                pageCount++;
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }else {
            try {
                WebElement nextBtn = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td[" + (pageCount + 1) + "]/a"));
                nextBtn.click();
                Thread.sleep(5000);
                subTableContent = appendToList(subTableContent, getActualGrantorData(listOfRows(driver, dataInLastPage)));
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return subTableContent;
    }

    private JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
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

        for (WebElement r:rowElement) {
            List<WebElement> cols = r.findElements(By.tagName("td"));
            for (int i = 0; i < cols.size(); i++) {
                objForSubRow.put(headerSubTable[i],cols.get(i).getText());
            }
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
