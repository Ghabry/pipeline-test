#!/bin/bash

set -e

(cd android
    ./1_download_library.sh
    ./2_build_toolchain.sh
    ./3_cleanup.sh

    # create portlib archive for convenience
    tar cf $BASEDIR/android_toolchain.tar.gz *-toolchain/include/ *-toolchain/lib/
)

artifact android_toolchain.tar.gz
