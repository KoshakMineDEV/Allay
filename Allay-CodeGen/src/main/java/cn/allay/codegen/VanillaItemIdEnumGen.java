package cn.allay.codegen;

import cn.allay.dependence.Identifier;
import cn.allay.dependence.StringUtils;
import cn.allay.dependence.VanillaItemId;
import com.squareup.javapoet.*;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

import static cn.allay.codegen.VanillaItemClassGen.MAPPED_ITEM_DATA;


/**
 * Allay Project 2023/5/13
 *
 * @author daoge_cmd
 */
public class VanillaItemIdEnumGen {
    private static final ClassName STRING_CLASS = ClassName.get("java.lang", "String");
    private static final ClassName GETTER_CLASS = ClassName.get("lombok", "Getter");
    private static final String JAVA_DOC = """
            Automatically generated by {@code cn.allay.codegen.VanillaItemIdEnumGen} <br>
            Allay Project <p>
            @author daoge_cmd | Cool_Loong
            """;
    private static final String PACKAGE_NAME = "cn.allay.api.data";

    public static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        generateToDependenceModule();
        generateToAPIModule();
    }

    @SneakyThrows
    public static void generateToDependenceModule() {
        var identifierClass = ClassName.get("cn.allay.dependence", "Identifier");
        TypeSpec.Builder codeBuilder = commonBuilder(identifierClass);
        addEnums(codeBuilder);
        var javaFile = JavaFile.builder("cn.allay.dependence", codeBuilder.build()).build();
        Files.writeString(Path.of("Allay-CodeGen/src/main/java/cn/allay/dependence/VanillaItemId.java"), javaFile.toString());
    }

    @SneakyThrows
    public static void generateToAPIModule() {
        var identifierClass = ClassName.get("cn.allay.api.identifier", "Identifier");
        var itemTypeRegistryClass = ClassName.get("cn.allay.api.item.type", "ItemTypeRegistry");
        var itemTypeClass = ClassName.get("cn.allay.api.item.type", "ItemType");
        TypeSpec.Builder codeBuilder = commonBuilder(identifierClass).addMethod(MethodSpec.methodBuilder("getItemType")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $T.getRegistry().get(this.getIdentifier())", itemTypeRegistryClass)
                .returns(itemTypeClass)
                .build()
        );
        codeBuilder.addMethod(MethodSpec.methodBuilder("fromIdentifier")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Identifier.class, "identifier")
                .addCode("""
                        try{
                            return valueOf(identifier.path().toUpperCase(java.util.Locale.ENGLISH));
                        }catch(IllegalArgumentException ignore){
                            return null;
                        }""")
                .addAnnotation(Nullable.class)
                .returns(VanillaItemId.class)
                .build()
        );
        addEnums(codeBuilder);
        var javaFile = JavaFile.builder(PACKAGE_NAME, codeBuilder.build()).build();
        String result = javaFile.toString()
                .replace("public ItemType", "public ItemType<?>")
                .replace("cn.allay.dependence.Identifier", "cn.allay.api.identifier.Identifier")
                .replace("cn.allay.dependence.VanillaItemId", "cn.allay.api.data.VanillaItemId");
        Files.writeString(Path.of("Allay-API/src/main/java/cn/allay/api/data/VanillaItemId.java"), result);
    }

    private static void addEnums(TypeSpec.Builder codeBuilder) {
        for (var entry : MAPPED_ITEM_DATA.entrySet()) {
            var split = StringUtils.fastTwoPartSplit(
                    StringUtils.fastTwoPartSplit(entry.getKey(), ":", "")[1],
                    ".", "");
            var valueName = split[0].isBlank() ? split[1].toUpperCase() : split[0].toUpperCase() + "_" + split[1].toUpperCase();
            var blockId = CodeGen.ITEM_ID_TO_BLOCK_ID_MAP.get(entry.getKey());
            if (blockId == null) {
                codeBuilder.addEnumConstant(valueName, TypeSpec.anonymousClassBuilder("$S, $L", entry.getKey(), entry.getValue().getShort("id")).build());
            } else {
                codeBuilder.addEnumConstant(valueName, TypeSpec.anonymousClassBuilder("$S, $L, $S", entry.getKey(), entry.getValue().getShort("id"), blockId).build());
            }
        }
    }

    private static TypeSpec.Builder commonBuilder(ClassName identifierClass) {
        return TypeSpec.enumBuilder("VanillaItemId")
                .addJavadoc(JAVA_DOC)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec
                        .builder(identifierClass, "identifier", Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(GETTER_CLASS)
                        .build())
                .addField(FieldSpec
                        .builder(int.class, "runtimeId", Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(GETTER_CLASS)
                        .build())
                .addField(FieldSpec
                        .builder(identifierClass, "blockIdentifier", Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(GETTER_CLASS)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(STRING_CLASS, "identifier")
                        .addParameter(int.class, "runtimeId")
                        .addStatement("this(identifier, runtimeId, null)")
                        .build()
                )
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(STRING_CLASS, "identifier")
                        .addParameter(int.class, "runtimeId")
                        .addParameter(STRING_CLASS, "blockIdentifier")
                        .addStatement("this.identifier = new $T(identifier)", identifierClass)
                        .addStatement("this.runtimeId = runtimeId")
                        .addStatement("this.blockIdentifier = blockIdentifier != null ? new $T(blockIdentifier) : null", identifierClass)
                        .build()
                )
                .addMethod(MethodSpec.methodBuilder("hasBlock")
                        .returns(boolean.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return blockIdentifier != null")
                        .build()
                );
    }
}
