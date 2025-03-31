plugins{
    id("solutions.sulfura.hyper-kit-dto-generator-plugin")
}

hyperKit{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="java.util.Optional"
    valueWrapperDefaultValue="Optional.ofNullable(null)"
}