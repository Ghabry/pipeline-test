node ("3ds") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-3ds", "liblcf-3ds", "3ds")
}
