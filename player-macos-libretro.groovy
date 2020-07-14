args = makeEnv("macos")

args << [
  label: 'macos',
  make: ['check', 'install'],
  buildtype: 'Release',
  cmake_args: '-DPLAYER_TARGET_PLATFORM=libretro -DBUILD_SHARED_LIBS=ON',
  pre: 'git submodule update --init'
]

args["artifacts_cmd"] = """
zip -9 ../easyrpg-libretro.dylib.zip easyrpg-libretro.dylib
"""

args["artifacts"] = ["easyrpg-libretro.dylib.zip"]

buildPlayerCmake(args)
