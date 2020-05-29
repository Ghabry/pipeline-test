buildLiblcfAutotools(
  label: 'vita',
  toolchain: 'toolchain-vita/vita',
  toolchain_path: 'vitasdk/bin',
  host: 'arm-vita-eabi',
  CXX: 'ccache arm-vita-eabi-g++',
  CXXFLAGS: '-g0 -O2',
)
