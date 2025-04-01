plugins{
    id("solutions.sulfura.hyperkit-dto-generator-plugin")
}

hyperKit{
    inputPaths=setOf("src/test_input_sources/")
    rootOutputPath="src/out/java"
    valueWrapperType="io.vavr.control.Option"
    valueWrapperDefaultValue="Option.none()"
}