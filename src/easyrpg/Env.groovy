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

    @NonCPS
    def load(String path) {
        def f = new File(path)
        if (!f.exists()) {
            return
        }

        f.eachLine { String line ->
            if (line.contains("=")) {
                def val = line.replace("'", "").replace('"', "").split("=")
                if (val[0] != "PATH") {
                    env[val[0]] = val[1]
                }
            }
        }
    }

    @NonCPS
    final def initPaths() {
        if (env.containsKey("LIBLCF_DIR")) {
            this.paths.add("${env['ROOTDIR']}/${env['LIBLCF_DIR']}/install")
        }
        if (env.containsKey("TOOLCHAIN_DIR")) {
            this.paths.add("${env['ROOTDIR']}/${env['TOOLCHAIN_DIR']}")
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
