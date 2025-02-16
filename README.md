# StrippableBlocks API

**StrippableBlocks API** is a lightweight Fabric API that enables blocks without an `axis` property to be stripped, similar to Minecraft's `StrippableBlockRegistry`. This is particularly useful for custom wood-based blocks, such as log pedestals, that don't require directional properties but should still be strippable.

## Features

- **Custom Strippable Blocks**: Register blocks that lack an `axis` property to be strippable.
- **Seamless Integration**: Operates alongside `StrippableBlockRegistry` without conflicts.
- **Lightweight and Efficient**: Designed for easy integration and minimal performance impact.

## Installation

### As a Dependency

To include **StrippableBlocks API** in your Fabric mod:

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/WhiteTowerGames/StrippableBlocksAPI.git
   ```

2. **Include in Your Project**:

   - If using Gradle, add the following to your `settings.gradle`:

     ```gradle
     include ':StrippableBlocksAPI'
     project(':StrippableBlocksAPI').projectDir = file('../StrippableBlocksAPI')
     ```

   - Then, in your `build.gradle`:

     ```gradle
     dependencies {
         implementation project(':StrippableBlocksAPI')
     }
     ```

   Adjust the path as necessary based on your project's structure.

### Bundled with Mods

If you're using a mod like **Pedestals** that already includes **StrippableBlocks API**, no separate installation is required.

## Usage

To register a strippable block:

```java
public class ModBlocks {

    public static final UNSTRIPPED_WOOD_BLOCK = registerBlock( "unstripped_wood_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()));

    public static final STRIPPED_WOOD_BLOCK = registerBlock( "stripped_wood_block",
                new Block(AbstractBlock.Settings.create()
                        .strength(2.0f)
                        .sounds(BlockSoundGroup.WOOD)
                        .burnable()));


    public static void registerModBlocks(){
        StrippableCustomRegistry.register(UNSTRIPPED_WOOD_BLOCK, STRIPPED_WOOD_BLOCK);
    }
}
```

This ensures that right-clicking the specified block with an axe will convert it to its stripped variant.

## Compatibility

- **Minecraft Version**: `1.21+`
- **Fabric API Required**: Yes

## License

This project is licensed under the CC0-1.0 License. For more details, refer to the [LICENSE](https://github.com/WhiteTowerGames/StrippableBlocksAPI/blob/master/LICENSE) file.

## Contributing

Contributions are welcome! If you encounter any issues or have suggestions, please open an issue or submit a pull request on the [GitHub repository](https://github.com/WhiteTowerGames/StrippableBlocksAPI).
