package com.endercrypt.cs2dspy.network.update;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

/**
 *	This file is part of Cs2dSpy and was created by Magnus Gunnarsson
 *
 *	Copyright (C) 2017  Magnus Gunnarsson (EnderCrypt)
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
