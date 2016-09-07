node {

   stage 'Checkout'
   checkout scm

   stage 'Build'
   sh './mvnw clean install'
   
   stage 'Archive artifact'
   archiveArtifacts artifacts: 'target/newsreadr-server*.jar', fingerprint: true

}