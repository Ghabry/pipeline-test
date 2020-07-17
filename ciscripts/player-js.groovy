node ("emscripten") {
    cleanWs()
    b = new easyrpg.PlayerBuild(this)
    b.checkout("https://github.com/easyrpg/player")
    b.run("player-js", "liblcf-js", "emscripten")
}
