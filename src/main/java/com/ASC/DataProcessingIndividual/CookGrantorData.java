package com.ASC.DataProcessingIndividual;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CookGrantorData extends CookGrantorChild{

    public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static final String subTableXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private final static String subTableNextButton = "//*[@id=\"DocList1_LinkButtonNext\"]";
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";
    public JSONObject getGrantorGranteeData(WebDriver driver, int row){

        int GrantorCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[3]")).getText());
        int GranteeCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[4]")).getText());
        JSONArray grantor= null,grantee = null;
        String clickBtn;
        if(GrantorCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl02\"]";
            }
            else{
               clickBtn =  "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl02\"]";
            }
            driver.findElement(By.xpath(clickBtn)).click();
            grantor = tableData(driver);

            driver.findElement(By.xpath(groupListButton)).click();
        }

        if(GranteeCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl03\"]";
            }
            else{
                clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl03\"]";
            }
            driver.findElement(By.xpath(clickBtn)).click();
            grantee = tableData(driver);
            driver.findElement(By.xpath(groupListButton)).click();
        }

        return new JSONObject().put("grantor",grantor).put("grantee",grantee);
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public JSONArray tableData(WebDriver driver) {
        String[] headers = grabHeader(driver,subHeaderXpath,0);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver, headers);

        boolean checkNext = true;

        while (checkNext) {
            try {
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(subTableNextButton));
                nextBtn.click();
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent, grabData(driver, headers));
            } catch (Exception e1) {
                checkNext = false;
            }
        }
        return tableDataContent;
    }

    public JSONArray grabData(WebDriver driver,String[] subHeader)
    {
        JSONArray objForSubPage = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject subAttributes = new JSONObject();

        WebElement subTable =  driver.findElement(By.xpath(subTableXpath));
        int subRowSize = subTable.findElements(By.tagName("tr")).size();

        for (int subRowCount=1;subRowCount<=subRowSize;subRowCount++)
        {
            WebElement subRow = driver.findElement(By.xpath(getMainTableRow(subRowCount)));
            List<WebElement> subCols = subRow.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < subCols.size()); column++, hdr++) {
                if (column != 0) {
                    objForSubRow.put(subHeader[hdr - 1], subCols.get(column).getText());
                    while(hdr-1 == 4){
                        Date dob;
                        try {
                            dob = new SimpleDateFormat("MM/dd/yyyy").parse(subCols.get(column).getText());
                            String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                            objForSubRow.put(subHeader[hdr-1],str);
                            break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            subAttributes.put("type", "SecondPage"); //will be changed
            subAttributes.put("referenceId","ref"+subRowCount+"_"+new Random().nextInt(100000));
            objForSubRow.put("attributes",subAttributes);
            //objForRow.put("GranteeCount_Result__r",getGrantorGranteeChildData(driver,rowCount));

            objForSubPage.put(objForSubRow);
            objForSubRow = new JSONObject();
            subAttributes = new JSONObject();
        }
        return objForSubPage;
    }

}
