package build

@groovy.transform.InheritConstructors
class Autotools extends Build {
    public make_check = true

    def stageBuildConfigure() {
        cmd "autoreconf -fi"
        def inst_dir = getInstallDir()
        scr.dir(getBuildDir()) {
            cmd "../configure --host=${env.host} --prefix=${inst_dir} --enable-static --disable-shared ${env.configureExtraArgs}"
        }
    }
}
