package camera;

import java.io.IOException;
import java.sql.Timestamp;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class MoveCamera {

	public static void main(String[] args) throws InterruptedException {

		String userName = "";
		String password = "";
		String host = "";
		String port = "";
		String sleep = "";
		String[] commands = null;
		HttpResponse response;
		CredentialsProvider provider = new BasicCredentialsProvider();
		java.util.Date date= new java.util.Date();
		try {
			userName = args[0];
			password = args[1];
			host = args[2];
			port = args[3];
			sleep = args[4];
			commands = args[5].split(",");
		} catch (Exception exc) {
			System.out.println("parameters: user password host port sleep_seconds commands_separated_coma");
		}

		while (true) {
			
			for (String command : commands) {
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
				provider.setCredentials(AuthScope.ANY, credentials);
				RequestConfig.Builder requestBuilder = RequestConfig.custom();
				requestBuilder = requestBuilder.setConnectTimeout(10000);
				requestBuilder = requestBuilder.setConnectionRequestTimeout(10000);
				HttpClientBuilder builder = HttpClientBuilder.create();
				builder.setDefaultRequestConfig(requestBuilder.build());
				builder.setDefaultCredentialsProvider(provider);
				HttpClient client = builder.build();

				try {
					
					 System.out.println();
					System.out.println(new Timestamp(date.getTime()) + ": before request: " + "http://" + host + ":" + port + "/decoder_control.cgi?command=" + command);
					response = client.execute(new HttpGet("http://" + host + ":" + port + "/decoder_control.cgi?command=" + command));
					int statusCode = response.getStatusLine().getStatusCode();
					System.out.println(new Timestamp(date.getTime()) + " after request status - " + statusCode);
					Thread.sleep(Integer.valueOf(sleep));
				} catch (Exception exc) {
					exc.getMessage();
					System.out.println("Will try again ...");
				}

			}
		}
	}

}
