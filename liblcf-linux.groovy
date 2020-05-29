pipeline {
  agent {
    label 'linux'
  }

  stages {
    stage('Build') {
      environment {
        STATICLIBSPATH = "$WORKSPACE/../toolchain-linux-static/linux-static"
        PKG_CONFIG_PATH = "$STATICLIBSPATH/lib/pkgconfig"
        CXXFLAGS = "-Wall -Wextra -O0 -g3"
      }
    
      steps {
        git 'https://github.com/easyrpg/liblcf'
        sh "autoreconf -fi"
        echo "${PKG_CONFIG_PATH}"
        sh "./configure --prefix=$WORKSPACE/build --enable-static --disable-shared"
        sh "make check -j$NUMCORES"
        sh "make dist"
        sh "make install"
      }
    }
  }
}
