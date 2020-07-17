#!/bin/bash

set -e

set_version_string

autotools_configure --enable-fmmidi --without-freetype --without-harfbuzz bashcompinstdir=etc/completion.d
autotools_make clean check install

set_version_string_cleanup

autotools_make dist

(cd $INSTALLDIR
    # separate debug information
    cp -up bin/easyrpg-player easyrpg-player
    objcopy --only-keep-debug easyrpg-player easyrpg-player.debug
    strip --strip-debug --strip-unneeded easyrpg-player
    objcopy --add-gnu-debuglink=easyrpg-player.debug easyrpg-player

    # binary dist
    tar czf $BASEDIR/easyrpg-player-static.tar.gz easyrpg-player
    tar czf $BASEDIR/easyrpg-player-static_dbgsym.tar.gz easyrpg-player.debug
)

artifact 'easyrpg-player-static.tar.gz'
artifact 'easyrpg-player-static_dbgsym.tar.gz'
