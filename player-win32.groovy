args = makeEnv("win32")

args << [
  label: 'windows',
  make: ['check', 'install'],
]

args["buildtype"] = 'RelWithDebInfo'
buildPlayerCmakeWindows(args)
