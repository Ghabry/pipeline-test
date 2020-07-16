@NonCPS
Map call(which) {
  env = makeEnv(which)

  env["pre"] = 'git submodule update --init'
  
  // TODO: depending on "which" Shared on or off
  env["cmake_args"] += " -DPLAYER_TARGET_PLATFORM=libretro -DBUILD_SHARED_LIBS=ON"
  
  return env
}
