package cn.allay.codegen;

import cn.allay.dependence.Identifier;
import com.squareup.javapoet.*;
import lombok.SneakyThrows;

import javax.lang.model.element.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

import static cn.allay.codegen.CodeGen.BLOCK_PALETTE_NBT;

/**
 * Allay Project 2023/3/26
 *
 * @author daoge_cmd | Cool_Loong
 */
public class VanillaBlockIdEnumGen {
    private static final ClassName stringClass = ClassName.get("java.lang", "String");
    private static final ClassName getterClass = ClassName.get("lombok", "Getter");
    private static final String javaDoc = """
            Automatically generated by {@code cn.allay.codegen.VanillaBlockIdEnumGen} <br>
            Allay Project <p>
            @author daoge_cmd | Cool_Loong
            """;

    public static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        generateDependence();
        generateAPI();
    }

    @SneakyThrows
    public static void generateDependence() {
        var identifierClass = ClassName.get("cn.allay.dependence", "Identifier");
        TypeSpec.Builder codeBuilder = commonBuilder(identifierClass);
        addEnums(codeBuilder);
        var javaFile = JavaFile.builder("cn.allay.dependence", codeBuilder.build()).build();
        Files.writeString(Path.of("Allay-CodeGen/src/main/java/cn/allay/dependence/VanillaBlockId.java"), javaFile.toString());
    }

    @SneakyThrows
    public static void generateAPI() {
        var identifierClass = ClassName.get("cn.allay.api.identifier", "Identifier");
        var blockTypeRegistryClass = ClassName.get("cn.allay.api.block.type", "BlockTypeRegistry");
        var blockTypeClass = ClassName.get("cn.allay.api.block.type", "BlockType");
        TypeSpec.Builder codeBuilder = commonBuilder(identifierClass)
                .addMethod(MethodSpec.methodBuilder("getBlockType")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return $T.getRegistry().get(this.getIdentifier())", blockTypeRegistryClass)
                        .returns(blockTypeClass)
                        .build()
                );
        addEnums(codeBuilder);
        var javaFile = JavaFile.builder("cn.allay.api.data", codeBuilder.build()).build();
        Files.writeString(Path.of("Allay-API/src/main/java/cn/allay/api/data/VanillaBlockId.java"), javaFile.toString().replace("public BlockType", "public BlockType<?>"));
    }

    private static void addEnums(TypeSpec.Builder codeBuilder) {
        var sortedidentifier = BLOCK_PALETTE_NBT.stream().map(block -> block.getString("name")).sorted(String::compareTo).map(Identifier::new).toList();
        for (var identifier : sortedidentifier) {
            codeBuilder.addEnumConstant(identifier.path().toUpperCase(), TypeSpec.anonymousClassBuilder("$S", identifier.toString()).build());
        }
    }

    private static TypeSpec.Builder commonBuilder(ClassName identifierClass) {
        return TypeSpec.enumBuilder("VanillaBlockId")
                .addJavadoc(javaDoc)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec
                        .builder(identifierClass, "identifier", Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(getterClass)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(stringClass, "identifier")
                        .addStatement("this.$N = new $T($N)", "identifier", identifierClass, "identifier")
                        .build()
                );
    }
}
