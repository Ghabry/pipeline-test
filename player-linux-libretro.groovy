args = makeEnvLibretro("linux")

args << [
  label: 'linux',
  make: ['check', 'install'],
  buildtype: 'Release',
]

args["artifacts_cmd"] = """
zip -9 ../easyrpg-libretro.so.zip easyrpg-libretro.so
"""

args["artifacts"] = ["easyrpg-libretro.so.zip"]

buildPlayerCmake(args)
