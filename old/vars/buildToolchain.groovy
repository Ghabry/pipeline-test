def call(args) {
  pipeline {
    agent {
      label args.label
    }
  
    stages {
      stage('Clear Workspace') {
        steps {
          cleanWs()
        }
      }
      stage('Checkout') {
        steps {
          gitClone(args.branch, args.url, 'https://github.com/easyrpg/buildscripts')
        }  
      }
      stage('Download') {
        steps {
          dir(args.dir) {
            sh "./1_download_library.sh"
          }
        }  
      }
      stage('Build') {
        steps {
          dir(args.dir) {
            sh "./2_build_toolchain.sh"
          }
        }  
      }
      stage('Cleanup') {
        steps {
          dir(args.dir) {
            sh "./3_cleanup.sh"
          }
        }  
      }
    }
  }
}
