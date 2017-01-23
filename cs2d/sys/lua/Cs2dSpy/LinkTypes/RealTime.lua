function Cs2dSpy.linkTransfer.realtime()
	-- open link
	local link = Cs2dSpy.openLink("Realtime")
	-- chat
	Cs2dSpy.writeChat(link)
	-- players
	local players = player(0,"tableliving")
	link:write(#players.."\n")
	for i, id in ipairs(players) do
		Cs2dSpy.writePlayer(link, id)
	end
	-- close
	link:close()
end

function Cs2dSpy.writePlayer(link, id)
	link:write(id.."\n")
	Cs2dSpy.writePlayerData(link, id, "bot")
	Cs2dSpy.writePlayerData(link, id, "name")
	Cs2dSpy.writePlayerData(link, id, "ip")
	Cs2dSpy.writePlayerData(link, id, "port")
	Cs2dSpy.writePlayerData(link, id, "ping")
	Cs2dSpy.writePlayerData(link, id, "usgn")
	Cs2dSpy.writePlayerData(link, id, "x")
	Cs2dSpy.writePlayerData(link, id, "y")
	Cs2dSpy.writePlayerData(link, id, "rot")
	Cs2dSpy.writePlayerData(link, id, "maxhealth")
	Cs2dSpy.writePlayerData(link, id, "health")
	if (game("sv_gamemode") == "1") and (player(id,"team") ~= 0) then
		link:write("4\n") -- pvp team
	else
		Cs2dSpy.writePlayerData(link, id, "team")
	end
	Cs2dSpy.writePlayerData(link, id, "money")
	Cs2dSpy.writePlayerData(link, id, "score")
	Cs2dSpy.writePlayerData(link, id, "deaths")
	Cs2dSpy.writePlayerData(link, id, "weapontype")
end

function Cs2dSpy.writePlayerData(link, id, type)
	link:write(tostring(player(id,type)).."\n")
end