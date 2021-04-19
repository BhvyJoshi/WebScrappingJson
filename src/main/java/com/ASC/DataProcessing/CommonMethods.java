package com.ASC.DataProcessing;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommonMethods {

    public JSONArray appendToList(JSONArray original, JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }

    public void generateFile(String fileName, JSONArray tableDataContent)
    {
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
}
