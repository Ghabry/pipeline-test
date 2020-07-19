node ("emscripten") {
    cleanWs()
    b = new easyrpg.ToolchainBuild(this)
    b.checkout("https://github.com/easyrpg/buildscripts")
    b.run(env.JOB_BASE_NAME)
}
