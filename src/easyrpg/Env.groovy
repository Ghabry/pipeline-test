package easyrpg

class Env {
    protected final Script scr
    public final paths
    public final env

    Env(Script scr) {
        this.scr = scr
        this.env = [:]
        this.paths = []
    }

    def load(String path) {
        if (!scr.fileExists(path)) {
            return
        }

        //scr.sh returnStdout: true

        /*def lines = scr.readFile(path).split("\n")

        for (line in lines) {
            if (line.contains("=")) {
                def val = line.replace("'", "").replace('"', "").split("=")
                if (val[0] != "PATH") {
                    env[val[0]] = val[1]
                }
            }
        }*/
        def props = scr.readProperties interpolate: true, file: path

        for (prop in props) {
            def val = prop.value
            if (val.startsWith('"')) {
                val = val[1..-1]
            }
            if (val.endsWith('"')) {
                val = val[0..-2]
            }
            env[prop.key] = val
        }

        if (env.containsKey("LIBLCF") && !env.containsKey("LIBLCF_DIR")) {
            env["LIBLCF_DIR"] = "${env['ROOTDIR']}/${env['LIBLCF']}/install"
        }
        if (env.containsKey("TOOLCHAIN") && !env.containsKey("TOOLCHAIN_DIR")) {
            env["TOOLCHAIN_DIR"] = "${env['ROOTDIR']}/${env['TOOLCHAIN']}"
        }
    }

    @NonCPS
    final def initPaths() {
        if (env.containsKey("LIBLCF_DIR")) {
            this.paths.add(env['LIBLCF_DIR'])
        }
        if (env.containsKey("TOOLCHAIN_DIR")) {
            this.paths.add(env['TOOLCHAIN_DIR'])
        }
    }

    @NonCPS
    def makeEnv() {
        def d = env.clone()

        def pkg = PKG_CONFIG_PATH()
        if (pkg.size() > 0) {
            d["PKG_CONFIG_PATH"] = pkg
        }
        def cmake = CMAKE_PREFIX_PATH()
        if (cmake.size() > 0) {
            d["CMAKE_PREFIX_PATH"] = cmake
        }

        scr.echo "Environment: ${d.toString()}"

        return d.collect { entry ->
            "${entry.key}=${entry.value}"
        }
    }

    @NonCPS
    final def CMAKE_PREFIX_PATH() {
        if (paths.size() == 0) {
            initPaths()
        }

        return paths.collect { entry ->
            "${entry}"
        }.join(":")
    }

    @NonCPS
    final def PKG_CONFIG_PATH() {
        if (paths.size() == 0) {
            initPaths()
        }

        return paths.collect { entry ->
            "${entry}/lib/pkgconfig"
        }.join(":")
    }
}
