package com.example.data

import androidx.compose.ui.graphics.Color

data class CustomOption(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val color: Color = Color.Unspecified,
    val isUnlocked: Boolean = true,
    val starCost: Int = 0
)

object CustomizationData {
    val FurColors = listOf(
        CustomOption("white", "Blanco Nieve", "🤍", Color(0xFFFAFAFA)),
        CustomOption("cream", "Crema Vainilla", "🧈", Color(0xFFFFF0C4)),
        CustomOption("gray", "Gris Nube", "☁️", Color(0xFFD1D5DB)),
        CustomOption("peach", "Melocotón", "🍑", Color(0xFFFFC6A5)),
        CustomOption("lilac", "Lila Mágico", "🔮", Color(0xFFE5D0FF)),
        CustomOption("mint", "Menta Pastel", "🌿", Color(0xFFBFF0DA)),
        CustomOption("cinnamon", "Canela Cálido", "🥨", Color(0xFFDCA876)),
        CustomOption("black", "Noche Azabache", "🌙", Color(0xFF4A4E69))
    )

    val Outfits = listOf(
        CustomOption("none", "Sin Ropa", "🐾"),
        CustomOption("hoodie_purple", "Hoodie Lila", "🧥", Color(0xFF9D4EDD)),
        CustomOption("sweater_striped", "Suéter Rayado", "🧶", Color(0xFFFF9EAA)),
        CustomOption("pajamas_stars", "Pijama Estrellas", "🌙", Color(0xFF7209B7)),
        CustomOption("tee_heart", "Polera Corazón", "👕", Color(0xFFFF4D6D)),
        CustomOption("hero_cape", "Capa Mágica", "🦸", Color(0xFF3A86FF)),
        CustomOption("denim_overalls", "Overol Denim", "👖", Color(0xFF48CAE4)),
        CustomOption("flower_dress", "Vestido Flores", "👗", Color(0xFFFFB703))
    )

    val Accessories = listOf(
        CustomOption("none", "Sin Accesorio", "✨"),
        CustomOption("pink_bow", "Moño Coquette", "🎀", Color(0xFFFF85A1)),
        CustomOption("glasses", "Lentes Redondos", "👓", Color(0xFF333333)),
        CustomOption("frog_hat", "Gorrito de Rana", "🐸", Color(0xFF70E000)),
        CustomOption("crown", "Corona Real", "👑", Color(0xFFFFD700)),
        CustomOption("cherry_flower", "Flor de Cerezo", "🌸", Color(0xFFFFB3C6)),
        CustomOption("party_hat", "Gorro de Fiesta", "🎉", Color(0xFFFF70A6)),
        CustomOption("headphones", "Audífonos Gamer", "🎧", Color(0xFF80FFDB))
    )

    val Cushions = listOf(
        CustomOption("purple", "Morado Misu", "🟪", Color(0xFFB19FFB)),
        CustomOption("pink", "Rosa Algodón", "🌸", Color(0xFFFFB3C6)),
        CustomOption("mint", "Menta Fresca", "🍃", Color(0xFF99E2D0)),
        CustomOption("yellow", "Amarillo Sol", "☀️", Color(0xFFFFE58F)),
        CustomOption("blue", "Azul Cielo", "🌊", Color(0xFF90E0EF))
    )

    val RoomThemes = listOf(
        CustomOption("cozy_room", "Cuarto Acogedor", "🛋️"),
        CustomOption("flower_garden", "Jardín Botánico", "🪴"),
        CustomOption("starry_night", "Noche Estrellada", "🌌"),
        CustomOption("warm_sunset", "Atardecer Cálido", "🌅"),
        CustomOption("pastel_dream", "Sueño Pastel", "☁️")
    )

    val Collars = listOf(
        CustomOption("bell_red", "Cascabel Rojo", "🔔", Color(0xFFFF4D6D)),
        CustomOption("pendant_heart", "Dije Corazón", "💖", Color(0xFFFFD700)),
        CustomOption("ribbon_gold", "Cinta Dorada", "🎗️", Color(0xFFFFB703)),
        CustomOption("none", "Sin Collar", "❌")
    )
}
