node ("linux") {
    cleanWs()
    b = new easyrpg.Build(this)
    b.checkout("https://github.com/easyrpg/liblcf")
    b.run(env.JOB_BASE_NAME, "linux")
}
