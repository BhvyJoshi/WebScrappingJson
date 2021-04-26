package com.ASC.Common;

import com.ASC.HeaderProcessing.NorthBristol;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public void tableData(WebDriver driver,String fileName,String request)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,request);

        int nextButtonCount = driver.findElement(By.xpath(nextButtonPath)).findElements(By.tagName("td")).size();

        for (int i = 2; i <=nextButtonCount ; i++) {

            try{
                Thread.sleep(1000);
                driver.findElement(By.xpath(nextButtonPath+"/td["+i+"]")).click();
                driver.findElement(By.xpath(nextButtonPath+"/td["+i+"]")).click();
                Thread.sleep(1500);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=3;rowCount<=rowSize-3;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));
            List<WebElement> cols = row.findElements(By.tagName("td"));

            String[] data = new String[cols.size()]; //data of each row
            for (int itr = 1; itr<=cols.size()-1; itr++){
                String xPath = getMainTableRow(rowCount)+"/td["+itr+"]";
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
            }

            for (int itr1 = 0;itr1< header.length;itr1++){ //mapping of header and data in json object
                while(itr1 == 3){
                    data[itr1] = generateDate(data[itr1]);
                    break;
                }
                objForRow.put(header[itr1],data[itr1]);
            }

            getObjectForRow(request,objForRow,rowCount);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    public String generateDate(String date){
        Date dob = null;
        try {
            dob = new SimpleDateFormat("MM/dd/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dob);
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr["+count+"]"; //starts from row 3
    }

}
