#!/bin/bash
# Helper script for running the infrastructure locally without Jenkins

set -a
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source common.sh

if [ -z "$GITURL" ]; then
    GITURL="https://github.com/easyrpg"
fi

BUILDJOB=$1
BUILDENV=$2

ROOTDIR=$SCRIPT_DIR/base
BASEDIR=$ROOTDIR/$BUILDJOB

case "$BUILDJOB" in
    liblcf*)
        REPO="liblcf";;
    player*)
        REPO="player";;
    toolchain*)
        REPO="buildscripts";;
    *)
        echo "Local build not supported for $BUILDJOB"
        exit 1;;
esac

source $SCRIPT_DIR/env/$BUILDENV

if [ -f "$SCRIPT_DIR/env/$BUILDJOB" ]; then
    source $SCRIPT_DIR/env/$BUILDJOB
fi

PKG_CONFIG_PATH="$ROOTDIR/$LIBLCF_DIR/install/lib/pkgconfig:$ROOTDIR/$TOOLCHAIN_DIR/lib/pkgconfig"
CMAKE_PREFIX_PATH="$ROOTDIR/$LIBLCF_DIR;$ROOTDIR/$TOOLCHAIN_DIR"

if [ -d $ROOTDIR/$BUILDJOB ]; then
    (cd $ROOTDIR/$BUILDJOB
        git fetch
        git reset --hard origin/master
    )
else
    mkdir -p $ROOTDIR
    git clone $GITURL/$REPO $ROOTDIR/$BUILDJOB
fi

CIBUILD=1
$SCRIPT_DIR/build.sh $BUILDJOB
