# PLOMBA

We use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/), e.g. `feat: add appointment endpoint`.
After cloning, run `git config core.hooksPath .githooks`. Java code will be formatted automatically on commit, and Jenkins additionally checks formatting.
The hook formats the staged files. If you staged only part of a file, it will ask you to stage the whole file or stash the remaining changes.

## Requirements

- JDK 25.
- Docker with Docker Compose.
- Bash/Zsh, or Git Bash on Windows, to run the commands below.

The Maven Wrapper in the repository downloads Maven 3.10.0.

## Starting Nexus

From the repository root:

```bash
docker volume create nexus-data
docker compose -f nexus/compose.yaml up -d --wait
```

The dashboard is available at <http://localhost:8081>. The first startup may take a few minutes.

On the first run of a new instance, read the administrator password:

```bash
docker compose -f nexus/compose.yaml exec nexus cat /nexus-data/admin.password
```

Log in as `admin`, complete the setup wizard, change the password, and enable anonymous read access to libraries.
Check that the `maven-public` group contains `maven-central`, `maven-snapshots`, and `maven-releases`.
Nexus data is persisted in the `nexus-data` volume.

## Building and running the application

From the repository root:

```bash
export NEXUS_URL=http://localhost:8081
cd PLOMBA/plomba
sh mvnw -s ../../maven-settings.xml clean verify
java -jar target/plomba-0.0.1-SNAPSHOT.jar
```

`clean verify` builds the application and runs the tests. The shared `maven-settings.xml` file routes library downloads through Nexus.
If you use a shared Nexus server, set its address in `NEXUS_URL` instead of `localhost`.

Once the application is running, check <http://localhost:8080/hello>.
Stop the application with `Ctrl+C`.

Stop Nexus from the repository root:

```bash
docker compose -f nexus/compose.yaml stop
```