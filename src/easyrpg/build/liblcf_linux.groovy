package easyrpg.build

node ("linux") {
    cleanWs()
    b = new easyrpg.LcfBuild(this)
    b.checkout("https://github.com/easyrpg/liblcf")
    b.run("liblcf-linux", "linux")
}

return this