function Cs2dSpy.linkTransfer.map()
	-- open link
	local link = Cs2dSpy.openLink("Map")
	-- write tileset name
	link:write(map("tileset").."\n")
	-- write map
	local xsize = map("xsize")
	link:write(xsize.."\n")
	local ysize = map("ysize")
	link:write(ysize.."\n")
	for x=0, xsize-1, 1 do
		for y=0, ysize-1, 1 do
			link:write(tile(x,y,"frame").."\n")
		end
	end
	-- close
	link:close()
end