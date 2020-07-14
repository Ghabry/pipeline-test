def call(args) {
  node(args.label) {
    withEnv([
      "CXX=${args.CXX}",
      "CPPFLAGS=${args.CPPFLAGS}",
      "CXXFLAGS=${args.CXXFLAGS}",
      "LDFLAGS=${args.LDFLAGS}"]) {

      stage('Clear Workspace') {
        cleanWs()
      }

      stage('Checkout') {
        gitClone(args.branch, args.url, 'https://github.com/easyrpg/liblcf')
      }
      
      stage('Pre Build') {
        if (args.pre != null) {
          bat "${args.pre}"
        }
      }

      stage('Build') {
        bat """${args.cmake_wrapper} cmake . -B${args.buildtype} -GNinja \
              -DCMAKE_BUILD_TYPE=${args.buildtype} -DCMAKE_INSTALL_PREFIX=build \
              ${args.cmake_args}"""

        for (cmd in args.make) {
          bat "${args.cmake_wrapper} cmake --build ${args.buildtype} --target ${cmd}"
        }
      }
    
      stage('Post Build') {
        if (args.post != null) {
          bat "${args.post}"
        }
      }

      stage('Collect Artifacts') {
        if (args.artifacts == true) {
          dir("build") {
            bat "7z a -mx5 ../liblcf_${args.label}.zip ."
          }
          archiveArtifacts artifacts: "liblcf_${args.label}.zip"
        }
      }
    }
  }
}
