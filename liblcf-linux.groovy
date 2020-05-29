buildLiblcfAutotools(
  label: 'linux',
  toolchain: 'toolchain-linux-static/linux-static',
  CXXFLAGS: '-Wall -Wextra -O0 -g3',
)
