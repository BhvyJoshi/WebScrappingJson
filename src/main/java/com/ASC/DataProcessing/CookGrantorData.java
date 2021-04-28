package com.ASC.DataProcessing;

import com.ASC.HeaderProcessing.Cook;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CookGrantorData extends Cook {

    public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static final String subTableXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private final static String subTableNextButton = "//*[@id=\"DocList1_LinkButtonNext\"]";
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";

    public JSONObject getGrantorGranteeData(WebDriver driver, int row){

        //driver.navigate().refresh();
        //driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //new WebDriverWait(driver,10 ).until(
          //      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        int GranteeCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[4]")).getText());
        int GrantorCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[3]")).getText());

        JSONObject grantor= new JSONObject();
        JSONObject grantee = new JSONObject();

        String clickBtn;

        if(GrantorCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl02\"]";
            }
            else{
               clickBtn =  "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl02\"]";
            }
            grantor.put("records",getChildObjects(driver, clickBtn));
        }

        if(GranteeCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl03\"]";
            }
            else{
                clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl03\"]";
            }
            grantee.put("records",getChildObjects(driver, clickBtn));
        }
        return new JSONObject().put("Grantor_Count__r",grantor).put("Grantee_Count__r",grantee);
    }

    private JSONArray getChildObjects(WebDriver driver, String clickBtn) {
        JSONArray dataRecords;
        try {
            //driver.navigate().refresh();
            driver.findElement(By.xpath(clickBtn)).click();
            new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(clickBtn))));
            driver.findElement(By.xpath(clickBtn)).click();
            Thread.sleep(1000);

        }catch(Exception e)
            {e.printStackTrace();}
        dataRecords = subTableData(driver);
        try{
            driver.findElement(By.xpath(groupListButton)).click();
            Thread.sleep(2000);
        }catch(Exception e){e.printStackTrace();}

        return dataRecords;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public String getSubTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public JSONArray subTableData(WebDriver driver) {
        String[] headers = grabHeader(driver,subHeaderXpath,0);

        JSONArray tableDataContent;
        tableDataContent = grabSubTable(driver, headers);

        boolean checkNext = true;

        while (checkNext) {
            try {
                Thread.sleep(1000);
                driver.navigate().refresh();
                driver.findElement(By.xpath(subTableNextButton)).click();
                Thread.sleep(1000);
                tableDataContent = appendToList(tableDataContent, grabSubTable(driver, headers));
            } catch (Exception e1) {
                checkNext = false;
            }
        }
        return tableDataContent;
    }

    public JSONArray grabSubTable(WebDriver driver,String[] subHeader){
        JSONArray objForSubPage = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject subAttributes = new JSONObject();

        WebElement subTable =  driver.findElement(By.xpath(subTableXpath));
        int subRowSize = subTable.findElements(By.tagName("tr")).size();

        for (int subRowCount=1;subRowCount<=subRowSize;subRowCount++)
        { System.out.println("---------------subTable row no --->"+subRowCount);
           WebElement subRow = driver.findElement(By.xpath(getSubTableRow(subRowCount)));
           List<WebElement>   subCols = subRow.findElements(By.tagName("td"));

            for (int subColumn = 0, subHdr = 0; (subColumn < subCols.size()); subColumn++, subHdr++) {
                if (subColumn != 0) {
                    objForSubRow.put(subHeader[subHdr - 1], subCols.get(subColumn).getText());
                    while(subHdr-1 == 4){
                        try {
                            objForSubRow.put(subHeader[subHdr-1],generateDate(subCols.get(subColumn).getText()));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            subAttributes.put("type", "Grantor_Grantee_Records__c");
            subAttributes.put("referenceId","ref"+subRowCount+"_"+new Random().nextInt(100000));
            objForSubRow.put("attributes",subAttributes);
            objForSubPage.put(objForSubRow);
            objForSubRow = new JSONObject();
            subAttributes = new JSONObject();
        }
        return objForSubPage;
    }

}
