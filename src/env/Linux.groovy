package env

@groovy.transform.InheritConstructors
class Linux extends EnvBase {
    def CC() {
        return "gcc -static-libgcc -static-libstdc++"
    }

    def PKG_CONFIG_PATH() {
        return "${s.WORKSPACE}/../toolchain-linux"
    }
}
