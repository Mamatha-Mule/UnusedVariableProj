package WMProject.Variables;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;
import org.unix4j.unix.Grep;

public class searchPatternClass {

	public JSONArray searchVarInFiles(File htmlFile,File JSFile,JSONArray varArr) {

		JSONArray unUnsedVarArr = new JSONArray();
		List<Line> lines = null;
		if(varArr.length()>0) {

			for(int i=0; i<varArr.length();i++) {

				try {
					JSONObject obj = varArr.getJSONObject(i);
					String searchKey = "";

					if(obj.getString("category").equalsIgnoreCase("wm.NotificationVariable") || 
							obj.getString("category").equalsIgnoreCase("wm.NavigationVariable") ||
							obj.getString("category").equalsIgnoreCase("wm.TimerVariable") ||
							obj.getString("category").equalsIgnoreCase("wm.LogoutVariable") ||
							obj.getString("category").equalsIgnoreCase("wm.LoginVariable")) {

						searchKey = "Actions."+obj.getString("Variable Name");
					}else {
						searchKey = "Variables."+obj.getString("Variable Name");
					}
					//String regrex = "(?<!^[\\p{Zs}\\t]*//.*)(?<!/\\*(?:(?!\\*/)[\\s\\S\\r])\\*?)\\b"+searchKey+"\\b";
					
					if(JSFile!= null && JSFile.exists())
						lines = Unix4j.grep(searchKey, JSFile).toLineList();
                        //System.out.println(lines);
					if(lines.isEmpty()|| lines==null) {
						
						 if(htmlFile != null && htmlFile.exists()) 
						lines = Unix4j.grep(searchKey,htmlFile).toLineList();
						 

						if(lines.isEmpty()|| lines==null)
							unUnsedVarArr.put(obj);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 			
			}

		}
		return unUnsedVarArr;
	}

}
