#!/bin/bash

function msg {
    echo ""
    echo "$1"
}

function autotools_configure {
    autoreconf -fi
    (cd $BUILDDIR
        $BASEDIR/configure --prefix=$INSTALLDIR --disable-shared --enable-static \
            --host=$TARGET_HOST $@
    )
}

function autotools_make {
    (cd $BUILDDIR
        for cmd in "$@"
        do
            make "$cmd"
        done
    )
}

function cmake_configure {
    cmake $BASEDIR -B$BUILDDIR -GNinja -DCMAKE_PREFIX_PATH="$PREFIXPATH" \
        -DCMAKE_INSTALL_PREFIX=$INSTALLDIR $@
}

function cmake_make {
    for cmd in "$@"
    do
        cmake --build $BUILDDIR --target "$cmd"
    done
}

function build_toolchain {
    (cd $1
        ./0_build_everything.sh

        # create portlib archive for convenience
        tar cf $BASEDIR/$2_toolchain.tar.gz include/ lib/
    )

    artifact $2_toolchain.tar.gz
}

function set_version_string {
    commitcount=$(git rev-list $(git rev-list --tags --no-walk --max-count=1).. --count)
    shorthash=$(git rev-parse --short HEAD)
    builddate=$(date +%Y-%m-%d)
    if [ $commitcount -eq 0 ]; then # release tag
        VER="($builddate)"
    else
        VER="(git~$commitcount@$shorthash, $builddate)"
    fi
    sed -i.clean "s/\(PLAYER_ADDTL\)\(.*\)$/\1 \"$VER\"/" src/version.h
}

function set_version_string_cleanup {
    mv src/version.h.clean src/version.h
}

function artifact {
    for art in "$@"
    do
        echo "${art#$BASEDIR/}" >> $BASEDIR/artifacts
    done
}

