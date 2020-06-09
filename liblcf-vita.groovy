args = makeEnv("vita")

args << [
  configure_args: "--disable-tools",

  CXX: 'ccache arm-vita-eabi-g++'
]

buildLiblcfAutotools(args)
