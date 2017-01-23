Cs2dSpy = {}
Cs2dSpy.baseDirectory = "sys/lua/Cs2dSpy/"

dofile(Cs2dSpy.baseDirectory.."UtilityFunctions.lua")
dofile(Cs2dSpy.baseDirectory.."Cs2dSpySettings.lua")

Cs2dSpy.AccessFolder = Cs2dSpy.baseDirectory..Cs2dSpy.settings.getString("Directory.AccessFolder").."/"
Cs2dSpy.LinkExtension = Cs2dSpy.settings.getString("Directory.LinkExtension")
Cs2dSpy.relativeCs2dDirectory = "../../../" -- relative directory to cs2d main folder from the Cs2dSpy directory (opposite of Cs2dSpy.baseDirectory)
Cs2dSpy.updateFrequency = Cs2dSpy.settings.getNumber("Cs2d.UpdateFrequency") -- every X frame
Cs2dSpy.chatBufferSize = Cs2dSpy.settings.getNumber("Cs2d.ChatSize") -- number of chat messages to keep in link

Cs2dSpy.linkTransfer = {}
dofile(Cs2dSpy.baseDirectory.."LinkTypes/Map.lua")
dofile(Cs2dSpy.baseDirectory.."LinkTypes/Weapons.lua")
dofile(Cs2dSpy.baseDirectory.."LinkTypes/Info.lua")
dofile(Cs2dSpy.baseDirectory.."LinkTypes/RealTime.lua")

Cs2dSpy.linkTransfer.map()
Cs2dSpy.linkTransfer.weapons()
Cs2dSpy.linkTransfer.info()

addhook("always","Cs2dSpy.always")
Cs2dSpy.frameNumber = 0
function Cs2dSpy.always()
	if (Cs2dSpy.frameNumber % Cs2dSpy.updateFrequency == 0) then
		Cs2dSpy.linkTransfer.realtime()
	end
	Cs2dSpy.frameNumber = Cs2dSpy.frameNumber + 1
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

Cs2dSpy.print("Spy active!")