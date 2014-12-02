import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

		String token1 = reverseString(strToReverse);
		String[] key2 = {"token", "string"};
		String[] values2 = {token, token1};
		JSONObject needleInAHaystack = sendRequest(key2, values2, "validatestring");
		
		

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


}
