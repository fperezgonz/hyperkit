plugins{
    id("solutions.sulfura.gen-d-spoon-annotation-processor")
}

genD{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="java.util.Optional"
    valueWrapperDefaultValue="Optional.ofNullable(null)"
}