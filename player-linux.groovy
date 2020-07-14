args = makeEnv("linux")

args << [
  label: 'linux',
  make: ['clean', 'check', 'install', 'dist'],
  
  CXXFLAGS: '-Wall -Wextra -O0 -g3'
]

// separate debug information
args["post"] = """
cd build
cp -up bin/easyrpg-player easyrpg-player
objcopy --only-keep-debug easyrpg-player easyrpg-player.debug
strip --strip-debug --strip-unneeded easyrpg-player
objcopy --add-gnu-debuglink=easyrpg-player.debug easyrpg-player
"""

// binary dist
args["artifacts_cmd"] = """
tar czf ../easyrpg-player-static.tar.gz easyrpg-player
tar czf ../easyrpg-player-static_dbgsym.tar.gz easyrpg-player.debug
"""

args["artifacts"] = ["easyrpg-player-*.tar.*z"]

buildPlayerAutotools(args)
