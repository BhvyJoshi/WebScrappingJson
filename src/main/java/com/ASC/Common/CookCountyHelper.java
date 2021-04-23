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
import java.util.List;
import java.util.Random;

public class CookCountyHelper extends CookGrantorData {

    private static final String mainTableNextButtonPath = "//*[@id=\"NameList1_LinkButtonNext\"]";
    private static final String mainTablePath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private static final String textBoxId = "SearchFormEx1_ACSTextBox_LastName1";
    private static final String mainHeaderPath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    private static final String searchBtn = "//*[@id=\"SearchFormEx1_btnSearch\"]";

    public void firstPage(WebDriver driver, String value) {
        try {
            Thread.sleep(2000);
            driver.findElement(By.id(textBoxId)).sendKeys(value);
            driver.findElement(By.xpath(searchBtn)).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void counter(WebDriver driver){
        int clickCount = driver.findElements(By.xpath("//*[@id=\"NameList1_ctl01\"]/tbody/tr/td[3]/a")).size();
    }
    public void tableData(WebDriver driver,String fileName,String request)
    {
        String[] headers = grabHeader(driver,mainHeaderPath);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,request);
        int nextBtnCliCkCount  = driver.findElements(By.xpath("//*[@id=\"NameList1_ctl01\"]/tbody/tr/td[3]/a")).size();
        boolean checkNext = true;
        int count = 0;

        while(checkNext && nextBtnCliCkCount>=0){
            try{
                Thread.sleep(2000);
                WebElement nextBtn = driver.findElement(By.xpath(mainTableNextButtonPath));
                new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOf(nextBtn));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
                nextBtn.click();
                System.out.println("\n------- next btn clicked------------"+(++count));
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request));
                nextBtnCliCkCount --;
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
        generateFile(fileName, tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        JSONObject attributes = new JSONObject();

        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < 2); column++, hdr++) { //as we need only 1st and 2nd column's data, i.e column<2
                objForRow.put(header[hdr], cols.get(column).getText());
                }

            attributes.put("type", "Lead_Search_Result__c"); //will be changed
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Grantor_Grantee_Records__r",getGrantorGranteeData(driver,rowCount));
            objForRow.put("Lead_Search__c",request);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            attributes = new JSONObject();
        }
        return objForPage;
    }


}
