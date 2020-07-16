// Every groovy file is a class
// List of instance properties
env
jobroot
toolchain_dir
liblcf_dir

@NonCPS
Map call(which) {
  env = [
    TOOLCHAIN_DIR: "${-> toolchain_dir}",
    LIBLCF_DIR: "${-> liblcf_dir}/build",
    PKG_CONFIG_PATH: "${-> toolchain_dir}/lib/pkgconfig:${-> liblcf_dir}/lib/pkgconfig",
    CC: "",
    CXX: "",
    PATH: "",
    CPPFLAGS: "",
    CXXFLAGS: "-O2 -g",
    LDFLAGS: "",
    
    label: which,
    make: ["install"],
    host: "",
    configure_args: "",
    cmake_args: "",
    configure_wrapper: "",
    cmake_wrapper: "",
    artifacts: true,
  ]

  jobroot = "${-> WORKSPACE}/.."

  if (which == "linux") {
    toolchain_dir = "${-> jobroot}/toolchain-linux-static/linux-static"
    liblcf_dir = "${-> jobroot}/liblcf-linux"
    
    env << [
      CC: "gcc -static-libgcc -static-libstdc++",
      CXX: "g++ -static-libgcc -static-libstdc++"
    ]

    return env
  } else if (which == "emscripten") {
    toolchain_dir = "${-> jobroot}/toolchain-emscripten/emscripten"
    liblcf_dir = "${-> jobroot}/liblcf-emscripten"
    
    env << [
      EM_PKG_CONFIG_PATH: env.PKG_CONFIG_PATH,
      configure_wrapper: "emconfigure",
      cmake_wrapper: "emcmake"
    ]

    return env
  } else if (which == "vita") {
    toolchain_dir = "${-> jobroot}/toolchain-vita/vita"
    liblcf_dir = "${-> jobroot}/liblcf-vita"

    env << [
      PATH: "${-> toolchain_dir}/vitasdk/bin",
      host: "arm-vita-eabi"
    ]

    return env
  } else if (which == "macos") {
    toolchain_dir = "${-> jobroot}/toolchain-macos/osx"
    liblcf_dir = "${-> jobroot}/liblcf-macos"

    env << [
      cmake_args: "-DCMAKE_CXX_COMPILER_LAUNCHER=ccache -DCMAKE_OSX_DEPLOYMENT_TARGET=10.9 -DOSX_ARCHITECTURES=x86_64"
    ]

    return env
  } else if (which == "win32") {
    toolchain_dir = ""
    liblcf_dir = "${-> jobroot}/liblcf-win32"
    
    env << [
      CXXFLAGS: "",
      cmake_args: "-DSHARED_RUNTIME=OFF -DCMAKE_TOOLCHAIN_FILE=${-> jobroot}/toolchain-windows/windows/vcpkg/scripts/buildsystems/vcpkg.cmake -DVCPKG_TARGET_TRIPLET=x86-windows-static",
      cmake_wrapper: 'call %VCVARSALL2017% amd64_x86 && '
    ]

    return env
  } else if (which == "win64") {
    toolchain_dir = ""
    liblcf_dir = "${-> jobroot}/liblcf-win64"
    
    env << [
      CXXFLAGS: "",
      cmake_args: "-DSHARED_RUNTIME=OFF -DCMAKE_TOOLCHAIN_FILE=${-> jobroot}/toolchain-windows/windows/vcpkg/scripts/buildsystems/vcpkg.cmake -DVCPKG_TARGET_TRIPLET=x64-windows-static",
      cmake_wrapper: 'call %VCVARSALL2017% x64 && '
    ]

    return env
  }
  
  assert(false)
}
