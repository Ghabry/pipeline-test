// Every groovy file is a class
// List of instance properties
env
jobroot
toolchain_dir

@NonCPS
Map call(which) {
  env = [
    PKG_CONFIG_PATH: "${-> toolchain_dir}/lib/pkgconfig",
    CXX: "",
    PATH: "",
    CPPFLAGS: "",
    CXXFLAGS: "-O2 -g",
    LDFLAGS: "",
    
    label: which,
    make: ["install"],
    host: "",
    configure_args: "",
    configure_wrapper: "",
    cmake_wrapper: "",
    artifacts: true
  ]

  jobroot = "${-> WORKSPACE}/.."
  
  if (which == "linux") {
    toolchain_dir = "${-> jobroot}/toolchain-linux-static/linux-static"

    return env
  } else if (which == "emscripten") {
    toolchain_dir = "${-> jobroot}/toolchain-emscripten/emscripten"
    
    env << [
      EM_PKG_CONFIG_PATH: env.PKG_CONFIG_PATH,
      configure_wrapper: "emconfigure",
      cmake_wrapper: "emcmake"
    ]

    return env
  } else if (which == "vita") {
    toolchain_dir = "${-> jobroot}/toolchain-vita/vita"
    
    env << [
      PATH: "${-> toolchain_dir}/vitasdk/bin",
      host: "arm-vita-eabi"
    ]

    return env
  }
  
  assert(false)
}
