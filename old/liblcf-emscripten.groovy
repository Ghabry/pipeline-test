args = makeEnv("emscripten")

args << [
  configure_args: "--disable-tools",
  make: ['check', 'install'],
  pre: """
    cd ../toolchain-emscripten/emscripten/emsdk-portable
    ./emsdk construct_env
    . ./emsdk_set_env.sh
  """,

  CXXFLAGS: "-Wno-warn-absolute-paths -g0 -O2",
  CPPFLAGS: "-DU_STATIC_IMPLEMENTATION"
]

buildLiblcfAutotools(args)
