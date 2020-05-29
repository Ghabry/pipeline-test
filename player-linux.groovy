pipeline {
  agent {
    label 'linux'
  }

  stages {
    stage('Build') {
      environment {
        PKG_CONFIG="pkg-config --static"
        STATICLIBSPATH="${JENKINS_HOME}/workspace/toolchain-linux-static/linux-static"
        PKG_CONFIG_PATH="${JENKINS_HOME}/workspace/liblcf-linux/build/lib/pkgconfig:${STATICLIBSPATH}/lib/pkgconfig"
        PKG_CONFIG_LIBDIR=""
        CC="ccache gcc -static-libgcc -static-libstdc++"
        CXX="ccache g++ -static-libgcc -static-libstdc++"
        CXXFLAGS='-Wall -Wextra -O0 -g3'
      }
    
      steps {
        git 'https://github.com/easyrpg/player'
            
        sh "autoreconf -fi"
        sh "./configure --prefix=${PWD} --enable-fmmidi --without-freetype --without-harfbuzz bashcompinstdir=etc/completion.d"
        sh "make check -j${NUMCORES}"
        sh "make install"
        sh "make dist"
      }

      post {
        success {
        }
      }
    }
  }
}
