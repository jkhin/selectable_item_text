package my.own.project.example.selector_item_component.component.item

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.Toast
import com.google.android.material.textview.MaterialTextView
import my.own.project.example.selector_item_component.R


class ItemSelectable @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet? = null, private val defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), ItemSelectableListener {

    private var strokeWidth: Float = Float.MIN_VALUE
        set(value) {
            if (field == Float.MIN_VALUE) {
                field = value
                setStrokeWidthValue(value)
            }
        }

    private var borderRadius: Float = Float.MIN_VALUE
        set(value) {
            if (field == Float.MIN_VALUE) {
                field = value
                cornerRadiiArray =
                    floatArrayOf(value, value, value, value, value, value, value, value)
            }
        }

    private var onDefaultStateStrokeColor: Int = Int.MIN_VALUE
    private var onSelectedStateStrokeColor: Int = Int.MIN_VALUE

    private var onDefaultStateBackgroundColor: Int = Int.MIN_VALUE
    private var onSelectedStateBackgroundColor: Int = Int.MIN_VALUE

    private var cornerRadiiArray = floatArrayOf()

    private val shape by lazy { GradientDrawable() }

    private val handler by lazy { ItemSelectableEventHandler(this) }

    init {
        initAttrs()
        initBehaviourState()
        initEvents()
    }

    private fun initAttrs() {
        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ItemSelectableAttrs,
                defStyleAttr,
                0
            )

        onDefaultStateBackgroundColor = typedArray.getColor(
            R.styleable.ItemSelectableAttrs_onDefaultStateBackgroundColor,
            Color.WHITE
        )
        onSelectedStateBackgroundColor = typedArray.getColor(
            R.styleable.ItemSelectableAttrs_onSelectedStateBackgroundColor,
            Color.WHITE
        )
        onDefaultStateStrokeColor = typedArray.getColor(
            R.styleable.ItemSelectableAttrs_onDefaultStateStrokeColor,
            Color.WHITE
        )
        onSelectedStateStrokeColor = typedArray.getColor(
            R.styleable.ItemSelectableAttrs_onSelectedStateStrokeColor,
            Color.WHITE
        )
        strokeWidth =
            typedArray.getDimension(
                R.styleable.ItemSelectableAttrs_itemSelectableStrokeWidth,
                Float.MIN_VALUE
            )
        borderRadius =
            typedArray.getDimension(
                R.styleable.ItemSelectableAttrs_itemSelectableBorderRadius,
                Float.MIN_VALUE
            )

        typedArray.recycle()
    }

    private fun initBehaviourState() {
        setAsDefault()
    }

    private fun initEvents() = setOnClickListener {
        handler.onExecutedEvent()
    }

    private fun setStrokeWidthValue(dp: Float) {
        val scale = context.resources.displayMetrics.density
        strokeWidth = dp * scale
    }

    private fun setAsDefault() =
        shape.changeColorState(onDefaultStateStrokeColor, onDefaultStateBackgroundColor)

    private fun setAsSelected() =
        shape.changeColorState(onSelectedStateStrokeColor, onSelectedStateBackgroundColor)


    override fun onSelectedState() = setAsSelected()

    override fun onDefaultState() = setAsDefault()

    private fun GradientDrawable.changeColorState(borderColor: Int, bgColor: Int) = with(this) {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = cornerRadiiArray
        setColor(bgColor)
        setStroke(strokeWidth.toInt(), borderColor)

        background = this
    }

    fun isItemSelected() = handler.isItemSelected()

}