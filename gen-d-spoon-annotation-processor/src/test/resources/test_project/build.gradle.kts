plugins{
    id("solutions.sulfura.gen-d-spoon-annotation-processor")
}

genD{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="io.vavr.control.Option"
    valueWrapperDefaultValue="Option.none()"
}