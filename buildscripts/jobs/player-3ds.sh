#!/bin/bash

set -e

set_version_string

cd builds/3ds

# prepare wildmidi
mkdir -p romfs
cp -r timidity/wildmidi.cfg timidity/instruments romfs

make

# generate zip dist (workaround to get instruments in /)
(cd romfs
    zip -r9 ../easyrpg-player-3ds.zip .
)

zip -9 easyrpg-player-3ds.zip easyrpg-player.3dsx easyrpg-player.smdh easyrpg-player.xml
zip -9 easyrpg-player.elf.zip easyrpg-player.elf

# TODO CIA

artifact 'builds/3ds/easyrpg-player-3ds.zip'
#artifact 'builds/3ds/easyrpg-player.cia'
artifact 'builds/3ds/easyrpg-player.elf.zip'
