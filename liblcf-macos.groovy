args = makeEnv("macos")

args << [
  label: 'macos',
  make: ['check', 'install'],
  buildtype: 'RelWithDebInfo'
]

buildLiblcfCMake(args)
