Cs2dSpy.print("Writting weapon data...")

function Cs2dSpy.linkTransfer.weapons()
	-- open link
	local link = Cs2dSpy.openLink("Weapons")
	-- relative cs2d path
	link:write(Cs2dSpy.relativeJarCs2dDirectory.."gfx/weapons/\n")
	-- write weapons
	local weaponTypeCount = Cs2dSpy.cs2dWeaponTypeCount()
	link:write(weaponTypeCount.."\n")
	for i=1,weaponTypeCount,1 do
		local name = itemtype(i,"name")
		if (name == false) then
			link:write("\n")
		else
			link:write(name.."\n")
		end
	end
	-- close
	link:close()
end