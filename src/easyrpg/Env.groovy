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
                env[val[0]] = val[1]
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
        d["PKG_CONFIG_PATH"] = PKG_CONFIG_PATH()
        d["CMAKE_PREFIX_PATH"] = CMAKE_PREFIX_PATH()

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
