args = makeEnvLibretro("win32")

args << [
  label: 'windows',
  make: ['check', 'install'],
  buildtype: 'Release',
]

args["artifacts_cmd"] = """
7z a -mx9 ../easyrpg-libretro.dll.zip easyrpg-libretro.dll.zip
"""

args["artifacts"] = ["easyrpg-libretro.dll.zip"]

buildPlayerCmakeWindows(args)
