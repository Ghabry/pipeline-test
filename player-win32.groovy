args = makeEnv("win64")

args << [
  label: 'windows',
  make: ['check', 'install'],
]

args["buildtype"] = 'RelWithDebInfo'
buildPlayerCMakeWindows(args)
