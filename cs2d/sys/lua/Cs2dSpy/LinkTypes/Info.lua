function Cs2dSpy.linkTransfer.info()
	-- open link
	local link = Cs2dSpy.openLink("Info")
	-- relative Cs2d directory
	link:write(Cs2dSpy.relativeCs2dDirectory)
	-- close
	link:close()
end