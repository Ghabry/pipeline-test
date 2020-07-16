def call(branch, url, url_default) {
    def b = branch ?: 'master'
    def u = url ?: url_default

    git branch: b, url: u
}
