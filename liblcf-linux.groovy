node('linux') {
    e = new env.Linux(this)
    b = new build.AutotoolsLcf(this, e)

    cleanWs()

    b.checkout("https://github.com/easyrpg/liblcf", "master")
    b.runPipeline()
}
