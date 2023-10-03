import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("idea")
    id("com.google.protobuf")
}

val errorProneAnnotations: String by project
val tomcatAnnotationsApi: String by project

dependencies {
    implementation("ch.qos.logback:logback-classic")

    implementation("io.grpc:grpc-netty")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.errorprone:error_prone_annotations:$errorProneAnnotations")

    implementation("org.apache.tomcat:annotations-api:$tomcatAnnotationsApi")
}


var filesBaseDir = "$projectDir/build/generated"
var protoSrcDir = "$projectDir/build/generated/proto"
var grpcSrcDir = "$projectDir/build/generated/grpc"

sourceSets {
    main {
        proto {
            srcDir(protoSrcDir)
        }
        java {
            srcDir(grpcSrcDir)
        }
    }
}

idea {
    module {
        sourceDirs = sourceDirs.plus(file(protoSrcDir));
        sourceDirs = sourceDirs.plus(file(grpcSrcDir));
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.56.1"

        }
    }

    generatedFilesBaseDir = filesBaseDir
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

afterEvaluate {
    tasks {
        getByName("generateProto").dependsOn(processResources)
    }
}
