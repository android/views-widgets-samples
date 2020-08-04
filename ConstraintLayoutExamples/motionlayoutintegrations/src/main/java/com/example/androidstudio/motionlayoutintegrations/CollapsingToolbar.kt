package com.example.androidstudio.motionlayoutintegrations

import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidstudio.motionlayoutintegrations.databinding.ActivityCollapsingToolbarBinding
import com.google.android.material.appbar.AppBarLayout

class CollapsingToolbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
            }
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        val binding = ActivityCollapsingToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listener = AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
            val seekPosition = -verticalOffset / appBar.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
            binding.background.translationProgress = (100 * seekPosition).toInt()
        }
        binding.appbarLayout.addOnOffsetChangedListener(listener)

        ViewCompat.setOnApplyWindowInsetsListener(binding.motionLayout) { _, insets: WindowInsetsCompat ->
            val collapsedTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()
            val endConstraintSet = binding.motionLayout.getConstraintSet(R.id.end)
            endConstraintSet.setGuidelineEnd(R.id.collapsed_top, collapsedTop)
            endConstraintSet.setGuidelineEnd(R.id.inset, collapsedTop - insets.systemWindowInsetTop)
            insets
        }
    }
}

private val INFLECTION_PART = 8
private val PI_OVER_2 = Math.PI / 2

class CutoutImage @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val scratchRect = RectF()

    private var _bottomCutSize: Float

    // this is just to make Kotlin happy
    var bottomCutSize: Float
        get() = _bottomCutSize
        set(value) {
            _bottomCutSize = value
            invalidate()
        }

    private var _endCutSize: Float
    var endCutSize: Float
        get() = _endCutSize
        set(value) {
            _endCutSize = value
            invalidate()
        }

    var translationProgress: Int = 0
        set(value) {
            field = value
            val matrix = imageMatrix
            val imageWidth = drawable.intrinsicWidth.toFloat()
            val scaleFactor = width.toFloat() / imageWidth
            matrix.setScale(scaleFactor, scaleFactor)
            matrix.postTranslate(0f, -100f + value)
            imageMatrix = matrix
        }

    private val painter = Paint()

    private val grayPainter = Paint().also {
        it.color = 0x33000000.toInt()
        it.strokeWidth = dpToF(1)
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CutoutImage,
                0,
                0
        )

        _endCutSize = typedArray.getDimension(R.styleable.CutoutImage_endCut, dpToF(0))
        _bottomCutSize = typedArray.getDimension(R.styleable.CutoutImage_bottomCut, dpToF(0))
        painter.color = typedArray.getColor(R.styleable.CutoutImage_cutoutColor, 0xFFaaFFaa.toInt())
        typedArray.recycle()
    }

    init {
        scaleType = ScaleType.MATRIX // ignore any other scale types
    }

    private fun dpToF(value: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )

    override fun onDraw(canvas: Canvas?) {
        // let the parent draw the bitmap
        super.onDraw(canvas)

        canvas?.drawCircle(
            width.toFloat() / 2,
            height.toFloat(),
            _bottomCutSize / 2,
            painter
        )

        val margin = dpToF(16)
        if (height.toFloat() <= _endCutSize) {
            // this is to fill in the area to the right of the circle to avoid showing a small
            // triangle of background in (bottom right & top left) during expansion
            val centerV = 2 * height.toFloat() / 3
            scratchRect.set(
                    width - margin,
                    centerV - _endCutSize / 2,
                    width.toFloat(),
                    centerV + _endCutSize / 2
            )
            canvas?.drawRect(scratchRect, painter)
        }

        canvas?.drawCircle(
            width - margin,
            2 * height.toFloat() / 3,
            _endCutSize / 2,
            painter
        )

        // add a 1px gray line to the bottom of the circle region so it clearly divides from
        // surrounding region
        canvas?.drawLine(
            width - margin - _endCutSize / 2,
            height.toFloat(),
            width.toFloat(),
            height.toFloat(),
            grayPainter
        )
    }
}