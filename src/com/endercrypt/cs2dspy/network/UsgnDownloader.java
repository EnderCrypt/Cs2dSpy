package com.endercrypt.cs2dspy.network;

import java.io.IOException;
import java.util.function.Consumer;

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
public class UsgnDownloader implements Runnable
{
	private int usgn;
	private Consumer<UsgnInfo> result;

	public UsgnDownloader(int usgn, Consumer<UsgnInfo> result)
	{
		this.usgn = usgn;
		this.result = result;
	}

	@Override
	public void run()
	{
		Connection connection = Jsoup.connect("http://www.unrealsoftware.de/inc_pub/userinfo.php?id=" + usgn);
		connection.method(Connection.Method.GET);

		Document document;
		try
		{
			Response response = connection.execute();
			document = response.parse();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		result.accept(new UsgnInfo(document));
	}

}
