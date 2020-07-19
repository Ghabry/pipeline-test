node ("wii") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-wii", "liblcf-wii", "wii")
}
