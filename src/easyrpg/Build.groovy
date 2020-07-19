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

    def checkout(String url, String branch = "") {
        if (branch.size() == 0) {
            if (scr.params.BRANCH != null) {
                branch = scr.params.BRANCH
            } else {
                branch = "master"
            }
        }

        scr.stage('Checkout') {
            if (branch.contains("/pr/")) {
                scr.checkout([$class: 'GitSCM',
                    branches: [[name: branch ]],
                    userRemoteConfigs: [[refspec: "+refs/pull/*:refs/remotes/origin/pr/*", url: url ]]
                ])
            } else {
                scr.checkout([$class: 'GitSCM',
                    branches: [[name: branch ]],
                    userRemoteConfigs: [[url: url]],
                    extensions: [[$class: 'CloneOption',
                        depth: 0,
                        noTags: false]]])
            }
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
                checkout(pipelineRepo, "master")

                prepare(job, system)

                env.env << envExtra
            }
        }

        scr.withEnv(env.makeEnv()) {
            scr.stage('Build') {
                build(job, system)
            }

            scr.stage('Artifacts') {
                collectArtifacts(system)
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

    protected def collectArtifacts(String system) {
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

        if (scr.params.BRANCH != null && scr.params.BRANCH.endsWith("-stable")) {
            for (artifact in artifacts) {
                def file = scr.readFile(file: artifact, encoding: "Base64")
                def outPath = "${scr.env.JENKINS_HOME}/ci-archives/$system"
                new File(outPath).mkdirs()
                def artName = new File("$artifact").getName()
                def outFile = new File("$outPath/$artName")
                outFile.bytes = file.decodeBase64()
            }
        }
    }
}
