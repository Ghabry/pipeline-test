node ("3ds") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    dir("builds/3ds/timidity") {
        b.checkout("https://github.com/Ghabry/timidity_gus.git", "master")
    }
    b.run("player-3ds", "liblcf-3ds", "3ds")
}
