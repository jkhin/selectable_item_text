package my.own.project.example.selector_item_component.component.item

class ItemSelectableEventHandler internal constructor(private val listener: ItemSelectableListener) {

    private var state = ItemSelectableState.DEFAULT

    fun onExecutedEvent() {
        state = if (state == ItemSelectableState.DEFAULT) ItemSelectableState.SELECTED
        else ItemSelectableState.DEFAULT

        state.handleState()
    }

    private fun ItemSelectableState.handleState() = when (this) {
        ItemSelectableState.SELECTED -> listener.onSelectedState()
        ItemSelectableState.DEFAULT -> listener.onDefaultState()
    }

    fun isItemSelected() = (state == ItemSelectableState.SELECTED)

}