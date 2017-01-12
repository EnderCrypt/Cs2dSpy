function Cs2dSpy.print(text)
	print("[Cs2dSpy] "..text)
end

function Cs2dSpy.openLink(fileName)
	return io.open(Cs2dSpy.linkDirectory..fileName.."."..Cs2dSpy.linkFileType, "w")
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