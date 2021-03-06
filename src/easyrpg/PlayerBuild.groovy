package easyrpg

@groovy.transform.InheritConstructors
class PlayerBuild extends Build {
    def skip_artifacts = false

    def run(String job, String lcfJob, String system) {
        def branch = scr.params.BRANCH

        if (branch != null && branch.endsWith("-stable")) {
            // Handle stable build
            branch = branch.replace("-stable", "").replace("-", ".")
            scr.echo "Stable branch. Building liblcf ${branch}"

            scr.dir(scr.pwd(tmp: true) + "/liblcf") {
                checkout("https://github.com/easyrpg/liblcf", branch)
                skip_artifacts = true
                execute(lcfJob, system)
                skip_artifacts = false

                env.paths.add(0, "${env.env['BASEDIR']}/install")
                env.env["LIBLCF_DIR"] = "${env.env['BASEDIR']}/install"
            }
        }

        execute(job, system)
    }

    protected def collectArtifacts(String system) {
        if (!skip_artifacts) {
            super.collectArtifacts(system)
        }
    }
}
