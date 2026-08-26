# Artifact structure

This repository uses Devrock/Hiconic artifacts. A new artifact is not complete merely because it has a `pom.xml` and compiles on the command line. Keep its build, IDE and generated-source descriptors consistent with an existing artifact of the same kind.

## Every Java artifact

- `pom.xml` with the repository parent, artifact version and explicit dependencies.
- `build.xml` importing the appropriate shared Ant script (`library-ant-script`, `gm-api-ant-script`, `model-ant-script`, or the dedicated module/test script).
- `src/` for sources and classpath resources.
- `.project` with the Java nature and the Artifact Reflection builder before the Java builder.
- `.classpath` containing `src`, the JRE container, `Braintribe.ArtifactClasspathContainer`, `classes` output and exported `class-gen` library.
- `.gitignore` covering `/classes`, `/build` and `/dist`.
- `class-gen/.gitignore` containing `*` and `!.git*/classes`, so the required generated-source directory exists in a fresh checkout while generated content stays untracked.
- Do not add `.container.cfg.xml` to new artifacts. It is a legacy Malaclypse Eclipse descriptor, not part of the current Devrock project contract.

## GM model artifacts

- Set `<archetype>model</archetype>` when that is the convention of the group, or use the existing model Ant script and `asset.man` conventions of this repository.
- Add the Model Builder and Model Nature to `.project`.
- Never hand-edit generated model classes in `class-gen`.

## Metadata annotation artifacts

- Keep annotations in a dedicated `*-metadata-annotations` artifact depending on the corresponding metadata model.
- Put annotation-to-metadata declarations in `src/META-INF/gmf.mda`.
- The annotation artifact is a functional dependency of models using its annotations; it must not become a model dependency.
- Annotation defaults must preserve metadata semantics. Use `@NullDefault` with `AnnotationDefaults.NULL_STRING` for nullable strings; do not use an empty string as a stand-in for `null`.
- Add a conversion test that obtains the registered `MdaHandler` and verifies the constructed metadata, independently of CX or RX wiring.

## Test artifacts

- Depend on the repository's established JUnit generation (`gm-unit-test-deps` here) and keep Eclipse on JUnit 4 unless the whole line is intentionally migrated.
- Test artifacts are ordinary Java artifacts and require the same IDE/build descriptors and `class-gen` placeholder.

## Resource-only and aggregator artifacts

- Resource-only artifacts still need the build descriptor and the artifact-specific `asset.man`/POM markers, but do not invent Java source/classpath descriptors unless the artifact is intentionally importable as a Java project.
- Aggregators contain a `pom.xml`, `build.xml` and their asset declaration; they do not need fake Java sources.

Before handing off a structural change, compare `find <artifact> -maxdepth 2 -type f` with a known-good sibling, run `git diff --check`, and build/test through `hc` rather than plain Maven or Eclipse Ant.
