# Release Checklist

- [ ] Ensure all bom/pom.xml dependencies are up to date
  - [ ] jakarta.jakartaee-bom.version points to staged version for profile/platform release
  - [ ] jakarta.tck.arquillian.version points to released version of Arquillian TCK protocol artifacts
  - [ ] jakarta.tck.common.version points to released version of common TCK artifacts
  - [ ] jakarta.tck.sigtest.version points to released version of signaturetest TCK artifacts
- [ ] Ensure all pom.xml properties duplicated from bom/pom.xml match
    - [ ] jakarta.jakartaee-bom.version
    - [ ] jakarta.tck.arquillian.version
    - [ ] jakarta.tck.common.version
    - [ ] jakarta.tck.sigtest.version
- [ ] Ensure the release/README.adoc has updated release note highlights
- [ ] Ensure the release/README.adoc has updated excluded tests
- [ ] Drop any previously staged release using its staging repository id and the
  https://ci.eclipse.org/jakartaee-tck/job/DropStagingRepo/ job if this is a restage
- [ ] Delete any previous tag before running the https://ci.eclipse.org/jakartaee-tck/job/11/job/stage-artifacts/job/TCKDistRelease/ job if this is a restage