function Cs2dSpy.print(text)
	print("[Cs2dSpy] "..text)
end

function Cs2dSpy.error(text)
	error("[Cs2dSpy] "..text)
end

function Cs2dSpy.openLink(fileName)
	local fullFileName = Cs2dSpy.linkDirectory..fileName.."."..Cs2dSpy.linkFileType
	local file = io.open(fullFileName, "w")
	if (file == nil) then
		Cs2dSpy.error("Failed to open link (file: "..fullFileName..")")
	end
	return file
end

function Cs2dSpy.cs2dWeaponTypeCount()
	local id = 1000
	while (true) do
		id = id - 1
		if (itemtype(id,"name") ~= false) then
			return id
		end
	end
end