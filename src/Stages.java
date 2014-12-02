import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;




public class Stages {

	//keys = {email, github}
	// values = {asdfasd@gmail.com, http:///sdfasfd}




	public static void main(String [] args) throws IOException{
		// Gets original token DFAD$5435
		String[] keys = {"email", "github"};
		String[] values = {"wbrown4@stanford.edu", "https://github.com/wbrown4/CODE2040-Challenge"};
		JSONObject tokenJSON = sendRequest(keys, values, "register");
		String token = tokenJSON.getString("result");

		// Gets string you need to reverse
		String[] key1 = {"token"};
		String[] values1 = {token};

		JSONObject strToReversJSON = sendRequest(key1, values1, "getstring");
		String strToReverse = strToReversJSON.getString("result");

		String finishedReversedStr = reverseString(strToReverse);
		String[] key2 = {"token", "string"};
		String[] values2 = {token, finishedReversedStr};
		JSONObject validateString = sendRequest(key2, values2, "validatestring");
		JSONObject needleInAHaystack = sendRequest(key1, values1, "haystack");
		
		JSONObject needleHayJSON = needleInAHaystack.getJSONObject("result");
		
		String needle = needleHayJSON.getString("needle");
		JSONArray haystack = needleHayJSON.getJSONArray("haystack");
		ArrayList<String> fields = new ArrayList<String>();
		for(int i = 0; i < haystack.length(); i++) {
			fields.add((String) haystack.get(i));
		}
		int needlePosition = findNeedleInAHaystack(needle, fields);
		String[] key3 = {"token", "needle"};
		String needleAsString = "" + needlePosition;
		String[] values3 = {token, needleAsString};
		JSONObject result = sendRequest(key3, values3, "validateneedle");
		JSONObject prefixRequest = sendRequest(key1, values1, "prefix");
		
		JSONObject prefixJSONObject = prefixRequest.getJSONObject("result");
		String prefix = prefixJSONObject.getString("prefix");
		JSONArray prefixList = prefixJSONObject.getJSONArray("array");
		
		ArrayList<String> listOfWords = new ArrayList<String>();
		for(int i = 0; i < prefixList.length(); i++) {
			listOfWords.add((String) prefixList.get(i));
		}
		ArrayList<String> newWordList = findWordWithPrefix(prefix, listOfWords);
		String[] key4 = {"token" , "array"};
		sendRequestArray(key4, token, newWordList, "validateprefix");


	}

	public static JSONObject sendRequest(String [] keys, String[] values, String urlEnding) throws IOException {
		String url= "http://challenge.code2040.org/api/" + urlEnding;

		URL object = new URL(url);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();

		con.setDoOutput(true);

		con.setDoInput(true);

		con.setRequestProperty("Content-Type", "application/json; charset=utf8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");		
		con.connect();

		JSONObject cred = new JSONObject();
		for(int i = 0; i < keys.length; i++) {
			cred.put(keys[i], values[i]);
		}

		OutputStream os = con.getOutputStream();
		os.write(cred.toString().getBytes("UTF-8"));
		os.close();

		StringBuilder sb = new StringBuilder();  

		int HttpResult = con.getResponseCode(); 

		if(HttpResult == HttpURLConnection.HTTP_OK){

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  

			String line = null;  

			while ((line = br.readLine()) != null) {  
				sb.append(line + "\n");  
			}  

			br.close();  

			String str = ""+sb.toString(); 
			JSONObject result = new JSONObject(str);

			return result;
			// Take this string and then convert it to JSON object
			//JSONObejct

		}else{
			System.out.println(con.getResponseMessage());  
			return null;
		} 

	}

	private static String reverseString (String string){
		String reversedString = "";
		for(int i = string.length() - 1; i >= 0 ; i--){
			char ch = string.charAt(i);
			reversedString += ch;
		}
		return reversedString;
	}
	
	private static int findNeedleInAHaystack(String needle, ArrayList<String> fields){
		for(int i = 0; i < fields.size(); i++){
			if(needle.equals(fields.get(i))) return i;
		}
		return -1;
	}
	
	private static ArrayList findWordWithPrefix(String prefix, ArrayList<String> listOfWords){
		for(int i = 0; i<listOfWords.size(); i++){
			String currentWord = listOfWords.get(i);
			String currentWordPrefix = "";
			for(int j = 0; j < prefix.length(); j++){
				char ch = currentWord.charAt(j);
				currentWordPrefix += ch;
			}
			if(prefix.equals(currentWordPrefix)) listOfWords.remove(i);
		}
		return listOfWords;
	}
	
	public static JSONObject sendRequestArray(String [] keys, String token, ArrayList <String> array, String urlEnding) throws IOException {
		String url= "http://challenge.code2040.org/api/" + urlEnding;

		URL object = new URL(url);

		HttpURLConnection con = (HttpURLConnection) object.openConnection();

		con.setDoOutput(true);

		con.setDoInput(true);

		con.setRequestProperty("Content-Type", "application/json; charset=utf8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");		
		con.connect();

		JSONObject cred = new JSONObject();
		cred.put(keys[0], token);
		cred.put(keys[1], array);
		

		OutputStream os = con.getOutputStream();
		os.write(cred.toString().getBytes("UTF-8"));
		os.close();

		StringBuilder sb = new StringBuilder();  

		int HttpResult = con.getResponseCode(); 

		if(HttpResult == HttpURLConnection.HTTP_OK){

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  

			String line = null;  

			while ((line = br.readLine()) != null) {  
				sb.append(line + "\n");  
			}  

			br.close();  

			String str = ""+sb.toString(); 
			JSONObject result = new JSONObject(str);

			return result;
			// Take this string and then convert it to JSON object
			//JSONObejct

		}else{
			System.out.println(con.getResponseMessage());  
			return null;
		} 

	}


}
