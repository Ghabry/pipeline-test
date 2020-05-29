def call(args) {
  pipeline {
    agent {
      label args.label
    }
  
    stages {
      stage('Build') {
        environment {
          TOOLCHAIN_DIR = "$WORKSPACE/../${args.toolchain}"
          PKG_CONFIG_PATH = "$TOOLCHAIN_DIR/lib/pkgconfig"
          PATH = "$TOOLCHAIN_DIR/${args.toolchain_path}:$PATH"
          CXX = "${args.CXX ?: ''}"
          CPPFLAGS = "${args.CPPFLAGS ?: ''}"
          CXXFLAGS = "${args.CXXFLAGS ?: '-O2 -g'}"
          LDFLAGS = "${args.LDFLAGS ?: ''}"
        }
      
        steps {
          git 'https://github.com/easyrpg/liblcf'
          echo "$PATH"
          sh "autoreconf -fi"
          sh "./configure --host=${args.host ?: ''} --prefix=$WORKSPACE/build --enable-static --disable-shared"
          sh "make check -j$NUMCORES"
          sh "make dist"
          sh "make install"
        }
      }
    }
  }
}
