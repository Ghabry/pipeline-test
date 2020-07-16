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
      stage('Build') {
        steps {
          dir(args.dir) {
            bat "build.cmd"
          }
        }  
      }
    }
  }
}
