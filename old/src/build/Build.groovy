package build

/** Base class describing a build process */
abstract class Build {
    protected final Script scr
    protected final env.Env env

    Build(Script script, env.Env env) {
        this.scr = script
        this.env = env
    }

    def getBuildDir() {
        return "${scr.pwd()}/build"
    }

    def getInstallDir() {
        return "${scr.pwd()}/install"
    }

    def cmd(String command) {
        if (getCommandPrefix() != "") {
            scr.sh "${getCommandPrefix()};${command}"
        } else {
            scr.sh command
        }
    }

    def start() {}

    def checkout(String url, String branch) {
        scr.stage('Checkout') {
            //scr.git branch: branch, url: url
            scr.checkout([$class: 'GitSCM',
                branches: [[name: branch ]],
                userRemoteConfigs: [[url: url]],
                extensions: [[$class: 'CloneOption',
                    depth: 0,
                    noTags: false]]])
        }
    }

    def preBuild() {}

    def stageBuildConfigure() {}

    def stageBuildMake() {}

    def postBuild() {}

    def stageCollectArtifacts() {}

    def postCollectArtifacts() {}

    def finish() {}

    def getCommandPrefix() {
        return ""
    }

    def runPipeline() {
        scr.withEnv(env.makeEnv()) {
            start()
            preBuild()
            scr.stage('Build') {
                stageBuildConfigure()
                stageBuildMake()
            }
            env.paths.add(0, "${scr.pwd()}/install");
            postBuild()

            scr.stage('Artifacts') {
                stageCollectArtifacts()
            }
            postCollectArtifacts()
            finish()
        }
    }
}
