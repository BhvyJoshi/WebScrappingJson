package com.ASC.Common;

import com.ASC.DataProcessingIndividual.CookGrantorData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CookCountyHelper extends CookGrantorData {

    private static final String mainTableNextButtonPath = "//*[@id=\"NameList1_LinkButtonNext\"]";
    private static final String mainTablePath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private static final String textBoxId = "SearchFormEx1_ACSTextBox_LastName1";
    private static final String mainHeaderPath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";

    public void firstPage(WebDriver driver,String value){
        driver.findElement(By.id(textBoxId)).sendKeys(value);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_btnSearch\"]")).click();
    }

    public void tableData(WebDriver driver,String fileName,String request)
    {
        //String[] headers = grabHeader(driver,mainHeaderPath);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,grabHeader(driver,mainHeaderPath),request);

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(mainTableNextButtonPath));
                nextBtn.click();
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,grabHeader(driver,mainHeaderPath),request));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
        generateFile(fileName, tableDataContent.toString());
    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        JSONObject attributes = new JSONObject();


        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()); column++, hdr++) {
                objForRow.put(header[hdr], cols.get(column).getText());
                }

            attributes.put("type", "Main_Page"); //will be changed
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("GranteeCount_Result__r",getGrantorGranteeData(driver,rowCount));
            objForRow.put("Lead_Search__c",request);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            attributes = new JSONObject();
        }
        return objForPage;
    }

    public void generateFile(String fileName, String tableDataContent)
    {
        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(tableDataContent);
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
