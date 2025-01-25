class Robot(private var x: Int, private var y: Int, private var direction: Char) {

    private val directions = listOf('N', 'E', 'S', 'W') // Clockwise order

    private fun turnLeft() {
        val currentIndex = directions.indexOf(direction)
        direction = directions[(currentIndex - 1 + directions.size) % directions.size]
    }

    private fun turnRight() {
        val currentIndex = directions.indexOf(direction)
        direction = directions[(currentIndex + 1) % directions.size]
    }

    private fun moveForward(gridLimit: Pair<Int, Int>) {
        when (direction) {
            'N' -> if (y < gridLimit.second) y++
            'E' -> if (x < gridLimit.first) x++
            'S' -> if (y > 0) y--
            'W' -> if (x > 0) x--
        }
    }

    fun executeInstructions(instructions: String, gridLimit: Pair<Int, Int>) {
        for (instruction in instructions) {
            when (instruction) {
                'L' -> turnLeft()
                'R' -> turnRight()
                'M' -> moveForward(gridLimit)
            }
        }
    }

    fun getPosition(): String {
        return "$x $y $direction"
    }
}

fun main() {
    // Function to read non-empty lines with length restriction
    fun readNonEmptyLine(prompt: String, maxLength: Int = 100): String {
        while (true) {
            println(prompt)
            val input = readLine()?.trim()
            // Debugging input length
            println("Input length: ${input?.length}")
            if (!input.isNullOrEmpty() && input.length <= maxLength) return input
            if (input != null && input.length > maxLength) {
                println("Input exceeds the maximum length of $maxLength characters. Please try again.")
            } else {
                println("Input cannot be blank. Please try again.")
            }
        }
    }

    try {
        // Read grid size
        val gridInput = readNonEmptyLine("Enter grid size (x y):", maxLength = 10)
        val (gridX, gridY) = gridInput.split(" ").map { it.toIntOrNull() ?: throw IllegalArgumentException("Grid size must be integers.") }
        require(gridX > 0 && gridY > 0) { "Grid dimensions must be positive integers." }

        // Read robot's starting position
        val startInput = readNonEmptyLine("Enter robot's starting position (x y direction):", maxLength = 20)
        val startData = startInput.split(" ")
        require(startData.size == 3) { "Starting position must have x, y, and direction." }

        val startX = startData[0].toIntOrNull() ?: throw IllegalArgumentException("Starting x-coordinate must be an integer.")
        val startY = startData[1].toIntOrNull() ?: throw IllegalArgumentException("Starting y-coordinate must be an integer.")
        require(startX in 0..gridX && startY in 0..gridY) { "Starting position must be within grid bounds." }

        val startDirection = startData[2].firstOrNull()?.takeIf { it in listOf('N', 'E', 'S', 'W') }
            ?: throw IllegalArgumentException("Direction must be one of N, E, S, W.")

        // Read navigation instructions
        val instructions = readNonEmptyLine("Enter navigation instructions:", maxLength = 500) // Reduced the max length to 500
        require(instructions.all { it in listOf('L', 'R', 'M') }) { "Instructions can only contain L, R, M." }

        // Initialize robot
        val robot = Robot(startX, startY, startDirection)

        // Execute instructions
        robot.executeInstructions(instructions, Pair(gridX, gridY))

        // Output final position
        println(robot.getPosition())

    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

