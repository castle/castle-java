
Releasing
=========

1. Create branch `release-X.Y.Z`.
2. Update the package version to the new version in `pom.xml` and `README.md` files.
3. Update `CHANGELOG.md` for the impending release.
4. `git commit -am "release X.Y.Z."` (where X.Y.Z is the new version).
5. Push to Github, make PR, and when ok, merge.
6. Make a release on Github, specify tag as `vX.Y.Z` to create a tag. Copy the Changelog entry to the release description.
7. Run `mvn -P castle-java-sdk,deploy clean deploy`.
