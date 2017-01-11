package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;


//HTTP request 를 보내기 위한 class
public class HttpConnection {
	
	static final String ipaddr = "143.248.56.220:8080";
	
	// HTTP GET request
	public static void getFoods() throws Exception {

		String url = "http://" + ipaddr + "/api/foods";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}

	// HTTP POST request
	public static void postFood(int position, String file_name) throws Exception {

		String url = "http://" + ipaddr + "/api/foods";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setDoOutput(true);
	    con.setDoInput(true);
	    con.setRequestProperty("Content-Type", "application/json");
	    con.setRequestProperty("Accept", "*/*");
	    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	
		//make JSON
	    JSONObject data = new JSONObject();
	    data.put("file_name", file_name);
	    data.put("position", String.valueOf(position));

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	    wr.writeBytes(data.toString());
	    wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + data.toString());
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}

	//HTTP PUT request
	public static void putFoodPosition(int from_section, int to_section) throws Exception {

		String url = "http://" + ipaddr + "/api/foods/position/" + String.valueOf(from_section);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is PUT
		con.setRequestMethod("PUT");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		//make JSON
	    JSONObject data = new JSONObject();
	    data.put("position", String.valueOf(to_section));

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	    wr.writeBytes(data.toString());
	    wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("PUT parameters : " + data.toString());
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		
	}
	
	//HTTP DELETE request
	public static void deleteFoodPosition(int from_section) throws Exception {

		String url = "http://" + ipaddr + "/api/foods/position/" + String.valueOf(from_section);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is DELETE
		con.setRequestMethod("DELETE");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		
	}
	
	
}
