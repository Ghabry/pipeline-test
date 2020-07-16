#!/bin/bash

set -e

set_version_string

cmake_configure -DCMAKE_BUILD_TYPE="RelWithDebInfo"

cmake_make check install

(cd $BUILDDIR
    zip -r9 "$BASEDIR/easyrpg-player-linux.zip" "easyrpg-player"
)

artifact "easyrpg-player-linux.zip"
