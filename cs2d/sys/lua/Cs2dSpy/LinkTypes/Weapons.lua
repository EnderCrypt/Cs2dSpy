function Cs2dSpy.linkTransfer.weapons()
	-- open link
	local link = Cs2dSpy.openLink("Weapons")
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