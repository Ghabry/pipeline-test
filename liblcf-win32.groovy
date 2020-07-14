args = makeEnv("windows")

args << [
  label: 'windows',
  make: ['check', 'install'],
  cmake_wrapper: 'call %VCVARSALL2017% amd64_x86 && '
]

args["cmake_args"] += " -DVCPKG_TARGET_TRIPLET=x86-windows-static"

args["buildtype"] = 'Debug'
buildLiblcfCMakeWindows(args)

args["buildtype"] = 'RelWithDebInfo'
buildLiblcfCMakeWindows(args)
