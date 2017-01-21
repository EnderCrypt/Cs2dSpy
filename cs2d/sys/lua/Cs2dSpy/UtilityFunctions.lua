function Cs2dSpy.print(text)
	print("[Cs2dSpy] "..text)
end

function Cs2dSpy.error(text)
	error("[Cs2dSpy] "..text)
end

function Cs2dSpy.openLink(fileName)
	local fullFileName = Cs2dSpy.AccessFolder..fileName.."."..Cs2dSpy.LinkExtension
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

function Cs2dSpy.split(text,separator)
    local ar = {}
    local last = -1
    local i = 0
    while (i < #text) do
         i = i + 1
         c = text:sub(i,i)
         if (c == separator) then
            ar[#ar+1] = text:sub(last+1,i-1)
            last = i
         end
    end
    ar[#ar+1] = text:sub(last+1,#text)
    return ar
end

function Cs2dSpy.trim(text)
	-- start
	local i = 0
	while (true) do
		i = i + 1
		if ( i > string.len(text) ) then
			break
		end
		if ( string.sub(text, i, i) ~= " " ) then
			break
		end
	end
	text = string.sub(text, i, string.len(text))

	-- end
	local i = string.len(text) + 1
	while (true) do
		i = i - 1
		if (i < 1) then
			break
		end
		if ( string.sub(text, i, i) ~= " " ) then
			break
		end
	end
	text = string.sub(text, 1, i)

	-- done
	return text
end