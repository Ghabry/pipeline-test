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