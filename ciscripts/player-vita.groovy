node ("vita") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-vita", "liblcf-vita", "vita")
}
