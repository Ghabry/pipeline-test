args = makeEnv("win64")

args << [
  label: 'windows',
  make: ['check', 'install'],
]

args["buildtype"] = 'Debug'
buildLiblcfCMakeWindows(args)

args["buildtype"] = 'RelWithDebInfo'
buildLiblcfCMakeWindows(args)
