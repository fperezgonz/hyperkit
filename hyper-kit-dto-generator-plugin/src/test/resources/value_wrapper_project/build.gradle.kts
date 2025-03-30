plugins{
    id("solutions.sulfura.hyper-kit-dto-generator-plugin")
}

genD{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="java.util.Optional"
    valueWrapperDefaultValue="Optional.ofNullable(null)"
}