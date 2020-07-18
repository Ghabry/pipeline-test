#!/bin/bash

set -e

set_version_string

cmake_configure -DCMAKE_BUILD_TYPE="RelWithDebInfo"

cmake_make install

(cd $BUILDDIR
    zip -r9 "$BASEDIR/EasyRPG-Player-macos.app.zip" "EasyRPG Player.app"
)

artifact "EasyRPG-Player-macos.app.zip"
