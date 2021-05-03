package com.ASC.Common;

import com.ASC.DataProcessing.CookGrantorData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CookCountyHelper extends CookGrantorData {

    private static final String mainTableNextButtonPath = "//*[@id=\"NameList1_LinkButtonNext\"]";
    private static final String mainTablePath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private static final String textBoxId = "SearchFormEx1_ACSTextBox_LastName1";
    private static final String mainHeaderPath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    private static final String searchBtn = "//*[@id=\"SearchFormEx1_btnSearch\"]";

    public void firstPage(WebDriver driver, String value) {
        try {
            Thread.sleep(1500);
            driver.findElement(By.id(textBoxId)).sendKeys(value);
            driver.findElement(By.xpath(searchBtn)).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tableData(WebDriver driver,String fileName,String request,String[] headers,String[] subHeaders)
    {
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,request,subHeaders);
        int nextBtnCliCkCount  = driver.findElements(By.xpath("//*[@id=\"NameList1_ctl01\"]/tbody/tr/td[3]/a")).size();
        boolean checkNext = true;
        int count = 0;

        while(checkNext && nextBtnCliCkCount>=0){
            try{
                try {
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(mainTableNextButtonPath)).click();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
                    executor.executeScript("arguments[0].click();", driver.findElement(By.linkText("Next")));
                }
                /*new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(mainTableNextButtonPath))));
                WebElement nextBtn = driver.findElement(By.xpath(mainTableNextButtonPath));
                nextBtn.click();*/
                System.out.println("\n------- next btn clicked------------"+(++count));
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request,subHeaders));
                nextBtnCliCkCount --;
            }
            catch (Exception e1){
                e1.printStackTrace();
                checkNext = false;
            }
        }
        generateFile(fileName, tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request,String[] subHeaders)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objCommon = new JSONObject();

        int rowSize =  driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            JSONArray temp;
            System.out.println("\n-----------main table row ------->"+rowCount);
            new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(getMainTableRow(rowCount)+"/td")));
            for (int itr = 0; itr < 2; itr++) {
                String column = driver.findElement(By.xpath(getMainTableRow(rowCount)+"/td["+(itr+1)+"]")).getText();
                objCommon.put(header[itr], column);
            }
            temp = getGrantorGranteeData(driver,rowCount,objCommon,request,subHeaders);

            for (int i = 0; i < temp.length(); i++) {
                objForPage.put(temp.getJSONObject(i));
            }
        }
        return objForPage;
    }
}
