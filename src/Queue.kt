class Queue: MutableList<Element> by mutableListOf() {
    fun peakMin(): Element {
        var minIdx = 0
        for (idx in 1..this.lastIndex) {
            if (this[idx].energySum < this[minIdx].energySum) {
                minIdx = idx
            }
        }
        return this[minIdx]
    }

    fun extractMin(): Element {
        var minIdx = 0
        for (idx in 1..this.lastIndex) {
            if (this[idx].energySum < this[minIdx].energySum) {
                minIdx = idx
            }
        }
        return this.removeAt(minIdx)
    }
}