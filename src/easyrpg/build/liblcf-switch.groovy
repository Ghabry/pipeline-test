node ("switch") {
    cleanWs()
    b = new easyrpg.LcfBuild(this)
    b.checkout("https://github.com/easyrpg/liblcf")
    b.run(env.JOB_BASE_NAME, "switch")
}
