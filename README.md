# PLOMBA

We use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/), e.g. `feat: add appointment endpoint`.
After cloning, run `git config core.hooksPath .githooks`. Java code will be formatted automatically on commit, and Jenkins additionally checks formatting.
The hook formats the staged files. If you staged only part of a file, it will ask you to stage the whole file or stash the remaining changes.

## Requirements

- JDK 25.
- Docker with Docker Compose.
- Bash/Zsh, or Git Bash on Windows, to run the commands below.

The Maven Wrapper in the repository downloads Maven 3.10.0.

## Docker network

Jenkins and Nexus run in separate containers and communicate over a shared Docker network.
Create it once, before starting either of them:

```bash
docker network create plomba-ci
```

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

## Deploy user for Jenkins

Jenkins publishes the built application to Nexus with a dedicated user, not with admin.
In Settings → Security:

1. Roles → Create Role (Nexus role), ID and name plomba-deployer, with these privileges:
    - nx-repository-view-maven2-maven-releases-add, -edit, -read, -browse
    - nx-repository-view-maven2-maven-snapshots-add, -edit, -read, -browse
2. Users → Create local user, ID jenkins, status Active, role plomba-deployer.

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

## Starting Jenkins
Jenkins is configured in code:

- jenkins/plugins.txt - installed plugins,
- jenkins/casc.yaml - admin account, tools (jdk-25, maven-3), credentials (github-creds, nexus-creds) and the plomba-pipeline job,
- jenkins/.env - your passwords and tokens (not in the repository).

Start Nexus first, because the pipeline downloads libraries from it and publishes the application to it.

On the first run, create your .env file from the template and fill in the values:
```bash
cp jenkins-ci/.env.example jenkins-ci/.env
```

You need a classic GitHub token with the public_repo and repo:status scopes, and the password of the jenkins user in Nexus.

From the repository root:
```bash
docker compose -f jenkins-ci/docker-compose.yml up -d --build
```

The dashboard is available at http://localhost:8082.
Log in with JENKINS_ADMIN_ID and JENKINS_ADMIN_PASSWORD from jenkins/.env. The plomba-pipeline job is already there.
Jenkins data is persisted in the jenkins-ci_jenkins_home volume.

The pipeline:

1. Check formatting – runs spotless:check and stops the build if the code is not formatted.
2. Build and test – builds the application with JDK 25 and runs the tests. Test results and JaCoCo coverage are shown in Jenkins.
3. Deploy to Nexus – publishes the .jar file to the maven-snapshots repository.