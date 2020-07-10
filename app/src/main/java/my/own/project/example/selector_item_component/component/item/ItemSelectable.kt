package my.own.project.example.selector_item_component.component.item

import android.content.Context
import android.graphics.*
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

    private var onDefaultStateStrokeColor: Int = Int.MIN_VALUE
    private var onSelectedStateStrokeColor: Int = Int.MIN_VALUE

    private var onDefaultStateBackgroundColor: Int = Int.MIN_VALUE
    private var onSelectedStateBackgroundColor: Int = Int.MIN_VALUE

    private val strokePaint by lazy { Paint() }
    private val shapePaint by lazy { Paint() }

    private val borderPath by lazy { Path() }
    private val fillPath by lazy { Path() }

    private val rect by lazy { RectF() }
    private val innerRect by lazy { RectF() }

    private val handler by lazy { ItemSelectableEventHandler(this) }

    init {
        initAttrs()
        initInternals()
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

    private fun initInternals() {
        strokePaint.apply {
            color = onDefaultStateStrokeColor
            style = Paint.Style.FILL
            isAntiAlias = true
            isDither = true
        }

        shapePaint.apply {
            color = onDefaultStateBackgroundColor
            style = Paint.Style.FILL
            isAntiAlias = true
            isDither = true
        }
    }

    private fun initBehaviourState() {
        setAsDefault()
    }

    private fun initEvents() = setOnClickListener {
        handler.onExecutedEvent()
    }

    private fun setShapeColor(color: Int) {
        shapePaint.color = color
    }

    private fun setStrokeColor(color: Int) {
        strokePaint.color = color
    }

    private fun setStrokeWidthValue(dp: Float) {
        val scale = context.resources.displayMetrics.density
        strokeWidth = dp * scale
    }

    private fun setAsDefault() {
        setShapeColor(onDefaultStateBackgroundColor)
        setStrokeColor(onDefaultStateStrokeColor)
        invalidate()
    }

    private fun setAsSelected() {
        setShapeColor(onSelectedStateBackgroundColor)
        setStrokeColor(onSelectedStateStrokeColor)
        invalidate()
    }

    fun isItemSelected() = handler.isItemSelected()

    override fun onSelectedState() = setAsSelected()

    override fun onDefaultState() = setAsDefault()

    override fun onDraw(canvas: Canvas?) {
        drawCanvasPath(canvas)
        super.onDraw(canvas)
    }

    private fun drawCanvasPath(canvas: Canvas?) {
        canvas?.apply {
            rect.let {
                it.set(0F, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

                borderPath.addRoundRect(it, borderRadius, borderRadius, Path.Direction.CW)

                innerRect.set(it)
            }

            innerRect.let {
                it.inset(strokeWidth, strokeWidth)

                if (it.width() > 0 && it.height() > 0)
                    borderPath.addRoundRect(it, borderRadius, borderRadius, Path.Direction.CW)
            }

            borderPath.fillType = Path.FillType.EVEN_ODD

            fillPath.addRoundRect(rect, borderRadius, borderRadius, Path.Direction.CW)

            drawPath(fillPath, shapePaint)
            drawPath(borderPath, strokePaint)
        }
    }

}