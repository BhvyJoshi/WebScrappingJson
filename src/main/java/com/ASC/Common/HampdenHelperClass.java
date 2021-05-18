package com.ASC.Common;

import com.ASC.HeaderProcessing.Hampden;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

    public void tableData(WebDriver driver,String fileName,String requestID,String logFileName) {
        if (!checkForData(driver, mainTablePath)) {
            generateEmptyFile(fileName);

        } else {
            String[] headers = grabHeader(driver);
            generateFile(fileName, grabData(driver, headers, requestID, logFileName));

            while (checkForData(driver, mainTablePath)) {
                try {
                    Thread.sleep(1000);
                    driver.navigate().refresh();
                    driver.findElement(By.xpath(nextButtonPath)).click();
                    writeLog("\n-----------Next Btn clicked--------\n", logFileName);
                    Thread.sleep(1500);
                    //tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,logFileName));
                    appendJSONinFile(fileName, grabData(driver, headers, requestID, logFileName));
                } catch (Exception e1) {
                    //e1.printStackTrace();
                    writeLog(e1.toString(), logFileName);
                }
            }
            //generateFile(fileName,tableDataContent);
        }
    }

    static boolean checkForData(WebDriver driver,String tableXpath){
        WebElement table = driver.findElement(By.xpath(tableXpath));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement column = rows.get(rows.size()-1).findElement(By.tagName("td"));
        String columnText = column.getText();
        return columnText.equals("More names may be available");
    }

    public JSONArray grabData(WebDriver driver,String[] header,String requestID,String logFileName) {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(mainTablePath + "/tr")));
        int rowSize = driver.findElement(By.xpath(mainTablePath)).findElements(By.tagName("tr")).size();

        for (int rowCount = 2; rowCount < rowSize; rowCount++)
        {
            int noOfColumn = driver.findElement(By.xpath(getMainTableRow(rowCount))).findElements(By.tagName("td")).size();
            while (noOfColumn>3) {
                String[] data = new String[9];
                writeLog("------------- Row Number is ------"+rowCount,logFileName);
                //System.out.println("------------- Row Number is ------"+rowCount);
                for (int itr = 0; itr <= 6; itr++) {
                    //*[@id="search"]/div/table/tbody/tr[13]
                    new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getMainTableRow(rowCount) + "/td")));
                    String xPath = getMainTableRow(rowCount) + "/td[" + (itr + 1) + "]";
                    data[itr] = driver.findElement(By.xpath(xPath)).getText();
                }
                data[7] = HampdenHelperClass.generatePage(data[6]);
                data[8] = HampdenHelperClass.generateType(data[0]);
                data[3] = generateDateFormat(data[3]); // replacing date format
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
