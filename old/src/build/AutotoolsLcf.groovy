package build

@groovy.transform.InheritConstructors
class AutotoolsLcf extends Autotools {
    def stageBuildMake() {
        scr.dir(getBuildDir()) {
            if (make_check) {
                cmd "make check"
            }
            cmd "make install"
            cmd "make dist"
        }
    }

    def stageCollectArtifacts() {
        src.dir(getBuildDir()) {
            scr.archiveArtifacts artifacts: "liblcf-*.tar.*z"
        }
    }
}
