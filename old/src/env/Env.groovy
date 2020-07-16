package env

abstract class Env {
    protected final Script scr
    public final paths
    public final ccache = true

    public CC = ""
    public CXX = ""
    public CXXFLAGS = ""
    public LDFLAGS = ""
    public PKG_CONFIG = "pkg-config --static"

    public host = ""
    public configureExtraArgs = ""
    public cmakeToolchainFile = ""
    public cmakeExtraArgs = ""

    Env(Script scr) {
        this.scr = scr
        this.paths = []
    }

    @NonCPS
    final def initPaths() {
        this.paths.add("${getLcfDir()}/install")
        this.paths.add("${getToolchainDir()}")
    }

    def env() {
        def d = [:]
        if (CC != "")
            d["CC"] = CC
        if (CXX != "")
            d["CXX"] = CXX
        if (CXXFLAGS != "")
            d["CXXFLAGS"] = CXXFLAGS
        if (LDFLAGS != "")
            d["LDFLAGS"] = LDFLAGS
        if (PKG_CONFIG != "")
            d["PKG_CONFIG"] = PKG_CONFIG
        if (PKG_CONFIG_PATH() != "")
            d["PKG_CONFIG_PATH"] = PKG_CONFIG_PATH()
        if (MAKEFLAGS() != "")
            d["MAKEFLAGS"] = MAKEFLAGS()

        return d
    }

    def makeEnv() {
        return env().collect { entry ->
            "${entry.key}=${entry.value}"
        }
    }

    @NonCPS
    def getRoot() {
        return "${scr.WORKSPACE}/../"
    }

    abstract def getToolchainDir()

    abstract def getLcfDir()

    final def CMAKE_PREFIX_PATH() {
        if (paths.size() == 0) {
            initPaths()
        }

        return paths.collect { entry ->
            "${entry}"
        }.join(":")
    }

    final def PKG_CONFIG_PATH() {
        if (paths.size() == 0) {
            initPaths()
        }

        return paths.collect { entry ->
            "${entry}/lib/pkgconfig"
        }.join(":")
    }

    def MAKEFLAGS() {
        return "-j${scr.env.NUMCORES ?: '2'}"
    }
}
