node ("windows") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-win32", "liblcf-win32", "windows")
}
