args = makeEnv("win32")

args << [
  label: 'windows',
  make: ['check', 'install'],
]

args["buildtype"] = 'Debug'
buildLiblcfCMakeWindows(args)

args["buildtype"] = 'RelWithDebInfo'
buildLiblcfCMakeWindows(args)
