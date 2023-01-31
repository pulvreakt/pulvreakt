const publishCmd = `
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md || exit 1
git push --force origin \${nextRelease.version} || exit 2
./gradlew -PstagingRepoId=\${process.env.STAGING_REPO_ID} releaseStagingRepositoryOnMavenCentral || exit 3
`;

const config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
  [
    "@semantic-release/exec",
    {
      "publishCmd": publishCmd,
    }
  ],
  "@semantic-release/github",
  "@semantic-release/git",
)
module.exports = config
