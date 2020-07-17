package easyrpg

abstract class Build {
    protected final Script scr
    protected final Env env
    protected String tmpDir

    public envExtra = [:]
    public pipelineRepo = "https://github.com/ghabry/pipeline-test"

    Build(Script script, Env env = null) {
        this.scr = script
        if (env == null) {
            this.env = new Env(script)
        } else {
            this.env = env
        }
    }

    def checkout(String url, String branch = "master") {
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

    def makeEnv(env) {
        return env.collect { entry ->
            "${entry.key}=${entry.value}"
        }
    }

    protected def execute(String job, String system) {
        tmpDir = scr.pwd(tmp: true) + "/scripts"

        env.env["CIBUILD"] = 1
        env.env["ROOTDIR"] = new File("${scr.env.WORKSPACE}/..").getCanonicalPath()
        env.env["BASEDIR"] = scr.pwd()

        scr.stage('Prepare') {
            scr.dir(tmpDir) {
                checkout(pipelineRepo)

                prepare(job, system)

                env.env << envExtra
            }
        }

        scr.withEnv(env.makeEnv()) {
            scr.stage('Build') {
                build(job, system)
            }

            scr.stage('Artifacts') {
                collectArtifacts()
            }
        }
    }

    protected def prepare(String job, String system) {
        scr.dir("$tmpDir") {
            env.load("buildscripts/env/$system")
            env.load("buildscripts/env/$job")
        }
    }

    protected def build(String job, String system) {
        scr.sh """
        . "$tmpDir/buildscripts/env/$system"
        $tmpDir/buildscripts/build.sh ${job}
        """
    }

    protected def collectArtifacts() {
        if (!scr.fileExists("artifacts")) {
            scr.echo "No artifacts"
            return
        }

        def artifacts = scr.readFile("artifacts").split("\n")
        for (artifact in artifacts) {
            if (artifact.size() > 0) {
                scr.echo "Archiving $artifact"
                scr.archiveArtifacts artifacts: artifact
            }
        }
    }
}
