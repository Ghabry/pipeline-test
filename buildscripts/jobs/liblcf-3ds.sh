#!/bin/bash

set -e

autotools_configure --disable-xml
autotools_make install

# Create portlib archive for convenience
(cd $INSTALLDIR
    tar -czf $BASEDIR/liblcf_3ds.tar.gz include/ lib/
)

artifact "liblcf_3ds.tar.gz"
