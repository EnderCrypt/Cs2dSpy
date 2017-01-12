Cs2dSpy.print("Writting map data...")

local link = Cs2dSpy.openLink("Map")
link:write(Cs2dSpy.relativeJarCs2dDirectory.."gfx/tiles/"..map("tileset").."\n")
local xsize = map("xsize")
link:write(xsize.."\n")
local ysize = map("ysize")
link:write(ysize.."\n")
for x=0, xsize-1, 1 do
	for y=0, ysize-1, 1 do
		link:write(tile(x,y,"frame").."\n")
	end
end
link:close()