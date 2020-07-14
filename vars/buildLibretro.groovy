def call(args) {
  pipeline {
    agent {
      label args.label
    }
    
    environment {
      PATH = "${args.PATH}:${env.PATH}"
      CXX = "${args.CXX}"
      CPPFLAGS = "${args.CPPFLAGS}"
      CXXFLAGS = "${args.CXXFLAGS}"
      LDFLAGS = "${args.LDFLAGS}"
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
          sh """${args.cmake_wrapper} cmake . -B${args.buildtype} -GNinja -DCMAKE_PREFIX_PATH="${args.TOOLCHAIN_DIR}" \
                -DCMAKE_BUILD_TYPE=${args.buildtype} -DCMAKE_INSTALL_PREFIX=build \
                ${args.cmake_args}"""

          script {
            for (cmd in args.make) {
              sh "${args.cmake_wrapper} cmake --build ${args.buildtype} --target ${cmd}"
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
