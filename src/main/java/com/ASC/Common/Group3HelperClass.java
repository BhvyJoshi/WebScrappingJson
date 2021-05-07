package com.ASC.Common;

import com.ASC.DataProcessing.GrantorData;
import com.ASC.HeaderProcessing.Group3;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Group3HelperClass extends GrantorData {

    public WebDriver driver;

    private static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table";
    private static final String nextButtonPath = "//*[@id=\"DocList1_LinkButtonNext\"]";

    public void tableData(WebDriver driver, String fileName, String request,String logFileName)
    {
        String[] headers = new Group3().grabHeader(driver);
        //driver.findElement(By.xpath("//*[@id=\"DocList1_PageView5Btn\"]")).click();
        JSONArray tableDataContent;
        try{
            Thread.sleep(2000);
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

        }catch(Exception e){

        }
        tableDataContent = grabData(driver,headers,request,logFileName);

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                driver.findElement(By.xpath(nextButtonPath)).click();
                Thread.sleep(1500);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request,logFileName));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request,String logfileName)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        GrantorData childRecord = new GrantorData();
        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            String[] data = new String[8]; //data of each row
            for (int itr = 0; itr<8; itr++){
                new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount)+"/td")));

                    String xPath = getMainTableRow(rowCount)+"/td["+(itr+2)+"]";
                    data[itr] = driver.findElement(By.xpath(xPath)).getText();
                }

            data[5] = generateDate(data[5]);

            for (int itr1 = 0;itr1< header.length;itr1++){ //mapping of header and data in json object
                objForRow.put(header[itr1],data[itr1]);
            }

            getObjectForRow(driver,request,objForRow,rowCount,logfileName,childRecord);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            childRecord = new GrantorData();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }
}
