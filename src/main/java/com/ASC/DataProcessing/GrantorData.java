package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;

public class GrantorData extends CommonMethods{

    public GrantorData(){}

    public final String[] headerSubTable = {"Name", "Type__c"};

    public static final String labelPath = "//*[@id=\"DocDetails1_Label_GrantorGrantee\"]";
    public static final String subTableBody = "//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/";
    public static final String nextBtnLocator = "//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td";

    public JSONObject getGrantorData(WebDriver driver, int rowID) {

        String grantorLabel = null;

        try {
            Thread.sleep(3500);
            driver.findElement(By.xpath(getButtonXpath(rowID))).click();
            Thread.sleep(2000);
            new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelPath)));
            grantorLabel = driver.findElement(By.xpath(labelPath)).getText();
            Thread.sleep(1000);

        }catch (Exception e){e.printStackTrace();}
        int dataRecords = Integer.parseInt(grantorLabel.substring(16));
        System.out.println("value of dataRecords == --------------------"+dataRecords);

        if (dataRecords<=10){
            return new JSONObject().put("records",getActualGrantorData(driver,rowID,dataRecords));

        }else{
            return new JSONObject().put("records",getMultipleGrantorData(driver,dataRecords,rowID));
        }
    }

    private JSONArray getMultipleGrantorData(WebDriver driver,int dataRecords,int mainRowId){
        int noOfPages = dataRecords/10;
        int dataInLastPage = dataRecords % 10;

        JSONArray subTableContent = getActualGrantorData(driver,mainRowId,10);
        int pageCount = 1;

        if(pageCount<noOfPages){
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(driver,mainRowId,10));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }else {
            try{
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent,getActualGrantorData(driver,mainRowId,dataInLastPage));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        return subTableContent;
    }

    private void clickOnNextPage(WebDriver driver, int pageCount) throws InterruptedException {
        pageCount++;
        String locatorXpath = "["+pageCount+"]/a";
        Thread.sleep(1000);
        new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(nextBtnLocator+locatorXpath)));
        boolean check = true;
        WebElement nextBtn = null;
        while(check){
            nextBtn = driver.findElement(By.xpath(nextBtnLocator+locatorXpath));
            check = !(nextBtn!=null);
        }
        nextBtn.click();
        Thread.sleep(3000);
    }

    private JSONArray getActualGrantorData(WebDriver driver,int mainRowId,int length){

        JSONArray objForSubTable = new JSONArray();
        JSONObject objForSubRow = new JSONObject();

        //problem found. the value is not getting updated as per new grantor grantee table, it takes the previous table's value.
        System.out.println("value of length is --------------->"+length);
           while(objForSubTable.length()!=length){
               try{
                    for (int itr=2;itr<=length+1;itr++){
                        Thread.sleep(2500);
                        new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(subTableBody+"/tr")));
                        driver.findElement(By.xpath(subTableBody+"tr["+itr+"]"));
                        System.out.println("SubTable row number: ---> "+itr);
                        WebElement column1 = driver.findElement(By.xpath(subTableBody+"tr["+itr+"]/td[1]"));
                        WebElement column2 = driver.findElement(By.xpath(subTableBody+"tr["+itr+"]/td[2]"));
                        objForSubRow.put(headerSubTable[0],column1.getText());
                        objForSubRow.put(headerSubTable[1],column2.getText());
                        objForSubRow.put("attributes",attributes(mainRowId));
                        objForSubTable.put(objForSubRow);
                        objForSubRow = new JSONObject();
                    }
                }catch(Exception e1){
                   e1.printStackTrace();
                }
           }
       return objForSubTable;
    }

    private JSONObject attributes(int rowID){
        JSONObject objAttributes = new JSONObject();
        objAttributes.put("type","Grantor__c");
        objAttributes.put("referenceId","ref"+new Random().nextInt(10000)+"_"+rowID);
        return objAttributes;
    }

    private String getButtonXpath(int rowValue){
        if(rowValue<9){
            return "//*[@id=\"DocList1_GridView_Document_ctl0"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }else{
            return "//*[@id=\"DocList1_GridView_Document_ctl"+(rowValue+1)+"_ButtonRow_Name/ Corporation_"+(rowValue-1)+"\"]";
        }
    }

}
