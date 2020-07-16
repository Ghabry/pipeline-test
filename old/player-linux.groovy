@groovy.transform.InheritConstructors
class B extends build.AutotoolsPlayer {
    def postBuild() {
        src.dir(getBuildDir()) {
            cmd $/
            # separate debug information
            cp -up bin/easyrpg-player easyrpg-player
            objcopy --only-keep-debug easyrpg-player easyrpg-player.debug
            strip --strip-debug --strip-unneeded easyrpg-player
            objcopy --add-gnu-debuglink=easyrpg-player.debug easyrpg-player/$
        }
    }
}

node('linux') {
    e = new env.Linux(this)
    b = new B(this, e)

    cleanWs()

    b.checkout("https://github.com/easyrpg/player", "master")
    b.runPipeline()
}
