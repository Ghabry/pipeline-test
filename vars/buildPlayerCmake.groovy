def call(args) {
  node(args.label) {
    withEnv([
      "PATH=${args.PATH}:${env.PATH}",
      "CC=${args.CC}",
      "CXX=${args.CXX}",
      "CPPFLAGS=${args.CPPFLAGS}",
      "CXXFLAGS=${args.CXXFLAGS}",
      "LDFLAGS=${args.LDFLAGS}"]) {

      stage('Clear Workspace') {
        cleanWs()
      }

      stage('Checkout') {
        gitClone(args.branch, args.url, 'https://github.com/easyrpg/player')
      }
      
      stage('Pre Build') {
        if (args.pre != null) {
          sh "${args.pre}"
        }
      }

      stage('Build') {
        sh """${args.cmake_wrapper} cmake . -B${args.buildtype} -GNinja \
              -DCMAKE_PREFIX_PATH="${args.TOOLCHAIN_DIR};${args.LIBLCF_DIR}" \
              -DCMAKE_BUILD_TYPE=${args.buildtype} -DCMAKE_INSTALL_PREFIX=build \
              ${args.cmake_args}"""

        for (cmd in args.make) {
          sh "${args.cmake_wrapper} cmake --build ${args.buildtype} --target ${cmd}"
        }
      }
    
      stage('Post Build') {
        if (args.post != null) {
          sh "${args.post}"
        }
      }

      stage('Collect Artifacts') {
        if (args.artifacts != null) {
          if (args.artifacts_cmd != null) {
            dir("build") {
              sh "${args.artifacts_cmd}"
            }
          }
          for (artifacts in args.artifacts) {
            archiveArtifacts artifacts: "${artifacts}"
          }  
        }
      }
    }
  }
}
