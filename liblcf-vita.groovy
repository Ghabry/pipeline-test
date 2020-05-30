opt = [
  label: 'vita',
  CXX: 'ccache arm-vita-eabi-g++',
  CXXFLAGS: '-g0 -O2',
]

buildLiblcfAutotools(
  opt + makeEnv()
)
