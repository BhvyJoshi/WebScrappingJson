package com.ASC.Common;

import com.ASC.HeaderProcessing.Hampden;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HampdenHelperClass extends Hampden {

    public static final String searchLastNameText = "//*[@id=\"W9SNM\"]";
    public static final String searchFirstNameText = "//*[@id=\"W9GNM\"]";
    public static final String mainTablePath = "//*[@id=\"search\"]/div/div[5]/table/tbody";
    public static final String nextButtonPath = "//*[@id=\"search\"]/div/div[3]/div/a[2]";
    public static final String searchBtn = "//*[@id=\"search\"]/div[4]/div[2]/input[1]";

    public void firstPageForHampden(WebDriver driver, String keyWord,String firstName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://search.hampdendeeds.com/html/Hampden/V3/search.html");
        driver.findElement(By.xpath("/html/body/section[1]/div/div[2]/div[1]/div/ul/li[2]/a")).click();
        driver.findElement(By.xpath(searchLastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(searchFirstNameText)).sendKeys(firstName);
      /*Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText(value);*/
        driver.findElement(By.xpath(searchBtn)).click();
    }

    public void tableData(WebDriver driver,String fileName,String requestID)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID);
        //boolean keepLooping = true;

        while(checkForData(driver,mainTablePath)){
            try{
                Thread.sleep(1000);
                driver.navigate().refresh();
                driver.findElement(By.xpath(nextButtonPath)).click();
                Thread.sleep(1500);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID));
                //keepLooping = false;
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
       generateFile(fileName,tableDataContent);
    }

    static boolean checkForData(WebDriver driver,String tableXpath){
        WebElement table = driver.findElement(By.xpath(tableXpath));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement column = rows.get(rows.size()-1).findElement(By.tagName("td"));
        String columnText = column.getText();
        return columnText.contains("More names may be available");
    }

    public JSONArray grabData(WebDriver driver,String[] header,String requestID)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount=2;rowCount<rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()-3); column++, hdr++) {
                //addDataToJsonObj(header, objForRow, cols, column, hdr);
                new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount) + "/td")));
                //*[@id="search"]/div/div[5]/table/tbody/tr[2]/td[1]
                String[] data = new String[9]; //data of each row
                for (int itr = 0; itr <= 6; itr++) {
                    String xPath = getMainTableRow(rowCount)+"/td["+(itr+1)+"]";
                    data[itr] = driver.findElement(By.xpath(xPath)).getText();
                }
                data[7] = generatePage(data[6]);
                data[8] = generateType(data[0]);
                data[3] = generateDateFormat(data[3]); // replacing data format
                data[0] = getName(data[0]);
                data[6] = getBook(data[6]);

                for (int itr1 = 0; itr1 < header.length; itr1++) { //mapping of header and data in json object
                    objForRow.put(header[itr1], data[itr1]);
                }
            }
            getObjectForRow(requestID, objForRow, rowCount);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    static void addDataToJsonObj(String[] header, JSONObject objForRow, List<WebElement> cols, int column, int hdr) {
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

    public String getMainTableRow(int count){
        return " //*[@id=\"search\"]/div/div[5]/table/tbody/tr["+count+"]";
    }

    static String generatePage(String str){
        String[] split = str.split("-");
        return split[1];
    }

    static String generateType(String str){
        String[] strings = str.split("[(]+",2);
        if(strings[1].contains("Gtor")){
            return "Grantor";
        }else
            return "Grantee";
    }
    static String getBook(String str){
        String[] split = str.split("-");
        return split[0];
    }
    static String getName(String str){
        String[] split = str.split("[(]+");
        return split[0];
    }
}
