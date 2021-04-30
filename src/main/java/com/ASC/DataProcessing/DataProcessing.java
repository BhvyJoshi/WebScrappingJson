package com.ASC.DataProcessing;

import com.ASC.HeaderProcessing.Group1;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DataProcessing extends GrantorData{

    public static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table";
    public static final String nextButtonPath = "//*[@id=\"DocList1_LinkButtonNext\"]";
    public static final String recordsPerPage = "//*[@id=\"DocList1_PageView5Btn\"]";

    public void tableData(WebDriver driver,String fileName,String request)
    {
        String[] headers = new Group1().grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,request);

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(nextButtonPath)));
                driver.findElement(By.xpath(nextButtonPath)).click();
                System.out.println("\n------------------next Button clicked------------------");
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            System.out.println("MainTable row number is ----->"+rowCount);
            String[] data = new String[9]; //data of each row
            for (int itr = 0; itr<9; itr++){
                new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount)+"/td")));
                String xPath = getMainTableRow(rowCount)+"/td["+(itr+2)+"]";
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
            }

            for (int itr1 = 0;itr1 <header.length;itr1++){ //mapping of header and data in json object
                while(itr1 == 5){
                    data[itr1] = generateDate(data[itr1]);
                    break;
                }
                objForRow.put(header[itr1],data[itr1]);
            }
            getObjectForRow(driver,request,objForRow,rowCount);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

}
