args = makeEnv("win32")

args << [
  label: 'windows',
  make: ['check', 'install'],
  buildtype: 'Release',
  cmake_args: '-DPLAYER_TARGET_PLATFORM=libretro -DBUILD_SHARED_LIBS=ON',
  pre: 'git submodule update --init'
]

args["artifacts_cmd"] = """
7z a -mx9 ../easyrpg-libretro.dll.zip easyrpg-libretro.dll.zip
"""

args["artifacts"] = ["easyrpg-libretro.dll.zip"]

buildPlayerCmakeWindows(args)
