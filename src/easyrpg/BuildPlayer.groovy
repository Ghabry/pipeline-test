package easyrpg

@groovy.transform.InheritConstructors
class BuildPlayer extends Build {
    def run(String job, String lcfJob, String system) {
        if ("XXX".endsWith("-stable")) {
            def branch = "XXX".replace("-stable", "").replace("-", ".")
            scr.echo "Stable branch. Building liblcf ${branch}"

            scr.dir(scr.pwd(tmp: true) + "/liblcf") {
                checkout("https://github.com/easyrpg/liblcf", branch)
                run(lcfJob, system)
            }
        }

        run(job, system)
    }
}
