def call(args) {
  pipeline {
    agent {
      label args.label
    }
    
    environment {
      PKG_CONFIG_PATH = "${args.PKG_CONFIG_PATH}"
      PATH = "${args.PATH}:${env.PATH}"
      CXX = "${args.CXX}"
      CPPFLAGS = "${args.CPPFLAGS}"
      CXXFLAGS = "${args.CXXFLAGS}"
      LDFLAGS = "${args.LDFLAGS}"
      MAKEFLAGS = "-j${env.NUMCORES ?: '2'}"
    }

    stages {    
      stage('Checkout') {
        steps {
          gitClone(args.branch, args.url, 'https://github.com/easyrpg/liblcf')
        }
      }
      
      stage('Pre Build') {
        when {
          expression { args.pre != null }
        }
        steps {
          sh "${args.pre}"
        }
      }

      stage('Build') {
        steps {
          sh """
    cd ../toolchain-emscripten/emscripten/emsdk-portable
    ./emsdk construct_env
    . ./emsdk_set_env.sh
          autoreconf -fi
          ${args.configure_wrapper} ./configure --host=${args.host} --prefix=$WORKSPACE/build --enable-static --disable-shared ${args.configure_args}
          """
          script {
            for (cmd in args.make) {
              sh "make ${cmd}"
            }
          }
        }
      }
    
      stage('Post Build') {
        when {
          expression { args.post != null }
        }
        steps {
          sh "${args.post}"
        }
      }

      stage('Collect Artifacts') {
        when {
          expression { args.artifacts == true }
        }
        steps {
          dir("build") {
            sh "tar -czf ../liblcf_${args.label}.tar.gz include/ lib/"
          }
          archiveArtifacts artifacts: "liblcf_${args.label}.tar.gz"
        }
      }
    }
  }
}
