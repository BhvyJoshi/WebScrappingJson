package com.ASC.Common;

import com.ASC.HeaderProcessing.SouthEssex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SouthEssexHelperClass extends SouthEssex {

    public static final String searchRecordsBtn= "//*[@id=\"ctl00_SampleContent_ImageButton1\"]";
    public static final String startDateText= "//*[@id=\"ASPxPageControl1_RecordedTab_NameSearchStart\"]";
    public static final String startDate = "1/2/1951";
    public static final String lastNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSLastName\"]";
    public static final String firstNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSFirstName\"]";
    public static final String searchBtn = "//*[@id=\"ASPxPageControl1_RecordedTab_cmdNameSearch\"]";
    public static final String searchResultCount = "//*[@id=\"dvSearchTerms\"]/table/tbody/tr[4]/td[1]";

    public void firstPage(WebDriver driver,String keyWord, String firstName){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath(searchRecordsBtn)).click();
        driver.findElement(By.xpath(startDateText)).sendKeys(startDate);
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(firstNameText)).sendKeys(firstName);
        driver.findElement(By.xpath(searchBtn)).click();
    }

    private int getSearchResultCount(WebDriver driver){
        String result = driver.findElement(By.xpath(searchResultCount)).getText();
        result =  result.replace("Result:","").replace("Rows","").trim();
        return Integer.parseInt(result);
    }

    public void tableData(WebDriver driver,String fileName,String requestID,String logFileName)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID,0,15,logFileName);
        int searchResultCount = getSearchResultCount(driver);
        int noOfLoop = searchResultCount/15;
        int lastPageData = searchResultCount % 15;
        int count =0;
        if(noOfLoop>10){
            while(count<noOfLoop-1){
                try{
                    String nextBtnClick;
                    count ++;
                    Thread.sleep(1000);
                    if(count == 1) {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[10]";
                    }else {
                        driver.navigate().refresh();
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]";
                    }
                    driver.findElement(By.xpath(nextBtnClick)).click();
                    writeLog("\n-----------Next Btn clicked---------\n",logFileName);
                    Thread.sleep(2000);
                    tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*count),(15*count)+15,logFileName));
                }
                catch (Exception e1){
                    writeLog(e1.toString(),logFileName);
                    //e1.printStackTrace();
                }
            }
            driver.navigate().refresh();
            driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]")).click();
        }else{
            while(count<noOfLoop-1){
                try{
                    String nextBtnClick;
                    count ++;
                    Thread.sleep(1000);
                    if(count == 1) {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[2]";
                    }else {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a["+(count+1)+"]";
                    }
                    driver.navigate().refresh();
                    driver.findElement(By.xpath(nextBtnClick)).click();
                    writeLog("\n----------------------Next Btn clicked------------------\n",logFileName);
                    Thread.sleep(1500);
                    tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*count),(15*count)+15,logFileName));
                }
                catch (Exception e1){
                    writeLog(e1.toString(),logFileName);
                    //e1.printStackTrace();
                }
            }
            driver.navigate().refresh();
            driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a["+(noOfLoop+1)+"]")).click();
        }
        tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*noOfLoop),(15*noOfLoop)+lastPageData,logFileName));

        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String requestID,int lowerLimit, int upperLimit,String logFileName)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        for (int rowCount=lowerLimit;rowCount<upperLimit;rowCount++)
        {
            new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount)+"/td")));
            writeLog("-----------------Row Number -------"+rowCount,logFileName);
            String[] data = new String[15]; //data of each row
            for (int itr = 0; itr<=13; itr++){
                String xPath = getMainTableRow(rowCount)+"/td["+(itr+3)+"]";
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
            }
            data[0] = generateDate(data[0]);

            for (int itr1 = 0;itr1< header.length;itr1++){ //mapping of header and data in json object
                objForRow.put(header[itr1],data[itr1]);
            }

            getObjectForRow(requestID,objForRow,rowCount);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }
     public String getMainTableRow(int count){
        return "//*[@id=\"ASPxGridView1_DXDataRow"+count+"\"]";
    }
}
