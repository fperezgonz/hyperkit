allprojects {
    group = "solutions.sulfura"
    version = "1.3.0-SNAPSHOT"
}

tasks.register("publish") {
    dependsOn(subprojects.map { it.tasks.named("publish") })
}

tasks.register("publishMavenPublicationToMavenLocal") {
    dependsOn(subprojects.map { it.tasks.named("publishMavenPublicationToMavenLocal") })
}