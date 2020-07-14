args = makeEnv("linux")

args << [
  label: 'linux',
  make: ['check', 'install'],
  buildtype: 'Release',
  cmake_args: '-DPLAYER_TARGET_PLATFORM=libretro -DBUILD_SHARED_LIBS=ON',
  pre: 'git submodule update --init'
]

args["artifacts_cmd"] = """
zip -9 ../easyrpg-libretro.so.zip easyrpg-libretro.so
"""

args["artifacts"] = ["easyrpg-libretro.so.zip"]

buildPlayerCmake(args)
