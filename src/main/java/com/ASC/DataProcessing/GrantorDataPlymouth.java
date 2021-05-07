package com.ASC.DataProcessing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class GrantorDataPlymouth extends CommonMethods {

    public static final String labelPath = "//*[@id=\"DocDetails1_Label_GrantorGrantee\"]";
    public static final String subTableBody = "//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/";
    public final String[] headerSubTable = {"Name", "Type__c"};

    public JSONObject getGrantorData(WebDriver driver, int rowID, String logFileName) {

        String grantorLabel = null;

        try {
            Thread.sleep(2500);
            new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath(getButtonXpath(rowID))));
            new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(getButtonXpath(rowID))));
            driver.findElement(By.xpath(getButtonXpath(rowID))).click();
            //writeLog("-----row is clicked-----"+rowID,logFileName);
            System.out.println("-----row is clicked-----" + rowID);
            Thread.sleep(2500);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelPath)));
            grantorLabel = driver.findElement(By.xpath(labelPath)).getText();
            //Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //writeLog("value of grantorLabel == --------------------"+grantorLabel,logFileName);
        System.out.println("value of grantorLabel == --------------------" + grantorLabel);
        String[] dataRecordValue = grantorLabel.split("-");
        int dataRecords = Integer.parseInt(dataRecordValue[1]);
        //writeLog("value of dataRecords == -------------"+dataRecords,logFileName);
        System.out.println("value of dataRecords == -------------" + dataRecords);

        grantorLabel = null;
        System.out.println("value of grantor label is ----->" + grantorLabel);
        System.gc();
        if (dataRecords <= 10) {
            JSONArray records = getActualGrantorData(driver, rowID, dataRecords, logFileName);
            return new JSONObject().put("records", records);

        } else {
            JSONArray rec = getMultipleGrantorData(driver, rowID, dataRecords, logFileName);
            return new JSONObject().put("records", rec);
        }
    }

    private JSONArray getMultipleGrantorData(WebDriver driver, int dataRecords, int mainRowId, String logFileName) {
        int noOfPages = dataRecords / 10;
        int dataInLastPage = dataRecords % 10;

        JSONArray subTableContent = getActualGrantorData(driver, mainRowId, 10, logFileName);
        int pageCount = 1;

        if (pageCount < noOfPages) {
            try {
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent, getActualGrantorData(driver, mainRowId, 10, logFileName));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                clickOnNextPage(driver, pageCount);
                subTableContent = appendToList(subTableContent, getActualGrantorData(driver, mainRowId, dataInLastPage, logFileName));
            } catch (Exception e1) {
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
        while (check) {
            nextBtn = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody/tr[12]/td/table/tbody/tr/td[" + pageCount + "]/a"));
            check = !(nextBtn != null);
        }
        nextBtn.click();
        Thread.sleep(3000);
    }

    private JSONArray getActualGrantorData(WebDriver driver, int mainRowId, int length, String logFileName) {

        JSONArray objForSubTable = new JSONArray();
        JSONObject objForSubRow = new JSONObject();

        //writeLog("value of length is --------------->"+length,logFileName);
        System.out.println("value of length is --------------->" + length);
        while (objForSubTable.length() != length) {
            try {
                for (int itr = 2; itr <= length + 1; itr++) {
                    //Thread.sleep(2000);
                    new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(subTableBody + "/tr")));
                    driver.findElement(By.xpath(subTableBody + "tr[" + itr + "]"));
                    //writeLog("SubTable row number: ---> "+itr,logFileName);
                    System.out.println("SubTable row number: ---> " + itr);
                    WebElement column1 = driver.findElement(By.xpath(subTableBody + "tr[" + itr + "]/td[1]"));
                    WebElement column2 = driver.findElement(By.xpath(subTableBody + "tr[" + itr + "]/td[2]"));
                    objForSubRow.put(headerSubTable[0], column1.getText());
                    objForSubRow.put(headerSubTable[1], column2.getText());
                    objForSubRow.put("attributes", putSubAttributes(mainRowId));
                    objForSubTable.put(objForSubRow);
                    objForSubRow = new JSONObject();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return objForSubTable;
    }

    private String getButtonXpath(int rowValue) { //for plymouth county only

        if (rowValue < 9) {
            return "//*[@id=\"DocList1_GridView_Document_ctl0" + (rowValue + 1) + "_ButtonRow_Name_" + (rowValue - 1) + "\"]";
        } else {
            return "//*[@id=\"DocList1_GridView_Document_ctl" + (rowValue + 1) + "_ButtonRow_Name_" + (rowValue - 1) + "\"]";
        }
    }
}
