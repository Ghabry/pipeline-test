package easyrpg

@groovy.transform.InheritConstructors
class ToolchainBuild extends Build {
    def run(String job) {
        execute(job, null)
    }

    protected def prepare(String job, String system) {
        // prevent loading of environment
    }
}
