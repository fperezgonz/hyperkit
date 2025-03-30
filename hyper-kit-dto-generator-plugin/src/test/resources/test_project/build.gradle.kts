plugins{
    id("solutions.sulfura.hyper-kit-dto-generator-plugin")
}

genD{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="io.vavr.control.Option"
    valueWrapperDefaultValue="Option.none()"
}