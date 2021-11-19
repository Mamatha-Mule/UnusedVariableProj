package WMProject.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReadJsonClass {

	public JSONArray readJSONFile(File variableJsonFile,String pageName){
		JSONArray unUsedVariableArry = new JSONArray();

		try {
			if (variableJsonFile.exists()){

				InputStream inputStream = new FileInputStream(variableJsonFile);
				String jsonTxt = IOUtils.toString(inputStream, "UTF-8");
				JSONObject json = new JSONObject(jsonTxt); 

				if(json.length() >0) {

					Iterator<String> keys = json.keys();
					while(keys.hasNext()) {

						String objName = keys.next();
						JSONObject jsonObj= json.getJSONObject(objName);
						if(!jsonObj.has("bindCount")) {

							JSONObject unUsedVarObj = new JSONObject();
							unUsedVarObj.put("Page Name", pageName);
							unUsedVarObj.put("Variable Name", jsonObj.get("name"));
							unUsedVarObj.put("category", jsonObj.get("category"));
							unUsedVarObj.put("owner", jsonObj.get("owner"));
							unUsedVariableArry.put(unUsedVarObj);
						}
					}
				} 
			} 
		}catch(Exception e) {
			System.out.println("Exception while reading the json"+e);
		}
		return unUsedVariableArry;
	}
}
