Cs2dSpy = {}
Cs2dSpy.baseDirectory = "sys/lua/Cs2dSpy/"
Cs2dSpy.linkDirectory = Cs2dSpy.baseDirectory.."Access/"
Cs2dSpy.linkFileType = "link"
Cs2dSpy.relativeJarCs2dDirectory = "../../../" -- relative directory to cs2d main folder from the jar (opposite of Cs2dSpy.baseDirectory)
Cs2dSpy.updateFrequency = 5 -- every X frame
Cs2dSpy.chatBufferSize = 10 -- number of chat messages to keep in link

dofile(Cs2dSpy.baseDirectory.."UtilityFunctions.lua")

dofile(Cs2dSpy.baseDirectory.."Cs2dSpyMapReporter.lua")
dofile(Cs2dSpy.baseDirectory.."Cs2dSpyWeaponReporter.lua")

addhook("always","Cs2dSpy.always")
Cs2dSpy.frameNumber = 0
function Cs2dSpy.always()
	if (Cs2dSpy.frameNumber % Cs2dSpy.updateFrequency == 0) then
		Cs2dSpy.reportLiveInfo()
	end
	Cs2dSpy.frameNumber = Cs2dSpy.frameNumber + 1
end

function Cs2dSpy.reportLiveInfo()
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
	link:close()
end

Cs2dSpy.chatBuffer = {}
function Cs2dSpy.writeChat(link)
	link:write(#Cs2dSpy.chatBuffer.."\n")
	for index, value in ipairs(Cs2dSpy.chatBuffer) do
		if ( (game("sv_gamemode") == "1") and (value.team ~= 0) ) then
			link:write("4\n") -- pvp team
		else
			link:write(value.team.."\n")
		end
		link:write(value.name.."\n")
		link:write(value.text.."\n")
	end
end

addhook("say","Cs2dSpy.say")
function Cs2dSpy.say(id, text)
	table.insert(Cs2dSpy.chatBuffer, 1, {team=tostring(player(id,"team")), name=player(id,"name"), text=text})
	-- remove old messages
	for index, value in ipairs(Cs2dSpy.chatBuffer) do
		if (index > Cs2dSpy.chatBufferSize) then
			Cs2dSpy.chatBuffer[index] = nil
		end
	end
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

Cs2dSpy.print("Spy active!")