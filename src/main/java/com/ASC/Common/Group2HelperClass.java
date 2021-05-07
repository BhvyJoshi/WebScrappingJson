package com.ASC.Common;

import com.ASC.DataProcessing.CommonMethods;
import com.ASC.HeaderProcessing.Group2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

        while(HampdenHelperClass.checkForData(driver,mainTablePath)){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
                nextBtn.click();
                System.out.print("\n------------next Button clicked----");
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID));
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String requestID)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(mainTablePath+"/tr")));
        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=2;rowCount<rowSize;rowCount++)
        {
            int noOfColumn = driver.findElement(By.xpath(getMainTableRow(rowCount))).findElements(By.tagName("td")).size();
            while (noOfColumn>3) {
                String[] data = new String[9];
                System.out.println("------------- Row Number is ------"+rowCount);
                for (int itr = 0; itr <= 6; itr++) {
                    //*[@id="search"]/div/table/tbody/tr[13]
                    new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount) + "/td")));
                    String xPath = getMainTableRow(rowCount) + "/td[" + (itr + 1) + "]";
                    data[itr] = driver.findElement(By.xpath(xPath)).getText();
                }
                data[7] = HampdenHelperClass.generatePage(data[6]);
                data[8] = HampdenHelperClass.generateType(data[0]);
                data[3] = generateDateFormat(data[3]); // replacing data format
                data[0] = HampdenHelperClass.getName(data[0]);
                data[6] = HampdenHelperClass.getBook(data[6]);

                for (int itr1 = 0; itr1 < header.length; itr1++) { //mapping of header and data in json object
                    objForRow.put(header[itr1], data[itr1]);
                }
                getObjectForRow(requestID,objForRow,rowCount);
                objForPage.put(objForRow);
                objForRow = new JSONObject();

                break;
            }

        }
        return objForPage;
    }
    public String getMainTableRow(int count){
        return "//*[@id=\"search\"]/div/table/tbody/tr["+count+"]";
    }
}
