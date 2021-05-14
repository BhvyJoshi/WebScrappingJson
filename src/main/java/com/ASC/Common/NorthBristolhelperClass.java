package com.ASC.Common;

import com.ASC.HeaderProcessing.NorthBristol;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class NorthBristolhelperClass extends NorthBristol {

    public static final String searchRecords = "//*[@id=\"menu-19531-1\"]/a";
    public static final String recordedLandInquiry = "//*[@id=\"landingMenu\"]/li[1]/ul/li[1]/a";
    public static final String lastNameText = "//*[@id=\"ctl00_cphMainContent_txtLastName_vtbTextBox_text\"]";
    public static final String firstNameText = "//*[@id=\"ctl00_cphMainContent_txtFirstName_vtbTextBox_text\"]";
    public static final String searchBtn = "//*[@id=\"ctl00_cphMainContent_btnSearch\"]";

    public static final String mainTablePath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody";
    public static final String nextButtonPath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr[1]/td/table/tbody/tr";

    public void firstPage(WebDriver driver, String keyWord,String firstName){
        driver.findElement(By.xpath(searchRecords)).click();
        driver.findElement(By.xpath(recordedLandInquiry)).click();
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(firstNameText)).sendKeys(firstName);
        driver.findElement(By.xpath(searchBtn)).click();
    }

    public void tableData(WebDriver driver,String fileName,String request,String logFileName)
    {
        String[] headers = grabHeader(driver);

        generateFile(fileName,grabData(driver,headers,request,logFileName));

        int nextButtonCount = driver.findElement(By.xpath(nextButtonPath)).findElements(By.tagName("td")).size();

        for (int i = 2; i <=nextButtonCount ; i++) {
            try{
                Thread.sleep(1000);
                driver.findElement(By.xpath(nextButtonPath+"/td["+i+"]")).click();
                Thread.sleep(1500);
                //tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request,logFileName));
                appendJSONinFile(fileName,grabData(driver,headers,request,logFileName));
            }
            catch (Exception e1){
                //e1.printStackTrace();
                writeLog(e1.toString(),logFileName);
            }
        }
        //generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request,String logFileName)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(mainTablePath+"/tr")));
        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=3;rowCount<=rowSize-3;rowCount++)
        {
            new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount)+"/td")));

            writeLog("---------------Row is --------------"+rowCount,logFileName);

            String[] data = new String[9]; //data of each row
            for (int itr = 0; itr<8; itr++){
                String xPath = getMainTableRow(rowCount)+"/td["+(itr+2)+"]";
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
            }
            data[1] = generateDate(data[1]);
            data[2] = data[2].contains("TEE")?data[2].replace("TEE","Grantee"):data[2].replace("TOR","Grantor");

            for (int itr1 = 0;itr1< header.length;itr1++){ //mapping of header and data in json object
                objForRow.put(header[itr1],data[itr1]);
            }

            getObjectForRow(request,objForRow,rowCount);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr["+count+"]"; //starts from row 3
    }

}
