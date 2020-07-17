#!/bin/bash

set -e

cmake_configure -DCMAKE_BUILD_TYPE="RelWithDebInfo"

cmake_make check install

(cd $BUILDDIR
    tar -czf "$BASEDIR/liblcf-macos.tar.gz" include/ lib/
)

artifact "liblcf-macos.tar.gz"
