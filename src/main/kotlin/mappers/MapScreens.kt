package mappers

sealed class Screen {
    object Home : Screen() // Tela inicial
    object LadderOfSuccess : Screen()
    object Report2 : Screen()
    object Settings : Screen()
    object Option1 : Screen()
    object Option7 : Screen()
    object Option8 : Screen()
    object Option9 : Screen()
}