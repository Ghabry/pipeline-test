#!/bin/bash

set -e

(cd linux-static
    ./0_build_everything.sh

    # create portlib archive for convenience
    tar cvf $BASEDIR/linux_toolchain.tar.gz include/ lib/
)

artifact linux_toolchain.tar.gz
