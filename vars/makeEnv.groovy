Map call() {
  def workspace = "${-> $WORKSPACE}"
  def jobroot = "${-> $workspace}/.."

  return [
    toolchain: "${-> $jobroot/toolchain-vita/vita}",
    toolchain_path: "${-> $toolchain/vitasdk/bin}",
    host: 'arm-vita-eabi'
  ]
}
