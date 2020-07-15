package env

class EnvBase {
    protected final Script s

    EnvBase(Script s) {
        this.s = s
    }

    def CC() {
        return ""
    }

    def CXX() {
        return ""
    }

    def PKG_CONFIG_PATH() {
        return ""
    }
}
