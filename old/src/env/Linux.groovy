package env

@groovy.transform.InheritConstructors
class Linux extends Env {
    {
        CC = "gcc -static-libgcc -static-libstdc++"
        CXX = "gcc -static-libgcc -static-libstdc++"
        CXXFLAGS='-Wall -Wextra -O0 -g3'
    }

    @NonCPS
    def getToolchainDir() {
        return getRoot() + "toolchain-linux-static/linux-static"
    }

    @NonCPS
    def getLcfDir() {
        return getRoot() + "liblcf-linux"
    }
}
