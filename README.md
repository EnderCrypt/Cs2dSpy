# Cs2dSpy

public download: http://www.unrealsoftware.de/files_show.php?file=17266

Cs2dSpy is a utility software (open source!)

This is a software for Cs2d Server owners, allowing them to view whats going on in server even if they arent in it (dedicated servers)

requirements:
- Cs2d server that runs lua
- Java SE Runtime Environment 8 (JavaSE-1.8)


Installation instructions:<br>
1. extract Cs2dSpy into "sys/lua/" (so that you'll have sys/lua/Cs2dSpy/*files*)<br>
2. edit sys/lua/server.lua<br>
3. write in dofile("sys/lua/Cs2dSpy/Cs2dSpy.lua")<br>
4. start the game server, either using the normal cs2d client or by using a dedicated server<br>
5. once the server has started, launch the jar (by double clicking, or right click open with > java)

final note:<br>
always run the Cs2d server BEFORE running the Cs2dSpy client!

Known issues:
if you started the server, but still get "unable to reach server" error message<br>
try launch the bat instead of the jar

regarding bugs:<br>
if you have encountered any bugs OR have ideas<br>
feel free to suggest them to me as an issue

Sources & License included

License:  GNU General Public License Version 3<br>
if you want to compile yourself and have issues, feel free to pm me<br>

Tested on Mac OS and Windows 10/XP
