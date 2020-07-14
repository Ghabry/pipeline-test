def call(args) {
  pipeline {
    agent {
      label args.label
    }
    
    environment {
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
          bat "${args.pre}"
        }
      }

      stage('Build') {
        steps {
          bat """${args.cmake_wrapper} cmake . -B${args.buildtype} -GNinja \
                -DCMAKE_BUILD_TYPE=${args.buildtype} -DCMAKE_INSTALL_PREFIX=build \
                ${args.cmake_args}"""

          script {
            for (cmd in args.make) {
              bat "${args.cmake_wrapper} cmake --build ${args.buildtype} --target ${cmd}"
            }
          }
        }
      }
    
      stage('Post Build') {
        when {
          expression { args.post != null }
        }
        steps {
          bat "${args.post}"
        }
      }

      stage('Collect Artifacts') {
        when {
          expression { args.artifacts == true }
        }
        steps {
          dir("build") {
            bat "7z a -mx5 ../liblcf_${args.label}.zip ."
          }
          archiveArtifacts artifacts: "liblcf_${args.label}.zip"
        }
      }
    }
  }
}
