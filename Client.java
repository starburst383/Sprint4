import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedList;

import com.google.gson.Gson;

public class Client {
	static Gson g = new Gson();
	HttpURLConnection conn;
	
	public void add(ResultRacer output) {
		try {

			URL site = new URL("http://localhost:8000/add");
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			
			String content = g.toJson(output);
			
			out.writeBytes(content);
			out.flush();
			out.close();
			
			InputStreamReader inputStr = new InputStreamReader(conn.getInputStream());

			// string to hold the result of reading in the response
			StringBuilder sb = new StringBuilder();

			// read the characters from the request byte by byte and build up
			// the Response
			int nextChar;
			while ((nextChar = inputStr.read()) > -1) {
				sb = sb.append((char) nextChar);
			}
			//System.out.println(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
