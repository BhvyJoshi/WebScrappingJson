package com.ASC.Common;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NorthBristolhelperClass {

    public static final String searchRecords = "//*[@id=\"menu-19531-1\"]/a";
    public static final String recordedLandInquiry = "//*[@id=\"landingMenu\"]/li[1]/ul/li[1]/a";
    public static final String lastNameText = "//*[@id=\"ctl00_cphMainContent_txtLastName_vtbTextBox_text\"]";
    public static final String firstNameText = "//*[@id=\"ctl00_cphMainContent_txtFirstName_vtbTextBox_text\"]";
    public static final String searchBtn = "//*[@id=\"ctl00_cphMainContent_btnSearch\"]";

    public static final String headerTagPath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr[2]";
    public static final String mainTablePath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody";
    public static final String nextButtonPath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr[1]/td/table/tbody/tr";
                                                //*[@id="ctl00_cphMainContent_gvSearchResults"]/tbody/tr[1]/td/table/tbody/tr/td[1]/span
    public void firstPage(WebDriver driver, String keyWord){
        driver.findElement(By.xpath(searchRecords)).click();
        driver.findElement(By.xpath(recordedLandInquiry)).click();
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(searchBtn)).click();
    }

    public void firstPage(WebDriver driver, String keyWord,String firstName){
        driver.findElement(By.xpath(searchRecords)).click();
        driver.findElement(By.xpath(recordedLandInquiry)).click();
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(firstNameText)).sendKeys(firstName);
        driver.findElement(By.xpath(searchBtn)).click();
    }


    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()-1];
        for (int i =0;i<headers.size()-1;i++)
        {
            header[i]=headers.get(i).getText();
        }
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("V","V__c").replace("Doc","Doc__c");
        header = header.replace("Type","Type__c").replace("Name","Name__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c").replace("Type__c_Desc","Type_Desc__c");
        header = header.replace("Date","Date__c").replace("Name__cType__c","NameType__c");
        header = header.replace("Description","Description__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        System.out.println(header);
        hdr = header.split(",");

        return hdr;
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
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath+"/td["+i+"]"));
                nextBtn.click();
                System.out.println("Clicked on button --->"+i);
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }

        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(tableDataContent.toString());
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        JSONObject attributes = new JSONObject();

        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=3;rowCount<=rowSize-3;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()-1); column++, hdr++) {
                objForRow.put(header[hdr], cols.get(column).getText());
                while(hdr == 2){
                    Date dob;
                    try {
                        dob = new SimpleDateFormat("MM/dd/yyyy").parse(cols.get(column).getText());
                        String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                        objForRow.put(header[hdr],str);
                        break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

            attributes.put("type", "Lead_Search_Result__c");
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Lead_Search__c",request);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            attributes = new JSONObject();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr["+count+"]"; //starts from row 3
    }

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }
}
