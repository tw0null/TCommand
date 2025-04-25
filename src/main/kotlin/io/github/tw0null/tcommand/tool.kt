
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

fun saveDataToYAML(plugin: JavaPlugin, key: String, location: Location) {
    val file = File(plugin.dataFolder, "data.yml")
    val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)

    val locationMap = mapOf(
        "world" to location.world?.name,
        "x" to location.x,
        "y" to location.y,
        "z" to location.z,
        "yaw" to location.yaw,
        "pitch" to location.pitch
    )

    config.set(key, locationMap)
    config.save(file)
}


fun removeDataFromYAML(plugin: JavaPlugin, key: String) {
    val file = File(plugin.dataFolder, "data.yml")
    if (!file.exists()) return

    val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)

    if (config.contains(key)) {
        config.set(key, null)
        config.save(file)
    }
}


fun loadAllDataFromYAML(plugin: JavaPlugin): Map<String, Location> {
    val file = File(plugin.dataFolder, "data.yml")
    if (!file.exists()) return emptyMap()

    val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
    val locations = mutableMapOf<String, Location>()

    for (key in config.getKeys(false)) {
        val worldName = config.getString("$key.world") ?: continue
        val world = plugin.server.getWorld(worldName) ?: continue
        val x = config.getDouble("$key.x")
        val y = config.getDouble("$key.y")
        val z = config.getDouble("$key.z")
        val yaw = config.getDouble("$key.yaw").toFloat()
        val pitch = config.getDouble("$key.pitch").toFloat()

        locations[key] = Location(world, x, y, z, yaw, pitch)
    }

    return locations
}

fun loadHomeLocationFromYAML(plugin: JavaPlugin): Location? {
    val locations = loadAllDataFromYAML(plugin)
    return locations["home"]
}