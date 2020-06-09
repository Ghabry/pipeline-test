args = makeEnv("linux")

args << [
  label: 'linux',
  pre: "echo 'Hello World'",
  make: ['check', 'dist', 'install'],
  
  CXXFLAGS: '-Wall -Wextra -O0 -g3'
]

buildLiblcfAutotools(args)
