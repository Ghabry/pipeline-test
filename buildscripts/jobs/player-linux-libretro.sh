#!/bin/bash

set -e

set_version_string

cmake_configure -DCMAKE_BUILD_TYPE="RelWithDebInfo"

cmake_make check install

echo 'easyrpg-player-static.tar.gz' > artifacts
echo 'easyrpg-player-static_dbgsym.tar.gz' > artifacts

(cd $BUILDDIR
    zip -r9 "$BASEDIR/EasyRPG-Player-macos.app.zip" "EasyRPG Player.app"
)

echo "EasyRPG-Player-macos.app.zip" > artifacts
