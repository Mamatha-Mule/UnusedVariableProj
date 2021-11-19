package WMProject.Variables;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import WMProject.Variables.searchPatternClass;

public class UnusedVariableClass {

	//change the file path with your local file path
	public static String filePath = "/home/mamatham/Downloads/sampleProj_1.0/src/main/webapp";
	public static void main(String[] args) throws Exception {

		UnusedVariableClass unUsedVarObj = new UnusedVariableClass();
		JSONArray finalArray = unUsedVarObj.getUnusedVariables();
		System.out.println(finalArray);
	}

	private JSONArray getAppVarWithOutBindCnt(File appVarJsonFile) {

		JSONArray jsonArr = null;
		ReadJsonClass readJsonObj = new ReadJsonClass();
		if (appVarJsonFile.isFile() && appVarJsonFile.getName().equalsIgnoreCase("app.variables.json"))
			jsonArr = readJsonObj.readJSONFile(appVarJsonFile,"App");
		return jsonArr;
	}

	private JSONArray getFinalAppVar() {

		searchPatternClass searchPatternObj= new searchPatternClass();
		File getAppVarJsonFile = new File(filePath+"/app.variables.json");
		JSONArray appWithOutBindCnt = getAppVarWithOutBindCnt(getAppVarJsonFile);
		File appJSFile = new File(filePath+"/app.js");
		return searchPatternObj.searchVarInFiles(null, appJSFile, appWithOutBindCnt);		
	}

	public JSONArray getUnusedVariables() throws Exception {

		JSONArray applvlVar = getFinalAppVar();
		JSONArray resultJSONArray = new JSONArray();
		ReadJsonClass readJsonObj = new ReadJsonClass();
		searchPatternClass searchPatternObj= new searchPatternClass();

		File folder = new File(filePath+"/pages");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			if (!file.isFile()) {
				String pageName= file.getName();
				File pageLvlFile = new File(filePath+"/pages/"+file.getName()+"/"+file.getName()+".variables.json");

				JSONArray jsonArr = readJsonObj.readJSONFile(pageLvlFile,pageName);

				if(jsonArr.length()>0) {

					File pageLvlJSFile = new File(filePath+"/pages/"+file.getName()+"/"+file.getName()+".js");
					File pageLvlHtmlFile = new File(filePath+"/pages/"+file.getName()+"/"+file.getName()+".html");
					JSONArray results = searchPatternObj.searchVarInFiles(pageLvlHtmlFile, pageLvlJSFile, jsonArr);
					finalJsonCombine(results);
					applvlVar = searchPatternObj.searchVarInFiles(pageLvlHtmlFile, pageLvlJSFile, applvlVar);
				}
			}

		}
		return finalJsonCombine(applvlVar);

	}

	JSONArray finalUnusedVariable = new JSONArray();
	private JSONArray finalJsonCombine(JSONArray jsonArr) throws JSONException {

		if(jsonArr.length()>0) {
			for(int i=0; i<jsonArr.length();i++) {
				JSONObject obj = jsonArr.getJSONObject(i);
				finalUnusedVariable.put(obj);
			}
		}		
		return finalUnusedVariable;
	}

}	
