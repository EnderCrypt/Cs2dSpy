# Cs2dSpy

public download: http://www.unrealsoftware.de/files_show.php?file=17266

Cs2dSpy is a utility software (open source!)

Its made it for Cs2d Server owners, allowing them to view whats going on in server even if they arent in it (dedicated servers)

requirements:
- Cs2d server that runs lua
- Java SE Runtime Environment 8 (JavaSE-1.8)


Installation instructions:

1. extract Cs2dSpy into "sys/lua/" (so that you'll have sys/lua/Cs2dSpy/*files*)

2. edit sys/lua/server.lua

3. write in [code]dofile("sys/lua/Cs2dSpy/Cs2dSpy.lua")[/code]

4. start the game server, either using the normal cs2d client or by using a dedicated server

5. once the server has started, launch the jar (by double clicking, or right click open with > java)


Known issues:
if you started the server, but still get "unable to reach server error message"

try launch the bat instead of the jar


regarding bugs:

if you have encountered any bugs OR have ideas

feel free to suggest them to me as an issue


Sources & License included in the main download from USGN, as well as here in github, if you want to compile yourself and have issues, feel free to pm me

Sources & License included

License:  GNU General Public License Version 3

Tested on Mac OS and Windows 10
