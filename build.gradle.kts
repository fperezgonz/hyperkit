allprojects {
    group = "solutions.sulfura"
    version = "2.0.1-RELEASE"
}

tasks.register("publish") {
    dependsOn(subprojects.map { it.tasks.named("publish") })
}

tasks.register("publishMavenPublicationToMavenLocal") {
    dependsOn(subprojects.map { it.tasks.named("publishMavenPublicationToMavenLocal") })
}