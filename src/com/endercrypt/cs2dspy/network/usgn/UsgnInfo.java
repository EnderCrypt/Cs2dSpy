package com.endercrypt.cs2dspy.network.usgn;

import java.util.function.Consumer;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
public class UsgnInfo
{
	private String name;
	private String prefferedLanguage;
	private int daysRegistered;

	public UsgnInfo(Document document)
	{
		name = document.select("h2").first().text().trim();

		Elements imgCountryElement = document.select("h2").first().select("img");
		if (imgCountryElement.size() > 0)
			prefferedLanguage = imgCountryElement.get(0).attr("title");
		else
			prefferedLanguage = "Unspecified";

		daysRegistered = Integer.parseInt(document.select("span").last().html().split("<br>")[1].split(" ")[2].replaceAll(",", ""));
	}

	public void addInfo(Consumer<String> infoConsumer)
	{
		//infoConsumer.accept("{");
		infoConsumer.accept("┣ Username: " + name);
		infoConsumer.accept("┣ Country: " + prefferedLanguage);
		infoConsumer.accept("┗ Days Registered: " + daysRegistered);
		//infoConsumer.accept("}");
	}
}
