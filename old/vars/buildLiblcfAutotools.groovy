def call(args) {
  node(args.label) {
    withEnv([
      "PKG_CONFIG_PATH=${args.PKG_CONFIG_PATH}",
      "PATH=${args.PATH}:${env.PATH}",
      "CXX=${args.CXX}",
      "CPPFLAGS=${args.CPPFLAGS}",
      "CXXFLAGS=${args.CXXFLAGS}",
      "LDFLAGS=${args.LDFLAGS}",
      "MAKEFLAGS=-j${env.NUMCORES ?: '2'}"]) {

      stage('Clear Workspace') {
        cleanWs()
      }

      stage('Checkout') {
        gitClone(args.branch, args.url, 'https://github.com/easyrpg/liblcf')
      }
      
      stage('Pre Build') {
        if (args.pre != null) {
          sh "${args.pre}"
        }
      }

      stage('Build') {
        sh "autoreconf -fi"
        sh "${args.configure_wrapper} ./configure --host=${args.host} --prefix=$WORKSPACE/build --enable-static --disable-shared ${args.configure_args}"

        for (cmd in args.make) {
          sh "make ${cmd}"
        }
      }
    
      stage('Post Build') {
        if (args.post != null) {
          sh "${args.post}"
        }
      }

      stage('Collect Artifacts') {
        if (args.artifacts == true) {
          dir("build") {
            sh "tar -czf ../liblcf_${args.label}.tar.gz include/ lib/"
          }
          archiveArtifacts artifacts: "liblcf_${args.label}.tar.gz"
        }
      }
    }
  }
}
