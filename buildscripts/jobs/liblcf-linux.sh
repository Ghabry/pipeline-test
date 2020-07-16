#!/bin/bash

set -e

autotools_configure
autotools_make check dist install

artifact "$BUILDDIR/liblcf-*.tar.*z"
