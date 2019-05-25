package com.zelkatani.model

import java.io.File

// TODO: trees need to be added if they're useful
/**
 * A model for the "map/" folder. Everything is relative to the folder.
 */
data class WorldMap(
    val default: Default,
    val definition: Definition,
    val provincesBMP: Bitmap,
    val terrainBMP: Bitmap,
    val riversBMP: Bitmap,
    val positions: Positions,
    val terrainDefinition: Terrain,
    val continent: Continent,
    val region: Region
) {
    companion object : ModelBuilder<WorldMap> {
        override fun from(file: File): WorldMap {
            val default = Default.from(file.resolve("default.map"))

            // A dumb hack to resolve Paradox's lazy hack...
            // v2/mod/Napoleon's Legacy/map -> v2/map
            val gameMapFolder = file.resolve("../../../map")
            val definitionFile = gameMapFolder.resolve(default.definitions)
            // this just brings us back to the

            val definition = Definition.from(definitionFile)
            val provincesBMP = Bitmap.from(file.resolve(default.provinces))
            val terrainBMP = Bitmap.from(file.resolve(default.terrain))
            val riversBMP = Bitmap.from(file.resolve(default.rivers))
            val positions = Positions.from(file.resolve(default.positions))
            val terrainDefinition = Terrain.from(file.resolve(default.terrainDefinition))
            val continent = Continent.from(file.resolve(default.continent))
            val region = Region.from(file.resolve(default.region))

            return WorldMap(
                default,
                definition,
                provincesBMP,
                terrainBMP,
                riversBMP,
                positions,
                terrainDefinition,
                continent,
                region
            )
        }
    }
}