package build

@groovy.transform.InheritConstructors
class Autotools extends Build {
    public make_check = true

    def preBuild() {
        scr.dir(getBuildDir()) {
            cmd '''
            # set version string
            commitcount=$(git rev-list $(git rev-list --tags --no-walk --max-count=1).. --count)
            shorthash=$(git rev-parse --short HEAD)
            builddate=$(date +%Y-%m-%d)
            if [ $commitcount -eq 0 ]; then # release tag
                VER="($builddate)"
            else
                VER="(git~$commitcount@$shorthash, $builddate)"
            fi
            sed -i.clean "s/\(PLAYER_ADDTL\)\(.*\)$/\1 \"$VER\"/" src/version.h
            '''
        }
    }

    def stageBuildMake() {
        scr.dir(getBuildDir()) {
            if (make_check) {
                cmd "make check"
            }
            cmd "make install"
            cmd "mv src/version.h.clean src/version.h"
            cmd "make dist"
        }
    }

    def stageCollectArtifacts() {
        src.dir(getBuildDir()) {
            cmd 'tar czf easyrpg-player-static-PR${ghprbPullId}.tar.gz easyrpg-player'
            cmd 'tar czf easyrpg-player-static-PR${ghprbPullId}_dbgsym.tar.gz easyrpg-player.debug'
            scr.archiveArtifacts artifacts: "easyrpg-player-static-*.tar.*z"
        }
    }
}
