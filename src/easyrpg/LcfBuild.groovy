package easyrpg

@groovy.transform.InheritConstructors
class LcfBuild extends Build {
    def run(String job, String system) {
        execute(job, system)
    }
}
