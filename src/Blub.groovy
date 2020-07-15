class Blub {
    protected final Script s
    protected final env.EnvBase env

    Blub(Script script, env.EnvBase env) {
        this.s = script
        this.env = env
    }

    def env() {
        def d = [:]
        if (env.CC() != "")
            d["CC"] = env.CC()
        if (env.CXX() != "")
            d["CXX"] = env.CXX()
        if (env.PKG_CONFIG_PATH() != "")
            d["PKG_CONFIG_PATH"] = env.PKG_CONFIG_PATH()

        return d
    }

    def makeEnv() {
        def e = env().collect { entry ->
            "${entry.key}=${entry.value}"
        }
        return e
    }

    def hello() {
        s.echo 'BlubClass!'
    }

    def clean() {
      s.stage('Clear Workspace') {
        s.cleanWs()
      }
    }
}
