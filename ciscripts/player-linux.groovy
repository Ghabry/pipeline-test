node ("linux") {
    cleanWs()
    b = new easyrpg.BuildPlayer(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-linux", "liblcf-linux", "linux")
}
