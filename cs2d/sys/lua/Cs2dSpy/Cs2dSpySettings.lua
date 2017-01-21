Cs2dSpy.settings = {}
Cs2dSpy.settings.file = Cs2dSpy.baseDirectory.."Settings.txt"
Cs2dSpy.settings.settings = {}

function Cs2dSpy.settings.processLine(line)
	if (string.sub(line, 1, 2) == "//") then
		return false
	end
	local split = Cs2dSpy.split(line,"=")
	local key = Cs2dSpy.trim(split[1])
	local value = Cs2dSpy.trim(split[2])
	Cs2dSpy.print("[Setting]"..key.." = "..value)
	Cs2dSpy.settings.settings[key] = value
	return true
end

local file = io.open(Cs2dSpy.settings.file,"r")
local line = file:read()
Cs2dSpy.print("Reading settings...")
while (line ~= nil) do
	if (line ~= "") then
		Cs2dSpy.settings.processLine(line)
	end
	line = file:read()
end

-- READ FUNCTIONS --

function Cs2dSpy.settings.getString(key)
	local value = Cs2dSpy.settings.settings[key]
	if (value == nil) then
		Cs2dSpy.error("Failed to read setting \""..key.."\" key does not exist")
	end
	return value
end

function Cs2dSpy.settings.getNumber(key)
	local value = tonumber(Cs2dSpy.settings.getString(key))
	if (value == nil) then
		Cs2dSpy.error("Failed to read (number) setting for [key: "..key.."] value: "+key)
	end
	return value
end

function Cs2dSpy.settings.getBoolean(key)
	local value = Cs2dSpy.settings.getString(key)
	if (value == "1") or (string.lower(value) == "true") then
		return true
	end
	if (value == "0") or (string.lower(value) == "false") then
		return true
	end
	Cs2dSpy.error("Failed to read (boolean) setting for [key: "..key.."] value: "+key)
end