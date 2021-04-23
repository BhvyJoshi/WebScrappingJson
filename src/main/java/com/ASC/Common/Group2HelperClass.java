package com.ASC.Common;

import com.ASC.DataProcessing.CommonMethods;
import com.ASC.HeaderProcessing.Group2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Group2HelperClass extends CommonMethods {

    private static final String nextButtonPath =  "//*[@id=\"search\"]/div/div[3]/div[1]/div/a[3]";
    private final static String mainTablePath = "//*[@id=\"search\"]/div/table/tbody";

    private static final String searchLastNameText = "//*[@id=\"W9SNM\"]";
    private static final String searchRecordsClick = "//*[@id=\"search\"]/div/input";
    private static final String searchFirstNameText = "//*[@id=\"W9GNM\"]";

  public void checkFirstName(WebDriver driver,String keyWord,String searchRegistryRecordClick,String firstName){
      driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
      try{
          Thread.sleep(2000);
          new WebDriverWait(driver, 20).until(
                  webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
      }catch(Exception e)
        {e.printStackTrace();}
      ((JavascriptExecutor) driver).executeScript(searchRegistryRecordClick);
      driver.findElement(By.xpath(searchLastNameText)).sendKeys(keyWord);
      driver.findElement(By.xpath(searchFirstNameText)).sendKeys(firstName);
      driver.findElement(By.xpath(searchRecordsClick)).click();
  }

  public void tableData(WebDriver driver,String fileName,String requestID)
    {
        String[] headers = new Group2().grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID);

        while(checkForData(driver)){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
                nextBtn.click();
                //System.out.print("\n------------next Button clicked----");
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        generateFile(fileName,tableDataContent);
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
            for (int column = 0, hdr = 0; (column < cols.size()-4); column++, hdr++) {
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
    public String getMainTableRow(int count){
        return "//*[@id=\"search\"]/div/table/tbody/tr["+count+"]";
    }
}
