#!groovy
properties(
        [[$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/hmcts/div-document-generator-client'],
         pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

@Library(['Divorce', 'Reform'])
import uk.gov.hmcts.Packager
import uk.gov.hmcts.Versioner

Packager packager = new Packager(this, 'divorce')
Versioner versioner = new Versioner(this)

def channel = '#div-dev'


timestamps {
    lock(resource: "div-document-generator-${env.BRANCH_NAME}", inversePrecedence: true) {
        node {
            try {
                def version

                def onDevelopOrMaster = env.BRANCH_NAME == "master" || env.BRANCH_NAME == "develop"

                String divorceDocumentGeneratorRPMVersion

                stage('Checkout') {
                    deleteDir()
                    checkout scm
                    env.CURRENT_SHA = gitSha()
                }


                stage('Build') {

                    onDevelop {
                        sh "./gradlew clean developAddRelaseSuffix build -x test"
                    }

                    onPR {
                        sh "./gradlew clean developAddRelaseSuffix build -x test"
                    }

                    onMaster {
                        sh "./gradlew clean build -x test"
                    }
                }

                stage('OWASP dependency check') {
                    try {
                        sh "./gradlew -DdependencyCheck.failBuild=true dependencyCheck"
                    } catch (ignored) {
                        archiveArtifacts 'build/reports/dependency-check-report.html'
                        notifyBuildResult channel: channel, color: 'warning',
                                message: 'OWASP dependency check failed see the report for the errors'
                    }
                }

                stage('Test (Unit)') {
                    try {
                        sh "./gradlew test"
                    } finally {
                        junit 'build/test-results/test/**/*.xml'
                    }
                }

                stage('Mutation Testing (Pitest)') {
                    onPR {
                        sh "./gradlew pitest"
                    }
                }

                stage('Sonar') {
                    onPR {
                        sh "./gradlew -Dsonar.analysis.mode=preview -Dsonar.host.url=$SONARQUBE_URL sonarqube"
                    }

                    if (onDevelopOrMaster) {
                        sh "./gradlew -Dsonar.host.url=$SONARQUBE_URL sonarqube"
                    }
                }

                stage('Package (JAR)') {
                    versioner.addJavaVersionInfo()
                    sh "./gradlew installDist bootRepackage"
                }

                stage('Package (Docker)') {
                    divorceDocumentGeneratorVersion = dockerImage imageName: 'divorce/div-document-generator'
                }

                stage('Package (RPM)') {

                    if(onDevelopOrMaster) {
                        divorceDocumentGeneratorRPMVersion = packager.javaRPM('div-document-generator',
                                'build/libs/div-document-generator-$(./gradlew -q printVersion).jar',
                                'springboot',
                                'src/main/resources/application.yml')
                        println("DivorceDocumentGeneratorPRMVersion " + divorceDocumentGeneratorRPMVersion)

                        version = "{div_document_generator_buildnumber: ${divorceDocumentGeneratorRPMVersion} }"

                        packager.publishJavaRPM('div-document-generator')
                    }

                }

            onDevelop {
                deploy(divorceDocumentGeneratorRPMVersion, 'dev')
            }

            onMaster() {
                deploy(divorceDocumentGeneratorRPMVersion, 'test')
            }

        } catch (err) {
            notifyBuildFailure channel: channel
            throw err
        } finally {
            step([$class           : 'InfluxDbPublisher',
                  customProjectName: 'Div Document Generator',
                  target           : 'Jenkins Data'])
        }
    }
}

notifyBuildFixed channel: channel
}

private void deploy(version, onEnv) {
    lock(resource: "Divorce-deploy-" + onEnv, inversePrecedence: true) {
        stage('Deploy ') {
            deploy app: 'div-document-generator', version: version, sha: env.CURRENT_SHA, env: onEnv
        }
    }
}
