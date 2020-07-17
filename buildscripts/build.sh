#!/bin/bash
# Entry point for the pipeline
# Variables provided by the pipeline:
# ROOTDIR - Path that contains all job folders
# BASEDIR - Path of the current job folder
# BUILDDIR - Path of the current job build directory
# INSTALLDIR - Path of the current job install directory

# Environment variables provided by the pipeline:
# PKG_CONFIG_PATH
# CMAKE_PREFIX_PATH

set -e
set -a

if [ -z "$CIBUILD" ]; then
    echo "This script can only run in a CI environment"
    echo "Use build_local.sh instead"
    exit 1
fi

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $SCRIPT_DIR/common.sh

BUILDJOB=$1

if [ -z "$ROOTDIR" ]; then
    echo "ROOTDIR not set"
    exit 1
fi

if [ -z "$BASEDIR" ]; then
    echo "BASEDIR not set"
    exit 1
fi

if [ -z "$BUILDDIR" ]; then
    BUILDDIR=$BASEDIR/build
    mkdir -p $BUILDDIR
fi

if [ -z "$INSTALLDIR" ]; then
    INSTALLDIR=$BASEDIR/install
fi

if [ -z "$MAKEFLAGS" ]; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        MAKEFLAGS="-j$(getconf _NPROCESSORS_ONLN)"
    else
        MAKEFLAGS="-j${nproc}"
    fi
fi

TOOLCHAIN_DIR=$ROOTDIR/$TOOLCHAIN_DIR
LIBLCF_DIR=$ROOTDIR/$LIBLCF_DIR

rm -f $BASEDIR/artifacts

(cd $BASEDIR
    $SCRIPT_DIR/jobs/$BUILDJOB.sh
)
