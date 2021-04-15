package com.ASC.Common;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HampdenHelperClass {


    public final static String headerTagPath = "//*[@id=\"search\"]/div/div[5]/table/tbody/tr[1]";
    public static final String searchLastNameText = "//*[@id=\"W9SNM\"]";
    public static final String searchFirstNameText = "//*[@id=\"W9GNM\"]";
    public static final String mainTablePath = "//*[@id=\"search\"]/div/div[5]/table/tbody";
    public static final String nextButtonPath = "//*[@id=\"search\"]/div/div[3]/div/a[2]";

    public void firstPageForHampden(WebDriver driver, String keyWord)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://search.hampdendeeds.com/html/Hampden/V3/search.html");
        driver.findElement(By.xpath("/html/body/section[1]/div/div[2]/div[1]/div/ul/li[2]/a")).click();
        driver.findElement(By.xpath(searchLastNameText)).sendKeys(keyWord);
      /*Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText(value);*/
        driver.findElement(By.xpath("//*[@id=\"search\"]/div[4]/div[2]/input[1]")).click();
    }
    public void firstPageForHampden(WebDriver driver, String keyWord,String firstName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://search.hampdendeeds.com/html/Hampden/V3/search.html");
        driver.findElement(By.xpath("/html/body/section[1]/div/div[2]/div[1]/div/ul/li[2]/a")).click();
        driver.findElement(By.xpath(searchLastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(searchFirstNameText)).sendKeys(firstName);
      /*Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText(value);*/
        driver.findElement(By.xpath("//*[@id=\"search\"]/div[4]/div[2]/input[1]")).click();
    }

  public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()-2];
        for (int i =0;i<headers.size()-2;i++)
        {
            header[i]=headers.get(i).getText();
        }

        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Reverse Party","Reverse_Party__c").replace("Name","Name__c");
        header = header.replace("Town","Town__c").replace("Date Received","Date_Received__c");
        header = header.replace("Book (page)","Book_(page)__c").replace("Document Type","Document_Type__c").replace("Document Desc","Document_Desc__c");
        header = header.replace("[","").replace("]","");
        System.out.println(header);
        hdr = header.split(",");
        return hdr;
    }

    public void tableData(WebDriver driver,String fileName,String requestID)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID);

        while(checkForData(driver)){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
               /* new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOf(nextBtn));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);*/
                nextBtn.click();
                System.out.print("\n------------next Button clicked----");
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID));
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

    private boolean checkForData(WebDriver driver){
        WebElement table = driver.findElement(By.xpath(mainTablePath));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement column = rows.get(rows.size()-1).findElement(By.tagName("td"));
        String columnText = column.getText();
        return columnText.contains("More names may be available");
    }


    public JSONArray grabData(WebDriver driver,String[] header,String requestID)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        JSONObject attributes = new JSONObject();
        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=2;rowCount<rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()-3); column++, hdr++) {
                //for (int column = 0, hdr = 0; (column < 7); column++, hdr++) {
                objForRow.put(header[hdr], cols.get(column).getText());
                while(hdr == 3) {
                    Date dob;
                    try {
                        dob = new SimpleDateFormat("MM-dd-yyyy").parse(cols.get(column).getText());
                        String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                        objForRow.put(header[hdr], str);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            attributes.put("type", "Lead_Search_Result__c");
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Lead_Search__c",requestID); //add request id
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
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

    public String getMainTableRow(int count){
        return " //*[@id=\"search\"]/div/div[5]/table/tbody/tr["+count+"]";
    }


}
