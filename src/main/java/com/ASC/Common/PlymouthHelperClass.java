package com.ASC.Common;

import com.ASC.DataProcessing.GrantorData;
import com.ASC.DataProcessing.GrantorDataPlymouth;
import com.ASC.HeaderProcessing.Plymouth;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PlymouthHelperClass extends GrantorDataPlymouth {

    public static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    public static final String nextButtonPath = "//*[@id=\"DocList1_LinkButtonNext\"]";

    public void tableData(WebDriver driver,String fileName,String request,String logFileName)
    {
        String[] headers = new Plymouth().grabHeader(driver);
        generateFile(fileName,grabData(driver,headers,request,logFileName));

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                new WebDriverWait(driver,30).until(ExpectedConditions.elementToBeClickable(By.xpath(nextButtonPath)));
                driver.findElement(By.xpath(nextButtonPath)).click();
                writeLog("\n--------------Next btn clicked--------\n",logFileName);
                //System.out.println("\n--------------Next btn clicked--------\n");
                Thread.sleep(2000);
                //tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request,logFileName));
                appendJSONinFile(fileName,grabData(driver,headers,request,logFileName));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request,String logFileName)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        GrantorDataPlymouth childData = new GrantorDataPlymouth();

        new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(mainTablePath+"/tr")));

        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount)+"/td")));
            writeLog("----Main Table row------"+rowCount,logFileName);
            //System.out.println("----Main Table row------"+rowCount);

            String[] data = new String[10];
            for (int i = 0; i <=9; i++) {
                data[i] = driver.findElement(By.xpath(getMainTableRow(rowCount)+"/td["+(i+2)+"]")).getText();
            }

            data[0] = data[0].contains("OR")?data[0].replace("OR","Grantor"):data[0].replace("EE","Grantee");
            data[7] = generateDate(data[7]);

            for (int i = 0; i < header.length; i++) {
                objForRow.put(header[i],data[i]);
            }

            objForRow.put("attributes",putAttributes(rowCount));
            objForRow.put("Grantors__r",childData.getGrantorData(driver,rowCount,logFileName));
            objForRow.put("Lead_Search__c",request);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            childData = new GrantorDataPlymouth();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }
}
