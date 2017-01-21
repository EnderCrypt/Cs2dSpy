package com.endercrypt.cs2dspy.network.update;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class UpdateCheckThread implements Callable<VersionNumber>
{
	private static final String USGN_FILE_URL = "http://www.unrealsoftware.de/files_show.php?file=17266";

	@Override
	public VersionNumber call() throws Exception
	{
		Connection connection = Jsoup.connect(USGN_FILE_URL);
		connection.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.85 Safari/537.36");
		connection.method(Connection.Method.GET);

		Response response = connection.execute();
		int statusCode = response.statusCode();
		if (statusCode != 200)
		{
			String statusMessage = response.statusMessage();
			throw new IOException("Unexpected HTTP status code: " + statusCode + " (" + statusMessage + ")");
		}
		Document document = response.parse();
		return new VersionNumber(document);
	}
}
