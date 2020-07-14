args = makeEnv("windows")

args << [
  label: 'windows',
  make: ['check', 'install'],
  cmake_wrapper: 'call %VCVARSALL2017% x64 && '
]

args["cmake_args"] += " -DVCPKG_TARGET_TRIPLET=x64-windows-static"

args["buildtype"] = 'Debug'
buildLiblcfCMakeWindows(args)

args["buildtype"] = 'RelWithDebInfo'
buildLiblcfCMakeWindows(args)
