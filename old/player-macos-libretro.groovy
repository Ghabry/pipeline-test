args = makeEnvLibretro("macos")

args << [
  label: 'macos',
  make: ['check', 'install'],
  buildtype: 'Release',
]

args["artifacts_cmd"] = """
zip -9 ../easyrpg-libretro.dylib.zip easyrpg-libretro.dylib
"""

args["artifacts"] = ["easyrpg-libretro.dylib.zip"]

buildPlayerCmake(args)
