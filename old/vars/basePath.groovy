@groovy.transform.SourceURI
basePath

@NonCPS
def call() {
    return new File(getClass().protectionDomain.codeSource.location.path).parent
}
